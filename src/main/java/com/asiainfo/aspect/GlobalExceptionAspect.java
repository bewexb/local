package com.asiainfo.aspect;

import com.asiainfo.util.ExceptionOutputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.asiainfo.common.Response;
import com.asiainfo.common.ResponseEnum;
import com.asiainfo.exceptions.BasicException;
import com.asiainfo.exceptions.BusinessException;

import javax.servlet.http.HttpServletRequest;

/** 
 * Created by jack on 2017/8/24. 
 * 统一异常处理 
 */  
@RestControllerAdvice  
public class GlobalExceptionAspect {
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionAspect.class);
    @ExceptionHandler(value = Exception.class)  
    public Response defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    	logger.error(ExceptionOutputUtil.errInfo(e));
//    	e.printStackTrace();

    	Response response = new Response();
    	
    	if(e instanceof BusinessException){
        	response.setRespCode(ResponseEnum.BUSINESS_EXCEPTION.getResp_code());
        	if("".equals(e.getMessage().trim())){
        		response.setRespDesc(ResponseEnum.BUSINESS_EXCEPTION.getResp_desc());
        	}
        	else{
        		response.setRespDesc(e.getMessage().trim());
        	}
    	}
    	else if(e instanceof BasicException){
        	response.setRespCode(ResponseEnum.BASIC_EXCEPTION.getResp_code());
        	if("".equals(e.getMessage().trim())){
        		response.setRespDesc(ResponseEnum.BASIC_EXCEPTION.getResp_desc());
        	}
        	else{
        		response.setRespDesc(e.getMessage().trim());
        	}
    	}
    	else{
        	response.setRespCode(ResponseEnum.SYSTEM_EXCEPTION.getResp_code());
        	response.setRespDesc(ResponseEnum.SYSTEM_EXCEPTION.getResp_desc());
    	}
		return response;
    }  
  
  
} 
