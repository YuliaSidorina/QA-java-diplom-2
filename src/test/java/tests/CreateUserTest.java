package tests;

import data.User;
import data.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserAction;
import user.UserCreate;

import static data.Constants.*;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    private User user;
    private UserAction userAction;
    private UserCreate createUser;
    private Response response;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        createUser = new UserCreate();
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    public void createNewUserTest() {
        response = createUser.createUser(user);
        response.then()
                .assertThat().body(RESPONSE_FIELD_SUCCESS, equalTo(true))
                .and().statusCode(SC_OK)
                .and().assertThat().body(RESPONSE_FIELD_USER_EMAIL, equalTo(user.getEmail()))
                .and().assertThat().body(RESPONSE_FIELD_USER_NAME, equalTo(user.getName()));
    }

    @Test
    @DisplayName("Проверка создания одинакового пользователя")
    public void createTheSameUserTest() {
        response = createUser.createUser(user);
        Response duplicateResponse = createUser.createUser(user);
        duplicateResponse.then()
                .assertThat().body(RESPONSE_FIELD_SUCCESS, equalTo(false))
                .and().assertThat().body(RESPONSE_FIELD_MESSAGE, equalTo(CREATE_EXIST_USER_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка создания пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        response = createUser.createUserWithOptionalFields(user, false, true, true);
        response.then()
                .assertThat().body(RESPONSE_FIELD_SUCCESS, equalTo(false))
                .and().assertThat().body(RESPONSE_FIELD_MESSAGE, equalTo(MISSING_FIELD_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка создания пользователя без электронной почты")
    public void createUserWithoutEmailTest() {
        response = createUser.createUserWithOptionalFields(user, true, false, true);
        response.then()
                .assertThat().body(RESPONSE_FIELD_SUCCESS, equalTo(false))
                .and().assertThat().body(RESPONSE_FIELD_MESSAGE, equalTo(MISSING_FIELD_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    public void createUserWithoutNameTest() {
        response = createUser.createUserWithOptionalFields(user, true, true, false);
        response.then()
                .assertThat().body(RESPONSE_FIELD_SUCCESS, equalTo(false))
                .and().assertThat().body(RESPONSE_FIELD_MESSAGE, equalTo(MISSING_FIELD_ERROR))
                .and().statusCode(SC_FORBIDDEN);
    }

    @After
    public void tearDown() {
        if (response != null && response.then().extract().path(RESPONSE_FIELD_SUCCESS).equals(true)) {
            userAction.deleteUser(response);
        }
    }
}
