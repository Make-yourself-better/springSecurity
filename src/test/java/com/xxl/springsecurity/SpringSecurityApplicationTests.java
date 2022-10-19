package com.xxl.springsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringSecurityApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
        boolean matches = bCryptPasswordEncoder.matches("123456","$2a$10$wYElKbRfmL.iVFfekufeWOE0feihr0jzw2hfiTl/MP0pr6TfP3TnW" );
        System.out.println("matches"+matches);
    }

}
