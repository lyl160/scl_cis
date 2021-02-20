package cn.dofuntech.mina.process;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.core.util.ErrorInfo;
import cn.dofuntech.core.util.json.Jacksons;
import cn.dofuntech.core.util.spring.SpringContextUtil;
import cn.dofuntech.mina.bo.AuthticationBo;
import cn.dofuntech.mina.process.domain.HandleResult;
import cn.dofuntech.mina.protocol.domain.MsgPack;
import cn.dofuntech.mina.protocol.parse.PackBuildUtil;
import cn.dofuntech.mina.protocol.parse.PackParseUtil;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:AbstractHandle.java 
 */
public abstract class AbstractHandle {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract boolean canDo(MsgPack msgPack);

    protected abstract HandleResult handle(MsgPack msgPack, IoSession session);

    public HandleResult doHandle(MsgPack msgPack, IoSession session) {
        HandleResult result = null;
        try {
            //请求包与信息包首先进行鉴权
            if (PackParseUtil.isRequest(msgPack) || PackParseUtil.isInfoResponse(msgPack)) {
                AuthticationBo authticationBo = SpringContextUtil.getBean(AuthticationBo.class);
                Map<String, Object> data = Jacksons.me().json2Map(msgPack.getData());
                String token = MapUtils.getString(data, "token");
                long orgId = MapUtils.getLongValue(data, "orgId");
                ErrorInfo errorInfo = authticationBo.doAuth(token, orgId, "");
                if (errorInfo.getStatus() == 1) {
                    result = this.handle(msgPack, session);
                }
                else {
                    String errorinfo = Jacksons.me().readAsString(errorInfo.getObject());
                    result = new HandleResult(PackBuildUtil.buildNCKPack(msgPack.getMsgCw(), errorinfo));
                }
            }
            else {
                result = this.handle(msgPack, session);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            //返回异常包
            result = new HandleResult(PackBuildUtil.buildNCKPack(msgPack.getMsgCw()));
        }
        return result;
    }
}
