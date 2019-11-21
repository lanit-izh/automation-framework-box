package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Map;

public class PluginUtils {

    public static final String MODHEADER_SETUP_URL = "chrome-extension://idgpnmonknjnojddfkpgkljpfnnfcklj/icon.png";

    public static void setUpHeaders(Map<String, String> headerParams, WebDriver driver) {
        String a = "window.open('" + MODHEADER_SETUP_URL + "','_blank');";
        String workingTab = driver.getWindowHandle();
        ArrayList<String> beforeTabs = new ArrayList<>(driver.getWindowHandles());
        ((JavascriptExecutor) driver).executeScript(a);
        ArrayList<String> afterTabs = new ArrayList<>(driver.getWindowHandles());
        afterTabs.removeAll(beforeTabs);
        driver.switchTo().window(afterTabs.get(0));
        driver.navigate().to(MODHEADER_SETUP_URL);
        String headerData = "";
        for (Map.Entry<String, String> data : headerParams.entrySet()) {
            headerData += "{enabled: true, name: '" + data.getKey() + "', value: '" + data.getValue() + "', comment: ''},";
        }
        ((JavascriptExecutor) driver).executeScript(
                "localStorage.setItem('profiles', JSON.stringify([{                " +
                        "  title: 'Selenium', hideComment: true, appendMode: '',           " +
                        "  headers: [                                                      " +
                        "    " + headerData + " " +
                        "  ],                                                              " +
                        "  respHeaders: [],                                                " +
                        "  filters: []                                                     " +
                        "}]));                                                             ");
        driver.close();
        driver.switchTo().window(workingTab);
    }
}
