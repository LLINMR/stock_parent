package com.lin.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class TestPasswordEncoder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 测试密码加密
     */
    @Test
    public void testPwd() {
        String pwd = "123456";
        //$2a$10$j/pIk7x7tG8yve2Iv458vu92ABudoDkTe0nwMRdezpkOs3tkkMjVi
        String encodePwd = passwordEncoder.encode(pwd);
        System.out.println(encodePwd);

                /*
            matches()匹配明文密码和加密后密码是否匹配，如果匹配，返回true，否则false
            just test
         */
        boolean flag = passwordEncoder.matches(pwd, "$2a$10$j/pIk7x7tG8yve2Iv458vu92ABudoDkTe0nwMRdezpkOs3tkkMjVi");
        System.out.println(flag);

    }


}
