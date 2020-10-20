package packets;

import packets.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Интерфейс, отвечающий за десериализацию полученных сообщений от сервера
 */
public interface Deserializer {

    /**
     * Блок, отвечающий за десериализацию
     * @param byteCommand - ответ от сервера в виде байтового потока
     * @return возвращает десериализованный ответ от сервера
     * @throws IOException - исключение ввода/вывода
     * @throws ClassNotFoundException - исключение ненайденного класса
     */
    default Packet deserialize(byte[] byteCommand) throws IOException, ClassNotFoundException {

        try (final ObjectInputStream out = new ObjectInputStream(new ByteArrayInputStream(byteCommand))){
            return (Packet) out.readObject();
        }
    }
}
