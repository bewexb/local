package com.asiainfo.common;

public enum ResponseEnum {

	SUCCESS("0", "成功"), 
	SYSTEM_EXCEPTION("-1", "系统异常，联系相关人员查看后台报错日志"),
	BASIC_EXCEPTION("-100", "基础支持异常"),
	WRITE_IP_EXCEPTION("-101", "请求地址不在白名单中，请求被拒绝"),
	JWT_TOKEN_EXCEPTION("-102", "解析token异常，请确认token是否正确"),
	BUSINESS_EXCEPTION("-200", "业务异常");
	
	private String resp_code;
	
	private String resp_desc;
	
    private ResponseEnum(String resp_code, String resp_desc) {  
        this.resp_code = resp_code;  
        this.resp_desc = resp_desc;  
    }

	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_desc() {
		return resp_desc;
	}

	public void setResp_desc(String resp_desc) {
		this.resp_desc = resp_desc;
	} 
	
}
