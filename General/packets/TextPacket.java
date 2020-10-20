package packets;

import java.io.Serializable;

/**
 * Класс текстового сообщения. Содержит только текст-уведомление о выполненной операции
 */
public class TextPacket extends Packet implements Serializable {

    /**
     * Конструктор для задания текста сообщения
     * @param messageText - текст сообщения
     */
    public TextPacket(String messageText){
        super(messageText);
    }
}
