package services;

import java.nio.channels.SelectionKey;

/**
 * Интерфейс, отвечающий за отправку ответов на запросы клиентов
 */
public interface MessageWriter {

    /**
     * Блок, осуществляющий отправку ответов на запросы клиентов
     * @param key - ключ селектора
     * @throws Exception - исключение
     */
    void write(SelectionKey key) throws Exception;
}
