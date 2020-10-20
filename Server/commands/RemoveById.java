package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.PriorityQueue;

/**
 * Класс команды remove_by_id
 */
public class RemoveById extends AbstractCommand implements Command {

    /**
     * id элемента
     */
    protected int id;

    /**
     * Поле пользователя
     */
    protected User user;

    /**
     * База данных
     */
    protected DataBase dataBase;

    /**
     * Конструктор класса команды remove_by_id
     * @param organisations - коллекция элементов класса {@link Organisation}
     * @param id - id элемента
     */
    public RemoveById(PriorityQueue<Organisation> organisations, int id, User user, DataBase dataBase){
        super(organisations);
        this.id = id;
        this.user = user;
        this.dataBase = dataBase;
    }

    /**
     * Метод, приводящий команду в исполнение
     * @return возвращает сообщение клиенту
     */
    @Override
    public Packet execute() {
        synchronized (organisations){
            boolean check = dataBase.deleteElementById(user, id);
            Organisation org = new Organisation();
            for (Organisation o: organisations) {
                if (o.getId() == id) {
                    org = o;
                }
            }
            if (!check){
                return new TextPacket("Элемент не был удалён. У вас либо нет прав на его удаление, " +
                        "либо не существует элемента с данным id.");
            }
            else {
                organisations.remove(org);
                return new TextPacket("Элемент по заданному id успешно удалён.");
            }
        }
    }
}

