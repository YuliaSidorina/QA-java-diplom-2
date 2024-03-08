package user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import data.User;

import static io.restassured.RestAssured.given;
import static data.Constants.BASE_URI;
import static data.Constants.CREATE_USER_PATH;

public class UserCreate {

    public UserCreate() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправить запрос на создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }

    @Step("Отправить запрос на создание пользователя без пароля")
    public Response createUserWithoutPassword(User user) {
        user.setPassword(null);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }

    @Step("Отправить запрос на создание пользователя без электронной почты")
    public Response createUserWithoutEmail(User user) {
        user.setEmail(null);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }

    @Step("Отправить запрос на создание пользователя без имени")
    public Response createUserWithoutName(User user) {
        user.setName(null);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }
}
