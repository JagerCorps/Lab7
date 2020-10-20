package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Класс команды remove_lower
 */
public class RemoveLower extends AbstractCommand implements Command {

    /**
     * Поле пользователя
     */
    protected User user;

    /**
     * База данных
     */
    protected DataBase dataBase;

    /**
     * Количество сотрудников. Требуется для сравнения
     */
    protected int employees;

    /**
     * Конструктор класса команды remove_lower
     * @param organisations - коллекция элементов класса {@link Organisation}
     * @param employees - количество сотрудников
     */
    public RemoveLower(PriorityQueue<Organisation> organisations, int employees, User user, DataBase dataBase){
        super(organisations);
        this.employees = employees;
        this.user = user;
        this.dataBase = dataBase;
    }

    /**
     * Метод, приводящий команду в исполнение
     * @return возвращает сообщение для клиента
     */
    @Override
    public Packet execute(){
        synchronized (organisations){
            int size = organisations.size();
            if (size > 0){
                PriorityQueue<Organisation> toDelete = organisations.stream()
                        .filter(o -> o.getOwner().equals(user.getLogin()))
                        .filter(organisation -> organisation.getEmployeesCount() > employees)
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
                            organisations.remove(org);
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
            else{
                return new TextPacket("Коллекция пуста.");
            }
        }
    }
}
