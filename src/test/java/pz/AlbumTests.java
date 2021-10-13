package pz;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import pz.Album.AlbumResponse;
import static io.restassured.RestAssured.given;
import static pz.Ebdpoint.Endpoints.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlbumTests extends BaseTest {
    static String albumId;
    static String createAlbumId;
    static RequestSpecification requestSpecificationAlbum;
    AlbumResponse albumResponse;

    @BeforeEach
    void  beforeTest(){
        requestSpecificationAlbum = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .build();
    }

    @Order(1)
    @Test
    void createAlbumWithoutParamTest() {
       albumResponse=   given(requestSpecificationAlbum,positiveResponseSpecificationAlbumAndImage)
                .post(CREATE_ALBUM)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(AlbumResponse.class);
        albumId=albumResponse.getData().getId();
        createAlbumId=albumResponse.getData().getDeletehash();
    }

    @Order(2)
    @Test
    void favoriteAlbumTest(){
        given(requestSpecificationAlbum,positiveResponseSpecificationAlbumAndImageFavUnfav)
                .post(FAFORITE_ALBUM,albumId)
                .prettyPeek();
    }

    @Order(3)
    @Test
    void unFavoriteAlbumTest(){
        given(requestSpecificationAlbum,positiveResponseSpecificationAlbumAndImageFavUnfav)
                .post(FAFORITE_ALBUM,albumId)
                .prettyPeek();
    }

    @Order(4)
    @Test
    void delAlbum() {
        given(requestSpecificationAlbum,positiveResponseSpecification)
                .delete(GET_ALBUM, createAlbumId)
                .prettyPeek();
    }

    @Order(5)
    @Test
    void getNonExistedAlbumTest() {
        given(requestSpecificationAlbum,negativeResponseSpecification404)
                .get(GET_ALBUM, createAlbumId)
                .prettyPeek();
    }

    @Order(6)
    @Test
    void favoriteNonExistedTest(){
        given(requestSpecificationAlbum,negativeResponseSpecification404)
                .post(NONEXISTEN_ALBUM)
                .prettyPeek();
    }

    @Order(7)
    @Test
    void unFavoriteNonExistedTest(){
        given(requestSpecificationAlbum,negativeResponseSpecification404)
                .post(NONEXISTEN_ALBUM)
                .prettyPeek();
    }

    @Order(8)
    @Test
    void delAlbumAgain(){
        given(requestSpecificationAlbum,negativeResponseSpecification404)
                .delete(GET_ALBUM, createAlbumId)
                .prettyPeek();
    }
}
