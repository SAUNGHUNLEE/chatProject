package com.chat.project.chat.service;


import com.chat.project.chat.dto.UserDTO;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import util.SmsCertificationDao;
import util.SmsCertificationUtil;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UnauthUserService {

    private final SmsCertificationUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.path.member.profile}")
    private String memberPath;

    public UnauthUserService(SmsCertificationUtil smsUtil, SmsCertificationDao smsCertificationDao, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.smsUtil = smsUtil;
        this.smsCertificationDao = smsCertificationDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public UserDTO save(UserDTO userDTO) {

    /*    final String email = userDTO.getEmail();
        if(memberRepository.existsByEmail(email)){
            throw new RuntimeException("해당 email이 이미 존재합니다.");
        }
        // 닉네임 중복 체크
        final String name = userDTO.getName();
        if(memberRepository.existsByName(name)){
            throw new RuntimeException("해당 이름이 이미 존재합니다.");
        }*/

        try{
            //dto -> entity
            User user = User.builder()
                    .email(userDTO.getEmail())
                    .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                    .name(userDTO.getName())
                    .phone(userDTO.getPhone())
                    .joinDay(LocalDate.now())
                    .state(1) //회원 가입 시 인증이 되어야만 가입가능
                    .role(userDTO.getRole())
                    .birthDay(userDTO.getBirthDay())
                    .build();
            userRepository.save(user).getId();

            if(userDTO.checkProfileImgRequestNull() && !userDTO.getProfileImgRequest().isEmpty()){
                MultipartFile multipartFile = userDTO.getProfileImgRequest().get(0);

                if(!multipartFile.isEmpty()){
                    String absolutePath = memberPath;
                    String path = absolutePath;
                    File file = new File(path);

                    if(!file.exists()){
                        boolean wasSuccessful = file.mkdirs();
                        if(!wasSuccessful){
                            System.out.println("파일없음");
                        }
                    }
                    while(true){
                        String originFile;
                        String contentType = multipartFile.getContentType();
                        if(ObjectUtils.isEmpty(contentType)) {
                            break;
                        }else{
                            if(contentType.contains("image/jpeg")){
                                originFile = ".jpg";
                            }else if(contentType.contains("image/png")){
                                originFile = ".png";
                            }else{
                                System.out.println("지원하지 않는 확장자");
                                break;
                            }
                        }

                        String new_file_name = String.valueOf(user.getId());
                        user.setProfile(new_file_name + originFile);
                        userRepository.save(user);

                        file = new File(absolutePath + File.separator + new_file_name);
                        multipartFile.transferTo(file);
                        file.setWritable(true);
                        file.setReadable(true);
                        break;

                    }

                }

            }

            //entity -> dto
            return user.toUserDTO();


        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("UnauthMemberService.add() : 에러 발생.");
        }

    }


    public void sendSms(UserDTO.SmsCertificationRequest requestDto){
        try {
            String phone = requestDto.getPhone();
            int randomNumber = (int) (Math.random() * 9000) + 1000;
            String certificationNumber = String.valueOf(randomNumber);
            smsUtil.sendSms(phone, certificationNumber);
            smsCertificationDao.createSmsCertification(phone, certificationNumber);
        } catch (Exception e) {
            // Redis 연결 실패 시 예외 처리
            // Redis 서버에 연결할 수 없거나 오류가 발생해도 애플리케이션의 다른 기능에 영향을 미치지 않습니다.
            throw new IllegalStateException("문자 인증 서비스를 사용할 수 없습니다. 잠시 후 다시 시도해주세요.", e);
        }
    }

    public void verifySms(UserDTO.SmsCertificationRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
        smsCertificationDao.removeSmsCertification(requestDto.getPhone());
    }

    public boolean isVerify(UserDTO.SmsCertificationRequest requestDto) {
        return !(smsCertificationDao.hasKey(requestDto.getPhone()) &&
                smsCertificationDao.getSmsCertification(requestDto.getPhone())
                        .equals(requestDto.getVerification()));
    }

    // 예외적으로 인증처리를위해서 return을 user entity사용
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        // 로그인 실패 시 null 반환
        System.out.println("로그인 실패");
        return null;
    }


    //핸드폰 + 이름 -> 이메일찾기
    public UserDTO getUserEmail(UserDTO.LookForEmail lookForEmailDTO){
        String email = userRepository.lookforEmail(lookForEmailDTO.getName(), lookForEmailDTO.getPhone())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자 이메일이 없습니다."));

        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .build();

        return userDTO;
    }

}