package cn.dofuntech.wechat.msg.resp;

/**
 * 音乐消息
 * 
 * @author lxu 
 * @date 2015-01-19
 */
public class MusicMessage extends BaseMessage {

    // 音乐
    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}