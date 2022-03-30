package com.uyibai.slock.confg;

import com.uyibai.slock.utils.AssertUtil;
import com.uyibai.slock.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date : 2022/3/23
 */
@Slf4j
public class SlockConfig {
    private static final ConcurrentHashMap<String, String> props = new ConcurrentHashMap<>();

    private static void loadProps() {
        Properties properties = SlockConfigLoader.getProperties();
        Iterator var1 = properties.keySet().iterator();

        while (var1.hasNext()) {
            Object key = var1.next();
            setConfig((String) key, (String) properties.get(key));
        }

    }

    public static String getConfig(String key) {
        AssertUtil.notNull(key, "key cannot be null");
        return props.get(key);
    }

    public static void setConfig(String key, String value) {
        AssertUtil.notNull(key, "key cannot be null");
        AssertUtil.notNull(value, "value cannot be null");
        props.put(key, value);
    }

    public static String removeConfig(String key) {
        AssertUtil.notNull(key, "key cannot be null");
        return (String) props.remove(key);
    }

    public static void setConfigIfAbsent(String key, String value) {
        AssertUtil.notNull(key, "key cannot be null");
        AssertUtil.notNull(value, "value cannot be null");
        String v = (String) props.get(key);
        if (v == null) {
            props.put(key, value);
        }

    }
//
//    /**
//     * 获取 api Model 配置
//     *
//     * @return String {@link Constant}
//     */
//    public static String getApiModel() {
//        String apiModel = props.get(QIYE_WECAHT_API_MODEL);
//        if (StringUtils.isBlank(apiModel)) {
//            log.warn("[QiyeWechatConfig] Parse {} fail, use default value: {}", QIYE_WECAHT_API_MODEL, Constant.API_MODEL_INNER);
//            apiModel = Constant.API_MODEL_INNER;
//        }
//        return apiModel;
//    }
//
//    public static String getApiBaseUrl() {
//        String baseUrl = props.get(QIYE_WECAHT_BASE_URL);
//        if (StringUtils.isBlank(baseUrl)) {
//            baseUrl = Constant.DEFAULT_BASE_API_URL;
//        }
//        return baseUrl;
//    }


    public static SlockConfigVo getAppConfig() {
        try {
            SlockConfigVo configVo = new SlockConfigVo();
            configVo.setName(props.get(QIYE_WECAHT_NAME));
            configVo.setAppId(props.get(QIYE_WECAHT_APPID));
            configVo.setAgentId(Integer.parseInt(props.get(QIYE_WECAHT_AGENTID)));
            configVo.setSecret(props.get(QIYE_WECAHT_APPSECRET));
            configVo.setCorpId(props.get(QIYE_WECAHT_CORPID));
            configVo.setHomeUrl(props.get(QIYE_WECAHT_HOMEURL));
            configVo.setEventToken(props.get(QIYE_WECAHT_EVENT_TOKEN));
            configVo.setEventEncodingAesKey(props.get(QIYE_WECAHT_EVENT_ENCODING_AESKEY));
            return configVo;
        } catch (Throwable var1) {
            log.warn("[QiyeWechatConfig] Parse coldFactor fail, use default value: 3", var1);
        }
        return null;
    }

    public static SlockConfigVo getSysAppConfig(String app) {
        try {
            SlockConfigVo configVo = new SlockConfigVo();
            configVo.setName(props.get(String.format(QIYE_WECAHT_TEMPL_NAME, app)));
            configVo.setAppId(props.get(String.format(QIYE_WECAHT_TEMPL_APPID, app)));
            configVo.setAgentId(Integer.parseInt(props.get(String.format(QIYE_WECAHT_TEMPL_AGENTID, app))));
            configVo.setSecret(props.get(String.format(QIYE_WECAHT_TEMPL_APPSECRET, app)));
            configVo.setCorpId(props.get(String.format(QIYE_WECAHT_TEMPL_CORPID, app)));
            configVo.setHomeUrl(props.get(String.format(QIYE_WECAHT_TEMPL_HOMEURL, app)));
            configVo.setEventToken(props.get(String.format(QIYE_WECAHT_TEMPL_EVENT_TOKEN, app)));
            configVo.setEventEncodingAesKey(props.get(String.format(QIYE_WECAHT_TEMPL_EVENT_ENCODING_AESKEY, app)));

            if (StringUtils.isEmpty(configVo.getCorpId())) {
                configVo.setCorpId(props.get(QIYE_WECAHT_CORPID));
            }
            return configVo;
        } catch (Throwable var1) {
            log.info("[QiyeWechatConfig] Parse fail", var1);
        }
        return null;
    }

    private SlockConfig() {
    }

    static {
        try {
            loadProps();
            log.info("[QiyeWechatConfig] properties config resolved: {}", props);
        } catch (Throwable var1) {
            log.warn("[QiyeWechatConfig] Failed to initialize", var1);
            var1.printStackTrace();
        }

    }
}
