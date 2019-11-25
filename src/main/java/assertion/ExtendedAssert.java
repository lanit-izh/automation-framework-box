package assertion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;
import ru.lanit.at.context.Context;
import ru.lanit.at.driver.DriverManager;
import ru.lanit.at.utils.ScreenShooter;
import utils.AllureHelper;

import java.util.Map;

public class ExtendedAssert extends SoftAssert {
    private static Logger log = LogManager.getLogger(ExtendedAssert.class.getSimpleName());
    private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
    private Boolean isCritical = false;


    public void setCritical() {
        isCritical = true;
    }


    @Override
    protected void doAssert(IAssert<?> a) {
        onBeforeAssert(a);
        try {
            a.doAssert();
            onAssertSuccess(a);
            log.debug("Успешно проверено: [{}]", a.getActual());
        } catch (AssertionError ex) {
            log.error(ex.getMessage());
            onAssertFailure(a, ex);
            m_errors.put(ex, a);
            AllureHelper.attachTxt("Error message", ex.getLocalizedMessage());
            AllureHelper.setStepStatusBroken("SoftAssert:" + ex.getMessage());
            DriverManager driverManager = Context.getInstance().getBean(DriverManager.class);
            if (driverManager.isActive()) {
                AllureHelper.attachScreenShot("SoftAssert", Context.getInstance().getBean
                        (ScreenShooter.class).takeScreenshot());
            }
            if (isCritical) {
                this.assertAll();
            }
        } finally {
            this.isCritical = false;
            onAfterAssert(a);
        }
    }

    @Override
    public void assertAll() {
        if (!m_errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following asserts failed:");
            boolean first = true;
            for (Map.Entry<AssertionError, IAssert<?>> ae : m_errors.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append("\n\t");
                sb.append(ae.getKey().getMessage());
                ae.getKey().printStackTrace();
            }
            if (isCritical) sb.append(" [BLOCKER]");
            m_errors.clear();
            throw new AssertionError(sb.toString());
        }
    }

    public void flush() {
        m_errors.clear();
    }
}
