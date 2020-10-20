package services;

import packets.data.Organisation;
import java.util.Scanner;

/**
 * Класс, содержащий статические методы, приглашающие пользователя к вводу
 * Значений полей класса {@link Organisation},
 * Которые затем проверяют правильность ввода
 * И возвращают эти значения для дальнейшей обработки
 */
public class FieldReaders {

    /**
     * Метод для считывания полей строчного типа
     * @param scanner - сканер
     * @param message - приглашение к вводу
     * @return возвращает значение поля строчного типа, считанного с клавиатуры
     */
    public static String readString(Scanner scanner, String message){
        System.out.println(message);
        String name = "";
        do {
            name = scanner.nextLine().trim();
            if (name.isEmpty() | name.equals("")){
                System.out.println("Поле не может быть пустым.");
                System.out.println("Повторите ввод:");
            }
            else{
                break;
            }

        }while (true);
        return name;
    }

    /**
     * Метод для считывания координаты Х
     * @param scanner - сканер
     * @return - возвращает координату Х, считанную с клавиатуры
     */
    public static Double readX(Scanner scanner) {
        System.out.println("Ввод координат. Вводите число без пробелов и разделителей.");
        System.out.println("Введите х:");
        double x = -423;
        do {
            try {
                x = Double.parseDouble(scanner.nextLine());
                if (x < -422) {
                    System.out.println("Координата х не может быть меньше -422.");
                    System.out.println("Повторите ввод:");
                } else {
                    break;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Неправильный формат ввода. Вводите число без пробелов и разделителей.");
                System.out.println("Повторите ввод:");
            }

        } while (x < -422);
        return x;
    }

    /**
     * Метод для считывания координаты У
     * @param scanner - сканер
     * @return - возвращает координату У, считанную с клавиатуры
     */
    public static Double readY(Scanner scanner){
        System.out.println("Введите у:");
        double y;
        do {
            try {
                y = Double.parseDouble(scanner.nextLine());
                break;
            }
            catch (NumberFormatException ex) {
                System.out.println("Неправильный формат ввода. Вводите число без пробелов и разделителей.");
                System.out.println("Повторите ввод:");
            }
        }while (true);
        return y;
    }

    /**
     * Метод для считывания годового оборота организации
     * @param scanner - сканер
     * @return - возвращает значение годового оборота, считанное с клавиатуры
     */
    public static Float readTurnover(Scanner scanner){
        System.out.println("Введите годовой оборот:");
        float turnover = -1;
        do {
            try {
                turnover = Float.parseFloat(scanner.nextLine());
                if (turnover < 0) {
                    System.out.println("Годовой оборот не может быть меньше 0.");
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
        }while (turnover < 0);
        return turnover;
    }

    /**
     * Метод для считывания полей целочисленного типа
     * @param scanner - сканер
     * @param message - приглашение к вводу
     * @return возвращает значение поля целочисленного типа, считанного с клавиатуры
     */
    public static Integer readInt(Scanner scanner, String message){
        System.out.println(message);
        int number = -1;
        do {
            try {
                number = Integer.parseInt(scanner.nextLine());
                if (number < 0) {
                    System.out.println("Число не может быть меньше 0.");
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
        }while (number < 0);
        return number;
    }

    /**
     * Метод для считывания типа организации
     * @param scanner - сканер
     * @return - возвращает тип организации, считанный с клавиатуры
     */
    public static String readType(Scanner scanner){
        Organisation organization = new Organisation();
        System.out.println("Ввод типа организации. Вводить название типа строго по примерам далее. Типы организаций:");
        System.out.println("COMMERCIAL");
        System.out.println("GOVERNMENT");
        System.out.println("PRIVATE_LIMITED_COMPANY");
        System.out.println("OPEN_JOINT_STOCK_COMPANY");
        System.out.println("Введите тип организации:");
        String type;
        do {
            try{
                type = scanner.nextLine();
                if (type.isEmpty()) {
                    System.out.println("Строка не может быть пустой.");
                    System.out.println("Повторите ввод:");
                }
                else{
                    organization.setType(type);
                    return type;
                }
            }
            catch (IllegalArgumentException ex) {
                System.out.println("Тип введён в неправильном формате.");
                System.out.println("Вводить название типа строго по примерам далее. Типы организаций:");
                System.out.println("COMMERCIAL");
                System.out.println("GOVERNMENT");
                System.out.println("PRIVATE_LIMITED_COMPANY");
                System.out.println("OPEN_JOINT_STOCK_COMPANY");
                System.out.println("Повторите ввод:");
            }
        }while (true);
    }



}
