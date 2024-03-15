package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import data.User;
import data.UserGenerator;
import order.OrderList;
import user.UserAction;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static data.Constants.USER_NOT_AUTH_ERROR;

public class OrderListTest {
    private User user;
    private UserAction userAction;
    private Response response;
    private OrderList orderList;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        orderList = new OrderList();
    }

    @Test
    @DisplayName("Проверка списка заказов с авторизацией")
    public void getOrderListWithAuthTest() {
        response = orderList.getOrderListWithAuth(user);
        userAction.deleteUser(userAction.login(user));
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("orders", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Проверка списка заказов без авторизации")
    public void getOrderListWithoutAuthTest() {
        response = orderList.getOrderListWithoutAuth();
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_NOT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }
}
