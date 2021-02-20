package cn.dofuntech.dfauth.util;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import cn.dofuntech.dfauth.bean.AccessToken;
import cn.dofuntech.dfauth.util.AccessTokenUtil;



public class APIServiceImpl {

	  private static final String HLJ_SERVICE_APPKEY    = "7a76c23a33254036b78761754356f6c0";
	  private static final String HLJ_SERVICE_ACCESS_TOKEN    = "2e74590b93b7969cf51ccc3b4ea00386";
	  public static AccessToken access_token=null;


	    // 查询用户
	    
	    public String queryUser(String sid){
	    	CloseableHttpClient httpclient = HttpClients.createDefault();  
	    	access_token=AccessTokenUtil.getAccessToken(HLJ_SERVICE_APPKEY, HLJ_SERVICE_ACCESS_TOKEN);
			
	    	HttpPost httppost = new HttpPost("http://open.whhlj.com/api?appkey="+HLJ_SERVICE_APPKEY+"&version=1.0&access_token="+access_token.getAccessToken()+"&method=core.user.list.school&schoolId="+sid+"&version=1.0&roleName=teacher&page=1&limit=1000");

	    	List formparams = new ArrayList();  
	        //formparams.add(new BasicNameValuePair("type", "house"));  
	        UrlEncodedFormEntity uefEntity;  
	        String message =  null;
			try {
				 uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
		            httppost.setEntity(uefEntity);  
		            System.out.println("executing request " + httppost.getURI());  
		            CloseableHttpResponse response = httpclient.execute(httppost);
		            HttpEntity entity = response.getEntity();  
	                if (entity != null) {  
	                	message = EntityUtils.toString(entity, "UTF-8");
	                    System.out.println("--------------------------------------");  
	                    System.out.println("接口结果: " + message);  
	                    System.out.println("--------------------------------------");  
	                }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return message;
	        

	    }
	    
	    // 查询部门
	    
	    public String queryDepartment(){
	    	CloseableHttpClient httpclient = HttpClients.createDefault();  
	    	access_token=AccessTokenUtil.getAccessToken(HLJ_SERVICE_APPKEY, HLJ_SERVICE_ACCESS_TOKEN);
			
	    	HttpPost httppost = new HttpPost("http://open.whhlj.com/api?appkey="+HLJ_SERVICE_APPKEY+"&version=1.0&access_token="+access_token.getAccessToken()+"&method=dept.list.school&schoolId=1010000001000022449&version=1.0&format=json&page=1&limit=1000");

	    	List formparams = new ArrayList();  
	        //formparams.add(new BasicNameValuePair("type", "house"));  
	        UrlEncodedFormEntity uefEntity;  
	        String message =  null;
			try {
				 uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
		            httppost.setEntity(uefEntity);  
		            System.out.println("executing request " + httppost.getURI());  
		            CloseableHttpResponse response = httpclient.execute(httppost);
		            HttpEntity entity = response.getEntity();  
	                if (entity != null) {  
	                	message = EntityUtils.toString(entity, "UTF-8");
	                    System.out.println("--------------------------------------");  
	                    System.out.println("接口结果: " + message);  
	                    System.out.println("--------------------------------------");  
	                }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return message;
	        

	    }
	    
	    // 查询部门下的用户
	    
	    public String queryDepartmentByuser(){
	    	CloseableHttpClient httpclient = HttpClients.createDefault();  
	    	access_token=AccessTokenUtil.getAccessToken(HLJ_SERVICE_APPKEY, HLJ_SERVICE_ACCESS_TOKEN);
			
	    	HttpPost httppost = new HttpPost("http://open.whhlj.com/api?format=json&appkey="+HLJ_SERVICE_APPKEY+"&version=1.0&access_token="+access_token.getAccessToken()+"&method=user.list.dept&schoolId=1010000001000022449&page=1&limit=1000");

	    	List formparams = new ArrayList();  
	        //formparams.add(new BasicNameValuePair("type", "house"));  
	        UrlEncodedFormEntity uefEntity;  
	        String message =  null;
			try {
				 uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
		            httppost.setEntity(uefEntity);  
		            System.out.println("executing request " + httppost.getURI());  
		            CloseableHttpResponse response = httpclient.execute(httppost);
		            HttpEntity entity = response.getEntity();  
	                if (entity != null) {  
	                	message = EntityUtils.toString(entity, "UTF-8");
	                    System.out.println("--------------------------------------");  
	                    System.out.println("接口结果: " + message);  
	                    System.out.println("--------------------------------------");  
	                }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return message;
	        

	    }


}
