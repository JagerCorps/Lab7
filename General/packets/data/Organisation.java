package packets.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Класс организации
 */

public class Organisation implements Comparable<Organisation>, Serializable {
    /**
     * Поле идентификационного номера организации. Значение поля должна быть больше 0, являться уникальным и заполняться автоматически
     */
    private int id;
    /**
     * Поле имени организации. Не может быть null, не может быть пустой
     */
    private String name;
    /**
     * Поле координат. Не может быть null
     */
    private Coordinates coordinates;
    /**
     * Поле даты основания организации. Не может быть null, генерируется автоматически
     */
    private java.time.LocalDate creationDate;
    /**
     * Поле годового оборота организации. Не может быть null, значение должно быть больше 0
     */
    private Float annualTurnover;
    /**
     * Поле полного имени организации. Не может быть null
     */
    private String fullName;
    /**
     * Поле количества сотрудников. Значение должно быть больше 0
     */
    private int employeesCount;
    /**
     * Поле типа организации. Не может быть null
     */
    private OrganizationType type;
    /**
     * Поле официального адреса организации. Не может быть null
     */
    private Address officialAddress;

    /**
     * Поле владельца объекта, который его создал
     */
    private String owner;

    /**
     * Пустой конструктор
     */
    public Organisation(){}

    /**
     * Конструктор для создания объектов класса организации в процессе считывания из файла
     * @param name - имя
     * @param x - координата x
     * @param y - координата y
     * @param annualTurnover - годовой оборот организации
     * @param fullName - полное имя
     * @param creationDate - дата создания
     * @param employeesCount - количество сотрудников
     * @param type - тип организации
     * @param address - адрес
     */
    public Organisation(String name, double x, double y, LocalDate creationDate, float annualTurnover, String fullName, int employeesCount, String type, String address){
        this.name = name;
        this.coordinates = new Coordinates(x,y);
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = OrganizationType.valueOf(type);
        this.officialAddress = new Address(address);
    }

    /**
     * Метод для задания id объекта
     * @param id - входной параметр id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Метод для задания имени объекта
     * @param name - входной параметр имени
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Метод для задания координат
     * @param x - координата х
     * @param y - координата у
     */
    public void setCoordinates(double x, double y){
        this.coordinates = new Coordinates(x,y);
    }

    /**
     * Метод для задания даты создания орагнизации
     * @param creationDate - входной параметр даты создания
     */
    public void setCreationDate(LocalDate creationDate){
        this.creationDate = creationDate;
    }

    /**
     * Метод для задания годового оборота организации
     * @param annualTurnover - входной параметр годового оборота
     */
    public void setAnnualTurnover(float annualTurnover){
        this.annualTurnover = annualTurnover;
    }

    /**
     * Метод для задания полного имени объекта
     * @param fullName - входной параметр полного имени
     */
    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    /**
     * Метод для задания количества сотрудников
     * @param employeesCount - количество сотрудников
     */
    public void setEmployeesCount(int employeesCount){
        this.employeesCount = employeesCount;
    }

    /**
     * Метод для задания типа организации
     * @param type - тип организации
     */
    public void setType(String type){
        this.type = OrganizationType.valueOf(type);
    }

    /**
     * Метод для задания официального адреса
     * @param street - улица адреса
     */
    public void setOfficialAddress(String street){
        this.officialAddress = new Address(street);
    }

    /**
     * Метод для задания владельца объекта
     * @param owner - владелец объекта
     */
    public void setOwner(String owner){
        this.owner = owner;
    }

    /**
     * Функция получения значения поля {@link Organisation#id}
     * @return возвращает id организации
     */
    public int getId() {
        return id;
    }

    /**
     * Функция получения значения поля {@link Organisation#name}
     * @return возвращает имя организации
     */
    public String getName() {
        return name;
    }

    /**
     * Функция получения значения поля {@link Organisation#coordinates}
     * @return возвращает координаты организации
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Функция получения значения поля {@link Organisation#creationDate}
     * @return возвращает дату создания объекта организации
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Функция получения значения поля {@link Organisation#annualTurnover}
     * @return возвращает годовой оборот организации
     */
    public Float getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * Функция получения значения поля {@link Organisation#fullName}
     * @return возвращает полное имя организации
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Функция получения значения поля {@link Organisation#employeesCount}
     * @return возвращает количество сотрудников организации
     */
    public int getEmployeesCount() {
        return employeesCount;
    }

    /**
     * Функция получения значения поля {@link Organisation#type}
     * @return возвращает тип организации
     */
    public OrganizationType getType() {
        return type;
    }

    /**
     * Функция получения значения поля {@link Organisation#officialAddress}
     * @return возвращает официальный адрес организации
     */
    public Address getOfficialAddress() {
        return officialAddress;
    }

    /**
     * Функция получения создателя объекта
     * @return возвращает создателя оъекта
     */
    public String getOwner(){
        return owner;
    }

    public String dbValues() {

        LocalDateTime time = LocalDateTime.of(creationDate,LocalTime.now());

        return String.format("values (%d; '%s'; %f; %f; '%s'; %f; '%s'; %d; '%s'; '%s'; '%s')",

                id,
                name,
                coordinates.getX(),
                coordinates.getY(),
                Timestamp.valueOf(time),
                annualTurnover,
                fullName,
                employeesCount,
                type,
                officialAddress.getAddress(),
                owner


        ).replaceAll(",", ".").replaceAll(";", ",");

    }

    public String dbProperties() {

        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                "id",
                "name",
                "x",
                "y",
                "creation_date",
                "annual_turnover",
                "full_name",
                "employees_count",
                "type",
                "official_address",
                "owner"
        );
    }

    /**
     * Метод для сравнения объектов класса организации
     * @param o - объект класса организации для сравнения
     * @return возвращает результат сравнения, в данном случае по количеству сотрудников
     */
    @Override
    public int compareTo(Organisation o){
        return this.employeesCount - o.employeesCount;
    }

    /**
     * Метод для представления объекта класса организации в текстовом представлении
     * @return возвращает строковое представление объектаы
     */
    @Override
    public String toString(){
        return String.format("ID: %d\nName: %s\nCoordinates: %s\nDate: %s\nAnnual turnover: %f\nFull name: %s" +
                        "\nEmployees count: %d\nOrganisation type: %s\nOfficial address: %s\nOwner: %s\n",

                id, name, coordinates.toString(), creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),

                annualTurnover, fullName, employeesCount, type.toString(), officialAddress.toString(), owner);
    }

    /**
     * Метод для вывода хеш-кода объекта
     * @return возвращает хеш-код
     */
    @Override
    public int hashCode(){
        return Objects.hash(id, name, coordinates, creationDate, annualTurnover, fullName, employeesCount, type, officialAddress);
    }

}