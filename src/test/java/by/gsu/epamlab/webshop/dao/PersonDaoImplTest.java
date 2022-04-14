package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonDaoImplTest {
    private DaoGeneralInterface<Person> personDao;
    private final ConnectionManager cm = Mockito.mock(ConnectionManager.class);
    private final Connection con = Mockito.mock(Connection.class);
    private final PreparedStatement pst = Mockito.mock(PreparedStatement.class);
    private final PreparedStatement pst1 = Mockito.mock(PreparedStatement.class);
    private final PreparedStatement pst2 = Mockito.mock(PreparedStatement.class);
    private final ResultSet rs = Mockito.mock(ResultSet.class);
    private final ResultSet rs1 = Mockito.mock(ResultSet.class);
    private final ResultSet rs2 = Mockito.mock(ResultSet.class);
    private Person person1;
    private final List<Person> personList = new ArrayList<>();


    @BeforeEach
    void setUp() throws Exception {
        person1 = new Person(200, "name1", "login1", "parole1", "user", "open");
        personList.add(person1);
        personDao = new PersonDaoImpl(cm);
        when(cm.getConnection()).thenReturn(con);
        when(con.prepareStatement(any(String.class))).thenReturn(pst);
        when(pst.executeQuery()).thenReturn(rs);
        when(pst.executeUpdate()).thenReturn(1);
        when(rs.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        when(rs.getInt(1)).thenReturn(200);
        when(rs.getString(2)).thenReturn("name1");
        when(rs.getString(3)).thenReturn("login1");
        when(rs.getString(4)).thenReturn("parole1");
        when(rs.getString(6)).thenReturn("user");
        when(rs.getString(5)).thenReturn("open");

    }

    @Test
    void testGetAll() throws DaoException, NoSuchMethodException {
        List<Person> resultList = personDao.getAll();
        for (Person person : resultList) {
            int i = 0;
            Assertions.assertEquals(personList.get(i), person);
            i++;
        }
    }

    @Test
    void testGetById() throws DaoException, NoSuchMethodException {
        Optional <Person> op = personDao.getById(200);
        if(op.isPresent()){
            Person resultPerson = op.get();
            Assertions.assertEquals(resultPerson, person1);
        }

    }

    @Test
    void testGetByLogin() throws DaoException, NoSuchMethodException {
        Optional <Person> op = personDao.getByLogin("login2");
        if(op.isPresent()){
            Person resultPerson = op.get();
            Assertions.assertEquals(resultPerson, person1);
        }
    }

    @Test
    void testUpdate() throws DaoException, NoSuchMethodException, SQLException {
        personDao.update(person1);
        verify(pst, times(4)).setString(anyInt(), anyString());
        verify(pst, times(1)).setString(1, person1.getName());
        verify(pst, times(1)).setString(2, person1.getLogin());
        verify(pst, times(1)).setString(3, person1.getRole());
        verify(pst, times(1)).setString(4, person1.getStatus());
        verify(pst, times(1)).setInt(5, person1.getId());
        verify(pst, times(1)).executeUpdate();
    }

    @Test
    void testDelete() throws DaoException, NoSuchMethodException, SQLException {
        personDao.delete(person1);
        verify(pst, times(1)).setObject(1, person1.getId());
    }

    @Test
    void testAddNew() throws DaoException, NoSuchMethodException, SQLException {
        String sqlGet = "SELECT * from person WHERE login = ?";
        String sqlAdd = "insert into person (name, login, password, roleId, statusId) " +
                "values (?,?,?, (select idRole from role where role=?), (select idstatus from status where statusName =?))";
        String sqlGetId = "select last_insert_id()";
        try(MockedStatic<Utility> utility = Mockito.mockStatic(Utility.class)){
            utility.when(()->Utility.getHas("parole1")).thenReturn("parole1");
            when(con.prepareStatement(eq(sqlGet))).thenReturn(pst);
            when(con.prepareStatement(eq(sqlAdd))).thenReturn(pst1);
            when(con.prepareStatement(eq(sqlGetId))).thenReturn(pst2);

            when(pst.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(Boolean.FALSE);
            when(pst1.executeQuery()).thenReturn(rs1);

            when(pst2.executeQuery()).thenReturn(rs2);
            when(rs2.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
            when(rs2.getInt(1)).thenReturn(200);
            int resultId = personDao.add(person1);
            Assertions.assertEquals(person1.getId(), resultId);
        }

    }

    @Test()
    void testAddExistId() throws DaoException, NoSuchMethodException, SQLException {
        String sqlGet = "SELECT * from person WHERE login = ?";
        String sqlAdd = "insert into person (name, login, password, roleId, statusId) " +
                "values (?,?,?, (select idRole from role where role=?), (select idstatus from status where statusName =?))";
        String sqlGetId = "select last_insert_id()";
        try(MockedStatic<Utility> utility = Mockito.mockStatic(Utility.class)){
            utility.when(()->Utility.getHas("parole1")).thenReturn("parole1");
            when(con.prepareStatement(eq(sqlGet))).thenReturn(pst);
            when(con.prepareStatement(eq(sqlAdd))).thenReturn(pst1);
            when(con.prepareStatement(eq(sqlGetId))).thenReturn(pst2);

            when(pst.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(Boolean.TRUE);

            int resultId = personDao.add(person1);
            Assertions.assertEquals(0, resultId);
        }

    }
}