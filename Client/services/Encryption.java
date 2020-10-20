package services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс для шифровки
 */
public class Encryption {

    /**
     * Поле кодировки
     */
    private static final MessageDigest md2;

    /**
     * Статический блок
     */
    static {

        MessageDigest digest_tmp = null;

        try {

            digest_tmp = MessageDigest.getInstance("MD2");

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        }

        md2 = digest_tmp;

    }



    public static String md2(String input) {

        byte[] hash = md2.digest(input.getBytes());

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {

            result.append(String.format("%02x", b));

        }



        return result.toString();

    }

}
