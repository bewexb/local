package com.asiainfo.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
 
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;
 
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] requestBody = null;
    
    // 传入是JSON格式 转换成JSONObject
    public JSONObject getRequestBody() throws UnsupportedEncodingException {
        return JSON.parseObject((new String(requestBody, "UTF-8")));
    }
 
    public void setRequestBody(JSONObject jsonObject) throws UnsupportedEncodingException {
        this.requestBody = jsonObject.toJSONString().getBytes("UTF-8");
    }
 
    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (requestBody == null) {
            requestBody = new byte[0];
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
 
            @Override
            public boolean isFinished() {
                return false;
            }
 
            @Override
            public boolean isReady() {
                return true;
            }
 
            @Override
            public void setReadListener(ReadListener listener) {
 
            }
        };
    }
 
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}