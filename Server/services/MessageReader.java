package services;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Интерфейс, отвечающий за чтение запросов
 */
public interface MessageReader {

    /**
     * Блок, осуществляющий чтение запросов
     * @param key - ключ селектора
     * @throws IOException - исключение ввода/вывода
     * @throws ClassNotFoundException - исключение ненайденного класса
     */
    void read(SelectionKey key) throws IOException, ClassNotFoundException;
}
