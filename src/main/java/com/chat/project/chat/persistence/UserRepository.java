package com.chat.project.chat.persistence;


import com.chat.project.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByEmail(String email);
    
    //해당 이메일 존재여부
    Boolean existsByEmail (String email);

    //해당 이름 존재여부
    Boolean existsByName (String name);

    //이름 + 핸드폰번호 -> 이메일
    @Query(value = "SELECT u.email FROM user u where name =:name AND phone  =:phone", nativeQuery = true)
    Optional<String> lookforEmail(@Param("name")String name, @Param("phone")int phone);

    //이름 + 핸드폰 = 이메일 체크
    @Query(value = "SELECT * FROM user u where name =:name AND phone  =:phone", nativeQuery = true)
    Optional<User> checkEmail(@Param("name")String name, @Param("phone")int phone);

    @Query(value = "SELECT * FROM member where email = :email", nativeQuery = true)
    Optional<User> findByMemberEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM member where email = :email AND password = :password", nativeQuery = true)
    User findByMemberPw(@Param("email") String email,@Param("password") String password);


}



