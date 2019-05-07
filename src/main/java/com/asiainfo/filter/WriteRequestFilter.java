package com.asiainfo.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import com.asiainfo.common.Response;
import com.asiainfo.common.ResponseEnum;
import com.asiainfo.config.LocalCoreApplication;
import com.google.gson.Gson;

@WebFilter(filterName="writeRequestFilter",urlPatterns="/*")
@Order(value = 1)
public class WriteRequestFilter implements Filter{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String securityAddress = LocalCoreApplication.getInstance().getProperty("security.address");

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String[] securityAddressCell = securityAddress.split(";");
        
        String remoteIP = getIpAddress((HttpServletRequest)request);
//        boolean flag = false;
        boolean flag = true;
        for(String cell : securityAddressCell)
        {
        	if(cell.equals(remoteIP))
        	{
        		flag = true;
        		break;
        	}
        }
        if(flag){
        	chain.doFilter(request, response);
        }else{
        	Response respData = new Response();
        	respData.setRespCode(ResponseEnum.WRITE_IP_EXCEPTION.getResp_code());
        	respData.setRespDesc(ResponseEnum.WRITE_IP_EXCEPTION.getResp_desc());
        	
        	Gson gs = new Gson();
        	String objectStr = gs.toJson(respData);
        	
        	ServletOutputStream out = response.getOutputStream();

        	OutputStreamWriter ow = new OutputStreamWriter(out,"GB2312");
        	//utf-8编码，返回前台乱码
        	ow.write(objectStr);
        	ow.flush();
        	ow.close();
        }
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 */
	private String getIpAddress(HttpServletRequest request) throws IOException {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
 
		String ip = request.getHeader("X-Forwarded-For");
		if (logger.isInfoEnabled()) {
			logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
		}
 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
				}
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}
}
