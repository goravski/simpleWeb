package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartDaoImplTest {
    private DaoGeneralInterface <Cart> cartDao;
    private final ConnectionManager cm = Mockito.mock(ConnectionManager.class);
    private final Connection con = Mockito.mock(Connection.class);
    private final PreparedStatement pst = Mockito.mock(PreparedStatement.class);
    private final ResultSet rs = Mockito.mock(ResultSet.class);
    private Cart cart;



    @BeforeEach
    void setUp() throws Exception {
        cart = new Cart(37, 4);
        cartDao = new CartDaoImpl(cm);
        when(cm.getConnection()).thenReturn(con);
        when(con.prepareStatement(any(String.class))).thenReturn(pst);
        when(pst.executeQuery()).thenReturn(rs);
        when(pst.executeUpdate()).thenReturn(1);
        when(rs.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(rs.getInt(1)).thenReturn(cart.getId());
        when(rs.getInt(2)).thenReturn(cart.getPersonId());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetById() throws DaoException, NoSuchMethodException {
        cart = new Cart(37, 4);
        Optional <Cart> op = cartDao.getById(4);
        if (op.isPresent()){
            Cart result = op.get();
            Assertions.assertEquals(cart, result);
        }
    }

    @Test
    void testWrongGetById() throws DaoException, NoSuchMethodException {
        cart = new Cart(33, 5);
        Optional <Cart> op = cartDao.getById(4);
        if (op.isPresent()){
            Cart result = op.get();
            Assertions.assertNotEquals(cart, result);
        }
    }

    @Test
    void testAdd() throws DaoException, NoSuchMethodException {
        cart = new Cart(37, 4);
        int result = cartDao.add(cart);
        Assertions.assertEquals(cart.getId(), result);
    }

}