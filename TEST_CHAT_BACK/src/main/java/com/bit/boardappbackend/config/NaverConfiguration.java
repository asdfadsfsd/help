package com.bit.boardappbackend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application.properties")
@Getter
public class NaverConfiguration {
    @Value("${ncp.accessKey}")
    private String accessKey;
    @Value("${ncp.secretKey}")
    private String secretKey;
    @Value("${ncp.regionName}")
    private String regionName;
    @Value("${ncp.endPoint}")
    private String endPoint;
    String liveStationUrl = "https://livestation.apigw.ntruss.com/api/v2/channels";

}
