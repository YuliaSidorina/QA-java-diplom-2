package user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import data.User;
import data.UserCredentials;
import data.UserInfo;

import static io.restassured.RestAssured.given;
import static data.Constants.*;

public class UserChange {
    private Response response;
    private String accessToken;
    private UserCredentials userCredentials;
    private UserInfo userInfo;
    private UserAction userAction = new UserAction();
    private UserCreate userCreate = new UserCreate();

    public UserChange() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправить запрос на изменение email пользователя с авторизацией")
    public Response changeEmailUserWithAuth(User user) {
        createUserAndFetchData(user);
        userInfo.setEmail("test" + userInfo.getEmail());
        return patchUserInfo();
    }

    @Step("Отправить запрос на изменение имени пользователя с авторизацией")
    public Response changeNameUserWithAuth(User user) {
        createUserAndFetchData(user);
        userInfo.setName("test" + userInfo.getName());
        return patchUserInfo();
    }

    @Step("Отправить запрос на изменение email пользователя без авторизации")
    public Response changeEmailUserWithoutAuth(User user) {
        createUserAndFetchData(user);
        userInfo.setEmail("test" + userInfo.getEmail());
        return patchUserInfo();
    }

    @Step("Отправить запрос на изменение имени пользователя без авторизации")
    public Response changeNameUserWithoutAuth(User user) {
        createUserAndFetchData(user);
        userInfo.setName("test" + userInfo.getName());
        return patchUserInfo();
    }

    @Step("Отправить запрос на изменение существующего email с авторизацией")
    public Response changeExistEmailWithAuth(User user, User userSecond, String email) {
        userCreate.createUser(userSecond);
        createUserAndFetchData(user);
        userInfo.setEmail(userSecond.getEmail());
        return patchUserInfo();
    }

    private void createUserAndFetchData(User user) {
        response = userCreate.createUser(user);
        userCredentials = response.as(UserCredentials.class);
        userInfo = userCredentials.getUser();
        accessToken = userCredentials.getAccessToken().replaceFirst("Bearer ", "");
    }

    private Response patchUserInfo() {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(userInfo)
                .when()
                .patch(UPDATE_USER_PATH);
    }
}
