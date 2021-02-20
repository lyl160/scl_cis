package cn.dofuntech.mina.bo;

import org.springframework.stereotype.Component;

import cn.dofuntech.core.util.ErrorInfo;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月16日)
 * @version 1.0
 * filename:AuthticationBo.java 
 */
@Component
public class AuthticationBo extends AbstractBo {

    /**
     * @param token
     * @param orgId
     * @param devId
     * @return
     */
    public ErrorInfo doAuth(String token, Long orgId, String devId) {
        return null;
    }
}
