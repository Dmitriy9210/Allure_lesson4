import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steps.ApiSteps;
import steps.WebSteps;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static config.Config.config;


@Feature("Создание новой задачи")
@Story("Использование Lambda steps")
@Owner("Dmitriy")

public class AllureStepsTests {


    private static final String REPOSITORY = config().getRepository();

    private static final String LOGIN_FORM_URI = config().getLoginFormUrl();
    private static final String TOKEN = config().getToken();
    private static final String USER = config().getLogin();
    private static final String PASSWORD = config().getPassword();
    private static final String BUG_LABEL = "bug";
    private static final String TITLE = config().getTitle();
    private int issue;

    private WebSteps webSteps = new WebSteps();
    private ApiSteps apiSteps = new ApiSteps();

    @BeforeEach
    public void initListener() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @Test
    public void createNewIssue() {
        webSteps.openLoginForm();
        webSteps.loginOnGitHub(USER, PASSWORD);
        webSteps.findRepo(REPOSITORY);
        webSteps.openRepo();
        webSteps.openIssue();
        webSteps.openNewIssue();
        webSteps.createNewIssue(TITLE, BUG_LABEL);
        int number = webSteps.getIssueNumber();
        apiSteps.checkByNumber(number, TOKEN);
    }

    @AfterEach
    public void closeDriver() {
        closeWebDriver();
    }

}
