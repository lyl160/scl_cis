package cn.dofuntech.gencode.util;

/**
 * @info:JVM内存工具类
 * @Author:dengying
 * @Date:2011-9-3
 * @Version:1.0
 */
public class MemoryUtil {
	public static final int KB = 1024;
	public static final int MB = KB * 1024;
	private static final Runtime RT = Runtime.getRuntime();

	/**
	 * 内存使用信息
	 * 
	 * @return
	 */
	public static final String getMemoryInfo() {
		final int maxMem = getMaxMem();
		final int freeMem = getFreeMem();
		final int totalMem = getTotalMem();
		final int usedMem = getUsedMem();
		final String formatStr = "使用内存:%dM 可用内存:%dM 总内存:%dM 最大可用内存:%dM";
		return String.format(formatStr, usedMem, freeMem, totalMem, maxMem);
	}

	/**
	 * 最大可用内存
	 * 
	 * @return
	 */
	public static final int getMaxMem() {
		return (int) (RT.maxMemory() / MB);
	}

	/**
	 * 可用内存
	 * 
	 * @return
	 */
	public static final int getFreeMem() {
		return (int) (RT.freeMemory() / MB);
	}

	/**
	 * 总内存数
	 * 
	 * @return
	 */
	public static final int getTotalMem() {
		return (int) (RT.totalMemory() / MB);
	}

	/**
	 * 使用内存数
	 * 
	 * @return
	 */
	public static final int getUsedMem() {
		return getTotalMem() - getFreeMem();
	}
}
