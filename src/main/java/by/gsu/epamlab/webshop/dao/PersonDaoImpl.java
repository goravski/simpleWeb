package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
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
    private int numberOfRecords;

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    DataBaseConnectionsPool connectionsPool = DataBaseConnectionsPool.getConnectionsPool();

    @Override
    public List<Person> getAll() throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        List<Person> personList = new ArrayList<>();
        Connection connection = null;
        String query = "SELECT idPerson, name, login, password, statusName, role " +
                "from person join role on idRole = person.roleId join status on idstatus = person.statusId";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    personList.add(resultSetLoadToPerson(resultSet));
                    LOGGER.info("List of Person initialized");
                }
            } catch (SQLException ex) {
                LOGGER.error("Didn't get sql connection in getAll()", ex.getCause());
                throw new DaoException(ex.getMessage(), ex.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return personList;
    }

    @Override
    public Optional<Person> getById(int id) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Person person = null;
        Connection connection = null;
        String query = "SELECT idPerson, name, login, password, statusName, role from person " +
                "join role on idRole = person.roleId\n" +
                "join status on idstatus = person.statusId WHERE idPerson =?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    person = resultSetLoadToPerson(resultSet);
                }
                LOGGER.trace("Get Person by Id");
            } catch (SQLException e) {
                LOGGER.error("Didn't get sql connection in getById()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getById()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return Optional.ofNullable(person);
    }

    public Optional<Person> getByLogin(String loginRequest) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Person person = null;
        Connection connection = null;
        String query = "SELECT idPerson, name, login, password, statusName, role from person " +
                "join role on idRole = person.roleId\n" +
                "join status on idstatus = person.statusId WHERE login =?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, loginRequest);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    person = resultSetLoadToPerson(resultSet);
                }
                LOGGER.trace("Get Person by Login");
            } catch (SQLException e) {
                LOGGER.error("Didn't get data from database in getByLogin()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return Optional.ofNullable(person);
    }

    @Override
    public void update(Person entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String sql = "update person set name = ?, login = ?, roleId = (select idRole from role where role = ?)" +
                ",statusId = (select idstatus from status where statusName = ?)  where idPerson = ?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getLogin());
                preparedStatement.setString(3, entity.getRole());
                preparedStatement.setString(4, entity.getStatus());
                preparedStatement.setInt(5, entity.getId());
                preparedStatement.executeUpdate();
                LOGGER.trace("Person updated in database");
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in update() or SQL error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
    }

    @Override
    public void delete(Person person) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String sql = "DELETE FROM person where idPerson = ?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, person.getId());
                int count = preparedStatement.executeUpdate();
                if (count != 1) {
                    LOGGER.error("On delete modify more then 1 record: " + count);
                    throw new DaoException("On delete modify more then 1 record: " + count);
                }
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in delete() or SQL error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in delete()", ex.getCause());
            ex.printStackTrace();
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
    }

    @Override
    public int add(Person entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String hashPassword = Utility.getHas(entity.getPassword());
        String login = entity.getLogin();
        int idPerson = 0;
        String sqlGet = "SELECT * from person WHERE login = ?";
        String sqlAdd = "insert into person (name, login, password, roleId, statusId) " +
                "values (?,?,?, (select idRole from role where role=?), (select idstatus from status where statusName =?))";
        String sqlGetId = "select last_insert_id()";
        try {
            connection = connectionsPool.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
                preparedStatement.setString(1, entity.getLogin());
                ResultSet resultSetGet = preparedStatement.executeQuery();
                if (!resultSetGet.next()) {
                    preparedStatement = connection.prepareStatement(sqlAdd);
                    preparedStatement.setString(1, entity.getName());
                    preparedStatement.setString(2, login);
                    preparedStatement.setString(3, hashPassword);
                    preparedStatement.setString(4, entity.getStatus());
                    preparedStatement.setString(5, entity.getRole());
                    preparedStatement.executeUpdate();
                    LOGGER.trace("User Added in database");
                    preparedStatement = connection.prepareStatement(sqlGetId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        idPerson = resultSet.getInt(1);
                    }
                } else {
                    LOGGER.trace("User with login " + login + "already exists.");
                }
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return idPerson;
    }

    private Person resultSetLoadToPerson(ResultSet resultSet) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String login = resultSet.getString(3);
        String password = resultSet.getString(4);
        String status = resultSet.getString(5);
        String role = resultSet.getString(6);
        return new Person(idPerson, name, login, password, role, status);
    }
}
