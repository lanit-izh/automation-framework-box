package pages.html_elements;

import io.qameta.atlas.webdriver.extension.FindBy;
import ru.lanit.at.pages.annotations.Title;
import ru.lanit.at.pages.element.UIElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Title(value = "Выбор даты")
public interface DatePicker extends UIElement {
    DateTimeFormatter FORMAT_DD_MM_YYYY = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    DateTimeFormatter MM_YYYY = DateTimeFormatter.ofPattern("MM.yyyy");


    default void setDate(LocalDate date) {
        setDate(date, FORMAT_DD_MM_YYYY);
    }

    default void setDate(LocalDate date, DateTimeFormatter formatter) {
        clear();
        sendKeys(date.format(formatter));
    }


    default LocalDate getValue() {
        return getValue(FORMAT_DD_MM_YYYY);
    }

    default LocalDate getValue(DateTimeFormatter dateTimeFormatter) {
        return LocalDate.parse(getAttribute("value"), dateTimeFormatter);
    }


    public interface WithDataPicker extends UIElement {
        @FindBy(".//*[contains(@class,'datepicker')]")
        DatePicker datapicker();
    }
}
