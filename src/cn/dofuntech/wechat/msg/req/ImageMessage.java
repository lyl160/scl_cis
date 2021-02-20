package cn.dofuntech.wechat.msg.req;

/**
 * 图片消息
 * 
 * @author lxu 
 * @date 2015-01-19
 */
public class ImageMessage extends BaseMessage {

    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}