package cn.dofuntech.wechat.msg.req;

/**
 * 文本消息
 * 
 * @author lxu 
 * @date 2015-01-19
 */
public class TextMessage extends BaseMessage {

    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}