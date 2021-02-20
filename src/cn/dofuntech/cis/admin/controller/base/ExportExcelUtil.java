package cn.dofuntech.cis.admin.controller.base;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellRangeAddress;

/**导出工具类
 * @author kai.luo
 */
public class ExportExcelUtil<T> {

	/**
	 * 导出Excel的方法
	 * 
	 * @param title
	 *            excel中的sheet名称
	 * @param headers
	 *            表头
	 * @param result
	 *            结果集
	 * @param out
	 *            输出流
	 * @param pattern
	 *            时间格式
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void exportoExcel(String title, String[] headers, String[] columns, Collection<T> result, OutputStream out,
			String pattern) throws Exception {

		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth((short) 20);
		//单元格数字样式
		HSSFCellStyle contentStyle = getContentStyle(workbook);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置单元格背景色，设置单元格背景色以下两句必须同时设置
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); // 设置填充色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置填充样式   

		 // 设置单元格上、下、左、右的边框线
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		//font.setColor(HSSFColor.VIOLET.index);
		// font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		// 指定当单元格内容显示不下时自动换行
		style.setWrapText(true);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		// 以下可以用于设置导出的数据的样式

	/*	// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); // 把字体应用到当前的样式
		style2.setFont(font2); // 
		//声明一个画图的顶级管理器 
		HSSFPatriarch patriarch2 =sheet.createDrawingPatriarch();*/

		// 定义注释的大小和位置,详见文档 HSSFComment comment = patriarch.createComment(new
		// HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5)); // 设置注释内容
		// comment.setString(new HSSFRichTextString("可以在POI中添加注释！")); //
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容. comment.setAuthor("leno");

		// 产生表格标题行
		// 表头的样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();// 创建样式对象
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 设置字体
		HSSFFont titleFont = workbook.createFont(); // 创建字体对象
		titleFont.setFontHeightInPoints((short) 15); // 设置字体大小
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		// titleFont.setFontName("黑体"); // 设置为黑体字
		titleStyle.setFont(titleFont);
		
		sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (headers.length - 1)));// 指定合并区域
		HSSFRow rowHeader = sheet.createRow(0);
		HSSFCell cellHeader = rowHeader.createCell((short) 0); // 只能往第一格子写数据，然后应用样式，就可以水平垂直居中
		HSSFRichTextString textHeader = new HSSFRichTextString(title);
		cellHeader.setCellStyle(titleStyle);
		cellHeader.setCellValue(textHeader);

		HSSFRow row = sheet.createRow(1);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		try{
			
		
			
		
		// 遍历集合数据，产生数据行
		if (result != null) {
			int index = 2;
			for (T t : result) {
				// Field[] fields = t.getClass().getDeclaredFields();
				row = sheet.createRow(index);
				index++;
				for (short i = 0; i < columns.length; i++) {
					HSSFCell cell = row.createCell(i);
					// Field field = fields[i];
					// String fieldName = field.getName();
					String fieldName = columns[i];
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					// getMethod.getReturnType().isInstance(obj);
					Object value = getMethod.invoke(t, new Class[] {});
					String textValue = null;
					if (value == null) {
						textValue = "";
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
								index);
						anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}

					if (textValue != null) {
						String p = "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+";
						if (Pattern.matches(p, textValue)) {
							// 是数字当作double处理
							cell.setCellStyle(contentStyle);
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				}
			}
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		workbook.write(out);
		out.flush();
		out.close();

	}
	
	/**
	 * 绩效考核导出
	 * 
	 * @param title
	 *            excel中的sheet名称
	 * @param headers
	 *            表头
	 * @param result
	 *            结果集
	 * @param out
	 *            输出流
	 * @param pattern
	 *            时间格式
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void exportoExcelSorce(String title, List<String> headers, List<String> xdata2nd, List<List<String>> xdataMain, OutputStream out,
			String pattern) throws Exception {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为16个字节
		sheet.setDefaultColumnWidth((short) 16);
		//单元格数字样式
		HSSFCellStyle contentStyle = getContentStyle(workbook);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置单元格背景色，设置单元格背景色以下两句必须同时设置
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); // 设置填充色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 设置填充样式   
		
		// 设置单元格上、下、左、右的边框线
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		//font.setColor(HSSFColor.VIOLET.index);
		// font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		
		// 指定当单元格内容显示不下时自动换行
		style.setWrapText(true);
		
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		
		// 以下可以用于设置导出的数据的样式
		
			// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL); // 把字体应用到当前的样式
		style2.setFont(font2); // 
		//声明一个画图的顶级管理器 
		HSSFPatriarch patriarch2 =sheet.createDrawingPatriarch();
		
		// 定义注释的大小和位置,详见文档 HSSFComment comment = patriarch.createComment(new
		// HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5)); // 设置注释内容
		// comment.setString(new HSSFRichTextString("可以在POI中添加注释！")); //
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容. comment.setAuthor("leno");
		
		// 产生表格标题行
		// 表头的样式
		HSSFCellStyle titleStyle = workbook.createCellStyle();// 创建样式对象
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 设置字体
		HSSFFont titleFont = workbook.createFont(); // 创建字体对象
		titleFont.setFontHeightInPoints((short) 15); // 设置字体大小
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		// titleFont.setFontName("黑体"); // 设置为黑体字
		titleStyle.setFont(titleFont);
		int index = 0;
		// 为sheet1生成第一行
		sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (headers.size() - 1)));// 指定合并区域
		HSSFRow rowHeader = sheet.createRow(index++);
		HSSFCell cellHeader = rowHeader.createCell((short) 0); // 只能往第一格子写数据，然后应用样式，就可以水平垂直居中
		HSSFRichTextString textHeader = new HSSFRichTextString(title);
		cellHeader.setCellStyle(titleStyle);
		cellHeader.setCellValue(textHeader);
		//第二行
		HSSFRow row = sheet.createRow(index++);
		HSSFCell cell = row.createCell((short) 0);
		for (int i = 0; i < headers.size(); i++) {
			cell = row.createCell((short) i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
			cell.setCellValue(text);
		}
		if(xdata2nd!=null){
			//第三行
			row = sheet.createRow(index++);
			for (int i = 0; i < xdata2nd.size(); i++) {
				cell = row.createCell((short) i);
				HSSFRichTextString text = new HSSFRichTextString(xdata2nd.get(i));
				cell.setCellValue(text);
			}
		}
		try{
			// 遍历集合数据，产生数据行
			if (xdataMain != null) {
				for (List<String> t : xdataMain) {
					row = sheet.createRow(index);
					index++;
					for (short i = 0; i < t.size(); i++) {
						cell = row.createCell(i);
						Object value = t.get(i);
						String textValue = null;
						if (value == null) {
							textValue = "";
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof byte[]) {
							// 有图片时，设置行高为60px;
							row.setHeightInPoints(60);
							// 设置图片所在列宽度为80px,注意这里单位的一个换算
							sheet.setColumnWidth(i, (short) (35.7 * 80));
							// sheet.autoSizeColumn(i);
							byte[] bsValue = (byte[]) value;
							HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
									index);
							anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
							patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
						} else {
							// 其它数据类型都当作字符串简单处理
							textValue = value.toString();
						}
						
						if (textValue != null) {
							String p = "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+";
							if (Pattern.matches(p, textValue)) {
								// 是数字当作double处理
								cell.setCellStyle(contentStyle);
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(textValue);
								cell.setCellValue(richString);
							}
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		workbook.write(out);
		out.flush();
		out.close();
		
	}
	
	
	public static HSSFCellStyle getContentStyle (HSSFWorkbook workbook) {  
	     HSSFCellStyle contentStyle = workbook.createCellStyle(); //设置内容行格式  
	     contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);    
	     contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中     
	     HSSFDataFormat df = workbook.createDataFormat();  //此处设置数据格式  
	     contentStyle.setDataFormat(df.getFormat("#,#0.00")); //数据格式(小数)  
	     return contentStyle;  
	} 
}
