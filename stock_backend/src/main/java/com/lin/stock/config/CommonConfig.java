package com.lin.stock.config;

import com.lin.stock.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 定义公共配置类
 */
@Configuration
public class CommonConfig {


    /**
     * 密码加密器
     * BCryptPasswordEncoder方法采用SHA-256对密码进行加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 基于雪花算法工具类
     * 配置id生成器bean
     *
     * @return
     */
    @Bean
    public IdWorker idWorker() {
        /*
        参数1：机器ID
        参数2：机房ID
        机房和机器编号一般由运维人员进行唯一性规划
         */
        return new IdWorker(11, 21);
    }
}
