package tests;

import org.junit.BeforeClass;
import static io.restassured.RestAssured.baseURI;

public class BaseAPITest {

    @BeforeClass
    public static void setupBaseURL() {
        baseURI = "https://stellarburgers.nomoreparties.site/api/auth";
    }
}
