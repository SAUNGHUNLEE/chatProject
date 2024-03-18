package util;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component // 스프링 컨텍스트에 빈으로 등록
public class SmsCertificationUtil {
    @Value("${spring.coolsms.api.senderNumber}")
    private String senderNumber;

    @Value("${spring.coolsms.api.key}")
    private String apiKey;

    @Value("${spring.coolsms.api.secret}")
    private String apiSecret;
    DefaultMessageService messageService;


    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendSms(String to, String verificationCode){
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText("[chat] 본인 확인 인증번호는 "+verificationCode+"입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        return response;
    }

}
