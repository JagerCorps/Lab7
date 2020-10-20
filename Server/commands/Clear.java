package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Класс команды clear
 */
public class Clear extends AbstractCommand implements Command{

    /**
     * Поле пользователя
     */
    protected User user;

    /**
     * База данных
     */
    protected DataBase dataBase;

    /**
     * Конструктор класса команды clear
     * @param organisations - коллекция объектов класса организации {@link Organisation}
     */
    public Clear(PriorityQueue<Organisation> organisations, User user, DataBase dataBase){
        super(organisations);
        this.user = user;
        this.dataBase = dataBase;
    }

    /**
     * Метод, приводящий команду в исполнени
     * @return возвращает сообщение клиенту
     */
    @Override
    public Packet execute(){
        synchronized (organisations){
            PriorityQueue<Organisation> toDelete = organisations.stream()
                    .filter(o -> o.getOwner().equals(user.getLogin()))
                    .collect(Collectors.toCollection(PriorityQueue::new));
            if (toDelete.size() == 0){
                return new TextPacket("Нет элементов, доступных вам для удаления.");
            }
            else{
                int errors = 0;
                for (Organisation org: toDelete){
                    boolean check = dataBase.deleteElement(user, org);
                    if (!check){
                        errors++;
                    }
                    else {
                        organisations.remove(org);
                    }
                }
                if (errors > 0){
                    return new TextPacket("Не все элементы удалены успешно. Количество ошибок: " + errors);
                }
                else {
                    return new TextPacket("Коллекция ваших элементов очищена.");
                }
            }
        }

    }
}
