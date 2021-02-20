package cn.dofuntech.gencode.util;

import java.util.Random;

/**
 * @info:随机数工具类 
 * @Author:dengying 
 * @Date:2011-9-3 
 * @Version:1.0
 */
public class RandomUtil {
	private static Random rand = new Random(System.currentTimeMillis());

	/**
	 * 给定范围内随机
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randInt(int min, int max) {
		if (max < min)
			return 0;
		if (max == min)
			return min;
		return (Math.abs(rand.nextInt()) % (max - min + 1)) + min;
	}
}
