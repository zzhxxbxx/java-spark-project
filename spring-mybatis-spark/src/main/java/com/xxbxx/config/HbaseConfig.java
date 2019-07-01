package com.xxbxx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: xxbxx
 * @Date: 2019/7/1 18:20
 * @Description:
 */

@Component
@Getter
@Setter
public class HbaseConfig {

	@Value("${private.hbase.crowdStats}")
	private String crowdStats;

}
