import packets.CommandPacket;
import packets.*;
import packets.UserPacket;
import packets.User;
import services.*;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Главный класс клиента
 */
public class Client {

    /**
     * Пользователь
     */
    private static User user;

    /**
     * Поле времени
     */
    private static LocalDateTime time;

    /**
     * Сокет
     */
    private static Socket socket;

    /**
     * Интерпретатор команд
     */
    private static ClientInterpreter interpreter;

    /**
     * Менеджер работы клиента
     */
    private static ClientManager clientManager;

    /**
     * Главный метод клиента
     * @param args - аргументы командной строки
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try {
            log("INFO: Клиент запускается...");
            log("INFO: Введите порт:");

            int port = getPort(scanner);
            InetAddress host = InetAddress.getLocalHost();
            log("INFO: Попытка подключения...");
            socket = new Socket(host, port);

            log("INFO: Клиент запущен. Подключение прошло успешно.");

            interpreter = new ClientInterpreter(scanner);
            clientManager = new ClientManager(scanner);
            boolean isDone = false;

            log("INFO: Происходит авторизация. Для регистрации введите register.");
            log("INFO: Для авторизации введите login:");
            log("Для выхода введите exit.");
            try {
                user = authorization();
            }
            catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
            interpreter.setUser(user);


            log("INFO: Авторизация прошла успешно. Вы в системе. Начало работы.");
            do{
                System.out.println(">>>>>>>");
                try {
                    CommandPacket clientCommand = interpreter.readLine(scanner.nextLine(),false);

                    if (clientCommand.getCommand() == CommandType.exit) {
                        isDone = true;
                    }

                    else if (clientCommand.getCommand() == CommandType.execute_script){
                        //Наведём шороху
                        try {
                            ArrayList<CommandPacket> commandArrayList = new ArrayList<>();
                            Scanner localScanner = new Scanner(new File(clientCommand.getStringField()));
                            do {
                                CommandPacket command = interpreter.readLine(localScanner.nextLine(),true);
                                if (command.getCommand() == CommandType.null_command){
                                    throw new NullPointerException();
                                }
                                else{
                                    commandArrayList.add(command);
                                }
                            } while (localScanner.hasNextLine());
                            for (CommandPacket command: commandArrayList){
                                log("INFO: Попытка выслать команду серверу...");
                                socket.getOutputStream().write(clientManager.serialize(command));
                                log("INFO: Команда успешно отправлена. Получение сообщения...");
                                byte[] buffer = new byte[10000];
                                socket.getInputStream().read(buffer);
                                clientManager.showResult(clientManager.deserialize(buffer));
                                //log("INFO: Получено сообщение. Зачитываю: "+command.getCommand());
                            }
                        }
                        catch (NullPointerException e){
                            log("INFO: Скрипт написан неправильно. Исправьте скрипт.");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    else if (clientCommand.getCommand() != CommandType.null_command){
                        log("INFO: Попытка выслать команду серверу...");
                        socket.getOutputStream().write(clientManager.serialize(clientCommand));
                        log("INFO: Команда успешно отправлена. Получение сообщения...");
                        byte[] buffer = new byte[10000];
                        socket.getInputStream().read(buffer);
                        clientManager.showResult(clientManager.deserialize(buffer));
                    }
                }
                catch(SocketException e){
                    log("ERROR: Проблемы с подключением к серверу.");
                    log("SOLUTION: Перезапустите клиент: воспользуйтесь командой exit");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            } while (!isDone);

        }
        catch (ConnectException e){
            log("ERROR: Нет подключения к серверу.");
            log(" Завершение работы.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Метод для логирования
     * @param message - сообщение
     */
    private static void log(String message){
        time = LocalDateTime.now();
        System.out.println(time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss ")) + message);
    }

    /**
     * Метод для авторизации пользователя
     * @throws IOException - исключение ввода/вывода
     * @throws ClassNotFoundException - исключение ненайденного класса
     */
    private static User authorization() throws IOException, ClassNotFoundException {
        boolean isOnline = false;
        while (true){
            System.out.println(">>>>>>>");
            UserPacket userPacket = interpreter.authorize();
            String status = userPacket.getMessageText();

            if (status.equals("login") | status.equals("register")){
                log("INFO: Попытка авторизации...");
                socket.getOutputStream().write(clientManager.serialize(userPacket));
                log("INFO: Запрос успешно отправлен. Получение результата...");
                byte[] buffer = new byte[10000];
                socket.getInputStream().read(buffer);
                UserPacket answer = (UserPacket) clientManager.deserialize(buffer);
                clientManager.showResult(answer);
                if (answer.getMessageText().equals("Success")){
                    return answer.getUser();
                }
            }
        }
    }

    /**
     * Статический метод для ввода порта для подключения
     * @param scanner - сканер
     * @return возвращает порт
     */
    private static int getPort(Scanner scanner){
        int port = -1;
        do {
            try {
                port = Integer.parseInt(scanner.nextLine());
                if (port < 0) {
                    System.out.println("Порт не может быть меньше 0.");
                    System.out.println("Повторите ввод:");
                }
                else{
                    break;
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("Неправильный формат ввода. Вводите число без пробелов и разделителей.");
                System.out.println("Повторите ввод:");
            }
        }while (port < 0);
        return port;
    }

}
