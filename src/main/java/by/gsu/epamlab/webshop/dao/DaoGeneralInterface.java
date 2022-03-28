package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface DaoGeneralInterface<E> {

    List<E> getAll() throws DaoException, NoSuchMethodException;

    Optional<E> getById(int id) throws DaoException;

    Optional<E> getByLogin(String loginRequest) throws DaoException, NoSuchMethodException;

    void update(E entity) throws DaoException, NoSuchMethodException;

    void delete(E entity) throws DaoException, NoSuchMethodException;

    int add(E entity) throws DaoException, NoSuchMethodException;
}
