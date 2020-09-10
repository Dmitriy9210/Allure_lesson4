package steps;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static config.Config.config;

public class WebSteps {
    private static final String LOGIN_FORM_URL = config().getLoginFormUrl();

    @Step("Open Login form")
    public void openLoginForm() {
        open("http://github.com");
    }

    @Step("Login on GitHub")
    public void loginOnGitHub(String user, String password){
        $("a[href='/login']").click();
        $("input[name='login']").sendKeys(user);
        $("input[name='password']").sendKeys(password);
        $("input[name='commit']").click();
    }

    @Step("Find repo")
    public void findRepo(String repository){
        $(".header-search-input").setValue(repository + Keys.ENTER);
        Assertions.assertTrue($(".repo-list").getText().contains(repository));
    }


    @Step("Open repo")
    public void openRepo(){
        $(".repo-list").$(".v-align-middle").click();
    }

    @Step("Open Issue")
    public void openIssue(){
        $("span[data-content=\"Issues\"]").click();
    }

    @Step("Open new Issue")
    public void openNewIssue(){
        $(byText("New issue"), 1).click();
    }

    @Step("Create new Issue")
  public void createNewIssue(String title, String bugLevel){
        $(".title").setValue(title);
        $(withText("Assignees")).click();
        $(".select-menu-item.text-normal").shouldBe(Condition.visible);
        $("#assignee-filter-field").setValue(String.valueOf(Keys.ENTER) + String.valueOf(Keys.ESCAPE));
        $(withText("Labels")).click();
        $(".select-menu-modal", 3).shouldBe(Condition.text(bugLevel), Condition.visible);
        $("#label-filter-field").setValue(bugLevel + Keys.ENTER + Keys.ESCAPE);
        $("#issue_body").setValue("Test");

        $(".discussion-sidebar-item.sidebar-labels").shouldBe(Condition.text(bugLevel));

        $(withText("Submit new issue")).click();

        $(".gh-header-show").shouldBe(Condition.text(title));
    }

    @Step("Get issue number")
    public int getIssueNumber(){
        return Integer.parseInt(($x("//span[contains(text(),'#')]").getText())
                .replace("#", ""));
    }
}
