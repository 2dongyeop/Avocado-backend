package io.wisoft.capstonedesign.config;


import org.springframework.beans.factory.annotation.Value;
<<<<<<< HEAD
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
=======
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.context.annotation.Bean;
>>>>>>> c95b240 (refactor: mac 내에 패키지 이전)
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

<<<<<<< HEAD
@ComponentScan(basePackages = { "io.wisoft.capsonedesign.mail" })
@PropertySource(value={"classpath:application.properties"})
public class EmailConfig {


=======
@ComponentScan(basePackages = { "io.wisoft.capstonedesign.domain.mail.application" })
@PropertySource(value={"classpath:application.properties"})
public class EmailConfig {

>>>>>>> c95b240 (refactor: mac 내에 패키지 이전)
    @Value("${spring.mail.host}")
    private String mailServerHost;

    @Value("${spring.mail.port}")
    private Integer mailServerPort;

    @Value("${spring.mail.username}")
    private String mailServerUsername;

    @Value("${spring.mail.password}")
    private String mailServerPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailServerAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailServerStartTls;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailServerHost);
        mailSender.setPort(mailServerPort);

        mailSender.setUsername(mailServerUsername);
        mailSender.setPassword(mailServerPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", mailServerAuth);
        props.put("mail.smtp.starttls.enable", mailServerStartTls);
        props.put("mail.debug", "true");

        return mailSender;
    }
}
