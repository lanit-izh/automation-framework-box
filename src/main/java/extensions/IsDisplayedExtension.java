package extensions;

import io.qameta.atlas.core.api.MethodExtension;
import io.qameta.atlas.core.internal.Configuration;
import io.qameta.atlas.core.internal.TargetMethodInvoker;
import io.qameta.atlas.core.util.MethodInfo;
import io.qameta.atlas.webdriver.AtlasWebElement;
import io.qameta.atlas.webdriver.exception.WaitUntilException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import ru.lanit.at.context.Context;
import ru.lanit.at.make.Make;

import java.lang.reflect.Method;
import java.util.Arrays;

import static ru.yandex.qatools.matchers.webdriver.ExistsMatcher.exists;

public class IsDisplayedExtension implements MethodExtension {
    private final String[] METHOD_NAMES = {
            "isDisplayed"
    };

    @Override
    public Object invoke(Object object, MethodInfo methodInfo, Configuration configuration) throws Throwable {
        if (object instanceof AtlasWebElement) {
            AtlasWebElement atlasWebElement = (AtlasWebElement) object;
            Make make = Context.getInstance().getBean(Make.class);
            try {
                atlasWebElement.waitUntil(exists(), 6);
                make.scrollIntoView(atlasWebElement);
            } catch (NoSuchElementException | WaitUntilException | StaleElementReferenceException ex) {
                return false;
            }
            return new TargetMethodInvoker().invoke(atlasWebElement, methodInfo, configuration);
        }
        return new TargetMethodInvoker().invoke(object, methodInfo, configuration);
    }

    @Override
    public boolean test(Method method) {
        return (Arrays.stream(METHOD_NAMES).
                anyMatch(s -> s.equalsIgnoreCase(method.getName()))
                && AtlasWebElement.class == method.getDeclaringClass());
    }
}
