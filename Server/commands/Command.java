package commands;

import packets.Packet;

/**
 * Интерфейс команды
 */
public interface Command {

    /**
     * Метод, приводящий команду в исполнение и возвращающий сообщение клиенту
     * @return возвращает сообщение клиенту
     */
    Packet execute();
}
