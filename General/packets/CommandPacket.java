package packets;
import packets.data.Organisation;

import java.io.Serializable;

/**
 * Абстрактный класс клиентской команды.
 * Служит обобщающим для клиентских команд
 * Команды поделены на категории по количеству аргументов
 */
public class CommandPacket extends Packet implements Serializable {

    /**
     * Пользователь
     */
    private User user;
    /**
     * Текстовое сообщение
     */
    private String messageText;

    /**
     * Поле с наименованием команды
     */
    private CommandType command;

    /**
     * Поле целочисленного значения (id или количество сотрудников)
     */
    private int integerValue;

    /**
     * Поле элемента класса {@link Organisation}
     */
    private Organisation element;

    /**
     * Поле годового оборота
     */
    private Float turnover;

    /**
     * Поле полного имени/пути до скрипта
     */
    private String stringField;

    /**
     * Конструктор для команд 1 категории
     * В их числе info, help, show, clear, print_field_descending_annual_turnover, remove_first
     * @param command - название команды
     */
    public CommandPacket(String messageText,CommandType command){
        super(messageText);
        this.command = command;
    }

    /**
     * Конструктор для команд 2 категории
     * В их числе remove_by_id, remove_lower
     * @param command - название команды
     * @param integerValue - значение целочисленного поля
     */
    public CommandPacket(String messageText,CommandType command, int integerValue){
        super(messageText);
        this.command = command;
        this.integerValue = integerValue;
    }

    /**
     * Конструктор команды update
     * @param command - название команды
     * @param id - id элемента
     * @param element - элемент класса {@link Organisation}
     */
    public CommandPacket(String messageText, CommandType command, int id, Organisation element){
        super(messageText);
        this.command = command;
        this.integerValue = id;
        this.element = element;
    }

    /**
     * Конструктор для команд 3 категории
     * В их числе add, add_if_max
     * @param command - название команды
     * @param element - элемент класса {@link Organisation}
     */
    public CommandPacket(String messageText, CommandType command, Organisation element){
        super(messageText);
        this.command = command;
        this.element = element;
    }

    /**
     * Конструктор команды filter_less_than_annual_turnover
     * @param command - название команды
     * @param turnover - годовой оборот
     */
    public CommandPacket(String messageText,CommandType command, Float turnover){
        super(messageText);
        this.command = command;
        this.turnover = turnover;
    }

    /**
     * Конструктор команды remove_all_by_full_name/execute_script
     * @param command - название команды
     * @param stringField - полное имя/путь до скрипта
     */
    public CommandPacket(String messageText, CommandType command, String stringField){
        super(messageText);
        this.command = command;
        this.stringField = stringField;
    }

    /**
     * Метод, возвращающий название команды
     * @return возвращает название команды
     */
    public CommandType getCommand() {
        return command;
    }

    /**
     * Метод, возвращающий целочисленное поле (id или количество сотрудников)
     * @return возвращает значение целочисленного поля
     */
    public int getIntegerValue() {
        return integerValue;
    }

    /**
     * Метод, возвращающий элемент класса {@link Organisation}
     * @return возвращает элемент
     */
    public Organisation getElement() {
        return element;
    }

    /**
     * Метод, возвращающий значение поля годового оборота
     * @return возвращает значение поля годового оборота
     */
    public Float getTurnover() {
        return turnover;
    }

    /**
     * Метод, возвращающий полное имя организации/путь до скрипта
     * @return возвращает полное имя/путь до скрипта
     */
    public String getStringField() {
        return stringField;
    }

    /**
     * Метод для задания пользователя
     * @param user - пользователь
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Метод для получения пользователя
     * @return возвращает пользователя
     */
    public User getUser(){
        return user;
    }
}
