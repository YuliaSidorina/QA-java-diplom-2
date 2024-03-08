package data;

import com.github.javafaker.Faker;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static User getUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();
        return new User(email, password, name);
    }
}
