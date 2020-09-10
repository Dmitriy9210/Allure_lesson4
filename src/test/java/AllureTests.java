import com.codeborne.selenide.Condition;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static config.Config.config;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;


@Feature("Создание новой задачи")
@Story("Использование Lambda steps")
@Owner("Dmitriy")

public class AllureTests {


    private static final String REPOSITORY = config().getRepository();

    private static final String LOGIN_FORM_URI = config().getLoginFormUrl();
    private static final String TOKEN = config().getToken();
    private static final String USER = config().getLogin();
    private static final String PASSWORD = config().getPassword();
    private static final String BUG_LABEL = "bug";
    private static final String TITLE = config().getTitle();
    private int issue;

    @Test
    void createNewIssue() {

        step("Open Github", () -> {
            open("http://github.com");
        });

        step("Open Login Form page", () -> {
            $("a[href='/login']").click();
            $("input[name='login']").sendKeys(USER);
            $("input[name='password']").sendKeys(PASSWORD);
            $("input[name='commit']").click();
        });

        step("Find repo", () -> {
            $(".header-search-input").setValue(REPOSITORY + Keys.ENTER);
            Assertions.assertTrue($(".repo-list").getText().contains(REPOSITORY));
        });

        step("Open repo", () -> {
            $(".repo-list").$(".v-align-middle").click();
        });

        step("Open Issue", () -> {
            $("span[data-content=\"Issues\"]").click();
        });

        step("Open new Issue", () -> {
            $(byText("New issue"), 1).click();
        });

        step("Create new Issue", () -> {
            $(".title").setValue(TITLE);
            $(withText("Assignees")).click();
            $(".select-menu-item.text-normal").shouldBe(Condition.visible);
            $("#assignee-filter-field").setValue(String.valueOf(Keys.ENTER) + String.valueOf(Keys.ESCAPE));
            $(withText("Labels")).click();
            $(".select-menu-modal", 3).shouldBe(Condition.text(BUG_LABEL), Condition.visible);
            $("#label-filter-field").setValue(BUG_LABEL + Keys.ENTER + Keys.ESCAPE);
            $("#issue_body").setValue("Test");

            $(".discussion-sidebar-item.sidebar-labels").shouldBe(Condition.text(BUG_LABEL));

            $(withText("Submit new issue")).click();

            $(".gh-header-show").shouldBe(Condition.text(TITLE));
        });

        step("Get issue number", () -> {
            issue = Integer.parseInt(($x("//span[contains(text(),'#')]").getText())
                    .replace("#", ""));
        });


        step(String.format("Check that issue #%d is created", issue), () -> {
            // @formatter:off
            given()
                    .baseUri("https://api.github.com")
                    .header("Authorization", "token " + config().getToken())
                    .filter(new AllureRestAssured())
                    .log().uri()

                    .when()
                    .get("/repos/Dmitriy9210/Allure_lesson4/issues/{issue}", issue)

                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("labels.name.flatten()", hasItems("bug"))
                    .body("assignee.login", is(config().getAssignee()))
                    .body("title", is(config().getTitle()));
            // @formatter:on
        });
    }
}
