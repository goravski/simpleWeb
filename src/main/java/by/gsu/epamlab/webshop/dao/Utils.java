package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.exceptions.AuthorizationException;
import by.gsu.epamlab.webshop.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;


public class Utils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static String getHas(String password) throws AuthorizationException {
        SecretKey secretKey;
        String keyString = null;
        try {
            secretKey = getKeyFromPassword(password, "@gorav$ki%100&500");
            keyString = convertSecretKeyToString(secretKey);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error of cryptographic algorithm", e.getCause());
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            LOGGER.error("Error cryptographic can't generate hash", e.getCause());
            e.printStackTrace();
        }
        return keyString;
    }

    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secretKey;
    }

    public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    public static void resultSetLoadToPerson(ResultSet resultSet, Person person) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String loginPerson = resultSet.getString(3);
        String role = resultSet.getString(5);
        boolean status = resultSet.getBoolean(6);
        person = new Person(idPerson, name, loginPerson, role, status);
    }

    public static void resultSetLoadToListPerson(ResultSet resultSet, List<Person> personList) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String login = resultSet.getString(3);
        String role = resultSet.getString(5);
        boolean status = resultSet.getBoolean(6);
        Person person = new Person(idPerson, name, login, role, status);
        personList.add(person);
    }

}
