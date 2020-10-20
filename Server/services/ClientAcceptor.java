package services;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Интерфейс, отвечающий за приём подключений к серверу
 */
public interface ClientAcceptor {

    /**
     * Блок, отвечающий за принятие подключений
     * @param key - ключ селектора
     * @throws IOException - исключение ввода/вывода
     */
    void accept(SelectionKey key) throws IOException;
}
