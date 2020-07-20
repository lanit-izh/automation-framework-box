package steps.blocks.steps;


import cucumber.api.java.ru.И;
import ru.yandex.qatools.matchers.webdriver.DisplayedMatcher;
import ru.yandex.qatools.matchers.webdriver.ExistsMatcher;
import steps.BaseSteps;

import java.util.Arrays;

import static org.hamcrest.Matchers.not;

public final class BlocksCommonStepsLibrary extends BaseSteps {


    @И("на текущей странице перейти к блоку {string}")
    public void focusOnBlock(String blockNameStr) {
        resetCurrentBlock();
        String[] blocks = blockNameStr.split(">");
        setCurrentBlockByName(blocks[0]);
        checkBlockExist();
        if (blocks.length > 1) {
            focusOnBlockInBlock(blockNameStr.substring(blockNameStr.indexOf(">") + 1));
        }
    }

    @И("на текущей странице перейти к блоку {string} - {string}")
    public void focusOnBlockWith(String blockNameStr, String param) {
        resetCurrentBlock();
        String[] blocks = blockNameStr.split(">");
        String[] params = param.split(",");
        setCurrentBlockByName(blocks[0], params);
        checkBlockExist();
        if (blocks.length > 1) {
            focusOnBlockInBlock(blockNameStr.substring(blockNameStr.indexOf(">") + 1));
        }
    }


    @И("на текущей странице перейти к блоку {string} под названием {string}")
    public void focusOnBlockWithName(String blockNameStr, String param) {
        resetCurrentBlock();
        String[] blocks = blockNameStr.split(">");
        String[] params = param.split(",");
        setCurrentBlockByName(blocks[0], params);
        checkBlockExist();
        if (blocks.length > 1) {
            focusOnBlockInBlock(blockNameStr.substring(blockNameStr.indexOf(">") + 1));
        }
    }


    @И("^в текущем блоке перейти к блоку '(.*)'$")
    public void focusOnBlockInBlock(String blockNameStr) {
        Arrays.stream(blockNameStr.split(">"))
                .forEach(blockName -> {
                    setCurrentBlockByName(blockName.trim());
                    checkBlockExist();
                });
    }


    @И("проверить что отображется блок")
    public void checkBlockVisible() {
        getCurrentBlock().should(DisplayedMatcher.displayed(), 10);
    }

    @И("проверить что на странице отображется блок {string} под названием {string}")
    public void checkBlockVisible(String blockNameStr, String param) {
        resetCurrentBlock();
        setCurrentBlockByName(blockNameStr, param);
        checkBlockVisible();
        resetCurrentBlock();
    }

    @И("проверить что блок существует")
    public void checkBlockExist() {
        getCurrentBlock().should(ExistsMatcher.exists(), 10);
    }

    @И("проверить что блок отсуствует")
    public void checkBlockNotExist() {
        getCurrentBlock().should(not(ExistsMatcher.exists()), 10);
    }
}