package handlers;

import packets.data.Organisation;
import database.DataBase;
import services.*;

import java.io.*;
import java.net.*;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * Главный класс сервера
 */
public class Server {

    /**
     * Поле времени
     */
    private static LocalDateTime time;

    /**
     * Менеджер сервера. Принимает подключения, организовывает чтение из канала, запись в канал.
     */
    private static ServerManager serverManager;

    /**
     * Канал сервера
     */
    private static ServerSocketChannel channel;

    /**
     * Селектор. Будет мониторить подключающиеся каналы
     */
    private static Selector selector;

    /**
     * База данных
     */
    private final static DataBase database = DataBase.getInstance();

    /**
     * Пул потоков для отправки ответов и обработки запросов
     */
    public static final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    /**
     * Пул потоков для чтения запросов
     */
    private static ExecutorService threadPool = Executors.newFixedThreadPool(1);

    /**
     * Флаг, является ли селектор закрытым
     */
    private static boolean selectorIsClosed = false;


    /**
     * Главный метод сервера
     * @param args - аргументы командной строки
     */
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        log(" Здравствуйте.");
        log("INFO: Настройка всех систем...");
        PriorityQueue<Organisation> organisations = database.getAllElements();
        log("INFO: Элементы из базы данных успешно загружены в память");


        try {
            log("INFO: Сервер запускается...");

            ServerInterpreter interpreter = new ServerInterpreter(organisations, database);

            log("INFO: Введите свободный порт для подключения:");
            int port = getPort(scanner);
            InetAddress hostIP = InetAddress.getLocalHost();
            channel = ServerSocketChannel.open();
            selector = Selector.open();
            InetSocketAddress address = new InetSocketAddress(hostIP,port);
            channel.configureBlocking(false);
            channel.bind(address);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            serverManager = new ServerManager(channel, selector, interpreter);

            log("INFO: Сервер запущен.");


            log("INFO: Сервер готов к работе.");

            boolean selectorIsOffline = false;

            threadPool.execute(()->{
                while(true) {
                    try{
                        selector.selectNow();
                        Set<SelectionKey> selectedKeys = selector.selectedKeys();


                        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                        while(keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            keyIterator.remove();
                            if (!key.isValid()){
                                continue;
                            }

                            if(key.isAcceptable()) {
                                log("INFO: Запрос на подключение клиента...");
                                serverManager.accept(key);
                                log("INFO: Клиент успешно подключен.");
                            } else if(key.isReadable()) {
                                log("INFO: Попытка чтения из канала...");
                                try{
                                    serverManager.read(key);
                                    log("INFO: Чтение из канала прошло успешно.");
                                }
                                catch (SocketException e){
                                    log("WARNING: Клиент отключился");
                                    key.cancel();
                                }
                                catch (ClosedSelectorException e){
                                    if (!selectorIsClosed){
                                        log("WARNING: Селектор прекращает работу.");
                                        selectorIsClosed = true;
                                        //Чтобы не была неразбериха в консоли
                                    }

                                }
                                catch (Exception e){
                                    log("ERROR: " + e.toString());
                                    key.cancel();
                                }

                            } else if(key.isWritable()) {
                                key.interestOps(SelectionKey.OP_READ);
                                forkJoinPool.execute(()->{
                                    try{
                                        log("INFO: Попытка отправки ответа клиенту...");
                                        serverManager.write(key);
                                        log("INFO: Сообщение клиенту успешно отправлено.");
                                    }
                                    catch (ClosedSelectorException e){
                                        if (!selectorIsClosed){
                                            log("WARNING: Селектор прекращает работу.");
                                            selectorIsClosed = true;
                                            //Чтобы не была неразбериха в консоли
                                        }

                                    }
                                    catch (Exception e){
                                        log("ERROR: " + e.toString());
                                    }
                                });

                            }

                        }
                    }
                    catch (SocketException e){
                        log("WARNING: Пользователь отключился.");
                    }
                    catch (ClosedSelectorException e){
                        if (!selectorIsClosed){
                            log("WARNING: Селектор прекращает работу.");
                            selectorIsClosed = true;
                            //Чтобы не была неразбериха в консоли
                        }

                    }
                    catch (Exception ignore){
                        log("ERROR: " + ignore.toString());
                    }

                }
            });
            checkConsole();

        }
        catch (SocketException e){
            log("WARNING: Пользователь отключился.");
        }
        catch (ClosedSelectorException e){
            if (!selectorIsClosed){
                log("WARNING: Селектор прекращает работу.");
                selectorIsClosed = true;
                //Чтобы не была неразбериха в консоли
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Метод для проверки консоли сервера
     */
    private static void checkConsole(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try{
            while (true){
                String command = bufferedReader.readLine();
                if(command.equals("exit")){
                    bufferedReader.close();
                    selector.close();
                    threadPool.shutdown();
                    forkJoinPool.shutdown();
                    log("WARNING: Все потоки закрыты.");
                    channel.close();
                    log("WARNING: Сетевой канал закрыт.");
                    log(" Завершение работы.");
                    System.exit(0);
                }
            }
        }
        catch (Exception e){
        }
    }

    /**
     * Приватный метод для логирования
     * @param message - сообщение
     */
    private static void log(String message){
        time = LocalDateTime.now();
        System.out.println(time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss ")) + message);
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
