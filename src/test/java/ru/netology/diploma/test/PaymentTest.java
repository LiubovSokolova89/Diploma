package ru.netology.diploma.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.diploma.data.Card;
import ru.netology.diploma.data.SQLHelper;
import ru.netology.diploma.page.PaymentPage;
import ru.netology.diploma.page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.diploma.data.DataHelper.*;

public class PaymentTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        SQLHelper.clearTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @Test
    void shouldBuyInPaymentGate() throws SQLException {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }


    @Test
    void shouldBuyInPaymentGateWithNameInLatinLetters() throws SQLException {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidNameInLatinLetters(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }


    @Test
    void shouldNotBuyInPaymentGateWithDeclinedCardNumber() throws SQLException {
        Card card = new Card(getDeclinedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkDeclineNotification();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }


    @Test
    void shouldNotBuyInPaymentGateWithInvalidCardNumber() throws SQLException {
        Card card = new Card(getInvalidCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkDeclineNotification();

    }


    @Test
    void shouldNotBuyInPaymentGateWithShortCardNumber() {
        Card card = new Card(getShortCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidFormat();
    }


    @Test
    void shouldNotBuyInPaymentGateWithEmptyCardNumber() {
        Card card = new Card(null, getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем Номер карты на "Поле обязательно для заполнения"
    }


    @Test
    void shouldNotBuyInPaymentGateWithInvalidMonth() {
        Card card = new Card(getApprovedNumber(), "00", getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDate(); //TODO Изменить надпись под полем Месяц на "Неверно указан срок действия карты"
    }


    @Test
    void shouldNotBuyInPaymentGateWithNonExistingMonth() {
        Card card = new Card(getApprovedNumber(), "13", getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDate();

    }


    @Test
    void shouldNotBuyInPaymentGateWithExpiredMonth() {
        Card card = new Card(getApprovedNumber(), getLastMonth(), getCurrentYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkExpiredDate(); //TODO Изменить надпись под полем Месяц на "Истёк срок действия карты"
    }


    @Test
    void shouldNotBuyInPaymentGateWithEmptyMonth() {
        Card card = new Card(getApprovedNumber(), null, getNextYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем Месяц на "Поле обязательно для заполнения"
    }


    @Test
    void shouldNotBuyInPaymentGateWithExpiredYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getLastYear(), getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkExpiredDate();
    }


    @Test
    void shouldNotBuyInPaymentGateWithEmptyYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), null, getValidName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем Год на "Поле обязательно для заполнения"
    }


    @Test
    void shouldNotBuyInPaymentGateWithOnlyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }


    @Test
    void shouldNotBuyInPaymentGateWithOnlyNameInLatinLetters() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyNameInLatinLetters(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }


    @Test
    void shouldNotBuyInPaymentGateWithOnlySurname() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlySurname(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }


    @Test
    void shouldNotBuyInPaymentGateWithOnlySurnameInLatinLetters() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlySurnameInLatinLetters(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }


    @Test
    void shouldNotBuyInPaymentGateWithNameAndSurnameWithDash() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), "Иван-Иванов", getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldNotBuyInPaymentGateWithTooLongName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getTooLongName(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkLongName(); //TODO Изменить надпись под полем Владелец "Значение поля не может содержать более 100 символов"
    }


    @Test
    void shouldNotBuyInPaymentGateWithDigitsInName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithNumbers(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDataName(); //TODO Изменить надпись под полем Владелец "Значение поля может содержать только буквы и дефис"
    }


    @Test
    void shouldNotBuyInPaymentGateWithTooShortName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithOneLetter(), getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkShortName(); //TODO Изменить надпись под полем Владелец "Значение поля должно содержать больше одной буквы"
    }


    @Test
    void shouldNotBuyInPaymentGateWithEmptyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), null, getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField();
    }


    @Test
    void shouldNotBuyInPaymentGateWithSpaceInsteadOfName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), " ", getValidCvc());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDataName(); //TODO Изменить надпись под полем Владелец "Значение поля может содержать только буквы и дефис"
    }



    @Test
    void shouldNotBuyInPaymentGateWithOneDigitInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithOneDigit());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidCvc(); //TODO Изменить надпись под полем CVC "Значение поля должно содержать 3 цифры"
    }


    @Test
    void shouldNotBuyInPaymentGateWithTwoDigitsInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithTwoDigits());
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidCvc(); //TODO Изменить надпись под полем CVC "Значение поля должно содержать 3 цифры"
    }


    @Test
    void shouldNotBuyInPaymentGateWithEmptyCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), null);
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем CVC на "Поле обязательно для заполнения"
    }


    @Test
    void shouldNotBuyInPaymentGateWithAllEmptyFields() {
        Card card = new Card(null, null, null, null, null);
        val startPage = new StartPage();
        startPage.buy();
        val paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkAllFieldsAreRequired(); //TODO Изменить надписи под полями на "Поле обязательно для заполнения"
    }

}

