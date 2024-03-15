package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import data.User;
import data.UserGenerator;
import order.CreateOrder;
import user.UserAction;
import user.UserCreate;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static data.Constants.CREATE_ORDER_WITHOUT_INGREDIENTS_ERROR;

public class CreateOrderTest {
    private User user;
    private UserAction userAction;
    private UserCreate createUser;
    private Response response;
    private CreateOrder createOrder;
    private ArrayList<String> ingredients;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        createUser = new UserCreate();
        createOrder = new CreateOrder();
        ingredients = new ArrayList<>();
    }


    @Test
    @DisplayName("Проверка создания заказа с авторизацией пользователя и без ингредиентов")
    public void createOrderWithAuthWithoutIngredientTest() {
        response = createOrder.createOrderWithAuthWithoutIngredient(user);
        userAction.deleteUser(userAction.login(user));
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(CREATE_ORDER_WITHOUT_INGREDIENTS_ERROR))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации пользователя и без ингредиентов")
    public void createOrderWithoutAuthWithoutIngredientTest() {
        response = createOrder.createOrderWithoutAuthWithoutIngredient();
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(CREATE_ORDER_WITHOUT_INGREDIENTS_ERROR))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации пользователя и с неправильными ингредиентами")
    public void createOrderWithoutAuthWithWrongIngredientTest() {
        response = createOrder.createOrderWithoutAuthWithWrongIngredient();
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией пользователя и с неправильными ингредиентами")
    public void createOrderWithAuthWithWrongIngredientTest() {
        response = createOrder.createOrderWithAuthWithWrongIngredient(user);
        userAction.deleteUser(userAction.login(user));
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
