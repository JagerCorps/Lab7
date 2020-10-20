package commands;

import packets.Packet;
import packets.StringPacket;
import packets.data.Organisation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;

/**
 * Класс команды info
 */
public class Info extends AbstractCommand implements Command {

    /**
     * Конструктор класса команды info
     * @param organisations - коллекция объектов класса {@link Organisation}
     */
    public Info(PriorityQueue<Organisation> organisations){
        super(organisations);
    }

    /**
     * Метод, необходимый для получения даты инициализации коллекции
     * Для этого метод ищет элемент с самой ранней датой инициализацией
     * @return возвращает дату инициализации
     */
    private LocalDate getOrganisationDate(){
        Organisation[] helpOrgArray  = new Organisation[organisations.size()];
        Organisation[] orgArray = organisations.toArray(helpOrgArray);
        if (orgArray.length > 0){
            LocalDate earlierDate = orgArray[0].getCreationDate();
            for (int i=1; i < orgArray.length; i++){
                if (orgArray[i].getCreationDate().isBefore(earlierDate)){
                    earlierDate = orgArray[i].getCreationDate();
                }
            }
            return earlierDate;
        }
        else{
            return null;
        }
    }

    /**
     * Метод, приводящий команду в исполнение
     * @return возвращает сообщение клиенту
     */
    @Override
    public Packet execute(){
        synchronized (organisations){
            String date;
            if (this.organisations.size()>0) {
                date = this.getOrganisationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            }
            else{
                date = "Коллекция пуста";
            }
            String[] strings = {"Тип коллекции: PriorityQueue",
                    "Коллекция содержит элементы класса: Organisation",
                    "Количество элементов коллекции: " + this.organisations.size(),
                    "Дата инициализации коллекции:" + date};
            return new StringPacket(
                    "Вызвана команда info. Информация о коллекции:", strings);
        }
    }
}
