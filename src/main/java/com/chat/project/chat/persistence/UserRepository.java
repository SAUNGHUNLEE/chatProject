package com.chat.project.chat.persistence;


import com.chat.project.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByEmail(String email);
    
    //해당 이메일 존재여부
    Boolean existsByEmail (String email);

    //해당 이름 존재여부
    Boolean existsByName (String name);

}



