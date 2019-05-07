package com.asiainfo.util;
import com.asiainfo.common.Response;
import com.asiainfo.common.ResponseEnum;

/**
 * @date Created on 2018/1/6
 */
public class ResponseMsgUtil {

    /**
     * 根据消息码等生成接口返回对象
     *
     * @param code 结果返回码
     * @param msg  结果返回消息
     * @param data 数据对象
     * @param <T>
     * @return
     */
    public static  <T> Response<T> build(String code, String msg, T data) {
        Response<T> res = new Response<T>();
        res.setRespCode(code);
        res.setRespDesc(msg);
        res.setData(data);
        return res;
    }

    /**
     * 请求异常返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> Response<T> exception() {

        return build(ResponseEnum.SYSTEM_EXCEPTION.getResp_code(), ResponseEnum.SYSTEM_EXCEPTION.getResp_desc(), null);
    }

    /**
     * 创建成功消息
     */

    public static <T> Response<T> buildSuccess(T data){
        return build(ResponseEnum.SUCCESS.getResp_code(),ResponseEnum.SUCCESS.getResp_desc(), data);
    }

}
