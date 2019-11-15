package com.study.javaedge.rpc.util;
/**
 * PropertiesUtils
 * 
 */

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

	private static Properties properties;

	static {
		try {
			properties = new Properties();
			properties.load(PropertiesUtils.class.getResourceAsStream("/app.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperties(String key) {
		return (String) properties.getProperty(key);
	}

}
