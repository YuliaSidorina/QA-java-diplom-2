package order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import data.User;
import data.UserCredentials;
import user.UserAction;

import static io.restassured.RestAssured.given;
import static data.Constants.*;

public class OrderList {
    private Response response;
    private String accessToken;
    private UserCredentials userCredentials;
    private UserAction userAction = new UserAction();
    private CreateOrder createOrder = new CreateOrder();

    public OrderList() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса для получения списка заказов с авторизацией")
    public Response getOrderListWithAuth(User user) {
        createOrder.createOrderWithAuthWithIngredient(user);
        response = userAction.login(user);
        userCredentials = response.as(UserCredentials.class);
        accessToken = userCredentials.getAccessToken().replaceFirst("Bearer ", "");
        return given()
                .auth().oauth2(accessToken)
                .get(ORDER_PATH);
    }

    @Step("Отправка запроса для получения списка заказов без авторизации")
    public Response getOrderListWithoutAuth() {
        return given()
                .auth().oauth2("")
                .get(ORDER_PATH);
    }
}
