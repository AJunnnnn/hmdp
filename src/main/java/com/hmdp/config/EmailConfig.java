package com.hmdp.config;

import com.hmdp.utils.SendEmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@Slf4j
@EnableAsync
public class EmailConfig {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Bean
    public SendEmailUtils sendEmailUtils(){
        SendEmailUtils sendEmailUtils = new SendEmailUtils(javaMailSender, from);
        log.info("初始化发送邮件工具类");
        return sendEmailUtils;
    }
}
