package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.exceptions.ConstantException;
import by.gsu.epamlab.webshop.exceptions.AuthorizationException;
import by.gsu.epamlab.webshop.model.Person;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class Utils {
    public static String getHas(String password) throws AuthorizationException {
        String hashPassword;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            hashPassword = new String(hash, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AuthorizationException(ConstantException.GETHASH_METHOD_CRYPTOGRAPHIC_ALGORITHM_GETINSTANCE_IS_NOT_AVAILABLE.toString());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new AuthorizationException(ConstantException.GETHASH_METHOD_FACTORY_CAN_NOT_GENERATE_HASH.toString());
        }
        return hashPassword;
    }

    public static void resultSetLoadToPerson(ResultSet resultSet, Person person) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String loginPerson = resultSet.getString(3);
        String role = resultSet.getString(5);
        boolean status = resultSet.getBoolean(6);
        person = new Person(idPerson, name, loginPerson, role, status);
    }

    public static void resultSeyLoadToListPerson(ResultSet resultSet, List<Person> personList) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String login = resultSet.getString(3);
        String role = resultSet.getString(5);
        boolean status = resultSet.getBoolean(6);
        Person person = new Person(idPerson, name, login, role, status);
        personList.add(person);
    }

    public static boolean checkLogin(String loginfromRequest, String passFromRequest) {
        boolean chek = false;
        try {
            chek = "FROM_DB_ADMIN_LOGIN".equals(loginfromRequest) && "FROM_DB_ADMIN_PASS".equals(getHas(passFromRequest));
        } catch (AuthorizationException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getCause());
        }
        return chek;
    }
}
