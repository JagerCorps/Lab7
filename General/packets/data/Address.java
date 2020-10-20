package packets.data;

import java.io.Serializable;

/**
 * Класс адреса
 */
public class Address implements Serializable {

    /**
     * Поле адреса, не может быть null
     */
    private String street; //Поле может быть null

    /**
     * Метод для получения поля адреса
     * @return возвращает значение поля адреса
     */
    public String getAddress() {
        return street;
    }

    /**
     * Конструктор класса адреса
     * @param street - адрес
     */
    public Address(String street) {
        this.street = street;
    }


    /**
     * Метод для получения адреса в строковом представлении
     * @return возвращает строку адреса
     */
    public String toString(){return street;}
}
