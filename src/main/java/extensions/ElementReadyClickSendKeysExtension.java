package extensions;

import io.qameta.atlas.core.api.MethodExtension;
import io.qameta.atlas.core.internal.Configuration;
import io.qameta.atlas.core.internal.TargetMethodInvoker;
import io.qameta.atlas.core.util.MethodInfo;
import io.qameta.atlas.webdriver.AtlasWebElement;
import ru.lanit.at.pages.element.UIElement;
import utils.StyleResourceHelper;

import java.lang.reflect.Method;
import java.util.Arrays;

import static ru.yandex.qatools.matchers.webdriver.EnabledMatcher.enabled;

public class ElementReadyClickSendKeysExtension implements MethodExtension {
    private final String[] METHOD_NAMES = {
            "click",
            "sendKeys",
    };

    @Override
    public Object invoke(Object proxy, MethodInfo methodInfo, Configuration configuration) throws Throwable {
        UIElement atlasWebElement = (UIElement) proxy;
        atlasWebElement.waitUntil(enabled(), 10);
        atlasWebElement.make().scrollIntoView(atlasWebElement);
        //Script add addMouseTracker
        StyleResourceHelper.addMouseTracker();
        return new TargetMethodInvoker().invoke(atlasWebElement, methodInfo, configuration);
    }

    @Override
    public boolean test(Method method) {
        return (Arrays.stream(METHOD_NAMES).
                anyMatch(s -> s.equalsIgnoreCase(method.getName()))
                && AtlasWebElement.class == method.getDeclaringClass());
    }
}
