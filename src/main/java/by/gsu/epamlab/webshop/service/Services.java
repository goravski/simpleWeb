package by.gsu.epamlab.webshop.service;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.AuthorizationException;
import by.gsu.epamlab.webshop.exceptions.ServiceException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Optional;


public class Services {
    private final Logger LOGGER = LogManager.getLogger();
    private PersonDaoImpl personDao = PersonDaoImpl.getDaoInstance();

    public boolean chekPassword(HttpServletRequest request) {
        boolean check = false;
        return check;
    }

    public boolean checkUser(HttpServletRequest request) {
        boolean check = false;

        return check;
    }

    public boolean checkPassword(Optional <Person>optionalPerson, String passwordRequest) throws ServiceException {
        Person person = optionalPerson.get();
        boolean check = false;
                try {
            check = person.getPassword().equals(getHas(passwordRequest));
        } catch (AuthorizationException ex) {
            LOGGER.error("Incorrect password", ex.getCause());
            System.err.println(ex.getMessage());
        }
        LOGGER.info("There isn't this person in database");
        return check;
    }

    public boolean checkLogin(String loginFromRequest, String loginFromDataBase) throws ServiceException {
        boolean check = false;
        check = loginFromDataBase.equals(loginFromRequest);
        LOGGER.info("There isn't this person in database");
        return check;
    }


    public String getHas(String password) throws AuthorizationException {
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

    public SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secretKey;
    }

    public String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    public Person resultSetLoadToPerson(ResultSet resultSet) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String login = resultSet.getString(3);
        String password = resultSet.getString(4);
        String role = resultSet.getString(5);
        boolean status = resultSet.getBoolean(6);
        return new Person(idPerson, name, login, password, role, status);
    }

    public void resultSetLoadToListPerson(ResultSet resultSet, List<Person> personList) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String login = resultSet.getString(3);
        String password = resultSet.getString(4);
        String role = resultSet.getString(5);
        boolean status = resultSet.getBoolean(6);
        Person person = new Person(idPerson, name, login, password, role, status);
        personList.add(person);
    }
}
