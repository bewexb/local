package com.asiainfo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json工具类
 *
 * @author jiaww5
 */
public class JsonHelper {
    /**
     * 将对象序列换成json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = create();

        return gson.toJson(obj);
    }

    private static Gson create() {
        GsonBuilder gb = new GsonBuilder();
        // gb.registerTypeAdapter(java.util.Date.class, new
        // DateSerializer()).setDateFormat(DateFormat.LONG);
        // gb.registerTypeAdapter(java.util.Date.class, new
        // DateDeserializer()).setDateFormat(DateFormat.LONG);
        Gson gson = gb.create();

        return gson;
    }

    /**
     * 将json转换成对象
     *
     * @param jsonData
     * @param type
     * @return
     */
    public static <T extends Object> T fromJson(String jsonData, Type type) {
        Gson gson = create();
        T t = gson.fromJson(jsonData, type);
        return t;
    }

    /**
     * 将json反序列化成数据
     *
     * @param jsonData
     * @param type
     * @return
     */
/*    public static <T extends Object> List<T> fromJson(String jsonData, Type type) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<T> list = gson.fromJson(jsonData, type);

        return list;
    }*/

    public static <V, M> Map<V, M> fromJsonToMap(String jsonData, Type type) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        HashMap<V, M> map = gson.fromJson(jsonData, type);

        return map;
    }


    /**
     * @Author: jiaww5
     * @Date: 2018/11/12 16:39
     * @Create By: jiaww5
     * @Descirption: 递归调用获取叶子节点的key
     */
    public static void getJsonKeys(List<String> keys, Object nodes) {
        //如果是Map节点
        if (nodes instanceof Map) {
            Map nodes1 = (Map) nodes;
            for (Object key : nodes1.keySet()) {
                Object node = nodes1.get(key);
                if (node instanceof String) {
                    //如果是叶子节点
                    keys.add((String) key);
                } else {
                    //如果是map节点
                    getJsonKeys(keys, ((Map) nodes).get(key));
                }
            }
        } else if (nodes instanceof List) {
            //如果是List节点
            for (Object node : (List) nodes) {
                getJsonKeys(keys, node);
            }

        }
    }

    /**
     * @Author: jiaww5
     * @Date: 2018/11/12 19:40
     * @Create By: jiaww5
     * @Descirption: 根据row，递归填充叶子节点value
     * row为一维map结构，nodes为复杂的Map结构
     */
    public static void fillPacketMap(Map<String,Object> row, Object nodes) {
        if (nodes instanceof Map) {
            Map nodes1 = (Map) nodes;
            for (Object key : nodes1.keySet()) {
                Object node = nodes1.get(key);
                if (node instanceof String) {
                    //如果是叶子节点,使用row中的数据填充叶子节点
                    nodes1.put(key,row.get(key));
                } else {
                    //如果是map节点
                    fillPacketMap(row, ((Map) nodes).get(key));
                }
            }
        } else if (nodes instanceof List) {
            for (Object node : (List) nodes) {
                fillPacketMap(row, node);
            }
        }
    }
}
