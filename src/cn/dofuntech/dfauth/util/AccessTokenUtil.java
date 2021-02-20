package cn.dofuntech.dfauth.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;





import cn.dofuntech.core.util.json.Jacksons;
import cn.dofuntech.dfauth.bean.AccessToken;
import net.sf.json.JSONObject;
import sun.net.www.protocol.http.HttpURLConnection;

public class AccessTokenUtil {

	
	public static AccessToken getAccessToken(String appID, String appScret) {
		AccessToken token = new AccessToken();
		// 访问服务器
		String url = "http://open.whhlj.com/authorization-token-server/oauth/token?client_id="+appID+"&client_secret="+appScret+"&grant_type=client_credentials&format=json"
		+ appScret;
		try {
		URL getUrl=new URL(url);
		HttpURLConnection http=(HttpURLConnection)getUrl.openConnection();
		http.setRequestMethod("GET"); 
		http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		http.setDoOutput(true);
		http.setDoInput(true);
	
		http.connect();
		InputStream is = http.getInputStream(); 
		int size = is.available(); 
		byte[] b = new byte[size];
		is.read(b);
	
		String message = new String(b, "UTF-8");
	
		Map<String,Object> json = Jacksons.me().json2Map(message);
	
	
		token.setAccessToken(MapUtils.getString(json,"access_token"));
		token.setExpiresIn(MapUtils.getIntValue(json, "expires_in"));
		} catch (MalformedURLException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		return token;
	}
}
