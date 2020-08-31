import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AllureTests {

    private final String LOGIN = "dimon9210@mail.ru";
    private final String PASSWORD = ""; //TODO fake account's password should be changed to secret password
    private final String TITLE = "Loading all photos from an instagram account";

    @Test
    void createNewIssue(){
        open("http://github.com");
        $("a[href='/login']").click();
        $("input[name='login']").sendKeys(LOGIN);
        $("input[name='password']").sendKeys(PASSWORD);
        $("input[name='commit']").click();

    }

}
