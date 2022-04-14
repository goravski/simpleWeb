package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Byn;
import by.gsu.epamlab.webshop.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductDaoImplTest {

    private final ConnectionManager cm = Mockito.mock(ConnectionManager.class);
    private final Connection con = Mockito.mock(Connection.class);
    private final PreparedStatement pst = Mockito.mock(PreparedStatement.class);
    private final PreparedStatement pst1 = Mockito.mock(PreparedStatement.class);
    private final PreparedStatement pst2 = Mockito.mock(PreparedStatement.class);
    private final ResultSet rs = Mockito.mock(ResultSet.class);
    private final ResultSet rs1 = Mockito.mock(ResultSet.class);
    private final ResultSet rs2 = Mockito.mock(ResultSet.class);
    private ProductDaoImpl productDao;
    private Product product1;
    private Product product2;
    private final List<Product> productList = new ArrayList<>();

    @BeforeEach
    void setUp() throws SQLException {
        product1 = new Product(10, "Product1", new Byn(1000), "Describe1");
        product2 = new Product(11, "Product2", new Byn(2000), "Describe2");
        productList.add(product1);
        productDao = new ProductDaoImpl(cm);
        when(cm.getConnection()).thenReturn(con);
        when(con.prepareStatement(any(String.class))).thenReturn(pst);
        when(pst.executeQuery()).thenReturn(rs);
    }

    @Test
    void testGetById() throws DaoException {
        Optional<Product> op = productDao.getById(10);
        if (op.isPresent()) {
            Product resultProduct = op.get();
            Assertions.assertEquals(product1, resultProduct);
        }
    }

    @Test
    void testGetByLogin() throws DaoException {
        Optional<Product> op = productDao.getByLogin("Product1");
        if (op.isPresent()) {
            Product resultProduct = op.get();
            Assertions.assertEquals(product1, resultProduct);
        }
    }

    @Test
    void testUpdate() throws DaoException, SQLException {
        Product productNew = new Product(10, "Product1", new Byn(2000), "DescribeNew");
        productDao.update(productNew);
        verify(pst, times(2)).setString(anyInt(), anyString());
        verify(pst, times(2)).setInt(anyInt(), anyInt());
        verify(pst, times(1)).setString(1, productNew.getProductName());
        verify(pst, times(1)).setString(3, productNew.getDescribe());
        verify(pst, times(1)).setInt(2, productNew.getPrice().getValue());
        verify(pst, times(1)).setInt(4, productNew.getIdProduct());
    }

    @Test
    void testDelete() throws DaoException, SQLException {
        productDao.delete(product1);
        verify(pst, times(1)).setObject(1, product1.getIdProduct());
    }

    @Test
    void testAdd() throws DaoException, SQLException {
        String sqlAdd = "insert into products (product_name, price, `describe`, idProduct) values (?,?,?,?)";
        String sqlGet = "SELECT * from products WHERE idProduct = ?";
        when(con.prepareStatement(eq(sqlGet))).thenReturn(pst2);
        when(con.prepareStatement(eq(sqlAdd))).thenReturn(pst);

        when(pst2.executeQuery()).thenReturn(rs2);
        when(rs2.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(rs2.getInt(1)).thenReturn(11);
        int resultId = productDao.add(product2);
        Assertions.assertEquals(product2.getIdProduct(), resultId);
    }


    @Test
    void testGetAllByPage() throws SQLException, DaoException {
        String sqlGet = "select * from products limit 0 , 5";
        String sqlCount = "select count(*) from products";
        when(con.prepareStatement(eq(sqlGet))).thenReturn(pst1);
        when(con.prepareStatement(eq(sqlCount))).thenReturn(pst2);
        when(pst1.executeQuery()).thenReturn(rs1);
        when(rs1.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(rs1.getInt(1)).thenReturn(product1.getIdProduct());
        when(rs1.getString(2)).thenReturn(product1.getProductName());
        when(rs1.getInt(3)).thenReturn(product1.getPrice().getValue());
        when(rs1.getString(4)).thenReturn(product1.getDescribe());
        when(rs2.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(rs2.getString(anyString())).thenReturn("1");
        List<Product> resultProductList = productDao.getAllByPage(1, 1);

        Assertions.assertTrue(productList.equals(resultProductList));
    }
}