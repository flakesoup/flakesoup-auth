package com.flakesoup.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.flakesoup.uc.api"})
public class FlakeSoupAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(FlakeSoupAuthApplication.class, args);
	}

}
