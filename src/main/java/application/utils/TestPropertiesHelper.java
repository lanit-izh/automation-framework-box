package application.utils;

import application.models.TestProperties;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class TestPropertiesHelper {
    private static Logger logger = LoggerFactory.getLogger(TestPropertiesHelper.class);

    /** Установка тестовых свойств */
    public void setTestProperties(String properties) throws UnsupportedEncodingException {
        logger.info("Установка тестовых свойств");
        TestProperties testProperties = new Gson().fromJson(URLDecoder.decode(properties, "UTF-8"), TestProperties.class);
        System.setProperty("browser", testProperties.getBrowser());
        System.setProperty("browser.config", testProperties.getBrowser_config());
        System.setProperty("site.url", testProperties.getSite_url());
        System.setProperty("remote", testProperties.getRemote());
        System.setProperty("hub_url", testProperties.getHub_url());
        System.setProperty("proxy", testProperties.getProxy());
        System.setProperty("proxy_config", testProperties.getProxy_config());
    }
}