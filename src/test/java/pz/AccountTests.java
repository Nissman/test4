package pz;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static pz.Ebdpoint.Endpoints.GET_ACCOUNT;

public class AccountTests extends BaseTest {
    static RequestSpecification requestSpecificationAccount=
       new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .build();

    @Test
    void gatAccountInfoTest(){
        given(requestSpecificationAccount,positiveResponseSpecificationAccount)
                .get(GET_ACCOUNT, username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
