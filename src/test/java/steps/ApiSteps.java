package steps;

import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import static config.Config.config;
import static io.qameta.allure.Allure.parameter;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class ApiSteps {

    @Step("Get issue by number")
    public void checkByNumber(int issue, String token) {
        parameter("Issue #", issue);
        // @formatter:off

                given()
                        .baseUri("https://api.github.com")
                        .header("Authorization", "token " + token)
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
}
