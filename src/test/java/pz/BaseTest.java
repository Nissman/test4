package pz;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.core.IsNull.notNullValue;

public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;
    static String username;
    static ResponseSpecification positiveResponseSpecification;
    static ResponseSpecification positiveResponseSpecificationUpdateImage;
    static ResponseSpecification positiveResponseSpecificationAccount;
    static ResponseSpecification positiveResponseSpecificationAlbumAndImage;
    static ResponseSpecification positiveResponseSpecificationAlbumAndImageFavUnfav;
    static ResponseSpecification negativeResponseSpecification400;
    static ResponseSpecification negativeResponseSpecification404;
    static RequestSpecification requestSpecificationWithAuth;

    @BeforeAll
    static  void beforeAll(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://api.imgur.com/3";
        getProperties();
        token=properties.getProperty("token");
        username = properties.getProperty("username");
        positiveResponseSpecification = new ResponseSpecBuilder()
            .expectBody("data", is(true))
            .expectBody("success",is(true))
            .expectContentType(ContentType.JSON)
            .expectStatusCode(200)
            .build();
        positiveResponseSpecificationUpdateImage = new ResponseSpecBuilder()
                .expectBody("success", is(true))
                .expectBody("status",equalTo(200))
                .expectBody("data.title",equalTo("Heart"))
                .expectBody("data.description",equalTo("This is an image of a heart outline."))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
        positiveResponseSpecificationAccount = new ResponseSpecBuilder()
                .expectBody("status",equalTo(200))
                .expectBody("success",is(true))
                .expectBody("data.url", equalTo(username))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
        positiveResponseSpecificationAlbumAndImage = new ResponseSpecBuilder()
                .expectBody("status",equalTo(200))
                .expectBody("success",is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
        positiveResponseSpecificationAlbumAndImageFavUnfav = new ResponseSpecBuilder()
                .expectBody("status",equalTo(200))
                .expectBody("success",is(true))
                .expectBody("data", anyOf(containsString("favorited"), endsWith("unfavorited")))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
        negativeResponseSpecification400= new ResponseSpecBuilder()
            .expectBody("status",equalTo(400))
            .expectBody("success", is(false))
            .build();
        negativeResponseSpecification404= new ResponseSpecBuilder()
            .expectStatusCode(404)
            .build();
        requestSpecificationWithAuth = new RequestSpecBuilder()
            .addHeader("Authorization", token)
            .build();

    }

    private static void getProperties(){
       try (InputStream output = new FileInputStream("src/test/resources/application.properties")){
           properties.load(output);
                  }
       catch (IOException e) {
           e.printStackTrace();
       }
    }
}
