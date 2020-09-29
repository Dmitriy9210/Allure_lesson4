import static com.codeborne.selenide.Condition.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static config.Config.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class AllureListeners {

    private static final String REPOSITORY = config().getRepository();

    private static final String LOGIN_FORM_URI = config().getLoginFormUrl();
    private static final String TOKEN = config().getToken();
    private static final String USER = config().getLogin();
    private static final String PASSWORD = config().getPassword();
    private static final String BUG_LABEL = "bug";
    private static final String TITLE = config().getTitle();
    private int issue;

    @BeforeEach
    public void initListener() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @Test
    void createNewIssue() {
        open("http://github.com");

        $("a[href='/login']").click();
        $(byName("login")).setValue(USER);
        $(byName("password")).setValue(PASSWORD);
        $(byName("commit")).click();

        $(".header-search-input").setValue(REPOSITORY).pressEnter();
        $(".repo-list").shouldHave(text(REPOSITORY));

        $(".repo-list").$(".v-align-middle").click();

        $("span[data-content=\"Issues\"]").click();

        $(byText("New issue"), 1).click();

        $(".title").setValue(TITLE);
        $(withText("Assignees")).click();
        $(".select-menu-item.text-normal").shouldBe(visible);
        $("#assignee-filter-field").pressEnter().pressEscape();
        $(withText("Labels")).click();
        $(".select-menu-modal", 3).shouldBe(visible, text(BUG_LABEL));
        $("#label-filter-field").setValue(BUG_LABEL ).pressEnter().pressEscape();
        $("#issue_body").setValue("Test");
        $(".discussion-sidebar-item.sidebar-labels").shouldBe(text(BUG_LABEL));

        $(withText("Submit new issue")).click();

        $(".gh-header-show").shouldBe(text(TITLE));

        issue = Integer.parseInt(($x("//span[contains(text(),'#')]").getText())
                .replace("#", ""));


        // @formatter:off
        given()
                .baseUri("https://api.github.com")
                .header("Authorization", "token " + TOKEN)
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
    }


    @AfterEach
    public void closeDriver() {
        closeWebDriver();
    }
}
