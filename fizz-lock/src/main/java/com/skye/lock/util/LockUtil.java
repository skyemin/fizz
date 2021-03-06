package com.skye.lock.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public class LockUtil {
	
	/**
	 * 生成随机key
	 * @return
	 */
	public static String generateRandomKey() {
		String dateKey = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		Random random=new Random();
		int randomKey = random.ints(100000, 999999).limit(1).findFirst().getAsInt();
		return new StringBuilder(dateKey).append(randomKey).toString();
	}

}
