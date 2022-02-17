package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionPool;
import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDaoImpl implements DaoGeneralInterface<Person> {

    private ConnectionPool pool;

    public PersonDaoImpl() throws DaoException {
        try {
            pool = DataBaseConnectionsPool.create();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Person> getAll() throws DaoException {
        List<Person> personList = new ArrayList<>();
        try {
            if (pool.getConnection().isPresent()) {
                try (Connection connection = pool.getConnection().get()) {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("SELECT * FROM person ");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Utils.resultSeyLoadToListPerson(resultSet, personList);
                    }
                } catch (SQLException | ConnectionException e) {
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return personList;
    }

    @Override
    public Optional<Person> getById(int id) throws DaoException {
        Person person = null;
        try {
            if (pool.getConnection().isPresent()) {
                try (Connection connection = pool.getConnection().get()) {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("SELECT id, name, login, role FROM person WHERE id=?");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Utils.resultSetLoadToPerson(resultSet, person);
                    }
                } catch (SQLException | ConnectionException e) {
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }

        return Optional.of(person);
    }

    @Override
    public Optional<Person> getByLogin(String login) throws DaoException {
        Person person = null;
        try {
            if (pool.getConnection().isPresent()) {
                try (Connection connection = pool.getConnection().get()) {
                    PreparedStatement preparedStatement = connection
                            .prepareStatement("SELECT id, name, login, role FROM person WHERE login=?");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Utils.resultSetLoadToPerson(resultSet, person);
                    }
                } catch (SQLException | ConnectionException e) {
                    throw new DaoException(e.getMessage(), e.getCause());
                }
            }
        } catch (ConnectionException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return Optional.of(person);
    }

    @Override
    public void update(Person entity) throws DaoException {
        String sql = "UPDATE person set password = ?, name = ? WHERE login = ?";
        try (Connection connection = pool.getConnection().get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLogin());
            preparedStatement.executeUpdate();
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void delete(Person person) throws DaoException {
        String sql = "DELETE FROM person where id = ?";
        try (Connection connection = pool.getConnection().get()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            try {
                preparedStatement.setObject(1, person.getId());
            } catch (Exception e) {
                throw new DaoException(e.getMessage());

            }
            int count = preparedStatement.executeUpdate();
            if (count != 1) {
                throw new DaoException("On delete modify more then 1 record: " + count);
            }
            preparedStatement.close();
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void add(Person entity) throws DaoException {
        try (Connection connection = pool.getConnection().get()) {
            String sql = "INSERT person (name, login, password, role, status) Values (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLogin());
            preparedStatement.setString(3, entity.getLogin());
            preparedStatement.setString(4, entity.getRole());
            preparedStatement.setBoolean(5, entity.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

}
