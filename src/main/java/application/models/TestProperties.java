package application.models;


/** Обьект с переменными для запуска тестов */
public class TestProperties {
    private String site_url;
    private String browser;
    private String browser_config;
    private String remote;
    private String proxy;
    private String proxy_config;
    private String hub_url;

    public String getSite_url() {
        return site_url;
    }

    public void setSite_url(String site_url) {
        this.site_url = site_url;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowser_config() {
        return browser_config;
    }

    public void setBrowser_config(String browser_config) {
        this.browser_config = browser_config;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getProxy_config() {
        return proxy_config;
    }

    public void setProxy_config(String proxy_config) {
        this.proxy_config = proxy_config;
    }

    public String getHub_url() {
        return hub_url;
    }

    public void setHub_url(String hub_url) {
        this.hub_url = hub_url;
    }
}
