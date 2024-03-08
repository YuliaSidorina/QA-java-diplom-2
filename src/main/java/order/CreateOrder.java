package order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import data.IngredientForOrder;
import data.IngredientResponse;
import data.User;
import data.UserCredentials;
import user.UserCreate;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static data.Constants.*;

public class CreateOrder {
    private Response response;
    private IngredientResponse ingredientResponse;
    private ArrayList<String> ingredients = new ArrayList<>();
    private String accessToken;
    private UserCredentials userCredentials;
    private IngredientForOrder ingredientForOrder;
    private UserCreate userCreate = new UserCreate();


    public CreateOrder() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса для получения ID ингредиентов")
    public IngredientResponse getIngredientID() {
        return given()
                .header("Content-type", "application/json")
                .get(GET_INGREDIENT_PATH)
                .body().as(IngredientResponse.class);
    }

    @Step("Отправка запроса для создания заказа с авторизацией и ингредиентами")
    public Response createOrderWithAuthWithIngredient(User user) {
        response = userCreate.createUser(user);
        userCredentials = response.as(UserCredentials.class);
        accessToken = userCredentials.getAccessToken().replaceFirst("Bearer ", "");
        ingredientResponse = getIngredientID();
        ingredients.add(ingredientResponse.getData().get(0).getId());
        ingredients.add(ingredientResponse.getData().get(1).getId());
        ingredientForOrder = new IngredientForOrder(ingredients.toArray(new String[]{}));
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(ingredientForOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа без авторизации и с ингредиентами")
    public Response createOrderWithoutAuthWithIngredient() {
        ingredientResponse = getIngredientID();
        ingredients.add(ingredientResponse.getData().get(0).getId());
        ingredients.add(ingredientResponse.getData().get(1).getId());
        ingredientForOrder = new IngredientForOrder(ingredients.toArray(new String[]{}));
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientForOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа с авторизацией и без ингредиентов")
    public Response createOrderWithAuthWithoutIngredient(User user) {
        response = userCreate.createUser(user);
        userCredentials = response.as(UserCredentials.class);
        accessToken = userCredentials.getAccessToken().replaceFirst("Bearer ", "");
        ingredientForOrder = new IngredientForOrder();
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(ingredientForOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа без авторизации и без ингредиентов")
    public Response createOrderWithoutAuthWithoutIngredient() {
        ingredientForOrder = new IngredientForOrder();
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2("")
                .and()
                .body(ingredientForOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа без авторизации и с неправильными ингредиентами")
    public Response createOrderWithoutAuthWithWrongIngredient() {
        ingredientResponse = getIngredientID();
        ingredients.add(ingredientResponse.getData().get(0).getId() + "test");
        ingredients.add(ingredientResponse.getData().get(1).getId());
        ingredientForOrder = new IngredientForOrder(ingredients.toArray(new String[]{}));
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientForOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа с авторизацией и с неправильными ингредиентами")
    public Response createOrderWithAuthWithWrongIngredient(User user) {
        response = userCreate.createUser(user);
        userCredentials = response.as(UserCredentials.class);
        accessToken = userCredentials.getAccessToken().replaceFirst("Bearer ", "");
        ingredientResponse = getIngredientID();
        ingredients.add(ingredientResponse.getData().get(0).getId() + "test");
        ingredients.add(ingredientResponse.getData().get(1).getId());
        ingredientForOrder = new IngredientForOrder(ingredients.toArray(new String[]{}));
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(ingredientForOrder)
                .when()
                .post(ORDER_PATH);
    }

}
