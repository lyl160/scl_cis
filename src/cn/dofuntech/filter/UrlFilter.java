package cn.dofuntech.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * url过滤器
 * @author Administrator
 *
 */
public class UrlFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(UrlFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (request.getRequestURI().contains(".css")
                || request.getRequestURI().contains(".js")) {
        } else {
            logger.error("过滤器Filter触发,拦截url:" + request.getRequestURI());
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}
