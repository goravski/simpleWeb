package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.command.InterfaceCommand;
import by.gsu.epamlab.webshop.command.RegistrationCommand;
import by.gsu.epamlab.webshop.connection.ConnectionPool;
import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.AuthorizationException;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PersonDaoImpl implements DaoGeneralInterface<Person> {
    private static final Logger LOGGER = LogManager.getLogger();

    private static ConnectionPool pool;

    private PersonDaoImpl() {
    }

    private final static PersonDaoImpl INSTANCE = new PersonDaoImpl();

    public static PersonDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Person> getAll() throws DaoException {
        List<Person> personList = new ArrayList<>();
        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            if (optionalConnection.isPresent()) {
                try (Connection connection = optionalConnection.get()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person ");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Utils.resultSetLoadToListPerson(resultSet, personList);
                        LOGGER.info("List of Person initialized");
                    }
                } catch (SQLException e) {
                    LOGGER.error("Didn't get sql connection in getAll()", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return personList;
    }

    @Override
    public Optional<Person> getById(int id) throws DaoException {
        Person person = null;

        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            if (optionalConnection.isPresent()) {
                try (Connection connection = optionalConnection.get()) {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("SELECT id, name, login, role FROM person WHERE id=?");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Utils.resultSetLoadToPerson(resultSet, person);
                    }
                    LOGGER.info("Get Person by Id");
                } catch (SQLException e) {
                    LOGGER.error("Didn't get sql connection in getById()", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getById()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return Optional.of(person);
    }

    @Override
    public Optional<Person> getByLogin(String login) throws DaoException {
        Person person = null;
        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            if (optionalConnection.isPresent()) {
                try (Connection connection = optionalConnection.get()) {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("SELECT id, name, login, role FROM person WHERE login=?");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Utils.resultSetLoadToPerson(resultSet, person);
                    }
                    LOGGER.info("Get Person by Login");
                } catch (SQLException e) {
                    LOGGER.error("Didn't get sql connection in getByLogin()", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return Optional.of(person);
    }

    @Override
    public void update(Person entity) throws DaoException {
        String sql = "UPDATE person set password = ?, name = ? WHERE login = ?";
        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            if (optionalConnection.isPresent()) {
                try (Connection connection = optionalConnection.get()) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, entity.getName());
                    preparedStatement.setString(2, entity.getLogin());
                    preparedStatement.executeUpdate();
                    LOGGER.info("Person updated in database");
                } catch (SQLException e) {
                    LOGGER.error("Didn't get connection in update() or SQL error", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void delete(Person person) throws DaoException {
        String sql = "DELETE FROM person where id = ?";
        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            if (optionalConnection.isPresent()) {
                try (Connection connection = optionalConnection.get()) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    try {
                        preparedStatement.setObject(1, person.getId());
                    } catch (Exception e) {
                        LOGGER.error("Didn't execute statment in delete()", e.getCause());
                        throw new DaoException(e.getMessage());
                    }
                    int count = preparedStatement.executeUpdate();
                    if (count != 1) {
                        LOGGER.error("On delete modify more then 1 record: " + count);
                        throw new DaoException("On delete modify more then 1 record: " + count);
                    }
                    preparedStatement.close();
                } catch (SQLException e) {
                    LOGGER.error("Didn't get connection in delete() or SQL error", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in delete()", ex.getCause());
            ex.printStackTrace();

        }
    }

    @Override
    public void add(Person entity) throws DaoException {
        RegistrationCommand command = new RegistrationCommand();
        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            try (Connection connection = optionalConnection.get()) {
                String sql = "INSERT person (name, login, password, role, status) Values (?,?,?,?,?)";
                PreparedStatement preparedStatement = connection
                        .prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getLogin());
                preparedStatement.setString(3, getPasswordFromRequest(command.getRequest()));
                preparedStatement.setString(4, entity.getRole());
                preparedStatement.setBoolean(5, entity.getStatus());
                preparedStatement.executeUpdate();
                LOGGER.info("Person Added in database");
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            ex.printStackTrace();
        }

    }

    public static String getPasswordFromRequest(HttpServletRequest request) {
        String hashPassword = "";
        String passFromRequest = request.getParameter("password");
        try {
            hashPassword = Utils.getHas(passFromRequest);
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }
        return hashPassword;
    }

    public static boolean chekPassword(String loginFromRequest, String passFromRequest) throws DaoException {
        boolean chek = false;
        String personLogin = null;
        String personPassword = null;
        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            if (optionalConnection.isPresent()) {
                try (Connection connection = optionalConnection.get()) {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("SELECT login, password FROM person WHERE login=?");
                    preparedStatement.setString(1, loginFromRequest);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        personLogin = resultSet.getString(1);
                        personPassword = resultSet.getString(2);
                    }
                    LOGGER.info("Init Password and Login");
                } catch (SQLException e) {
                    LOGGER.error("Didn't get sql connection in chekPassword()", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        try {
            chek = personPassword.equals(Utils.getHas(passFromRequest));
        } catch (AuthorizationException ex) {
            LOGGER.error("", ex.getCause());
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return chek;
    }


}
