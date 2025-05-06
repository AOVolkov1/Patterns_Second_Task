package ru.netology;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("Should successful login")
    void ShouldSuccessfullLogin(){
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(Condition.exactText("  Личный кабинет"),Condition.visible);
    }

    @Test
    @DisplayName("Should not  login if user not registered")
    void ShouldNotLoginNotRegisteredUser(){
        var notregisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notregisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notregisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")
                , Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
    @Test
    @DisplayName("Should not  login if user blocked")
    void ShouldNotLoginBlockedUser(){
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован")
                , Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should not  login with wrong login")
    void ShouldNotLoginWithWrongLogin(){
        var registeredUser = getRegisteredUser("active");
        var wrongLogin=getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")
                , Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should not  login with wrong password")
    void ShouldNotLoginWithWrongPassword(){
        var registeredUser = getRegisteredUser("active");
        var wrongPasword=getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPasword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль")
                , Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}
