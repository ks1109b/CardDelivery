package ru.netology.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class CallbackTest {

    public String plusDays(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.DELETE);
    }

    @Test
    void shouldSendForm() {
        String date = plusDays(5);
        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").val(date);
        $("[data-test-id=name] input").val("Василий Иванов-Петров");
        $("[data-test-id=phone] input").val("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=notification]")
                .shouldBe((visible), Duration.ofSeconds(11))
                .shouldHave(text("Успешно!"), Duration.ofSeconds(5));
        String text = $("[data-test-id='notification'] .notification__content").text();
        assertEquals("Встреча успешно забронирована на " + date, text);
    }

    @Test
    void shouldSendFormWithCityFromList() {
        String date = plusDays(5);
        $("[data-test-id=city] input").setValue("Мо");
        $(withText("Москва")).click();
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=notification]")
                .shouldBe((visible), Duration.ofSeconds(11))
                .shouldHave(text("Успешно!"), Duration.ofSeconds(5));
        String text = $("[data-test-id='notification'] .notification__content").text();
        assertEquals("Встреча успешно забронирована на " + date, text);
    }

    @Test
    void shouldGetErrorIfEmptyForm() {
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfOnlyCity() {
        $("[data-test-id=city] input").setValue("Москва");
        $(withText("Забронировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldGetErrorIfOnlyDate() {
        $("[data-test-id=date] input").setValue(plusDays(5));
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfOnlyName() {
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfOnlyPhone() {
        $("[data-test-id=phone] input").setValue("+79270000000");
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfOnlyAgreement() {
        $("[data-test-id=city] input").click();
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorWithoutCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfInvalidCity() {
        $("[data-test-id=city] input").setValue("Moscow");
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub")
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldGetErrorWithoutDate() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldGetErrorIfInvalidDate() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(plusDays(1));
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldGetErrorWithoutName() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfInvalidName() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=name] input").setValue("John");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(text("Имя и Фамилия указаны неверно"));
    }

    @Test
    void shouldGetErrorWithoutPhone() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfInvalidPhone() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=phone] input").setValue("89270000000");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(text("Телефон указан неверно"));
    }

    @Test
    void shouldGetErrorWithoutAgreement() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(plusDays(5));
        $("[data-test-id=name] input").setValue("Василий Иванов-Петров");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $(withText("Забронировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text")
                .shouldBe(visible)
                .shouldHave(text("Я соглашаюсь с условиями"));
    }
}
