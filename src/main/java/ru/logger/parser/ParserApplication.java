package ru.logger.parser;

import ru.logger.parser.converter.Converter;
import ru.logger.parser.model.LogItem;
import ru.logger.parser.model.SummaryStatistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class ParserApplication {
    private static final String HELP = "USE: --path=\"/tmp/file.log\"";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(HELP);
            return;
        }
        new ParserApplication().process(args);
    }

    void process(String[] args) {
        String filePath = getPath(args);
        Map<Long, LogItem> processes = null;
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            processes = stream.filter(Objects::nonNull).map(Converter::toLogItem).collect(
                    Collectors.toMap(LogItem::getId, Function.identity(), ParserApplication::merge));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert processes != null;
        Map<String, SummaryStatistics> stat = processes.values().stream()
                .filter(x -> x != null && x.getDirection() == null)
                .collect(
                        groupingBy(LogItem::getName,
                                Collector.of(
                                        SummaryStatistics::new,
                                        SummaryStatistics::accept,
                                        (l, r) -> {
                                            l.combine(r);
                                            return l;
                                        }
                                )));
        stat.forEach((x, y) -> System.out.format("OperationsImpl:%15s %s", x, y).println());
    }

    private String getPath(String[] args) {
        return Arrays.stream(args).filter(it -> it.startsWith("--path="))
                .map(arg -> arg.substring(7).replaceFirst("\"(.*?)\"", "$1"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mandatory argument PATH is absent"));
    }

    private static LogItem merge(LogItem a, LogItem b) {
        if (a == null || b == null) {
            return null;
        }
        a.calc(b.getTime());
        return a;
    }
}
