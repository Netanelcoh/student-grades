package com.backend.studentgrades.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.studentgrades.util.AWSService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import static org.mockito.Mockito.when;

import java.io.IOException;

@TestConfiguration
public class TestMocks {


    @Bean
    @Primary
    public AmazonS3 s3Client() throws IOException {
        AmazonS3 mockS3 = Mockito.mock(AmazonS3.class);

        return mockS3;

    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        var res =  Mockito.mock(AuthenticationManager.class);
        var auth = Mockito.mock(Authentication.class);
        when(res.authenticate(ArgumentMatchers.any())).thenReturn(auth);
        return res;
    }


 /*   @Bean
    @Primary
    public SmsService smsService() {
        SmsService service = Mockito.mock(SmsService.class);
        return service;
    }*/
    @Bean
    @Primary
    public AWSService awsService() {
        AWSService service = Mockito.mock(AWSService.class);
        return service;
    }
}
