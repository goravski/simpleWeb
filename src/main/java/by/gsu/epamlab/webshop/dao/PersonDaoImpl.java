package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.AuthorizationException;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
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

    public PersonDaoImpl() {
    }

    Services services = new Services();

    @Override
    public List<Person> getAll() throws DaoException {
        List<Person> personList = new ArrayList<>();
        String query = "SELECT idPerson, name, login, password, statusName, role " +
                "from person join role on idRole = person.roleId join status on idstatus = person.statusId";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                services.resultSetLoadToListPerson(resultSet, personList);
                LOGGER.info("List of Person initialized");
            }
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException ex) {
            LOGGER.error("Didn't get sql connection in getAll()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return personList;
    }

    @Override
    public Optional<Person> getById(int id) throws DaoException {
        Person person = null;
        String query = "SELECT idPerson, name, login, password, statusName, role from person " +
                "join role on idRole = person.roleId\n" +
                "join status on idstatus = person.statusId WHERE idPerson =?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                person = services.resultSetLoadToPerson(resultSet);
            }
            LOGGER.trace("Get Person by Id");
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get sql connection in getById()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getById()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return Optional.ofNullable(person);
    }

    public Optional<Person> getByLogin(String loginRequest) throws DaoException {
        Person person = null;
        String personPassword;
        String query = "SELECT idPerson, name, login, password, statusName, role from person " +
                "join role on idRole = person.roleId\n" +
                "join status on idstatus = person.statusId WHERE login =?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement(query);
            preparedStatement.setString(1, loginRequest);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idPerson = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String personLogin = resultSet.getString(3);
                personPassword = resultSet.getString(4);
                String status = resultSet.getString(5);
                String role = resultSet.getString(6);
                person = new Person(idPerson, name, personLogin, personPassword, role, status);
            }
            LOGGER.trace("Get Person by Login");
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get data from database in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return Optional.ofNullable(person);
    }

    @Override
    public void update(Person entity) throws DaoException {
        String sql = "update person set name = ?, login = ?, roleId = (select idRole from role where role = ?)" +
                ",statusId = (select idstatus from status where statusName = ?)  where idPerson = ?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLogin());
            preparedStatement.setString(3, entity.getRole());
            preparedStatement.setString(4, entity.getStatus());
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            LOGGER.trace("Person updated in database");
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in update() or SQL error", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void delete(Person person) throws DaoException {
        String sql = "DELETE FROM person where idPerson = ?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
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
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in delete() or SQL error", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in delete()", ex.getCause());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean add(Person entity) throws DaoException {
        boolean isAdd = false;
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            String hashPassword = services.getHas(entity.getPassword());
            String login = entity.getLogin();
            String sqlGet = "SELECT * from person WHERE login = ?";
            String sqlAdd = "insert into person (name, login, password, roleId, statusId) " +
                    "values (?,?,?, (select idRole from role where role=?), (select idstatus from status where statusName =?))";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
            preparedStatement.setString(1, entity.getLogin());
            ResultSet resultSetGet = preparedStatement.executeQuery();
            if (!resultSetGet.next()) {
                preparedStatement = connection.prepareStatement(sqlAdd);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, login);
                preparedStatement.setString(3, hashPassword);
                preparedStatement.setString(4, entity.getRole());
                preparedStatement.setString(5, entity.getStatus());
                isAdd = preparedStatement.executeUpdate() == 1 ? true : false;
                LOGGER.trace("User Added in database");
            } else {
                LOGGER.trace("User with login " + login + "already exists.");
            }
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (AuthorizationException e) {
            LOGGER.error("Hash password error", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
        return isAdd;
    }
}
