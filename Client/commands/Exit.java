package commands;

import java.util.Scanner;

/**
 * Класс команды exit
 */
public class Exit{

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
    public void execute(){
        scanner.close();
        System.out.println("Завершение работы.");
        System.exit(0);
    }
}
