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
        return createUserWithOptionalFields(user, true, true, true);
    }

    @Step("Отправить запрос на создание пользователя с необязательными полями")
    public Response createUserWithOptionalFields(User user, boolean includePassword, boolean includeEmail, boolean includeName) {
        if (!includePassword) user.setPassword(null);
        if (!includeEmail) user.setEmail(null);
        if (!includeName) user.setName(null);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }
}
