package commands;

import packets.Packet;
import packets.ElementsPacket;
import packets.TextPacket;
import packets.data.Organisation;

import java.util.PriorityQueue;

/**
 * Класс команды show
 */
public class Show extends AbstractCommand implements Command{

    /**
     * Конструктор класса команды show
     * @param organisations - коллекция объектов класса организации {@link Organisation}
     */
    public Show(PriorityQueue<Organisation> organisations){
        super(organisations);
    }

    /**
     * Метод, приводящий команду в исполнение
     * @return возвращает сообщение клиенту
     */
    @Override
    public Packet execute(){
        synchronized (organisations){
            Organisation[] helpArray = new Organisation[organisations.size()];
            if (organisations.size() == 0){
                return new TextPacket( "Коллекция пуста.");
            }
            else{
                return new ElementsPacket("Коллекция содержит элементы:", organisations.toArray(helpArray));
            }
        }
    }
}

