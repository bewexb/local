package com.asiainfo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

public class LocalCoreApplication {

    @Autowired
    private Properties properties = new Properties();

    private static final String FILE_NAME = "application-localCore.properties";

    private LocalCoreApplication() {
        load();
    }

    public static final LocalCoreApplication getInstance() {
        return LazyHolder.lazy;
    }

    private static class LazyHolder {
        private static final LocalCoreApplication lazy = new LocalCoreApplication();
    }


    private void load() {
        InputStream in = null;
        try {
            //使用spring资源加载器加载
            ClassPathResource resource = new ClassPathResource(FILE_NAME);
            in = resource.getInputStream();
            if (in == null) {
            } else {
                properties.load(in);
            }
            //注意，多模块中，下面这种方法加载不到文件，只适合于java单模块
//            String file_path = this.getClass().getClassLoader().getResource(FILE_NAME).getPath().toString();
//            in = new FileInputStream(new File(file_path));
//            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
