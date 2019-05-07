package com.asiainfo.util;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by jiaWW on 2017/11/29.
 */
public class CollectionUtils {
    /*
     集合深拷贝,map集合只能拷贝集合的实践者，不能传入父类引用
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj) {

        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clonedObj;
    }

    /**
     * list集合深度复制
     *
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * 随机在集合中获取几个值
     *
     * @param infos
     * @param count
     * @return
     */
    public static List randomGetList(List infos, int count) {
        List result = new ArrayList();
        int size = infos.size();

        int[] str = getRandomNumber(size, count);
        for (int i : str) {
            result.add(infos.get(i));
        }
        return result;

    }

    /**
     * 随机在集合中获取几个值
     *
     * @param infos
     * @return
     */
    public static List randomGetList(List infos) {
        int size = infos.size();
        int count = (int) (Math.random() * size % size);
        List result = new ArrayList();

        int[] str = getRandomNumber(size, count);
        for (int i : str) {
            result.add(infos.get(i));
        }
        return result;

    }

    /**
     * @author 贾武伟
     * 2：给出0-100个数size，随机取五个count
     */
    public static int[] getRandomNumber(int size, int count) {
        /*//如果班级容量<<学生人数
        if(size>=count){
            finalCount=count;
        }else{
            finalCount=size;
        }*/
        Random r = new Random();
        int[] temp = new int[count];
        for (int i = 0; i < count; ) {
            temp[i] = (int) (Math.random() * size % size);
            if (checkRepeat(temp, temp[i], i)) {
                i++;
            }

        }
        return temp;
    }

    /**
     * @return
     * @author 贾武伟
     * 3：检查随机数是否重复
     */
    public static boolean checkRepeat(int[] temp, Integer t, Integer index) {
        for (int j = 0; j < temp.length; j++) {
            if (temp[j] == t && index != j) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取[0-max]之间的值
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomGetNumber(int min, int max) {
        if (min == 0) {
            //return random.nextInt(max+1);
            return (int) (Math.random() * (max + 1) % (max + 1));
        } else {
            //return random.nextInt(max)%(max-min+1) + min;
            //(最小值+Math.random()*(最大值-最小值+1))
            return (int) (min + Math.random() * (max - min + 1));

        }
    }

    /**
     * 平均拆分集合
     *
     * @param sumZs
     * @return
     */
    public static int[] divideNumber(int sumZs, int averageSize) {
        if (sumZs <= averageSize) {
            int[] x = new int[1];
            x[0] = sumZs;
            return x;
        }
        //将学生分成几个均匀的班级
        int count = 1;
        int studentCount = 0;//每个班的平均人数
        int model = 0;
        while (true) {
            studentCount = sumZs / count;//平均分的每个班的学生人数
            model = sumZs % count;
            if (studentCount <= averageSize) {
                break;
            }
            count++;
        }
        int[] x = new int[count];
        //首先给x赋值
        for (int i = 0, length = x.length; i < length; i++) {
            x[i] = studentCount;
        }
        //余数肯定小于count

        for (int i = 0; i < model; i++) {
            //遍历到的班级每个加1，只给model个班加1
            x[i] += 1;
        }
        return x;
    }

    /**
     * 求n的阶乘
     *
     * @return
     */
    public static BigDecimal calN(int n) {
        BigDecimal result = new BigDecimal(1);
        BigDecimal i_value =null;
        for (int i = 1; i <= n; i++) {
            i_value = new BigDecimal(i);
            result = result.multiply(i_value);
        }
        return result;
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

}
