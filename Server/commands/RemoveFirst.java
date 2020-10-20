package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.PriorityQueue;

/**
 * Класс команды remove_first
 */
public class RemoveFirst extends AbstractCommand implements Command {

    /**
     * Поле пользователя
     */
    protected User user;

    /**
     * База данных
     */
    protected DataBase dataBase;

    /**
     * Конструктор класса команды remove_first
     * @param organisations - коллекция объектов класса организации {@link Organisation}
     */
    public RemoveFirst(PriorityQueue<Organisation> organisations, User user, DataBase dataBase){
        super(organisations);
        this.user = user;
        this.dataBase = dataBase;
    }

    /**
     * Метод, приводящий команду в исполнение
     * @return возвращает сообщение клиенту
     */
    @Override
    public Packet execute(){
        synchronized (organisations){
            Organisation org = organisations.peek();
            boolean check = dataBase.deleteElement(user, org);
            if (!check){
                return new TextPacket("Элемент не был удалён.");
            }
            else{
                organisations.remove(org);
                return new TextPacket("Первый элемент коллекции успешно удалён");
            }
        }
    }
}
