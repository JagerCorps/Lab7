package packets;

import packets.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Интерфейс, отвечающий за сериализацию объектов команд
 */
public interface Serializer {

    /**
     * Блок, отвечающий за сериализацию
     * @param commandPacket - клиентская команда
     * @return возвращает сериализованный объект в потока байтов
     * @throws IOException - исключение ввода/вывода
     */
    default byte[] serialize(Packet commandPacket) throws IOException{
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        try (final ObjectOutputStream out = new ObjectOutputStream(byteOut)){
            out.writeObject(commandPacket);
        }

        return byteOut.toByteArray();
    }
}
