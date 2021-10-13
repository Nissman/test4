package pz;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static pz.Ebdpoint.Endpoints.*;

public class ImageTests extends BaseTest{

    static RequestSpecification requestSpecificationNoImage;

    @BeforeEach
    void  beforeTest(){
        requestSpecificationNoImage = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .build();
    }

    @Test
    void uploadFileNoDataTest() {
         given(requestSpecificationNoImage,negativeResponseSpecification400)
                 .post(UPLOAD_IMAGE)
                 .prettyPeek();
    }

    @Test
    void getNonExistenImageTest() {
        given(requestSpecificationNoImage,negativeResponseSpecification404)
                .get(NONEXISTEN_IMAGE)
                .prettyPeek();
    }
    @Test
    void favoriteNonExistenTest(){
        given(requestSpecificationNoImage,negativeResponseSpecification404)
                .post(FAFORITE_NONEXISTEN_IMAGE)
                .prettyPeek();
    }

    @Test
    void unFavoriteNonExistenTest(){
        given(requestSpecificationNoImage,negativeResponseSpecification404)
                .post(FAFORITE_NONEXISTEN_IMAGE)
                .prettyPeek();
    }

}
