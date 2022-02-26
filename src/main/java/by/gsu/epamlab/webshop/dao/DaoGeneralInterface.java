package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.exceptions.DaoException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public interface DaoGeneralInterface<E> {

    public abstract List<E> getAll() throws DaoException;

    public abstract Optional<E> getById(int id) throws DaoException;

    public abstract Optional<E> getByLogin(String loginRequest) throws DaoException;

    public abstract void update(E entity) throws DaoException;

    public abstract void delete(E entity) throws DaoException;

    public abstract boolean add(E entity) throws DaoException;
}
