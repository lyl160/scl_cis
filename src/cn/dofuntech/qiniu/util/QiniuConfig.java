package cn.dofuntech.qiniu.util;

import org.springframework.beans.factory.annotation.Value;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年8月24日)
 * @version 1.0
 * filename:QiniuConfig.java 
 */
public class QiniuConfig {

    @Value("${qiniu.token}")
    private String token;

    @Value("${qiniu.domain}")
    private String domain;

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

}
