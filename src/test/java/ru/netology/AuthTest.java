package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.PropertyEditor;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void logInAsRegistredUserWithValidUsernameAndPassword() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("div#root").shouldBe(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void loginOfUserWithInvalidUsername() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] .input__control").setValue(wrongLogin);
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! " + "Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void loginOfUserWithInvalidPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(wrongPassword);
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! " + "Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void logInIfTheUserIsNotRegistered() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] .input__control").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! " + "Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void logInIfTheUserIsBlocked() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! " + "Пользователь заблокирован"));
    }
}
