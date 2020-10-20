package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Класс команды remove_all_by_full_name
 */
public class RemoveAllByFullName extends AbstractCommand implements Command {

    /**
     * Полное имя организации
     */
    protected String fullName;

    /**
     * Пользователь
     */
    protected User user;

    /**
     * База данных
     */
    protected DataBase dataBase;

    /**
     * Конструктор класса команды remove_all_by_full_name
     * @param organisations - коллекция объектов класса организации {@link Organisation}
     * @param fullName - полное имя
     */
    public RemoveAllByFullName(PriorityQueue<Organisation> organisations, String fullName, User user, DataBase dataBase){
        super(organisations);
        this.fullName = fullName;
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
            int size = organisations.size();
            if (size > 0){
                PriorityQueue<Organisation> toDelete = organisations.stream()
                        .filter(o -> o.getOwner().equals(user.getLogin()))
                        .filter(o -> o.getFullName().equals(fullName))
                        .collect(Collectors.toCollection(PriorityQueue::new));
                if (size == toDelete.size()){
                    return new TextPacket("Элементы, подлежащие удалению, отсутствуют.");
                }
                else{
                    int errors = 0;
                    for (Organisation org: toDelete){
                        boolean check = dataBase.deleteElement(user, org);
                        if (!check){
                            errors++;
                        }
                        else {
                            organisations = dataBase.getAllElements();
                        }
                    }
                    if (errors > 0){
                        return new TextPacket("Не все элементы удалены успешно. Количество ошибок: " + errors);
                    }
                    else {
                        return new TextPacket("Удаление элементов произошло успешно.");
                    }

                }
            }
            else {
                return new TextPacket("Коллекция пуста");
            }
        }

    }
}
