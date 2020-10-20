package services;

import commands.*;
import packets.*;
import packets.CommandType;
import packets.data.Organisation;
import database.DataBase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.PriorityQueue;

/**
 * Класс интерпретатора сервера
 */
public class ServerInterpreter {

    /**
     * База данных
     */
    private DataBase dataBase;

    /**
     * Коллекция элементов класса {@link Organisation}
     */
    protected PriorityQueue<Organisation> organisations;


    /**
     * Конструктор интерпретатора
     * @param organisations - коллекция элементов класса {@link Organisation}
     */
    public ServerInterpreter(PriorityQueue<Organisation> organisations, DataBase dataBase){
        this.organisations = organisations;
        this.dataBase = dataBase;
    }

    /**
     * Метод, считывающий строку
     * @param input - строка с командой
     */
    public Packet readMessage(Packet input){
        if (input instanceof CommandPacket){
            System.out.println("Пришёл пакет команды.");
            CommandPacket command = (CommandPacket) input;
            CommandType type = command.getCommand();
            switch (type){
                case help:
                    Help help = new Help();
                    return help.execute();

                case info:
                    Info info = new Info(organisations);
                    return info.execute();

                case show:
                    Show show = new Show(organisations);
                    return show.execute();

                case add:
                    Add add = new Add(organisations,command.getElement(),command.getUser(),dataBase);
                    return add.execute();

                case update:
                    Update update = new Update(organisations, command.getElement(), command.getIntegerValue(),
                            command.getUser(), dataBase);
                    return update.execute();

                case remove_by_id:
                    RemoveById removeById = new RemoveById(organisations, command.getIntegerValue(),
                            command.getUser(),dataBase);
                    return removeById.execute();

                case clear:
                    Clear clear = new Clear(organisations, command.getUser(), dataBase);
                    return clear.execute();

                case remove_first:
                    RemoveFirst removeFirst = new RemoveFirst(organisations, command.getUser(), dataBase);
                    return removeFirst.execute();

                case add_if_max:
                    AddIfMax addIfMax = new AddIfMax(organisations,command.getElement(),
                            command.getUser(), dataBase);
                    return addIfMax.execute();

                case remove_lower:
                    RemoveLower removeLower = new RemoveLower(organisations, command.getIntegerValue(),
                            command.getUser(), dataBase);
                    return removeLower.execute();

                case remove_all_by_full_name:
                    RemoveAllByFullName removeAllByFullName = new RemoveAllByFullName(organisations,
                            command.getStringField(), command.getUser(), dataBase);
                    return removeAllByFullName.execute();

                case filter_less_than_annual_turnover:
                    FilterLessThanAnnualTurnover fLTAT = new FilterLessThanAnnualTurnover(organisations, command.getTurnover());
                    return fLTAT.execute();

                case print_field_descending_annual_turnover:
                    PrintFieldDescendingAnnualTurnover pFDAT = new PrintFieldDescendingAnnualTurnover(organisations);
                    return pFDAT.execute();

                default:
                    return null;
            }
        }
        else if (input instanceof UserPacket){
            System.out.println("Пришёл UserPacket.");
            UserPacket userPacket = (UserPacket) input;
            String status = userPacket.getMessageText();
            User user = userPacket.getUser();
            if (status.equals("register")){
                boolean success = false;
                try {
                    success = registerUser(user);
                }
                catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                if (success){
                    return new UserPacket("Success", user);
                }
                else{
                    return new UserPacket("WARNING: Ползователь с данным логином уже существует.", user);
                }
            }
            else if(status.equals("login")){
                if (checkUser(user)){
                    return new UserPacket("Success", user);
                }
                else {
                    return new UserPacket("WARNING: Неверный пароль.", user);
                }
            }
            else {
                return new UserPacket("ERROR: Возникла проблема с получением запроса от клиента.", user);
            }
        }
        else {
            return new TextPacket("ERROR: Возникла проблема с получением запроса от клиента.");
        }

    }

    /**
     * Приватный метод, регистрирующий нового пользователя
     * @param user - пользователь
     * @return возвращает результат регистрации - успешно или нет
     */
    private boolean registerUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return dataBase.registerUser(user);
    }

    /**
     * Приватный метод, проверяющий совпадение пароля пользователя
     * @param user - пользователь
     * @return возвращает результат - совпадает пароль или нет
     */
    private boolean checkUser(User user){
        return dataBase.checkUser(user);
    }
}
