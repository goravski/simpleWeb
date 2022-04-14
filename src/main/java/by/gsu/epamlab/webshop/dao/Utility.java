package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.command.CommandConstant;
import by.gsu.epamlab.webshop.exceptions.ValidationException;
import by.gsu.epamlab.webshop.model.Byn;
import by.gsu.epamlab.webshop.model.Person;
import by.gsu.epamlab.webshop.model.Product;
import by.gsu.epamlab.webshop.model.Storage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public class Utility {

    public static boolean checkPassword(Optional<Person> optionalPerson, String passwordRequest) throws ValidationException {
        final Logger LOGGER = LogManager.getLogger();
        boolean check;
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            check = person.getPassword().equals(getHas(passwordRequest));
        } else {
            LOGGER.info("There isn't this person in database");
            throw new ValidationException();
        }
        return check;
    }

    public static String getHas(String password) {
        final Logger LOGGER = LogManager.getLogger();
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
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(rawData);
    }

    public static int getTotalPages(ProductDaoImpl entity, int rows) {
        int numberRecords = entity.getNumberOfRecords().get();
        int totalPages = numberRecords / rows;
        if (numberRecords % rows > 0) {
            totalPages += 1;
        }
        return totalPages;
    }

    public static Product createProductFromRequest(HttpServletRequest request) {
        String id = request.getParameter(CommandConstant.ID);
        String nameProduct = request.getParameter(CommandConstant.NAME);
        String price = request.getParameter(CommandConstant.PRICE);
        String describe = request.getParameter(CommandConstant.DESCRIBE);
        return new Product(Integer.parseInt(id), nameProduct, new Byn(Integer.parseInt(price)), describe);
    }


    public static Storage createStorage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Product product = (Product) session.getAttribute(CommandConstant.PRODUCT);
        int productID = product.getIdProduct();
        int quantity = Integer.parseInt(request.getParameter(CommandConstant.QUANTITY));

        return new Storage(productID, quantity);
    }

    public static Person createPersonFromRequest(HttpServletRequest request) {
        String name = request.getParameter(CommandConstant.NAME);
        String login = request.getParameter(CommandConstant.LOGIN);
        String password = request.getParameter(CommandConstant.PASSWORD);
        return new Person(name, login, password);
    }

    public static Person updatePersonFromRequest(HttpServletRequest request) {
        String status = request.getParameter(CommandConstant.STATUS);
        int id = Integer.parseInt(request.getParameter(CommandConstant.ID).trim());
        String name = request.getParameter(CommandConstant.NAME);
        String login = request.getParameter(CommandConstant.LOGIN);
        String role = request.getParameter(CommandConstant.ROLE);
        return new Person(id, name, login, role, status);
    }


    public static boolean isValidRequest(HttpServletRequest request) {
        boolean valid = false;
        Enumeration<String> paramName = request.getParameterNames();
        while (paramName.hasMoreElements()) {
            String param = paramName.nextElement();
            if (request.getParameter(param).trim().isEmpty()) {
                return false;
            }
            valid = true;
        }
        return valid;
    }

}
