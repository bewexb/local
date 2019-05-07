package com.asiainfo.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;

import com.asiainfo.common.Response;
import com.asiainfo.common.ResponseEnum;
import com.asiainfo.exceptions.BasicException;
import com.asiainfo.servlet.BodyReaderHttpServletRequestWrapper;
import com.asiainfo.util.HttpHelper;
import com.asiainfo.util.JwtTokenUtil;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;

/*
 *  如果前台同时传了customStaffInfo和_token，则最终的staffInfo用的是
 *  _token解析出来的staffInfo，而不是用的customStaffInfo
 */
@WebFilter(filterName="jwtRequestFilter",urlPatterns="/*")
@Order(value = 3)
public class JWTRequestFilter implements Filter{
	
	public void destroy() {
	}
	
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;
    	String reqMethod = hreq.getMethod();
    	if("POST".equals(reqMethod)){
    		
    		PrintWriter out = null; 
    		HttpServletResponse response = (HttpServletResponse) res;
    		response.setCharacterEncoding("UTF-8");  
		    response.setContentType("application/json; charset=utf-8");  
		    
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hreq);
			String body = HttpHelper.getBodyString(requestWrapper);
		    
    		//如果是POST请求则需要获取 param 参数
//			String param = URLDecoder.decode(body,"utf-8");
    		//json串 转换为Map
//	        if(param != null && param.contains("=")){
//	        	param = param.split("=")[1];
//    		}
	        Gson gson = new Gson();
	        Map<String, Object> paramMap = gson.fromJson(body, Map.class);
    		
    		if(paramMap == null){
    			chain.doFilter(requestWrapper, res);
    		}
    		else{
    			String token = (String) paramMap.get("_token");
        		if(token != null){
        			parseToken(token, req, res, chain);
        			
        			chain.doFilter(requestWrapper, res);
    			}else{
    				chain.doFilter(requestWrapper, res);	
    			}
    		}
    	}else{
    		//get请求直接放行
    		chain.doFilter(req, res);
    	}
	}

	
	public void parseToken(String token, ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if(token != null){
			Claims claims = null;
			try {
				claims = JwtTokenUtil.phaseToken(token);
			} catch (Exception e) {
				Response respData = new Response();
	        	respData.setRespCode(ResponseEnum.JWT_TOKEN_EXCEPTION.getResp_code());
	        	respData.setRespDesc(ResponseEnum.JWT_TOKEN_EXCEPTION.getResp_desc());
	        	
//	        	throw new BasicException("解析token异常，请确认token是否正确");
	        	
	        	Gson gs = new Gson();
	        	String objectStr = gs.toJson(respData);
	        	
	        	ServletOutputStream out = response.getOutputStream();

	        	OutputStreamWriter ow = new OutputStreamWriter(out,"GB2312");
	        	//utf-8编码，返回前台乱码
	        	ow.write(objectStr);
	        	ow.flush();
	        	ow.close();
			}
			
			if(claims != null){
				Iterator iterator = claims.entrySet().iterator();
				
				while(iterator.hasNext()){
					Map.Entry entry = (Map.Entry) iterator.next(); 
					request.setAttribute((String) entry.getKey(), entry.getValue());
				}
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
