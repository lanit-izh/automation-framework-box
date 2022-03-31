package utils.data.helpers;


import ru.lanit.at.context.Context;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Генератор рандомных данных */
public class DataRandomGenerator {
    public static final String DATE = ".*((?:(?:дата)!)\\(([^)]+)\\)).*";  // дата!(сегодня)
    public static final String RANDOM = ".*((?:(?:случайно|символы)!|\\$)\\(([^)]+)\\)).*"; // случайно!(25)  символы!(25 кириллических)  $(25 цифр)
    public static final Pattern FROM_PATTERN = Pattern.compile(".* из (\\S+).*"); // случайно!(25)  символы!(25 кириллических)  $(25 цифр)
    public static final String PATTERN = ".*((?:(?:шаблон)!)\\(([^)]+)\\)).*"; // шаблон!(##-lll//кк)
    public static final String DATAPROVIDER = ".*((?:(?:ДатаПровайдер)!)\\(([^)]+.)\\)).*";


    //символы для замены, подменяются на случайный символ из соответствующего ряда
    // ЦИФРЫ
    public static final char digits = '#';
    // ЦИФРЫ БЕЗ 0
    public static final char not_null_digits = '№';
    // КИРИЛЛИЧЕСКИЕ СИМВОЛЫ
    public static final char cyrillic = 'к';
    // КИРИЛЛИЧЕСКИЕ СИМВОЛЫ
    public static final char cyrillic_big = 'К';
    // СИМВОЛЫ ЛАТИНИЦЫ
    public static final char latin = 'l';
    // РИМСКИЕ ЦИФРЫ
    public static final char roman = 'L';
    public static final String SUPER_DATE_PATTERN = ".+\\[(.+)]";
    public static final String cyr = "абвгдежзиклмнопрстуфхцчшщъыьэюя";
    public static final String lat = "abcdefghijklmnopqrstuvwxyz";
    public static final String SYM = "@#$%^&*!?/|";
    public static final String SPECIAL_SYM = "^_.-'#№@»`«&,().!@#;:'\"<>/^*()_"; //Добавил спец символы доступные для ввода
    public static final String DIG = "0123456789";
    public static final String DIGPhone = "012345689";
    public static final String DIG_N = "123456789";
    public static final String CYR = "АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    public static final String Cyr = CYR + cyr;
    public static final String LAT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String Lat = lat + LAT;
    public static final String ALL = Cyr + Lat + DIG;
    public static final String CyrLatDIG = Cyr + Lat + DIG;
    public static final String main_ru = Cyr + DIG + "^_.-'\"#№@»`«&,()+/[]:";
    public static final String main_ru_n = Lat + "~%${}?\\|=;*";
    public static final String main = CyrLatDIG + "<>+:;*^_.-'\"#№@»`«&,!()/[]";
    public static final String main_n = "~$%{}?\\|=";
    public static final String main_fio = Cyr;
    private static final String romanianDigits = "IXVCMD";
    private static final String CYR_MARK = "КИР";
    private static final String cyr_MARK = "кир";
    private static final String Cyr_MARK = "Кир";
    private static final String lat_MARK = "лат";
    private static final String LAT_MARK = "ЛАТ";
    private static final String Lat_MARK = "Лат";
    private static final String SYM_MARK = "сим";
    private static final String ALL_MARK = "все";
    private static final String DIG_MARK = "цифр";
    private static final DateTimeFormatter DEFAULT_DATE_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /*

    | Дата рождения           | 15 лет назад       |
    | Контактный телефон      | случайно!(10 цифр) |
    | Адрес электронной почты | ${}                |
    | Серия       | случайно!(4 цифры)     |
    | номер       | случайно!(6 цифр)      |
    | Когда выдан | вчера                  |
    | Кем выдан   | случайно!(250 Русских) |

    */

    public static boolean containsGeneratingValue(String value) {
        return value.matches(RANDOM) || value.matches(DATE) || value.matches(PATTERN) || value.matches(DATAPROVIDER);
    }

    public static synchronized String replaceAllGeneratingValues(String command) {
        while (containsGeneratingValue(command)) { // имеется выход
            if (command.matches(RANDOM)) {
                command = replaceGeneratingValue(command, RANDOM);
            } else if (command.matches(DATE)) {
                command = replaceGeneratingValue(command, DATE);
            } else if (command.matches(PATTERN)) {
                command = replaceGeneratingValue(command, PATTERN);
            } else if (command.matches(DATAPROVIDER)) {
                command = replaceGeneratingValue(command, DATAPROVIDER);
            }
//            Log.error("Ошибка в шаблоне для генерации: \"" + command + "\"");
        }
        return command;
    }

    private static synchronized String replaceGeneratingValue(String command, String detectedPattern) {
        Matcher m = Pattern.compile(detectedPattern).matcher(command);
        if (m.find()) {
            String replacingValue = m.group(1);
            String sample = m.group(2);

            String generatedValue;
            switch (detectedPattern) {
                case DATE:
                    generatedValue = getDateString(sample);
                    break;
                case PATTERN:
                    generatedValue = applyPattern(sample);
                    break;
                case DATAPROVIDER:
                    generatedValue = getDPData(sample);
                    break;
                default:
                    generatedValue = extractArgsAndGenerateString(sample);
            }
            command = command.replace(replacingValue, generatedValue);
        }
        return command;
    }

    private static synchronized String applyPattern(final String sample) {
        char[] result = new char[sample.length()];
        for (int i = 0; i < sample.length(); i++) {
            char currentChar = sample.charAt(i);
            switch (currentChar) {
                case digits:
                    result[i] = getRandomString(1, DIG).charAt(0);
                    break;
                case not_null_digits:
                    result[i] = getRandomString(1, DIG_N).charAt(0);
                    break;
                case cyrillic:
                    result[i] = getRandomString(1, cyr).charAt(0);
                    break;
                case cyrillic_big:
                    result[i] = getRandomString(1, cyr).charAt(0);
                    break;
                case latin:
                    result[i] = getRandomString(1, lat).charAt(0);
                    break;
                case roman:
                    result[i] = getRandomString(1, romanianDigits).charAt(0);
                    break;

                default:
                    result[i] = currentChar;
            }
        }
        return new String(result);
    }

    private static synchronized String extractArgsAndGenerateString(String command) {
        int length = getLength(command);
        String charSrc = getCharSource(command);
        return getRandomString(length, charSrc);
    }

    /**
     * Возвращает случайную последовательность из заданных символов указанной длины
     *
     * @param length      длина генерируемой строки
     * @param sourceChars источник символов
     * @return строку со случайными символами
     */
    public static synchronized String getRandomString(int length, String sourceChars) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(sourceChars.charAt(rnd.nextInt(sourceChars.length())));
        }
        return sb.toString();
    }

    private static synchronized int getLength(String command) {
        try {
            Matcher matcher = Pattern.compile("\\s*(\\d+)[\\D\\s]?.*").matcher(command);
            if (matcher.find()) return Integer.parseInt(matcher.group(1));
            return Integer.parseInt(command.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Не задано количество символов в генераторе данных, строка \"" + command + "\" содержит ошибку ");
        }
    }

    private static synchronized String getCharSource(String command) {
        StringBuilder src = new StringBuilder();
        Matcher customCharSource = FROM_PATTERN.matcher(command);
        if (customCharSource.find()) src.append(customCharSource.group(1));
        if (command.toLowerCase().contains(DIG_MARK)) src.append(DIG);
        if (command.contains(CYR_MARK)) src.append(CYR);
        if (command.contains(Cyr_MARK)) src.append(Cyr);
        if (command.contains(cyr_MARK)) src.append(cyr);
        if (command.contains(LAT_MARK)) src.append(LAT);
        if (command.contains(Lat_MARK)) src.append(Lat);
        if (command.contains(lat_MARK)) src.append(lat);
        if (command.contains("main")) addMainCharSequences(command, src);
        if (command.toLowerCase().contains(SYM_MARK)) src.append(SYM);
        if (command.contains(ALL_MARK) || src.length() == 0) src.append(ALL);
        return src.toString();
    }

    private static void addMainCharSequences(String command, StringBuilder src) {
        if (command.contains("main_ru_n"))
            src.append(main_ru_n);
        else if (command.contains("main_ru"))
            src.append(main_ru);
        else if (command.contains("main_n"))
            src.append(main_n);
        else if (command.contains("main"))
            src.append(main);
    }

    synchronized static LocalDate getArithmeticDate(String sample) {
        LocalDate result = LocalDate.now();

        String[] ops = sample.split("[^+\\-]+");
        String[] args = sample.split("\\s*[+\\-]\\s*");

        if (args.length > ops.length + 1)
            throw new IllegalArgumentException("Неправильное количество аргументов и функций в строке \"" + sample + "\"");

        if (args[0].isEmpty())
            args = Arrays.copyOfRange(args, 1, args.length);

        if (ops.length == 0) {
            ops = args[0].contains("назад") ? new String[]{"-"} : new String[]{"+"};
        }

        if (ops[0].isEmpty()) {
            ops[0] = args[0].contains("назад") ? "-" : "+";
        }

        for (int i = 0; i < ops.length; i++) {
            Period period = getPeriod(args[i]);
            String operation = ops[i];

            if (operation.equals("+")) result = result.plus(period);
            else result = result.minus(period);
        }
        return result;
    }

    private synchronized static Period getPeriod(String arg) {
        int number = 1;
        try {
            number = Integer.parseInt(arg.replaceAll("\\D", ""));
        } catch (NumberFormatException ignore) {
        }

        arg = arg.toLowerCase();
        // по-умолчанию считаем, что это дни
        Period result = Period.ofDays(number);
        if (arg.contains("недел")) {
            result = Period.ofWeeks(number);
        } else if (arg.contains("мес")) {
            result = Period.ofMonths(number);
        } else if (arg.contains("лет") || arg.contains("год")) {
            result = Period.ofYears(number);
        }
        return result;
    }

    /**
     * Возвращает текущую дату в формате дд.мм.гггг, если {@sample} не удовлетворяет одному из заданных шаблонов. через запятую при необходимости указывается шаблон форматирования, по умолчанию это {@link #DEFAULT_DATE_PATTERN}
     *
     * @param sample текстовый шаблон-указатель на дату, относительно текущей, например: <i>полгода назад</i>, <i>завтра</i> и т.п.
     * @return дата в формате дд.мм.гггг (текущая дата если не удовлетворяет ни одному из прописанных шаблонов)
     */
    public synchronized static String getDateString(String sample) {

        LocalDate date = null;
        DateTimeFormatter dateFormatPattern = DEFAULT_DATE_PATTERN;
        String[] args = sample.split("\\s*,\\s*");
        sample = args[0];
        if (args.length > 1) dateFormatPattern = DateTimeFormatter.ofPattern(args[1]);

        if (!sample.equalsIgnoreCase("сегодня") && (sample.contains("+") || sample.contains("-") || LocalDate.now().equals(getDate(sample)))) {
            date = getArithmeticDate(sample);
        } else {
            // обычная дата, старый формат
            date = getDate(sample);
        }

        return date.format(dateFormatPattern);
    }

    /**
     * @deprecated рекомендуется {@link #getArithmeticDate(String sample)} как более простой и логичный
     */
    private synchronized static LocalDate getDate(String sample) {
        LocalDate date = LocalDate.now();
        if (sample.equalsIgnoreCase("вчера"))
            date = date.minusDays(1);
        else if (sample.equalsIgnoreCase("завтра"))
            date = date.plusDays(1);
        else if (sample.equalsIgnoreCase("через полгода"))
            date = date.plusMonths(6);
        else if (sample.equalsIgnoreCase("через месяц"))
            date = date.plusMonths(1);
        else if (sample.equalsIgnoreCase("месяц назад"))
            date = date.minusMonths(1);
        else if (sample.equalsIgnoreCase("через год"))
            date = date.plusYears(1);
        else if (sample.equalsIgnoreCase("год назад"))
            date = date.minusYears(1);
        else if (sample.equalsIgnoreCase("полгода назад"))
            date = date.minusMonths(6);
        else if (sample.matches("(\\d+) (?:года?|лет) назад")) {
            int yearsAgo = Integer.parseInt(sample.replaceAll("\\D", ""));
            date = date.minusYears(yearsAgo);
        }
        return date;
    }

    /**
     * Получение данных из дата провайдера
     */
    public synchronized static String getDPData(String sample) {

        String[] args = sample.split("\\s*,\\s*", 2);
        sample = args[0];
        String dpData = String.valueOf(((Map<String, Object>) Context.getInstance().getBean("dataKeeper")).get(args[0]));
        if (!dpData.equals("null")) {
            return dpData;
        }
        String key = args[0];
        String value = replaceAllGeneratingValues(args[1]);

        ((Map<String, Object>) Context.getInstance().getBean("dataKeeper")).put(key, value);
        return value;
    }


}
