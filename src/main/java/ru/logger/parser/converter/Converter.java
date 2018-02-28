package ru.logger.parser.converter;

import ru.logger.parser.enums.DirectionEnum;
import ru.logger.parser.model.LogItem;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
    private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final static String ITEM_PATTERN = "^((-?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\,[0-9]{3})?)(?:.*)(entry|exit)(?:.*)(?:\\()(.*):(\\d*)(?:\\))";
    private final static Pattern pattern = Pattern.compile(ITEM_PATTERN);

    public static LogItem toLogItem(String value) {
        LogItem.LogItemBuilder builder = LogItem.builder();
        Matcher m = pattern.matcher(value);
        while (m.find()) {
            try {
                builder.time(formatter.parse(m.group(1)).getTime())
                        .direction(DirectionEnum.byTitle(m.group(9)))
                        .name(m.group(10))
                        .id(Long.parseLong(m.group(11)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

}
