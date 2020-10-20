package services;

import packets.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;


/**
 * Менеджер работы клиента
 */
public class ClientManager implements Serializer, Deserializer, UserValidator{

    /**
     * Сканер менеджера клиента
     */
    private Scanner scanner;

    /**
     * Пустой конструктор
     */
    public ClientManager(Scanner scanner){}


    /**
     * Метод для вывода результата сообщения от сервера
     * @param packet - сообщение от сервера
     */
    public void showResult(Packet packet){
        System.out.println(packet.getMessageText());
        if (packet instanceof ElementsPacket){
            Arrays.stream(((ElementsPacket) packet).getOrganisations())
            .forEach(System.out::println);
        }
        else if (packet instanceof FloatPacket){
            Arrays.stream(((FloatPacket) packet).getTurnovers())
                    .sorted(Comparator.reverseOrder())
                    .forEach(System.out::println);
        }
        else if (packet instanceof StringPacket){
            Arrays.stream(((StringPacket) packet).getStrings())
                    .forEach(System.out::println);
        }
    }

    /**
     * Метод, производящий авторизацию
     * @return возвращает авторизованного/зарегистрированного пользователя
     */
    @Override
    public User authorize(){
        final String login;
        final String password;

        login = FieldReaders.readString(scanner,"Введите ваш логин:");
        password = FieldReaders.readString(scanner, "Введите пароль:");
        User user = new User(login, password);

        return user;
    }

}
