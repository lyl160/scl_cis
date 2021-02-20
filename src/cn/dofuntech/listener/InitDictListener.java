/**
 * <p> * Title: 叶明开发的基础模块代码*</p>
 * <p> * Description: * </p>
 * <p> * Copyright: Copyright (c) 2012-2016* </p>
 * <p> * Company: java工作者 * </p>
 * @author 叶明（开发）
 * @version 3.1
 */
package cn.dofuntech.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.dofuntech.dfauth.service.DictService;

/**spring启动加载数据字典监听
 * @author luokai
 *
 */
@SuppressWarnings("serial")
public class InitDictListener extends HttpServlet implements ServletContextListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(InitDictListener.class);

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
        if (null != wac) {
            LOGGER.info("初始化数据字典===========开始");
            DictService dictAction = wac.getBean(DictService.class);
            dictAction.queryDict();
            LOGGER.info("初始化数据字典===========结束");
        }

    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
