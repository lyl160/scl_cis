package cn.dofuntech.core.util.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*********************************************************************************
//* Copyright (C) 2015 bsteel. All Rights Reserved.
//*
//* Filename:      HttpUtils.java
//* Revision:      1.0
//* Author:        lxu
//* Created On:    2015-1-30
//* Modified by:   
//* Modified On:   
//*
//* Description:   HTTP请求工具类 基于HTTCLIENT对GET，POST请求进行封装
/********************************************************************************/
public class HttpUtils {

    private static Logger                             log                          = LoggerFactory.getLogger(HttpUtils.class);
    //定义编码格式 UTF-8
    public static final String                        URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";
    //定义编码格式 GBK
    public static final String                        URL_PARAM_DECODECHARSET_GBK  = "GBK";
    private static final String                       URL_PARAM_CONNECT_FLAG       = "&";
    private static final String                       EMPTY                        = "";

    private static int                                connectionTimeOut            = 45000;
    private static int                                socketTimeOut                = 45000;
    private static int                                maxConnectionPerHost         = 20;
    private static int                                maxTotalConnections          = 20;
    private static HttpClient                         client;
    private static MultiThreadedHttpConnectionManager connectionManager            = null;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
        //        client.getHostConfiguration().setProxy("sheraton.usv3-h.xduotai.com", 20619);
    }

    /**
     * GET 方式调用 默认UTF8编码
     * @param url
     * @return
     */
    public static String URLGet(String url) {
        return URLGet(url, null, URL_PARAM_DECODECHARSET_UTF8, null);
    }

    /**
     * GET 方式调用 默认UTF8编码
     * @param url
     * @return
     */
    public static String URLGet(String url, Map<String, String> params) {
        return URLGet(url, params, URL_PARAM_DECODECHARSET_UTF8, null);
    }

    /**
     * POST 方式调用 默认UTF8编码
     * @param url
     * @param params 请求参数
     * @return
     */
    public static String URLPost(String url, Map<String, String> params) {
        return URLPost(url, params, URL_PARAM_DECODECHARSET_UTF8);
    }

    /**
     * POST 方式调用 默认UTF8编码
     * @param url
     * @param params 采用inputstrem流接收参数
     * @return
     */
    public static String URLPost(String url, String params) {
        return URLPost(url, params, true);
    }

    /**
     * POST 方式调用 默认UTF8编码
     * @param url
     * @param params 采用inputstrem流接收参数
     * @return
     */
    public static String URLPost(String url, String params, boolean needParam) {
        if (needParam) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("data", params);
            return URLPost(url, map);
        }
        log.debug(" -- Http call url : " + url);
        log.debug(" -- Http call parmas : " + params);
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            //postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + URL_PARAM_DECODECHARSET_UTF8);
            postMethod.setRequestEntity(new StringRequestEntity(params, "application/json", URL_PARAM_DECODECHARSET_UTF8));
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
                log.debug("response = " + response);
            }
            else {
                log.error("响应状态码 = " + postMethod.getStatusCode());
                log.error("response = " + postMethod.getResponseBodyAsString());
            }
            log.debug(" -- Http call result : " + response);
        }
        catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        }
        catch (IOException e) {
            log.error("发生网络异常", e);
        }
        finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return response;
    }

    /**
     * Delete 方式调用 默认UTF8编码
     * @param url
     * @param params 采用inputstrem流接收参数
     * @return
     */
    public static String URLDelete(String url) {
        log.debug(" -- Http call url : " + url);
        String response = EMPTY;
        DeleteMethod deleteMethod = null;
        try {
            deleteMethod = new DeleteMethod(url);
            int statusCode = client.executeMethod(deleteMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = deleteMethod.getResponseBodyAsString();
                log.debug("response = " + response);
            }
            else {
                log.error("响应状态码 = " + deleteMethod.getStatusCode());
                log.error("response = " + deleteMethod.getResponseBodyAsString());
            }
            log.debug(" -- Http call result : " + response);
        }
        catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        }
        catch (IOException e) {
            log.error("发生网络异常", e);
        }
        finally {
            if (deleteMethod != null) {
                deleteMethod.releaseConnection();
                deleteMethod = null;
            }
        }
        return response;
    }

    /**
     * put 方式调用 默认UTF8编码
     * @param url
     * @param params 采用inputstrem流接收参数
     * @return
     */
    public static String URLPut(String url, String params) {
        log.debug(" -- Http call url : " + url);
        log.debug(" -- Http call params : " + params);
        String response = EMPTY;
        PutMethod putMethod = null;
        try {
            putMethod = new PutMethod(url);
            putMethod.setRequestEntity(new StringRequestEntity(params, "application/json", URL_PARAM_DECODECHARSET_UTF8));
            int statusCode = client.executeMethod(putMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = putMethod.getResponseBodyAsString();
                log.debug("response = " + response);
            }
            else {
                log.error("响应状态码 = " + putMethod.getStatusCode());
                log.error("response = " + putMethod.getResponseBodyAsString());
            }
            log.debug(" -- Http call result : " + response);
        }
        catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        }
        catch (IOException e) {
            log.error("发生网络异常", e);
        }
        finally {
            if (putMethod != null) {
                putMethod.releaseConnection();
                putMethod = null;
            }
        }
        return response;
    }

    /**
    * POST方式提交数据
    * @param url 待请求的URL
    * @param params 要提交的数据
    * @param enc 编码
    * @return 响应结果
    * @throws IOException  IO异常
    */
    public static String URLPost(String url, Map<String, String> params, String enc) {
        log.debug(" -- Http call url : " + url);
        log.debug(" -- Http call parmas : " + params);
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //将表单的值放入postMethod中
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                String value = params.get(key);
                postMethod.addParameter(key, value);
            }
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
                log.debug("response = " + response);
            }
            else {
                log.error("响应状态码 = " + postMethod.getStatusCode());
            }
            log.debug(" -- Http call result : " + response);
        }
        catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        }
        catch (IOException e) {
            log.error("发生网络异常", e);
        }
        finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return response;
    }

    /**
     * POST方式提交数据 (含有文件提交的)
     * @param url 待请求的URL
     * @param params 要提交的数据
     * @param enc 编码
     * @return 响应结果
     * @throws IOException  IO异常
     */
    public static String URLPost(String url, Map<String, String> params, Map<String, String> files) {
        log.debug(" -- Http call url : " + url);
        log.debug(" -- Http call parmas : " + params);
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            //postMethod.setRequestHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            // postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + URL_PARAM_DECODECHARSET_UTF8);
            //将表单的值放入postMethod中
            Set<String> keySet = params.keySet();
            Part[] parts = new Part[params.size() + files.size()];
            int i = 0;
            for (String key : keySet) {
                parts[i] = new StringPart(key, params.get(key));
                i++;
            }
            Set<String> filekeySet = files.keySet();
            for (String key : filekeySet) {
                parts[i] = new FilePart(key, new File(files.get(key)));
                i++;
            }
            postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
                log.debug("response = " + response);
            }
            else {
                log.error("响应状态码 = " + postMethod.getStatusCode());
            }
            log.debug(" -- Http call result : " + response);
        }
        catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        }
        catch (IOException e) {
            log.error("发生网络异常", e);
        }
        finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return response;
    }

    /**
    * GET方式提交数据
    * @param url 待请求的URL
    * @param params 要提交的数据
    * @param enc 编码
    * @return 响应结果
    * @throws IOException IO异常
    */
    public static String URLGet(String url, Map<String, String> params, String enc, Map<String, String> header) {
        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(EMPTY);
        strtTotalURL.append(url);
        if (MapUtils.isNotEmpty(params)) {
            if (url.indexOf("?") == -1) {
                strtTotalURL.append("?").append(getUrl(params, enc));
            }
            else {
                strtTotalURL.append("&").append(getUrl(params, enc));
            }
        }
        log.debug("GET请求URL = \n" + strtTotalURL.toString());

        try {
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            if (header != null) {
                for (Entry<String, String> entry : header.entrySet()) {
                    getMethod.setRequestHeader(entry.getKey(), entry.getValue());
                }
            }
            //执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = getMethod.getResponseBodyAsString();
                log.debug("response = " + response);
            }
            else {
                log.debug("响应状态码 = " + getMethod.getStatusCode());
                log.debug("response = " + getMethod.getResponseBodyAsString());
            }
        }
        catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        }
        catch (IOException e) {
            log.error("发生网络异常", e);
        }
        finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }
        return response;
    }

    /**
    * 据Map生成URL字符串
    * @param map Map
    * @param valueEnc URL编码
    * @return URL
    */
    private static String getUrl(Map<String, String> map, String valueEnc) {
        if (null == map || map.keySet().size() == 0) {
            return (EMPTY);
        }
        valueEnc = StringUtils.defaultIfEmpty(valueEnc, URL_PARAM_DECODECHARSET_UTF8);
        StringBuffer url = new StringBuffer();
        Set<String> keys = map.keySet();
        for (Iterator<String> it = keys.iterator() ; it.hasNext() ;) {
            String key = it.next();
            if (map.containsKey(key)) {
                String val = map.get(key);
                String str = val != null ? val : EMPTY;
                try {
                    str = URLEncoder.encode(str, valueEnc);
                }
                catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                }
                url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
            }
        }
        String strURL = EMPTY;
        strURL = url.toString();
        if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1))) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }
        return (strURL);
    }

    /**
     * 下载图片
     * 
     * @param request
     * @return
     */
    public static void dowloadImage(String imgurl, String file) {
        log.debug("GET请求URL = \n" + imgurl);
        GetMethod getMethod = null;
        try {
            getMethod = new GetMethod(imgurl);
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                InputStream inputStream = getMethod.getResponseBodyAsStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.close();

                byte[] getData = bos.toByteArray();
                File imageFile = new File(file);
                FileOutputStream fos = new FileOutputStream(imageFile);
                fos.write(getData);
                fos.close();
            }
            else {
                log.debug("响应状态码 = " + getMethod.getStatusCode());
                log.debug("response = " + getMethod.getResponseBodyAsString());
            }
        }
        catch (HttpException e) {
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
        }
        catch (IOException e) {
            log.error("发生网络异常", e);
        }
        finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }
    }
}
