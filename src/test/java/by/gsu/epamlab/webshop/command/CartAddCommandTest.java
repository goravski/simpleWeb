package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.CartDaoImpl;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.page.AbstractPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//import org.powermock.api.mockito.PowerMockito;


import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
//@PrepareForTest(fullyQualifiedNames = "by.gsu.epamlab.webshop.*")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartAddCommandTest {

    CartDaoImpl cartDao = Mockito.mock(CartDaoImpl.class);
    private Cart cart;
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private final HttpSession session = Mockito.mock(HttpSession.class);



    @BeforeEach
    void setUp() throws Exception {
        cart = new Cart(3);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(CommandConstant.CART)).thenReturn(cart);
//        PowerMockito.whenNew(CartDaoImpl.class).withAnyArguments().thenReturn(cartDao);
        when(cartDao.add(cart)).thenReturn(33);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testExecute() throws Exception {
        CartAddCommand command = new CartAddCommand();
        AbstractPage result = command.execute(request, response);
        Assert.assertEquals(ConstantJSP.CART_PAGE, result.getUrl());
    }
}