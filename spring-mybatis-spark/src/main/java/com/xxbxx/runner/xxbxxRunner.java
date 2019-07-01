package com.xxbxx.runner;

import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.springframework.boot.CommandLineRunner;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @Auther: Administrator
 * @Date: 2019/7/1 13:49
 * @Description:
 */
public class xxbxxRunner implements CommandLineRunner, Serializable {


	@Override
	public void run(String... args) throws Exception {
		String zkQuorum = "hadoop6:9092";
		String checkPoint = "Kafka_Canal_test";
		String topic = "canal-main";
		SparkConf sparkConf = new SparkConf().setAppName(this.getClass().getName()).setMaster("local[2]")
				.set("spark.speculation", "true")
				.set("spark.speculation.interval", "100")
				.set("spark.speculation.quantile", "0.75")
				.set("spark.speculation.multiplier", "1.5")
				.set("spark.streaming.concurrentJobs", "4");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		JavaStreamingContext jssc = new JavaStreamingContext(jsc, new Duration(1000));
		jssc.checkpoint("Kafka_Canal_msg");

		HashMap<String, String> map = new HashMap<>();
		map.put("metadata.broker.list", zkQuorum);
		map.put("group.id", checkPoint);

		String[] split = topic.split(",");
		HashSet<String> set = new HashSet<>(Arrays.asList(split));

		JavaPairInputDStream<String, String> directStream = KafkaUtils.createDirectStream(jssc, String.class, String.class, StringDecoder.class, StringDecoder.class, map, set);

		JavaDStream<String> javaDStream = directStream.map(Tuple2::_2);
		javaDStream.print();

		javaDStream.foreachRDD(new VoidFunction<JavaRDD<String>>() {
			@Override
			public void call(JavaRDD<String> stringJavaRDD) throws Exception {
				stringJavaRDD.foreach(new VoidFunction<String>() {
					@Override
					public void call(String binlog) throws Exception {
						System.out.println(binlog);

					}
				});
			}
		});

		jssc.start();
		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
