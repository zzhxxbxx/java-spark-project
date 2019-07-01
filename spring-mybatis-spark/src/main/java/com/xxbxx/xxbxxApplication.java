package com.xxbxx;

import org.springframework.boot.SpringApplication;

/**
 * @Auther: Administrator
 * @Date: 2019/7/1 17:33
 * @Description:
 */
public class xxbxxApplication {
	public static void main(String[] args) throws ClassNotFoundException {
		String application = args[0];
		Class<?> applicationClass = Class.forName("com.xxbxx.runner." + application);

		SpringApplication.run(applicationClass);
	}
}
