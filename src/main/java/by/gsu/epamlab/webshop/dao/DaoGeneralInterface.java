package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface DaoGeneralInterface<E> {

    default List<E> getAll() throws DaoException, NoSuchMethodException{
        throw new NoSuchMethodException("Method not implemented");
    }

    default Optional<E> getById(int id) throws DaoException, NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    default Optional<E> getByLogin(String loginRequest) throws DaoException, NoSuchMethodException{
        throw new NoSuchMethodException("Method not implemented");
    }

    default void update(E entity) throws DaoException, NoSuchMethodException{
        throw new NoSuchMethodException("Method not implemented");
    }

    default void delete(E entity) throws DaoException, NoSuchMethodException{
        throw new NoSuchMethodException("Method not implemented");
    }

    default int add(E entity) throws DaoException, NoSuchMethodException{
        throw new NoSuchMethodException("Method not implemented");
    }

    default void add(List <E> list) throws DaoException, NoSuchMethodException{
        throw new NoSuchMethodException("Method not implemented");
    }
}
