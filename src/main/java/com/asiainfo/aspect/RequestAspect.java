package com.asiainfo.aspect;

import java.lang.reflect.Method;
import java.net.URL;
import java.text.CollationElementIterator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asiainfo.util.CollectionUtils;
import com.asiainfo.util.JsonHelper;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.asiainfo.annotation.NeedLogin;
import com.asiainfo.common.Response;
import com.asiainfo.common.ResponseEnum;
import com.asiainfo.exceptions.BasicException;
import com.google.gson.Gson;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class RequestAspect {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	//定义切点，拦截所有controller
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerAspect() {  
    }
    
    private HttpServletRequest getRequest(){
    	RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        
        return request;
    }
    
    @Around("controllerAspect()")
    public <T> T doAround(ProceedingJoinPoint pjp) throws Throwable {
    	logger.info("----------进入请求过滤切面-------------");
    	logger.info("当前执行的请求方法是：" + pjp.getSignature().getName() +"方法..."); 

    	if(isAllowLogin(pjp)){
        	before(pjp);
        	T data = (T) pjp.proceed();
        	return after(data);
    	}
        else
        {
        	throw new BasicException("请求中没带有cookie等登录信息，请重新登录");
        }
    }
    
    /*
     * 判断类或方法名上是否有注解@NeedLogin，如果有则校验request的attribute中是否有cookie的信息,
     * 没有则不允许登录
     */
    private boolean isAllowLogin(ProceedingJoinPoint pjp) throws Exception{
        String methodName=pjp.getSignature().getName();
 
        Class<?> classTarget=pjp.getTarget().getClass();
        Class<?>[] par=((MethodSignature) pjp.getSignature()).getParameterTypes();
        Method objMethod=classTarget.getMethod(methodName, par);

        if(classTarget.getAnnotation(NeedLogin.class)!=null ||
        		objMethod.getAnnotation(NeedLogin.class) != null){
        	
        	HttpServletRequest request = getRequest();
        	String cookie = (String) request.getAttribute("cookie");
        	if(StringUtils.isEmpty(cookie)){
        		return false;
        	}
        	return true;
        }
        return true;
    }
    
    
    public void before(ProceedingJoinPoint pjp) throws Exception{

        HttpServletRequest request = getRequest();
        //获取请求数据
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String className = pjp.getTarget().getClass().getName();
        String ip = request.getRemoteAddr();
        String requestMethod = pjp.getSignature().getName();
        logger.info("当前执行的请求方法是：" + className + "." + requestMethod +"()方法......");
        logger.info("请求开始, 各个参数, URL: {}, METHOD: {}, IP: {}", url, method, ip);
        //获取方法参数
        int index =1;
        for (Object object : pjp.getArgs()) {
            if (object instanceof MultipartFile || object instanceof HttpServletRequest || object instanceof HttpServletResponse) {
                continue;
            }
            //递归遍历map
            if(object instanceof Map){
                LinkedHashMap map = (LinkedHashMap) object;
                //map克隆
                LinkedHashMap clone = CollectionUtils.clone(map);
                parseInparam(clone);
                logger.info("【参数" + index + "】:" + JsonHelper.toJson(clone)+"\r\n");
            }
            index++;
        }
    }

    /**
     * 递归解析map，对于图片节点不打印日志
     * @param nodes
     */
    public static void parseInparam(Object nodes) {
        //如果是Map节点
        if (nodes instanceof Map) {
            Map nodes1 = (Map) nodes;
            for (Object key : nodes1.keySet()) {
                Object node = nodes1.get(key);
                if (node instanceof String) {
                    int length = ((String) node).length();
                    if(length>1000){
                        String substring = ((String) node).substring(0, 1000)+"......";
                        nodes1.put(key,substring);
                    }

                } else {
                    //如果是map节点
                    parseInparam(node);
                }
            }
        } else if (nodes instanceof List) {
            //如果是List节点
            int index=0;
            for (Object node : (List) nodes) {
                if (node instanceof String) {
                    int length = ((String) node).length();
                    if(length>1000){
                        String substring = ((String) node).substring(0, 1000)+"......";
                        ((List) nodes).set(index,substring);
                    }

                } else {
                    //如果是map节点
                    parseInparam(node);
                }
                index++;
            }

        }
    }
    
    public <T> T after(T data){  
        //data的值就是被拦截方法的返回值
        Gson gson = new Gson();
        logger.info("请求结束，controller的返回值是 " + gson.toJson(data));
        
    	Response response = new Response();
    	response.setRespCode(ResponseEnum.SUCCESS.getResp_code());
    	response.setRespDesc(ResponseEnum.SUCCESS.getResp_desc());
    	
    	ResponseEnum.SUCCESS.name();
    	
    	response.setData(data);
        
        return (T) response;
    }  
}
