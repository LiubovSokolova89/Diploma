package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class StartPage {

    private final SelenideElement heading = $$("h2").findBy(Condition.text("Путешествие дня"));
    private final SelenideElement buyButton = $$("button").findBy(Condition.text("Купить"));
    private final SelenideElement creditButton = $$("button").findBy(Condition.text("Купить в кредит"));

    public StartPage() {
        heading.shouldBe(Condition.visible);
    }

    public PaymentPage buy() {
        buyButton.click();
        return new PaymentPage();

    }

    public CreditPage buyInCredit() {
        creditButton.click();
        return new CreditPage();
    }
}