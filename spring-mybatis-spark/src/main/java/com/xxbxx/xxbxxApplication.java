package com.xxbxx;

import org.springframework.boot.SpringApplication;

/**
 * @Auther: xxbxx
 * @Date: 2019/7/1 17:33
 * @Description:
 */
public class xxbxxApplication {
	public static void main(String[] args) throws ClassNotFoundException {
		/*
			根据main方法参数，利用反射获取需要运行的runner
		 */
		String application = args[0];
		Class<?> applicationClass = Class.forName("com.xxbxx.runner." + application);

		SpringApplication.run(applicationClass);
	}
}
