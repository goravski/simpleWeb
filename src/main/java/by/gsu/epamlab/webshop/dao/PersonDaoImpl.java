package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.AuthorizationException;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.exceptions.ServiceException;
import by.gsu.epamlab.webshop.model.Person;
import by.gsu.epamlab.webshop.service.Services;
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


    private PersonDaoImpl() {
    }

    private final static PersonDaoImpl INSTANCE = new PersonDaoImpl();

    public static PersonDaoImpl getDaoInstance() {
        return INSTANCE;
    }

    Services services = new Services();


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
                        services.resultSetLoadToListPerson(resultSet, personList);
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
                    preparedStatement.setInt(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        person = services.resultSetLoadToPerson(resultSet);
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
        return Optional.ofNullable(person);
    }

    @Override
    public Optional<Person> getByLoginAndPassword(String loginRequest, String passwordRequest) throws DaoException {
        Person person = null;
        String personPassword;

        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            if (optionalConnection.isPresent()) {
                try (Connection connection = optionalConnection.get()) {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("SELECT * FROM person WHERE (login, password) =(?,?)");
                    preparedStatement.setString(1, loginRequest);
                    preparedStatement.setString(2, services.getHas(passwordRequest));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int idPerson = resultSet.getInt(1);
                        String name = resultSet.getString(2);
                        String personLogin = resultSet.getString(3);
                        personPassword = resultSet.getString(4);
                        String role = resultSet.getString(5);
                        boolean status = resultSet.getBoolean(6);
                        if (services.checkPassword(passwordRequest, personPassword)) {
                            person = new Person(idPerson, name, personLogin, role, status);
                        }
                    }
                    LOGGER.info("Get Person by Login");
                } catch (SQLException e) {
                    LOGGER.error("Didn't get sql connection in getByLogin()", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                } catch (ServiceException e) {
                    LOGGER.error("Cant check password in Services", e.getCause());
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (AuthorizationException e) {
            LOGGER.error("Incorrect password", e.getCause());
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return Optional.ofNullable(person);
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
    public boolean add(Person entity) throws DaoException {
        boolean isAdd = false;
        try {
            Optional<Connection> optionalConnection = DataBaseConnectionsPool.getConnection();
            try (Connection connection = optionalConnection.get()) {
                String hashPassword = services.getHas(entity.getPassword());
                String sqlGet = "SELECT * from person WHERE (login, password) = (?,?)";
                String sqlAdd = "INSERT person (name, login, password, role, status) Values (?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
                preparedStatement.setString(1, entity.getLogin());
                preparedStatement.setString(2, hashPassword);
                ResultSet resultSetGet = preparedStatement.executeQuery();
                if (!resultSetGet.next()){
                    preparedStatement = connection.prepareStatement(sqlAdd);
                    preparedStatement.setString(1, entity.getName());
                    preparedStatement.setString(2, entity.getLogin());
                    preparedStatement.setString(3, hashPassword);
                    preparedStatement.setString(4, entity.getRole());
                    preparedStatement.setBoolean(5, entity.getStatus());
                    preparedStatement.executeUpdate();
                    isAdd = true;
                    LOGGER.info("Person Added in database");
                }
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            } catch (AuthorizationException e) {
                LOGGER.error("Hash password error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
        return isAdd;
    }
}
