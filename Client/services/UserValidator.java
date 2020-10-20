package services;

import packets.User;

/**
 * Интерфейс для авторизации пользователей
 */
public interface UserValidator {

    /**
     * Метод, инициализирующий пользователя
     * @return возвращает пользователя, который авторизовался
     */
    User authorize();
}
