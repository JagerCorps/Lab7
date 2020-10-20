package handlers;

import packets.Packet;
import services.ServerInterpreter;

import java.nio.channels.SelectionKey;
import java.util.concurrent.RecursiveAction;

/**
 * Класс для обработки запросов
 */
public class RequestTask extends RecursiveAction {

    /**
     * Пакет
     */
    private Packet packet;

    /**
     * Ключ
     */
    private SelectionKey key;

    /**
     * Интерпертатор серевра
     */
    private ServerInterpreter interpreter;

    /**
     * Конструктор обработчика
     * @param packet -пакет
     * @param key - ключ
     * @param interpreter - интерпретатор
     */
    public RequestTask(Packet packet, SelectionKey key, ServerInterpreter interpreter){
        this.packet = packet;
        this.key = key;
        this.interpreter = interpreter;
    }

    /**
     * Метод для исполнения
     */
    @Override
    protected void compute() {
        key.attach(interpreter.readMessage(packet));
        key.interestOps(SelectionKey.OP_WRITE);
    }

}
