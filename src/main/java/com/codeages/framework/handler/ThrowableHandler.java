package com.codeages.framework.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codeages.framework.exception.ServiceException;
import com.codeages.framework.response.ResponseWrapper;

@ControllerAdvice
public class ThrowableHandler{
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseWrapper jsonErrorHandler(HttpServletRequest req, MethodArgumentNotValidException e)
			throws Exception {
		ResponseWrapper err = new ResponseWrapper<List>(e.getBindingResult().getAllErrors());
		err.setMessage("数据校验失败！");
		err.setStatus(String.valueOf(HttpServletResponse.SC_NOT_ACCEPTABLE));
		return err;
	}
	
	@ExceptionHandler(value = ServiceException.class)
	@ResponseBody
	public ResponseWrapper jsonErrorHandler(HttpServletRequest req, ServiceException e) {
		ResponseWrapper err = new ResponseWrapper<String>(e.getMessage());
		err.setMessage(e.getMessage());
		err.setStatus(e.getCode());
		return err;
	}
	
	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseBody
	public ResponseWrapper accessDeniedExceptionHandler(HttpServletRequest req, AccessDeniedException e){
		ResponseWrapper err = new ResponseWrapper<String>(e.getMessage());
		err.setMessage("不允许访问");
		err.setStatus(String.valueOf(HttpServletResponse.SC_FORBIDDEN));
		return err;
	}
	
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResponseWrapper throwableHandler(HttpServletRequest req, Throwable e) throws Exception {
		e.printStackTrace();
		ResponseWrapper err = new ResponseWrapper<String>(e.getMessage());
		err.setMessage("服务器异常");
		err.setStatus(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
		return err;
	}
}
