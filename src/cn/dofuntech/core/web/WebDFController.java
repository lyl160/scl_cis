/**
 * 
 */
package cn.dofuntech.core.web;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lxu
 *
 */
public interface WebDFController {

	public static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * JSON返回参数
	 * <ul>
	 * <li>0: 表示正常返回
	 * <li>非0: 表示出错信息
	 * </ul>
	 */
	public static final String RETURN_CODE = "returnCode";
	
	/** JSON返回信息 */
	public static final String MSG = "msg";
	
	/** JSON数据的Key - data */
	public static final String DATA = "data";
	
	/** JSON数据的Key - list */
	public static final String LIST = "list";
	
	/** JSON数据的Key - page */
	public static final String PAGE = "page";
	
	/** JSON数据的Key - rows */
	public static final String ROWS = "rows";
	
	/** JSON数据的Key - total */
	public static final String TOTAL = "total";
	
	/** 正确返回 */
	public static final int SUCCESS = 1;
	
	/** 错误返回: 没有指定数据 */
	public static final int ERROR_NO_RECORD = 0;
	
	/** 未知错误 */
	public static final int ERROR_UNKNOWN = -1;
}
