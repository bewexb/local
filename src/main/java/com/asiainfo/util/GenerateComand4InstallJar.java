package com.asiainfo.util;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

;

/**
 * 功能:生成maven批量安装jar的语句 *
 *
 * @author jiaww5
 */
public class GenerateComand4InstallJar {
    //批量安装jar所在目录

    public static String filePath = "/Users/10100100010b/asiainfo/IdeaProjects/work/xx/esaleIntf/wo-esale/src/main/webapp/WEB-INF/lib";
        public static final String filter ="wuzhhXmlBean";
    public static void main(String[] args) {
        LinkedList<File> linkedList = new LinkedList<File>();
        File f = new File(filePath);
        File[] file = f.listFiles();
        for (int i = 0; i < file.length; i++) {
            linkedList.add(file[i]);
        }
        LinkedList<File> clone = CollectionUtils.clone(linkedList);

        //生成安装命令
        printFile(linkedList,filter);
        System.out.println("\r\n");
        System.out.println("\r\n");
        System.out.println("\r\n");
        //生成maven坐标
        printFiles(clone,filter);
    }

    /**
     * 功能:批量安装maven的本地的jar
     *
     * @param
     */
    public static void printFile(LinkedList<File> linkedList, String filter) {
        String sb = "mvn install:install-file -Dfile=" + filePath + "/AA.jar -DgroupId=com.asiainfo -DartifactId=com.asiainfo.AA -Dversion=1.0.0 -Dpackaging=jar";
        for (Iterator<File> iterator = linkedList.iterator();
             iterator.hasNext(); ) {
            File file = iterator.next();
            if (file.isFile()) {
                String fileName = file.getName();
                if (StringUtils.isNotBlank(filter)) {
                    if (fileName.contains(filter)) {
                        System.out.println(sb.replace("AA", fileName.substring(0, fileName.lastIndexOf("."))) + "\r\n");
                    }
                } else {
                    System.out.println(sb.replace("AA", fileName.substring(0, fileName.lastIndexOf("."))) + "\r\n");
                }

                linkedList.remove(file);
                printFile(linkedList, filter);
            } else {
                break;
            }
        }
    }

    /**
     * 功能:批量引入maven的pom
     */
    public static void printFiles(LinkedList<File> linkedList, String filter) {
        String sb = "<dependency><groupId>com.asiainfo</groupId><artifactId>com.asiainfo.BBB</artifactId><version>1.0.0</version><type>jar</type></dependency>";
        for (Iterator<File> iterator = linkedList.iterator(); iterator.hasNext(); ) {
            File file = iterator.next();
            if (file.isFile()) {
                String fileName = file.getName();
                if (StringUtils.isNotBlank(filter)) {
                    if(fileName.contains(filter)){
                        System.out.println(sb.replace("BBB", fileName.substring(0, fileName.lastIndexOf("."))));
                    }
                }else{
                    System.out.println(sb.replace("BBB", fileName.substring(0, fileName.lastIndexOf("."))));
                }
                linkedList.remove(file);
                printFiles(linkedList,filter);
            } else {
                break;
            }
        }
    }
}

