package services;

import commands.Exit;
import packets.data.Organisation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Класс для считывания данных из файла и запись в файл
 */
public class Parser {

    /**
     * Метод, осуществляющий запись данных в файл. Ничего не возвращает.
     * @param organisations - коллекция объектов класса {@link Organisation}
     * @param file - файл, в который будет осуществлена запись данных
     */
    public static void write(PriorityQueue<Organisation> organisations, File file){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            for (Organisation o: organisations) {

                String id = String.valueOf(o.getId());
                String name = o.getName();
                String coordinates = o.getCoordinates().toString();
                String creationDate = o.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                String annualTurnover = String.valueOf(o.getAnnualTurnover());
                String fullName = o.getFullName();
                String employeesCount = String.valueOf(o.getEmployeesCount());
                String organisationType = o.getType().toString();
                String address = o.getOfficialAddress().toString();

                Element organisation = doc.createElement("organisation");
                rootElement.appendChild(organisation);

                Element idE = doc.createElement("id");
                idE.appendChild(doc.createTextNode(id));
                organisation.appendChild(idE);

                Element nameE = doc.createElement("name");
                nameE.appendChild(doc.createTextNode(name));
                organisation.appendChild(nameE);

                Element coordinatesE = doc.createElement("coordinates");
                coordinatesE.appendChild(doc.createTextNode(coordinates));
                organisation.appendChild(coordinatesE);

                Element creationDateE = doc.createElement("creationDate");
                creationDateE.appendChild(doc.createTextNode(creationDate));
                organisation.appendChild(creationDateE);

                Element annualTurnoverE = doc.createElement("annualTurnover");
                annualTurnoverE.appendChild(doc.createTextNode(annualTurnover));
                organisation.appendChild(annualTurnoverE);

                Element fullNameE = doc.createElement("fullName");
                fullNameE.appendChild(doc.createTextNode(fullName));
                organisation.appendChild(fullNameE);

                Element employeesCountE = doc.createElement("employeesCount");
                employeesCountE.appendChild(doc.createTextNode(employeesCount));
                organisation.appendChild(employeesCountE);

                Element typeE = doc.createElement("type");
                typeE.appendChild(doc.createTextNode(organisationType));
                organisation.appendChild(typeE);

                Element addressE = doc.createElement("officialAddress");
                addressE.appendChild(doc.createTextNode(address));
                organisation.appendChild(addressE);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 3);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(bufferedWriter);
            transformer.transform(source, result);

            }
        catch (Exception e) {
            LocalDateTime time = LocalDateTime.now();
            System.out.println(time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss "))+
                    "ERROR: Возникла проблема при записи данных в файл");
        }
    }

    /**
     * Метод, осуществляющий считывание данных из файла
     * @param path - путь до нужного файла
     * @param scanner - сканер
     * @return возвращает коллекцию объектов класса {@link Organisation}
     */
    public static PriorityQueue<Organisation> read(String path, Scanner scanner){

        PriorityQueue<Organisation> organisationPriorityQueue = new PriorityQueue<>();

        int id;
        String name;
        double x;
        double y;
        LocalDate creationDate;
        float annualTurnover;
        String fullName;
        int employeesCount;
        String organisationType;
        String address;

        try{
            InputSource source = new InputSource(new InputStreamReader(new FileInputStream(new File(path))));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(source);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("organisation");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    name = element.getElementsByTagName("name").item(0).getTextContent();
                    x = Double.parseDouble(element.getElementsByTagName("coordinates").item(0).getTextContent().split(" ")[2]);
                    y = Double.parseDouble(element.getElementsByTagName("coordinates").item(0).getTextContent().split(" ")[5]);
                    creationDate = LocalDate.parse(element.getElementsByTagName("creationDate").item(0).getTextContent(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    annualTurnover = Float.parseFloat(element.getElementsByTagName("annualTurnover").item(0).getTextContent());
                    fullName = element.getElementsByTagName("fullName").item(0).getTextContent();
                    employeesCount = Integer.parseInt(element.getElementsByTagName("employeesCount").item(0).getTextContent());
                    organisationType = element.getElementsByTagName("type").item(0).getTextContent();
                    address = element.getElementsByTagName("officialAddress").item(0).getTextContent();

                    Organisation organisation = new Organisation(name, x, y, creationDate, annualTurnover, fullName, employeesCount, organisationType, address);
                    organisation.setId(id);
                    organisationPriorityQueue.add(organisation);
                }
            }

        }
        catch (Exception e) {
            Exit exit = new Exit(scanner);
            LocalDateTime time = LocalDateTime.now();
            System.out.println(time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss "))+
                    "ERROR: Возникла проблема при считывании файла");
            exit.execute();
        }
        finally {
            return organisationPriorityQueue;
        }
    }
}
