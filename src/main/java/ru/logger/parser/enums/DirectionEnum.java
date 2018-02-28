package ru.logger.parser.enums;

import java.util.Arrays;

public enum DirectionEnum {
    IN("entry"), OUT("exit");

    private String title;

    DirectionEnum(String title) {
        this.title = title;
    }

    public static DirectionEnum byTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title is empty");
        }
        return Arrays.stream(DirectionEnum.values())
                .filter(e -> e.title.equals(title))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported title %s", title)));
    }


}
