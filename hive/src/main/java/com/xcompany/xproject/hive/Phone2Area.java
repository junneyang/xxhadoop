package com.xcompany.xproject.hive;

import java.util.HashMap;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * mvn clean --projects=com.xcompany.xproject:hive
 * mvn install --projects=com.xcompany.xproject:hive
 * java -jar hive/target/hive-1.0.0-RELEASE.jar
*/
public class Phone2Area extends UDF {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Phone2Area.class);

	// Load Once, Speed Up
	private static HashMap<String, String> areaMap = new HashMap<String, String>();

	private static void loadData() {
		areaMap.put("135", "beijing");
		areaMap.put("136", "shanghai");
		areaMap.put("137", "xian");
		areaMap.put("138", "wuhan");
	}

	static {
//		System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.core.impl.Log4jContextFactory");
		loadData();
	}

	public String evaluate(String phoneNum) {
		String preKey = phoneNum.substring(0,3);
		return (areaMap.get(preKey) == null) ? "other" : areaMap.get(preKey);
	}
	
	public static void main(String[] args) {
		Phone2Area phone2Area = new Phone2Area();
		LOGGER.error(phone2Area.evaluate("18665817689"));
	}
}

