package cn.dofuntech.tools.jdbc;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.dofuntech.core.util.DateUtils;
import cn.dofuntech.tools.PropertyUtil;

import ch.epfl.lamp.fjbg.Main;

@Service
public class JdbcUtils {

	@Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
	
	private static final String FILE_EXT = ".sql";

	private String getDateBaseName(){
		return jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
	}
	
	/**
	 * 清除指定表数据
	 * @param tables 表名
	 */
	public void truncateTable(String[] tables){
		List<String> sqlList = new ArrayList<String>();
		for (String tableName : tables) {
			String sql = " TRUNCATE TABLE "+tableName;
			sqlList.add(sql);
		}
		jdbcTemplate.batchUpdate((String[])sqlList.toArray());
	}

	/**
	 * 备份当前数据库
	 * @return
	 * @throws Exception
	 */
	public boolean backall() throws Exception{
		String mysqldumpPath = PropertyUtil.get("system.sh.mysqldumpPath");
		String backupPath = PropertyUtil.get("system.backup.path");
		File saveFile  = new File(backupPath);
		if(!saveFile.exists()){
			saveFile.mkdirs();
		}
		if(!mysqldumpPath.endsWith(File.separator)){
			mysqldumpPath += File.separator;
		}
		if(!backupPath.endsWith(File.separator)){
			backupPath += File.separator;
		}
		String filename = DateUtils.format(new Date(), DateUtils.TIMESTAMP_FORMAT);
		StringBuffer sb = new StringBuffer();
			sb.append("/bin/sh -c "+mysqldumpPath+"mysqldump -u")
			.append(PropertyUtil.get("jdbc.username"))
			.append(" -p").append(PropertyUtil.get("jdbc.password"))
			.append(" --set-charset=UTF8 ").append(getDateBaseName())
			.append(" > "+backupPath+filename+FILE_EXT);
		Process process = Runtime.getRuntime().exec(sb.toString());
		if(process.waitFor() == 0){
			return true;
		}else{
			System.out.println(IOUtils.toString(process.getErrorStream(), Charset.forName("GBK")));
			return false;
		}
	}
	
}
