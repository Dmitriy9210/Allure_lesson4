import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static config.Config.config;
import static io.qameta.allure.Allure.step;


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
    private static final String DUPLICATE_LABEL = "duplicate";
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



    }

}
