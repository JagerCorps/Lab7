package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Класс для реализации команды add
 */
public class Add extends AbstractCommand implements Command{

    /**
     * Поле объекта класса {@link Organisation}, который подлежит додавить в коллекцию
     */
    protected Organisation newOrg;

    /**
     * Поле пользователя, вызвавшего команду
     */
    protected User owner;

    /**
     * База данных
     */
    protected DataBase dataBase;


    /**
     * Конструктор класса команды add
     * @param organisations - коллекция объектов класса {@link Organisation}
     * @param newOrg - добавляемый элемент
     */
    public Add(PriorityQueue<Organisation> organisations, Organisation newOrg, User owner, DataBase dataBase){
        super(organisations);
        this.newOrg = newOrg;
        this.owner = owner;
        this.dataBase = dataBase;
    }

    /**
     * Метод, возвращабщий максимальное значение id среди всех элементов коллекции
     * Нужно для того, чтобы каждый новый элемент имел уникальный id
     * При создании элемента, ему задаётся id, который на 1 больше максимального
     * Если коллекция пуста, новый элемент получит id = 1, так как метод вернёт 0
     * @return возвращает максимальный id среди всех элементов коллекции
     */
    protected int idCounter(){
        if (organisations.size()>0){
            return organisations.stream()
                    .map(Organisation::getId)
                    .max(Comparator.naturalOrder())
                    .get();
        }
        else{
            return 0;
        }
    }

    /**
     * Метод, приводящий команду в исполнение
     * @return возвращает сообщение клиенту
     */
    @Override
    public Packet execute(){
        synchronized (organisations){
            Organisation check = dataBase.insertElement(owner, newOrg);
            if (check == null){
                return new TextPacket("Элемент не был добавлен");
            }
            else {
                organisations.add(check);
                return new TextPacket("Элемент успешно добавлен в коллекцию");
            }
        }

    }
}