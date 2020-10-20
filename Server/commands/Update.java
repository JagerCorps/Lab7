package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.PriorityQueue;

/**
 * Класс команды update
 */
public class Update extends Add implements Command {

    /**
     * id элемента
     */
    protected int id;
    /**
     * Конструктор класса команды update
     * @param organisations - коллекция элементов класса {@link Organisation}
     * @param id - id элемента
     */
    public Update(PriorityQueue<Organisation> organisations, Organisation newOrg, int id, User owner, DataBase dataBase){
        super(organisations, newOrg, owner, dataBase);
        this.id = id;
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
                Organisation check = dataBase.updateElement(owner, id, newOrg);
                if (check == null){
                    return new TextPacket("Элемент не был добавлен");
                }
                else {
                    Organisation update = null;
                    for (Organisation o: organisations){
                        if (o.getId() == id){
                            update = o;
                        }
                    }
                    organisations.remove(update);
                    organisations.add(check);
                    return new TextPacket("Элемент успешно добавлен в коллекцию");
                }
            }
            else{
                return new TextPacket("Коллекция пуста.");
            }
        }
    }
}
