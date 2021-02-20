package cn.dofuntech.core.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.tools.Barcode4jUtil;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2018 dofuntech. All Rights Reserved.</font>
 * @author lxu(2018年4月9日)
 * @version 1.0
 * filename:QCodeServlet.java 
 */
public class QCodeServlet extends HttpServlet {

    /**
     * 
     */
    private static final long   serialVersionUID = 1L;

    private static final Logger LOGGER           = LoggerFactory.getLogger(QCodeServlet.class);

    public QCodeServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug(">>>>>>>>>>>>开始生成条形码");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        String sign = request.getParameter("barcode");
        Barcode4jUtil.generate(sign, response.getOutputStream());
        LOGGER.debug(">>>>>>>>>>>>开始生成条形码结束");
    }

    public void init(ServletConfig config) throws ServletException {
    }

}
