package packets;


import packets.data.Organisation;

import java.io.Serializable;

/**
 * Класс обЪектосодержащего сообщения
 */
public class ElementsPacket extends Packet implements Serializable {

    /**
     * Массив элементов
     */
    private Organisation[] organisations;

    /**
     * Конструктор класса обЪектосодержащего сообщения
     * @param textMessage - текстовое сообщение
     * @param organisationCollection - коллекция объектов класса {@link Organisation}
     */
    public ElementsPacket(String textMessage, Organisation[] organisationCollection){
        super(textMessage);
        this.organisations = organisationCollection;
    }

    /**
     * Метод для получения массива элементов
     * @return возвращает массив элементов
     */
    public Organisation[] getOrganisations() {
        return organisations;
    }
}
