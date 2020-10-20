package packets;

import java.io.Serializable;

/**
 * Класс пакета с пользователем
 */
public class UserPacket extends Packet implements Serializable {

    /**
     * Поле пользователя
     */
    private User user;

    /**
     * Конструктор пакета с пользователем
     * @param messageText - текст сообщения
     * @param user - пользователь
     */
    public UserPacket(String messageText, User user){
        super(messageText);
        this.user = user;
    }

    /**
     * Метод для получения пользователя из пакета
     * @return возвращает пользователя
     */
    public User getUser() {
        return user;
    }
}
