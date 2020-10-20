package commands;

import packets.Packet;
import packets.TextPacket;
import packets.data.Organisation;
import database.DataBase;
import packets.User;

import java.util.PriorityQueue;

/**
 * Класс команды add_if_max
 */
public class AddIfMax extends Add implements Command {

    /**
     * Поле максимального элемента коллекции
     */
    protected Organisation maxOrganisation;

    /**
     * Конструктор класса команды add_if_max
     * @param organisations - коллекция объектов класса {@link Organisation}
     */
    public AddIfMax(PriorityQueue<Organisation> organisations, Organisation newOrg, User owner, DataBase dataBase){
        super(organisations, newOrg, owner, dataBase);
        this.getMaxOrganisation();
    }

    /**
     * Метод, возвращающий максимальный элемент коллекции
     * @return возвращает максимальный элемент
     */
    protected void getMaxOrganisation() {
        maxOrganisation = organisations.stream()
                .max(Organisation::compareTo)
                .get();
    }
    /**
     * Метод, приводящий команду в исполнение
     * @return возвращает сообщение клиенту
     */
    @Override
    public Packet execute(){
        synchronized (organisations){
            if (this.maxOrganisation != null) {
                if (newOrg.compareTo(this.maxOrganisation) > 0) {
                    Organisation check = dataBase.insertElement(owner, newOrg);
                    if (check == null){
                        return new TextPacket("Элемент не был добавлен");
                    }
                    else {
                        organisations.add(check);
                        return new TextPacket("Элемент успешно добавлен в коллекцию");
                    }
                }
                else {
                    return new TextPacket("Добавляемый элемент меньше максимального. Элемент не добавлен");
                }
            }
            else {
                Organisation check = dataBase.insertElement(owner, newOrg);
                if (check == null){
                    return new TextPacket("Элемент не был добавлен");
                }
                else {
                    organisations = dataBase.getAllElements();
                    return new TextPacket("Элемент успешно добавлен в коллекцию");
                }
            }
        }

    }
}
