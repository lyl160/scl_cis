package cn.dofuntech.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {

    public static final Map<String, String> paramsMap = new HashMap<String, String>();

    public static final Logger              logger    = LoggerFactory.getLogger(PropertyUtil.class);

    static {
        new PropertyUtil().processProperties();
    }

    public void processProperties() {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = getClass().getClassLoader();
            inputStream = classLoader.getResourceAsStream("config.properties");
            properties.load(inputStream);
            for (Object key : properties.keySet()) {
                Object valueObject = properties.get(key);
                if (valueObject != null)
                    paramsMap.put((String) key, valueObject.toString());
            }

        }
        catch (Exception e) {
            logger.error("Process Properties faild,Cause:" + e.getMessage());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    logger.error("Close faild,Cause:" + e.getMessage());
                }
            }
        }
    }

    public void processPropertiesByClassPathFile(String file) {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            ClassLoader classLoader = getClass().getClassLoader();
            inputStream = classLoader.getResourceAsStream(file);
            properties.load(inputStream);
            for (Object key : properties.keySet()) {
                Object valueObject = properties.get(key);
                if (valueObject != null)
                    paramsMap.put((String) key, valueObject.toString());
            }

        }
        catch (Exception e) {
            logger.error("Process Properties faild,Cause:" + e.getMessage());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    logger.error("Close faild,Cause:" + e.getMessage());
                }
            }
        }
    }

    public void processProperties(String filePath) {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            File file = new File(filePath);
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
            for (Object key : properties.keySet()) {
                Object valueObject = properties.get(key);
                if (valueObject != null)
                    paramsMap.put((String) key, valueObject.toString());
            }
        }
        catch (Exception e) {
            logger.error("Process Properties faild,Cause:" + e.getMessage());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    logger.error("Close faild,Cause:" + e.getMessage());
                }
            }
        }
    }

    public static String get(String propertyName) {
        String value = null;
        if (paramsMap != null)
            value = paramsMap.get(propertyName);
        return value;
    }
}
