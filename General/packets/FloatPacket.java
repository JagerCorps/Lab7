package packets;

import java.io.Serializable;

/**
 * Класс сообщения с вещественными элементами
 */
public class FloatPacket extends Packet implements Serializable {

    /**
     * Поле массива вещественных элементов
     */
    private Float[] turnovers;

    /**
     * Конструктор сообщения
     * @param messageText - текст сообщения
     * @param turnovers - массив вещественных элементов
     */
    public FloatPacket(String messageText, Float[] turnovers){
        super(messageText);
        this.turnovers = turnovers;
    }

    /**
     * Метод для получения массива вещественных элементов
     * @return возвращает вещественных
     */
    public Float[] getTurnovers() {
        return turnovers;
    }
}
