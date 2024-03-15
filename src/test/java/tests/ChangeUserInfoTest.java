package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.Constants;
import data.User;
import data.UserGenerator;
import user.*;

import static io.restassured.RestAssured.baseURI;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserInfoTest {
    private User user;
    private User userSecond;
    private UserChange userChange;
    private UserAction userAction;
    private Response response;

    @Before
    public void setUp() {
        baseURI = Constants.BASE_URI;
        user = new User();
        user.setEmail(UserGenerator.generateEmail());
        user.setName(UserGenerator.generateName());
        userChange = new UserChange();
        userAction = new UserAction();
    }

    @Test
    @DisplayName("Проверка изменения электронной почты пользователя с авторизацией")
    public void changeUserEmailWithAuthTest(){
        response = userChange.changeEmailUserWithAuth(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo("test"+user.getEmail()))
                .and().assertThat().body("user.name", equalTo(user.getName()))
                .and().statusCode(SC_OK);
        user.setEmail("test"+user.getEmail());
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя с авторизацией")
    public void changeUserNameWithAuthTest(){
        response = userChange.changeNameUserWithAuth(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo(user.getEmail()))
                .and().assertThat().body("user.name", equalTo("test"+user.getName()))
                .and().statusCode(SC_OK);
        user.setName("test"+user.getEmail());
    }

    @Test
    @DisplayName("Проверка изменения электронной почты пользователя без авторизации")
    public void changeUserEmailWithoutAuthTest(){
        response = userChange.changeEmailUserWithoutAuth(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(Constants.USER_NOT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя без авторизации")
    public void changeUserNameWithoutAuthTest(){
        response = userChange.changeNameUserWithoutAuth(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(Constants.USER_NOT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Проверка изменения электронной почты пользователя на существующую с авторизацией")
    public void changeExistEmailWithAuthTest(){
        String emailFirstUser = user.getEmail();
        userSecond = new User();
        userSecond.setEmail(UserGenerator.generateEmail());
        userSecond.setName(UserGenerator.generateName());

        response = userChange.changeExistEmailWithAuth(user, userSecond, userSecond.getEmail());
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(Constants.USER_EMAIL_EXISTS_ERROR))
                .and().statusCode(SC_FORBIDDEN);

        user.setEmail(emailFirstUser);
        userAction.deleteUser(userAction.login(userSecond));
    }

    @After
    public void tearDown(){
        userAction.deleteUser(userAction.login(user));
    }
}
