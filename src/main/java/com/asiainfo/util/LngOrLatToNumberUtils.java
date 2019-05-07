package com.asiainfo.util;

public class LngOrLatToNumberUtils {
    /**
     *北纬N39°34′14.95″ 东经E116°34′52.18″
     *化为小数点的度为：39.5708181173,116.5811614825
     *具体化法：39+34÷60+14.95÷3600=39.5708181173
     */
    public static String lngOrLatToDouble(String str) {
        //String str="39°10′32\"";
        Double result=0.0;
        if(str.contains("°")){
            String[] first = str.split("°");
            result+=Double.parseDouble(first[0]);
            if(first[1].contains("′")){
                String[] second = first[1].split("′");
                result+=Double.parseDouble(second[0])/60;
                if(second[1].contains("\"")){
                    String[] third=second[1].split("\"");
                    result+=Double.parseDouble(third[0])/3600;
                }

            }

        }
        //System.out.println(result);
        return result.toString();

    }
}
