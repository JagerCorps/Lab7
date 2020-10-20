package packets;

import java.io.Serializable;

import java.util.Objects;


/**
 * Класс пользователя
 */
public class User implements Serializable {

    /**
     * Имя пользователя
     */
    private final String login;

    /**
     * Пароль пользователя
     */
    private final String password;

    /**
     * Конструктор пользователя
     * @param login - имя пользователя
     * @param password - пароль
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Метод, возвращающий имя пользователя
     * @return возвращает имя пользователя
     */
    public final String getLogin() {
        return login;
    }

    /**
     * Метод, возвращающий пароль
     * @return возвращает пароль
     */
    public final String getPassword() {return password;}


    /**
     * Метод для сравнения пользователей
     * @param o - сравниваемый объект
     * @return возвращает результат сравнения
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    /**
     * Переопределённый метод получения хеш-кода
     * @return возвращает хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    /**
     * Метод для преобразования объекта в строковом виде
     * @return возвращает строковое представление пользователя
     */
    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
