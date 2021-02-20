package cn.dofuntech.core.web.servlet;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import cn.dofuntech.core.util.web.CookiesUtil;
import cn.dofuntech.core.util.web.WebUtil;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月5日)
 * @version 1.0
 * filename:ValidateNumbeByCookies.java 
 */
public class ValidateNumbeByCookies extends HttpServlet {

    private int type;
    private int width;
    private int height;

    public ValidateNumbeByCookies() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        //        String sign = request.getParameter("sign");
        double sign = 0;
        for (int i = 0 ; i < 40 ; i++) {
            sign = sign + Math.round(Math.random());
        }
        ;
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics2D g = image.createGraphics();
        Random random = new Random();
        g.setColor(WebUtil.getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", 0, 24));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(3, 0.99F));
        g.setColor(WebUtil.getRandColor(160, 200));
        for (int i = 0 ; i < 180 ; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String numberString = RandomStringUtils.randomAlphabetic(4).toUpperCase();
        if (type == 2) {
            numberString = RandomStringUtils.randomAlphabetic(4).toUpperCase();
            numberString = numberString.replaceAll("0", "W");
            numberString = numberString.replaceAll("o", "R");
            numberString = numberString.replaceAll("i", "E");
            numberString = numberString.replaceAll("1", "T");
        }
        else if (type == 3) {
            numberString = RandomStringUtils.randomNumeric(4).toUpperCase();
        }
        else {
            numberString = numberString.replaceAll("0", "W");
            numberString = numberString.replaceAll("o", "R");
            numberString = numberString.replaceAll("i", "E");
            numberString = numberString.replaceAll("1", "T");
        }
        System.out.println((new StringBuilder("numberString=")).append(numberString).toString());
        for (int i = 0 ; i < numberString.length() ; i++) {
            String rand = String.valueOf(numberString.subSequence(i, i + 1));
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            int iSize = 20 + random.nextInt(4);
            g.drawString(rand, iSize * i + 3, 24);
        }

        request.getSession().setAttribute("ValidateNumber", numberString);
        g.dispose();
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    public void init(ServletConfig config) throws ServletException {
        String stype = config.getInitParameter("type");
        if (StringUtils.isBlank(stype))
            type = NumberUtils.toInt(stype);
        else
            type = 1;
        String swidth = config.getInitParameter("width");
        if (StringUtils.isBlank(swidth))
            width = NumberUtils.toInt(swidth);
        else
            width = 90;
        String sheight = config.getInitParameter("height");
        if (StringUtils.isBlank(sheight))
            height = NumberUtils.toInt(sheight);
        else
            height = 30;
    }

}
