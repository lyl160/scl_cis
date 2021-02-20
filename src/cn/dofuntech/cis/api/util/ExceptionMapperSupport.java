package cn.dofuntech.cis.api.util;

import javassist.NotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

/**
 * jersey统一异常处理器
 * 
 * @author Administrator
 *
 */
@Provider
@ResponseBody
public class ExceptionMapperSupport implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = Logger
			.getLogger(ExceptionMapperSupport.class);

	private static final String CONTEXT_ATTRIBUTE = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

	@Context
	private HttpServletRequest request;

	@Context
	private ServletContext servletContext;

	/**
	 * 异常处理
	 * 
	 * @param exception
	 * @return 异常处理后的Response对象
	 */
	public Response toResponse(Exception exception) {
		String message = "发生异常";
		Status statusCode = Status.INTERNAL_SERVER_ERROR;
		WebApplicationContext context = (WebApplicationContext) servletContext
				.getAttribute(CONTEXT_ATTRIBUTE);
		// 处理unchecked exception
		if (exception instanceof BaseException) {
			BaseException baseException = (BaseException) exception;
			String code = baseException.getCode();
			Object[] args = baseException.getValues();
			message = context.getMessage(code, args, exception.getMessage(),
					request.getLocale());

		} else if (exception instanceof NotFoundException) {
			message = "REQUEST_NOT_FOUND";
			statusCode = Status.NOT_FOUND;
		} else if (exception instanceof DaoException){
			DaoException daoException = (DaoException) exception;
			String code = daoException.getCode();
			Object[] args = daoException.getValues();
			message = context.getMessage(code, args, daoException.getMessage(),
					request.getLocale());
		} else if (exception instanceof ServiceException){
			ServiceException serviceException = (ServiceException) exception;
			String code = serviceException.getCode();
			Object[] args = serviceException.getValues();
			message = context.getMessage(code, args, serviceException.getMessage(),
					request.getLocale());
		}
		// checked exception和unchecked exception均被记录在日志里
		LOGGER.error(message, exception);
		return Response.ok(message, MediaType.APPLICATION_JSON)
				.status(statusCode).build();
	}

}
