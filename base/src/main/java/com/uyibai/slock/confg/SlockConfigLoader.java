package com.uyibai.slock.confg;

import com.uyibai.slock.utils.ConfigUtil;
import com.uyibai.slock.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Simple Lock config loader
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date : 2022/3/23
 */
@Slf4j
public class SlockConfigLoader {
    public static final String SLOCK_CONFIG_ENV_KEY = "SLOCK_CONFIG_FILE";
    public static final String SLOCK_CONFIG_PROPERTY_KEY = "slock.config.file";
    private static final String DEFAULT_SLOCK_CONFIG_FILE = "classpath:slock.properties";
    private static final Properties properties = new Properties();


    public SlockConfigLoader() {
    }

    private static void load() {
        String fileName = System.getProperty(SLOCK_CONFIG_PROPERTY_KEY);
        if (StringUtils.isBlank(fileName)) {
            fileName = System.getenv(SLOCK_CONFIG_ENV_KEY);
            if (StringUtils.isBlank(fileName)) {
                fileName = DEFAULT_SLOCK_CONFIG_FILE;
            }
        }

        Properties p = ConfigUtil.loadProperties(fileName);
        if (p != null && !p.isEmpty()) {
            log.info("[SlockConfigLoader] Loading Simple Lock config from {}", fileName);
            properties.putAll(p);
        }

        Iterator var2 = (new CopyOnWriteArraySet(System.getProperties().entrySet())).iterator();

        while (var2.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry) var2.next();
            String configKey = entry.getKey().toString();
            String newConfigValue = entry.getValue().toString();
            String oldConfigValue = properties.getProperty(configKey);
            properties.put(configKey, newConfigValue);
            if (oldConfigValue != null) {
                log.info("[SlockConfigLoader] JVM parameter overrides {}: {} -> {}", configKey, oldConfigValue, newConfigValue);
            }
        }

    }

    public static Properties getProperties() {
        return properties;
    }

    static {
        try {
            load();
        } catch (Throwable var1) {
            log.warn("[SlockConfigLoader] Failed to initialize configuration items", var1);
        }

    }

}
