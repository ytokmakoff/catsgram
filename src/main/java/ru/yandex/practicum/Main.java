package ru.yandex.practicum;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        log.setLevel(Level.WARN);
        log.trace("Трасировочное сообщение");
        log.debug("Отладочное сообщение");
        log.info("Информационное сообщение");
        log.warn("Предупреждающее сообщение");
        log.error("Сообщение о критической ошибке");
    }
}