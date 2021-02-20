package cn.dofuntech.cis.api.util;

import org.springframework.beans.factory.annotation.Value;

public class EnvUtil {

    @Value("${web.context.path}")
    private String webContextPath;

    @Value("${web.portal.path}")
    private String webPortalPath;

    @Value("${alipay.partner}")
    private String alipayPartner;

    @Value("${alipay.private.key}")
    private String alipayPrivateKey;

    @Value("${alipay.notify.url}")
    private String alipayNotifyUrl;

    @Value("${system.file.path}")
    private String systemFilePath;

    @Value("${qiniu.token}")
    private String qiniuToken;

    @Value("${qiniu.domain}")
    private String qiniuDomain;

    /**
     * @return the qiniuToken
     */
    public String getQiniuToken() {
        return qiniuToken;
    }

    /**
     * @param qiniuToken the qiniuToken to set
     */
    public void setQiniuToken(String qiniuToken) {
        this.qiniuToken = qiniuToken;
    }

    /**
     * @return the qiniuDomain
     */
    public String getQiniuDomain() {
        return qiniuDomain;
    }

    /**
     * @param qiniuDomain the qiniuDomain to set
     */
    public void setQiniuDomain(String qiniuDomain) {
        this.qiniuDomain = qiniuDomain;
    }

    public String getSystemFilePath() {
        return systemFilePath;
    }

    public void setSystemFilePath(String systemFilePath) {
        this.systemFilePath = systemFilePath;
    }

    /**
     * @return the alipayNotifyUrl
     */
    public String getAlipayNotifyUrl() {
        return alipayNotifyUrl;
    }

    /**
     * @param alipayNotifyUrl the alipayNotifyUrl to set
     */
    public void setAlipayNotifyUrl(String alipayNotifyUrl) {
        this.alipayNotifyUrl = alipayNotifyUrl;
    }

    /**
     * @return the alipayPartner
     */
    public String getAlipayPartner() {
        return alipayPartner;
    }

    /**
     * @param alipayPartner the alipayPartner to set
     */
    public void setAlipayPartner(String alipayPartner) {
        this.alipayPartner = alipayPartner;
    }

    /**
     * @return the alipayPrivateKey
     */
    public String getAlipayPrivateKey() {
        return alipayPrivateKey;
    }

    /**
     * @param alipayPrivateKey the alipayPrivateKey to set
     */
    public void setAlipayPrivateKey(String alipayPrivateKey) {
        this.alipayPrivateKey = alipayPrivateKey;
    }

    /**
     * @return the webContextPath
     */
    public String getWebContextPath() {
        return webContextPath;
    }

    /**
     * @param webContextPath the webContextPath to set
     */
    public void setWebContextPath(String webContextPath) {
        this.webContextPath = webContextPath;
    }

    /**
     * @return the webPortalPath
     */
    public String getWebPortalPath() {
        return webPortalPath;
    }

    /**
     * @param webPortalPath the webPortalPath to set
     */
    public void setWebPortalPath(String webPortalPath) {
        this.webPortalPath = webPortalPath;
    }

}
