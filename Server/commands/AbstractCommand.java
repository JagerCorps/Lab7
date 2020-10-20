package commands;

import packets.data.Organisation;

import java.io.Serializable;
import java.util.PriorityQueue;


/**
 * Абстрактный класс-предок для всех команд
 */
public abstract class AbstractCommand implements Command, Serializable {

    /**
     * Поле коллекции классов Organisation {@link Organisation}, с которыми идёт работа
     */
    protected PriorityQueue<Organisation> organisations;

    /**
     *Конструктор класса AbstractCommand
     * @param organisations - коллекция классов Organisation {@link Organisation}
     */
    protected AbstractCommand(PriorityQueue<Organisation> organisations){
        this.organisations = organisations;
    }

}
