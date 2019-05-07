package com.asiainfo.filter;

import com.asiainfo.common.Response;
import com.asiainfo.common.ResponseEnum;
import com.asiainfo.config.LocalCoreApplication;
import com.asiainfo.servlet.BodyReaderHttpServletRequestWrapper;
import com.asiainfo.util.HttpHelper;
import com.asiainfo.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.util.*;

/*
 *  处理前台传入的个性化工号信息，将数据以staffInfo重新放入request中，
 *  但是如果请求中，同时传入了_token，则staffInfo将会重新被token中的
 *  staffInfo覆盖
 */
@WebFilter(filterName="customStaffInfoRequestFilter",urlPatterns="/*")
@Order(value = 2)
public class CustomStaffInfoRequestFilter implements Filter{
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest hreq = (HttpServletRequest) req;

		ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hreq);
		String body = HttpHelper.getBodyString(requestWrapper);

		Gson gson = new Gson();
		Map paramMap = gson.fromJson(body, Map.class);

		if(paramMap == null){
			chain.doFilter(requestWrapper, resp);
		}
		else{
			Map staffInfo = new LinkedHashMap();

			Map customStaffInfo = (Map) paramMap.get("customStaffInfo");
			if(customStaffInfo != null){
				Iterator<Map.Entry<String, Blob>> i = customStaffInfo.entrySet().iterator();
				while(i.hasNext()){
					Map.Entry entry = i.next();
					staffInfo.put(entry.getKey(),entry.getValue());
				}
				req.setAttribute("staffInfo", staffInfo);
			}
			chain.doFilter(requestWrapper, resp);
		}
	}

	public void destroy() {

	}
}
