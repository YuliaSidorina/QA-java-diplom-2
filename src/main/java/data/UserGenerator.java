package data;

import com.github.javafaker.Faker;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static User getUser() {
        String email = generateEmail();
        String password = generatePassword();
        String name = generateName();
        return new User(email, password, name);
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static String generateName() {
        return faker.name().fullName();
    }
}
