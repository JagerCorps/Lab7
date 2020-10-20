package services;

import packets.CommandPacket;
import packets.UserPacket;
import packets.*;
import packets.data.Organisation;
import packets.User;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Клиентский интерпретатор. Содержит модули по чтению команд из строки и авторизации пользователей
 */
public class ClientInterpreter {

    /**
     * Пользователь
     */
    private User user;
    /**
     * Сканер. Если команды не будут являтся прочитанными из скрипта
     * Значения полей будут считываться с его помощью
     * Подразумевается, что он будет привязан к консоли
     */
    protected Scanner scanner;

    /**
     * Конструктор интерпретатора
     * @param scanner - сканер
     */
    public ClientInterpreter(Scanner scanner){
        this.scanner = scanner;
    }

    /**
     * Метод для задания пользователя
     * @param user - пользователь
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Метод для создания нового элемента при вызове определённых команд
     * clientCommand =  возвращает элемент класса Organisation (без id)
     */
    public Organisation newElement(boolean isScript, String line){
        Organisation newOrg = new Organisation();
        if (!isScript){
            newOrg.setOwner(user.getLogin());
            newOrg.setName(FieldReaders.readString(scanner, "Введите имя:"));
            newOrg.setCoordinates(FieldReaders.readX(scanner), FieldReaders.readY(scanner));
            newOrg.setCreationDate(LocalDate.now());
            newOrg.setAnnualTurnover(FieldReaders.readTurnover(scanner));
            newOrg.setFullName(FieldReaders.readString(scanner, "Введите полное имя:"));
            newOrg.setEmployeesCount(FieldReaders.readInt(scanner, "Введите количество сотрудников:"));
            newOrg.setType(FieldReaders.readType(scanner));
            newOrg.setOfficialAddress(FieldReaders.readString(scanner, "Введите официальный адрес организации:"));

            return newOrg;
        }
        else {
            String newLine = line.replaceAll("[{}\\s]", " ");
            String[] values = newLine.split("; ");

            String name;
            double x = 0;
            double y = 0;
            LocalDate creationDate = LocalDate.now();
            float annualTurnover = 0;
            String fullName;
            int employeesCount = 0;
            String organisationType;
            String address;

            boolean dataIsCorrect = true;

            name = values[0].trim();
            if (name.isEmpty()) {
                System.out.println("Возникла проблема. Имя не может быть пустым.");
                dataIsCorrect = false;
            }
            try {
                x = Double.parseDouble(values[1]);
                if (x < -422) {
                    System.out.println("Возникла проблема. Координата Х не может быть меньше -422.");
                    dataIsCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Возникла проблема с координатой X.");
                dataIsCorrect = false;
            }

            try {
                y = Double.parseDouble(values[2]);
            } catch (Exception e) {
                System.out.println("Возникла проблема с координатой Y.");
                dataIsCorrect = false;
            }
            try {
                creationDate = LocalDate.parse(values[3], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (Exception e) {
                System.out.println("Возникла проблема с датой.");
                dataIsCorrect = false;
            }
            try {
                annualTurnover = Float.parseFloat(values[4]);
                if (annualTurnover < 0) {
                    System.out.println("Возникла проблема. Годовой оборот не может быть меньше 0");
                    dataIsCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Возникла проблема с годовым оборотом.");
                dataIsCorrect = false;
            }
            fullName = values[5];
            if (fullName.isEmpty()) {
                System.out.println("Возникла проблема. Полное имя не может быть пустым");
                dataIsCorrect = false;
            }
            try {
                employeesCount = Integer.parseInt(values[6]);
                if (employeesCount < 0) {
                    System.out.println("Возникла проблема. Количество сотрудников не может быть меньше 0");
                    dataIsCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Возникла проблема с количеством сотрудников.");
                dataIsCorrect = false;
            }
            organisationType = values[7];
            address = values[8].trim();
            System.out.println(address);
            if (address.isEmpty()) {
                System.out.println("Возникла проблема. Адрес не может быть пустым");
                dataIsCorrect = false;
            }
            newOrg.setName(name);
            newOrg.setCoordinates(x, y);
            newOrg.setCreationDate(creationDate);
            newOrg.setAnnualTurnover(annualTurnover);
            newOrg.setFullName(fullName);
            newOrg.setEmployeesCount(employeesCount);
            newOrg.setOwner(user.getLogin());
            try {
                newOrg.setType(organisationType);
            } catch (IllegalArgumentException e) {
                System.out.println("Возникла проблема. Тип организации введён некорректно");
                dataIsCorrect = false;
            }
            newOrg.setOfficialAddress(address);
            if (dataIsCorrect) {
                return newOrg;
            } else {
                System.out.println("Исправьте скрипт.");
                return null;
            }
        }
    }


    /**
     * Метод, считывающий строку
     * @param inputCommand - строка с командой
     */
    public CommandPacket readLine(String inputCommand, boolean isScript){
        String[] command = inputCommand.split("[\\s]", 2);
        CommandPacket clientCommand = new CommandPacket("ignore",CommandType.null_command);
        switch (command[0]){
            case "help":
                clientCommand = new CommandPacket("ignore",CommandType.help);
                break;

            case "info":
                clientCommand = new CommandPacket("ignore",CommandType.info);
                break;

            case "show":
                clientCommand = new CommandPacket("ignore",CommandType.show);
                break;

            case "add":
                Organisation element = this.newElement(isScript,"");
                if (element.equals(null)){
                    //let the command be null_command
                }
                else{
                    clientCommand = new CommandPacket("ignore",CommandType.add, element);
                }

                break;

            case "update":
                Organisation updatedElement = this.newElement(isScript,"");
                if (updatedElement.equals(null)){
                    //let the command be null_command
                }
                else {
                    int id;
                    if(isScript){
                        try {
                            id = Integer.parseInt(command[1]);
                            if (id < 0) {
                                System.out.println("Возникла проблема. Id не может быть меньше 0.");
                                //let the command be null_command
                            }
                            else{
                                clientCommand = new CommandPacket("ignore",CommandType.update, id, updatedElement);
                            }
                        } catch (Exception e) {
                            System.out.println("Возникла проблема с id.");
                            //let the command be null_command
                        }
                    }
                    else{
                        clientCommand = new CommandPacket("ignore",CommandType.update,
                                FieldReaders.readInt(scanner,"Введите id элемента:"), updatedElement);
                    }
                }
                break;

            case "remove_by_id":
                int id;
                if(isScript){
                    try {
                        id = Integer.parseInt(command[1]);
                        if (id < 0) {
                            System.out.println("Возникла проблема. Id не может быть меньше 0.");
                            //let the command be null_command
                        }
                        else{
                            clientCommand = new CommandPacket("ignore",CommandType.remove_by_id, id);
                        }
                    } catch (Exception e) {
                        System.out.println("Возникла проблема с id.");
                        //let the command be null_command
                    }
                }
                else{
                    clientCommand = new CommandPacket("ignore",CommandType.remove_by_id,
                            FieldReaders.readInt(scanner,"Введите id элемента:"));
                }

                break;

            case "clear":
                clientCommand = new CommandPacket("ignore",CommandType.clear);
                break;

            case "save":
                System.out.println("Данная команда недоступна клиенту.\n " +
                        "Операции по сохранению данных в файл осуществляются на сервере.");
                break;

            case "execute_script":
                if (isScript){
                    System.out.println("Запрещено вызывать execute_script из скрипта.");
                    System.out.println("Поправьте скрипт и повторите попытку.");
                }
                else{
                    File file = new File(command[1].trim());
                    if (file.exists()){
                        clientCommand = new CommandPacket("ignore",CommandType.execute_script, command[1]);
                    }
                }
                break;

            case "exit":
                scanner.close();
                System.out.println("Завершение работы.");
                System.exit(0);

            case "remove_first":
                clientCommand = new CommandPacket("ignore",CommandType.remove_first);
                break;

            case "add_if_max":
                Organisation newElement = this.newElement(isScript,"");
                if (newElement.equals(null)){
                    //let the command be null_command
                }
                else{
                    clientCommand = new CommandPacket("ignore",CommandType.add_if_max, newElement);
                }
                break;

            case "remove_lower":
                int employeesCount;
                if (isScript){
                    try {
                        employeesCount = Integer.parseInt(command[1]);
                        if (employeesCount < 0) {
                            System.out.println("Возникла проблема. Количество сотрудников не может быть меньше 0");
                        }
                        else {
                            clientCommand = new CommandPacket("ignore",CommandType.remove_lower, employeesCount);
                        }
                    } catch (Exception e) {
                        System.out.println("Возникла проблема с количеством сотрудников.");
                        //let the command be null_command
                    }
                }
                else{
                    System.out.println("Сравнение происходит по количеству сотрудников.");
                    clientCommand = new CommandPacket("ignore",CommandType.remove_lower,
                            FieldReaders.readInt(scanner, "Введите количество сотрудников:"));
                }

                break;

            case "remove_all_by_full_name":
                String fullName;
                if (isScript){
                    fullName = command[1];
                    if (fullName.isEmpty()) {
                        System.out.println("Возникла проблема. Полное имя не может быть пустым");
                        //let the command be null_command
                    }
                    else{
                        clientCommand = new CommandPacket("ignore",CommandType.remove_all_by_full_name, fullName);
                    }
                }
                else{
                    clientCommand = new CommandPacket("ignore",CommandType.remove_all_by_full_name,
                            FieldReaders.readString(scanner, "Введите полное имя:"));
                }

                break;

            case "filter_less_than_annual_turnover":
                Float turnover = 0F;

                if (isScript){
                    boolean dataIsNormal = true;
                    try {
                        turnover = Float.parseFloat(command[1]);
                        if (turnover < 0) {
                            System.out.println("Возникла проблема. Годовой оборот не может быть меньше 0");
                            dataIsNormal = false;
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Возникла проблема с годовым оборотом.");
                        dataIsNormal = false;
                    }
                    if (dataIsNormal){
                        clientCommand = new CommandPacket("ignore",CommandType.filter_less_than_annual_turnover,
                                turnover);
                    }
                    else {
                        //let the command be null_command
                    }
                }
                else{
                    clientCommand = new CommandPacket("ignore",CommandType.filter_less_than_annual_turnover,
                            FieldReaders.readTurnover(scanner));
                }

                break;

            case "print_field_descending_annual_turnover":
                clientCommand = new CommandPacket("ignore",CommandType.print_field_descending_annual_turnover);
                break;

            default:
                System.out.println("Введённая команда не опознана.");
                System.out.println("Попробуйте ввести команду help для вывода списка команд.");
                break;
        }
        clientCommand.setUser(user);
        return clientCommand;
    }

    /**
     * Метод для авторизации пользователей
     * @return возвращает пакет с пользователем
     */
    public UserPacket authorize(){
        String login;
        String password;

        String command;
        command = scanner.nextLine().trim();
        switch (command){
            case "exit":
                scanner.close();
                System.out.println("Завершение работы.");
                System.exit(0);
            case "login":
                System.out.println("Происходит авторизация.");
                login = FieldReaders.readString(scanner,"Введите ваш логин:");
                password = FieldReaders.readString(scanner, "Введите пароль:");
                password = Encryption.md2(password);
                return new UserPacket("login",new User(login, password));

            case "register":
                System.out.println("Происходит регистрация.");
                login = FieldReaders.readString(scanner,"Введите ваш логин:");
                password = FieldReaders.readString(scanner, "Введите пароль:");
                password = Encryption.md2(password);
                return new UserPacket("register",new User(login, password));

            default:
                System.out.println("Команды недоступны неавторизированным пользователям");
                System.out.println("Для регистрации введите register.\n Для авторизации введите login.");
                System.out.println("Для выхода введите exit.");
                return new UserPacket("error",new User("null","null"));
        }
    }
}
