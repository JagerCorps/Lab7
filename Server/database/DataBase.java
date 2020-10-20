package database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import packets.data.Organisation;
import packets.User;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.PriorityQueue;

/**
 * Класс для реализации работы с базой данных
 */
public class DataBase {

    /**
     * Поле URL
     */
    private static final String URL = "jdbc:postgresql://pg:5432/studs";

    /**
     * Поле логина для подключения к базе данных на кафедральном сервере
     */
    private static final String USER = "s285699";

    /**
     * Поле пароля
     */
    private static final String PASSWORD = "jum939";

    /**
     * База данных
     */
    private static DataBase dataBase;

    /**
     * Поле, содержащее абстракцию соединения
     */
    private static Connection connection;

    /**
     * Статический блок инициализации
     */
    static {

        try {
            dataBase = new DataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Частный конструктор
     * @throws SQLException - исключение
     */
    private DataBase() throws SQLException {

        /**
        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(USER, "se.ifmo.ru", 2222);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(1488, "pg", 5432);
        }
        catch (Exception e){
            e.printStackTrace();
        }
         */

        //Разделитель
        DriverManager.setLogWriter(new PrintWriter(System.out));
        connection = DriverManager.getConnection(URL, USER, PASSWORD);

    }

    /**
     * Метод для прерывания соединения
     * @throws SQLException - исключение
     */
    public void interrupt() throws SQLException {
        connection.close();
    }

    /**
     * Статический метод для получения новой базы данных
     * @return возвращает базу данных
     */
    public static DataBase getInstance() {
        return dataBase;
    }

    /**
     * Главный класс. Напрямую не используется, нужен для тестов
     * @param args - аргументы командной строки
     */
    public static void main(String[] args) {

        User admin = new User("admin", "Besogon");

        Organisation organisation = new Organisation(
                "Test", 22, 33, LocalDate.now(), 223000, "Test Element",
                2200, "COMMERCIAL", "Lenin street 48"
        );
        organisation.setId(100000);
        //dataBase.insertElement(admin, organisation);

        //dataBase.deleteElement(admin, organisation);
        //dataBase.deleteElementById(admin, 100003);
        dataBase.getAllElements().forEach(System.out::println);

    }


    /**
     * Метод для получения всех элементов из базы данных
     * @return возвращает все элементы из базы данных для записи в коллекцию
     */
    public PriorityQueue<Organisation> getAllElements() {

        PriorityQueue<Organisation> organisations = new PriorityQueue<>();

        try (Statement statement = connection.createStatement()) {
            String sql = "select * from organisations";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Organisation org = new Organisation(
                        rs.getString(2), rs.getDouble(3), rs.getDouble(4),
                        rs.getTimestamp(5).toLocalDateTime().toLocalDate(),
                        rs.getFloat(6), rs.getString(7),
                        rs.getInt(8), rs.getString(9), rs.getString(10)
                );
                org.setId(rs.getInt(1));
                org.setOwner(rs.getString(11));
                organisations.add(org);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return organisations;
    }

    /**
     * Метод для добавления элемента в базу данных
     * @param user - пользователь
     * @param organisation - элемент класса {@link Organisation}
     * @return возвращает элемент в случае успешного его добавления
     */
    public Organisation insertElement(User user, Organisation organisation) {

        String sql = null;

        try (Statement statement = connection.createStatement()) {

            sql = "select nextval ('id_seq');";

            ResultSet rs = statement.executeQuery(sql);

            rs.next();
            int id = rs.getInt(1);
            organisation.setOwner(user.getLogin());
            organisation.setId(id);
            sql = "insert into organisations " + organisation.dbProperties() + " " + organisation.dbValues() + ";";
            if (statement.executeUpdate(sql) == 1)
                return organisation;
        } catch (SQLException throwable) {
            System.out.println(sql);
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * Метод для обновления элемента коллекции
     * @param user - пользователь
     * @param id - id элемента
     * @param organisation - элемент класса {@link Organisation}
     * @return возвращает элемент в случае его успешного обновления
     */
    public Organisation updateElement(User user, int id, Organisation organisation) {

        String sql = null;

        try (Statement statement = connection.createStatement()) {

            boolean check = deleteElementById(user,id);
            if (check){
                organisation.setOwner(user.getLogin());
                organisation.setId(id);
                sql = "insert into organisations " + organisation.dbProperties() + " " + organisation.dbValues() + ";";
                if (statement.executeUpdate(sql) == 1)
                    return organisation;
            }
        } catch (SQLException throwable) {
            System.out.println(sql);
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * Метод для удаления элемента из базы данных по его id
     * @param user - пользователь
     * @param id - id элемента
     * @return возвращает true, усли удаление прошло успешно, false в противном случае
     */
    public boolean deleteElementById(User user, int id) {

        try (Statement statement = connection.createStatement()) {
            String sql = "delete from organisations where id = " + id + " and owner = '" + user.getLogin() + "';";
            return statement.executeUpdate(sql) == 1;
        }
        catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    /**
     * Метод для удаления элемента из базы данных
     * @param user - пользователь
     * @param organisation - элемент класса {@link Organisation}
     * @return возвращает true, усли удаление прошло успешно, false в противном случае
     */
    public boolean deleteElement(User user, Organisation organisation) {

        return deleteElementById(user,organisation.getId());
    }

    /**
     * Метод, регистрирующий нового пользователя
     * @param user - пользователь
     * @return возвращает результат регистрации - успешно или нет
     */
    public boolean registerUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try (Statement statement = connection.createStatement()) {
            if (!isThere(user)) {
                final String sql = "insert into users (login, password) values " +
                        "('" + user.getLogin() + "', '" + user.getPassword() + "');";

                return statement.executeUpdate(sql) == 1;
            }
        }
        catch (SQLException th) {
            th.printStackTrace();
        }
        return false;
    }

    /**
     * Метод, проверяющий, существует ли пользователь с таким логином
     * @param user - пользователь
     * @return возвращает результат сравнения (существует/не существует)
     */
    public boolean isThere(User user) {
        try (Statement statement = connection.createStatement()) {
            String login = user.getLogin();

            final String sql = "select * from users where login = '" + login + "';";
            final ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException th) {
            th.printStackTrace();
        }
        return false;
    }

    /**
     * Метод, проверяющий совпадение пароля пользователя
     * @param user - пользователь
     * @return возвращает результат - совпадает пароль или нет
     */
    public boolean checkUser(User user){
        try (Statement statement = connection.createStatement()) {
            String login = user.getLogin();
            String password = user.getPassword();

            final String sql = "select password from users where login = '" + login + "';";
            final ResultSet rs = statement.executeQuery(sql);

            if (rs.next() && rs.getString("password").equals(password)) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
