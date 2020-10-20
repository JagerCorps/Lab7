package commands;

import packets.Packet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Класс команды exit
 */
public class Exit implements Command{

    /**
     * Поле сканера. Нужно, чтобы закрыть все потоки перед завершением работы программы
     */
    protected Scanner scanner;

    /**
     * Конструктор класса команды exit
     * @param scanner - сканер
     */
    public Exit(Scanner scanner){
        this.scanner = scanner;
    }
    /**
     * Метод, приводяший команду в исполнение
     */
    @Override
    public Packet execute(){
        scanner.close();
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss "))+
                " Завершение работы.");
        System.exit(0);
        return null;
    }
}
