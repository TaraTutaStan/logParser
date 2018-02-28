package ru.logger.parser;

import org.junit.Test;

public class ParserApplicationTests {

    @Test
    public void contextLoads() {
        String[] args = {"--path=\"E:\\Temp\\parse\\testlog.log\""};
        new ParserApplication().process(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void contextLoads_wrongPath() {
        String[] args = {"--pather=\"E:\\Temp\\parse\\testlog.log\""};
        new ParserApplication().process(args);
    }

}
