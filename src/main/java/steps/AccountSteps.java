package steps;

import cucumber.api.java.ru.И;
import io.cucumber.datatable.DataTable;
import utils.data.helpers.DataProviderHelper;

import java.util.List;
import java.util.Map;

public class AccountSteps extends BaseSteps{
    @И("получить данные УЗ по имени пользователя {string}")
    public void getAccountData(String value) {
        Map<String, Object> data = DataProviderHelper.getDpDataValue("user_name", value);
        data.forEach((key, mapValue) -> {
            if(!key.equals("user_id")){
                System.setProperty(key, (String) mapValue);
            }
        });
    }

    @И("сохранить данные УЗ пользователя:")
    public void saveAccountData(DataTable table) {
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        DataProviderHelper.saveDpDataValue(list);
    }

    @И("обновить данные УЗ пользователя по столбцу {string} и значению {string}:")
    public void patchAccountData(String key, String value, DataTable table) {
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        DataProviderHelper.patchDpDataValue(key, value, list);
    }

    @И("удалить данные УЗ пользователя по столбцу {string} и значению {string}")
    public void deleteAccountData(String key, String value) {
        DataProviderHelper.deleteDpDataValue(key, value);
    }

    @И("удалить все данные учетных записей")
    public void deleteAllAccountData() {
        DataProviderHelper.deleteAllDpDataValues();
    }
}
