package hooks;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.CaptureGroupTransformer;
import io.cucumber.cucumberexpressions.ParameterType;
import pages.html_elements.Button;
import pages.html_elements.CheckBox;
import pages.html_elements.DatePicker;
import pages.html_elements.DropDown;
import pages.html_elements.Input;
import pages.html_elements.Link;
import pages.html_elements.RadioButton;
import pages.html_elements.Text;
import pages.html_elements.TextArea;
import ru.lanit.at.exceptions.FrameworkRuntimeException;
import ru.lanit.at.pages.element.UIElement;
import steps.BaseSteps;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public class CustomRegistryConfigure extends BaseSteps implements TypeRegistryConfigurer {
    private static final String button = "кнопка|кнопку|кнопке|кнопки";
    private static final String checkBox = "чекбокс|чекбокса|чекбоксе";
    private static final String datePicker = "выбора даты|выбор даты";
    private static final String dropDown = "выпадающий список|выпадающем списке|выпадающего списка";
    private static final String input = "поле ввода|инпут|поля ввода|ввода";
    private static final String link = "ссылка|ссылку|ссылке|ссылки";
    private static final String radioButton = "радио кнопка|радио кнопку|радио кнопке|радио кнопки";
    private static final String textArea = "текстовое поле|текстовом поле|текстового поля";
    private static final String text = "текст|тексте";

    //registerLocalDate variables
    private static final String days = "день|дня|дней";
    private static final String weeks = "неделя|недель|недель|недели";
    private static final String months = "месяц|месяца|месяцев";
    private static final String years = "год|года|лет";


    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        registerLocalDate(typeRegistry);
        registerUIElement(typeRegistry);
    }


    @Override
    public Locale locale() {
        return Locale.getDefault();
    }

    private void registerUIElement(TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(new ParameterType<>(
                "element",
                "\"(" + button + "|" + checkBox + "|" + datePicker + "|" + dropDown + "|" + input + "|" + link + "|" + radioButton + "|" + textArea + "|" + text + ")(?:|\\s+(с параметрами:|с параметром:)(.*))\"",
                UIElement.class,
                new CaptureGroupTransformer<UIElement>() {
                    @Override
                    public UIElement transform(String[] args) {
                        getSearchContext();
                        if (args.length > 1) {
                            String[] params = args[2].replace(" ", "").split(",");
                            ;
                            if (args[2].isEmpty() || params.length == 0)
                                throw new FrameworkRuntimeException("не указаны параметры для инициализации элемента '" + args[0] + "'.");
                            getUIElement(getClassElement(args[0]), params);
                        }
                        return getUIElement(getClassElement(args[0]));
                    }
                },
                true,
                true)
        );
    }

    private Class<? extends UIElement> getClassElement(String arg) {
        if (button.contains(arg.toLowerCase().trim())) return Button.class;
        else if (checkBox.contains(arg.toLowerCase().trim())) return CheckBox.class;
        else if (input.contains(arg.toLowerCase().trim())) return Input.class;
        else if (datePicker.contains(arg.toLowerCase().trim())) return DatePicker.class;
        else if (dropDown.contains(arg.toLowerCase().trim())) return DropDown.class;
        else if (link.contains(arg.toLowerCase().trim())) return Link.class;
        else if (radioButton.contains(arg.toLowerCase().trim())) return RadioButton.class;
        else if (textArea.contains(arg.toLowerCase().trim())) return TextArea.class;
        else if (text.contains(arg.toLowerCase().trim())) return Text.class;
        return UIElement.class;
    }


    private void registerLocalDate(TypeRegistry typeRegistry) {
        typeRegistry.defineParameterType(new ParameterType<>(
                "date",
                "\"(?:(сегодня)(?:|\\s+([-+])\\s+(\\d+)\\s+(" + days + "|" + weeks + "|" + months + "|" + years + ")))\"",
                LocalDate.class,
                new CaptureGroupTransformer<LocalDate>() {
                    @Override
                    public LocalDate transform(String[] args) {
                        if (args.length > 1) {
                            Period period = applyPeriod(Integer.parseInt(args[2]), args[3]);
                            if (args[1].equalsIgnoreCase("+")) {
                                return LocalDate.now().plus(period);
                            } else {
                                return LocalDate.now().minus(period);
                            }
                        }
                        return LocalDate.now();
                    }
                },
                true,
                true)
        );
    }


    private static Period applyPeriod(int count, String arg) {
        Period result = Period.ofDays(count);
        if (weeks.contains(arg.toLowerCase())) {
            result = Period.ofWeeks(count);
        } else if (months.contains(arg.toLowerCase())) {
            result = Period.ofMonths(count);
        } else if (years.contains(arg.toLowerCase())) {
            result = Period.ofYears(count);
        }
        return result;

    }

}