package data;

public class Constants {
    // Базовый URI для API
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";

    // Пути к различным эндпоинтам API
    public static final String CREATE_USER_PATH = "/api/auth/register";
    public static final String UPDATE_USER_PATH = "/api/auth/user";
    public static final String LOGOUT_USER_PATH = "/api/auth/logout";
    public static final String LOGIN_USER_PATH = "/api/auth/login";
    public static final String GET_INGREDIENT_PATH = "/api/ingredients";
    public static final String ORDER_PATH = "/api/orders";

    // Сообщения об ошибках
    public static final String CREATE_EXIST_USER_ERROR = "User already exists";
    public static final String MISSING_FIELD_ERROR = "Email, password, and name are required fields";
    public static final String LOGIN_USER_ERROR = "Email or password is incorrect";
    public static final String USER_NOT_AUTH_ERROR = "You should be authorized";
    public static final String USER_EMAIL_EXISTS_ERROR = "User with such email already exists";
    public static final String CREATE_ORDER_WITHOUT_INGREDIENTS_ERROR = "Ingredient ids must be provided";

    // Дополнительные поля ответа API
    public static final String RESPONSE_FIELD_SUCCESS = "success";
    public static final String RESPONSE_FIELD_USER_EMAIL = "user.email";
    public static final String RESPONSE_FIELD_MESSAGE = "message";
    public static final String RESPONSE_FIELD_USER_NAME = "user.name";
}
