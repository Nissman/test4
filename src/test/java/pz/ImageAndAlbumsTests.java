package pz;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import pz.Album.AlbumResponse;
import pz.Image.ImageResponse;
import static io.restassured.RestAssured.given;
import static pz.Ebdpoint.Endpoints.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageAndAlbumsTests extends BaseTest{
    private static final String[] PATH_TO_IMAGE = {"src/test/resources/revenant.jpg","src/test/resources/revcat.PNG","src/test/resources/omg.PNG"};
    static ArrayList<MultiPartSpecification> base64MultiPartSpec = new ArrayList<>();
    static ArrayList<RequestSpecification> requestSpecificationWithAuthWithBase64 = new ArrayList<>();
    static ArrayList<String> encodedFile = new ArrayList<>();
    static List<String> imageId = Arrays.asList("","","");
    static List<String> uploadedImageId = Arrays.asList("","","");
    static RequestSpecification requestSpecification;
    static RequestSpecification requestSpecificationUpdateImage;
    static RequestSpecification requestSpecificationAlbum;
    static RequestSpecification requestSpecificationAddImageToAlbum;
    static RequestSpecification requestSpecificationSetAlbumImage;
    static String albumId;
    static String createAlbumId;
    ImageResponse imageResponse;
    AlbumResponse albumResponse;

    @BeforeEach
    void  beforeTest(){
        encodedFile.add(Base64.getEncoder().encodeToString(getFileContent(PATH_TO_IMAGE[0])));
        encodedFile.add(Base64.getEncoder().encodeToString(getFileContent(PATH_TO_IMAGE[1])));
        encodedFile.add(Base64.getEncoder().encodeToString(getFileContent(PATH_TO_IMAGE[2])));
        base64MultiPartSpec.add(new MultiPartSpecBuilder(encodedFile.get(0))
                .controlName("image")
                .build());
        base64MultiPartSpec.add(new MultiPartSpecBuilder(encodedFile.get(1))
                .controlName("image")
                .build());
        base64MultiPartSpec.add(new MultiPartSpecBuilder(encodedFile.get(2))
                .controlName("image")
                .build());
        requestSpecificationWithAuthWithBase64.add(new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart(base64MultiPartSpec.get(0))
                .build());
        requestSpecificationWithAuthWithBase64.add(new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "ImageTitle")
                .addFormParam("album", albumId)
                .addMultiPart(base64MultiPartSpec.get(1))
                .build());
        requestSpecificationWithAuthWithBase64.add(new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "ImageTitle")
                .addMultiPart(base64MultiPartSpec.get(2))
                .build());
        requestSpecification = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .build();
        requestSpecificationAlbum = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "My dank meme album")
                .build();
        requestSpecificationUpdateImage = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "Heart")
                .addFormParam("description", "This is an image of a heart outline.")
                .build();
        requestSpecificationAddImageToAlbum= new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("ids[]", imageId.get(0))
                .build();
        requestSpecificationSetAlbumImage= new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("ids[]", imageId.get(0))
                .addFormParam("ids[]", imageId.get(1))
                .addFormParam("ids[]", imageId.get(2))
                .build();

    }

    @Order(1)
    @Test
    void uploadFile1Test() {
        imageResponse =  given(requestSpecificationWithAuthWithBase64.get(0),positiveResponseSpecificationAlbumAndImage)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(ImageResponse.class);

        imageId.set(0,imageResponse.getData().getId());
        uploadedImageId.set(0,imageResponse.getData().getDeletehash());

    }

    @Order(2)
    @Test
    void favoriteAnUploadImageTest(){
        given(requestSpecification,positiveResponseSpecificationAlbumAndImageFavUnfav)
                .post(IMAGE_FAFORITE,uploadedImageId.get(0))
                .prettyPeek();
    }

    @Order(3)
    @Test
    void unFavoriteAnUploadImageTest(){
        given(requestSpecification,positiveResponseSpecificationAlbumAndImageFavUnfav)
                .post(IMAGE_FAFORITE,uploadedImageId.get(0))
                .prettyPeek();
    }

    @Order(4)
    @Test
    void createAlbumTest() {
    albumResponse =  given(requestSpecificationAlbum,positiveResponseSpecificationAlbumAndImage)
                .post(CREATE_ALBUM)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(AlbumResponse.class);
        albumId=albumResponse.getData().getId();
        createAlbumId=albumResponse.getData().getDeletehash();
    }

    @Order(5)
    @Test
    void getAlbumTest() {
        given(requestSpecification,positiveResponseSpecificationAlbumAndImage)
                .get(GET_ALBUM, albumId)
                .prettyPeek();
    }

    @Order(6)
    @Test
    void uploadFile2ToAlbumTest() {
        imageResponse = given(requestSpecificationWithAuthWithBase64.get(1),positiveResponseSpecificationAlbumAndImage)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(ImageResponse.class);
        imageId.set(1,imageResponse.getData().getId());
        uploadedImageId.set(1,imageResponse.getData().getDeletehash());
    }

    @Order(7)
    @Test
    void updateImage(){
        given(requestSpecificationUpdateImage,positiveResponseSpecification)
                .post(GET_IMAGE,imageId.get(0))
                .prettyPeek();
    }

    @Order(8)
    @Test
    void getImageTest() {
        given(requestSpecification,positiveResponseSpecificationUpdateImage)
                .get(GET_IMAGE,imageId.get(0))
                .prettyPeek();
    }

    @Order(9)
    @Test
    void uploadFile3Test() {
        imageResponse =  given(requestSpecificationWithAuthWithBase64.get(2),positiveResponseSpecificationAlbumAndImage)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(ImageResponse.class);
        imageId.set(2,imageResponse.getData().getId());
        uploadedImageId.set(2,imageResponse.getData().getDeletehash());
    }

    @Order(10)
    @Test
    void  addImageToAnAlbum(){
        given(requestSpecificationAddImageToAlbum,positiveResponseSpecification)
                .post(IMAGE_TO_ALBUM,albumId)
                .prettyPeek();
    }

    @Order(11)
    @Test
    void setAlbumImage(){
        given(requestSpecificationSetAlbumImage,positiveResponseSpecification)
                .post(GET_ALBUM,albumId)
                .prettyPeek();
    }

    @Order(12)
    @Test
    void removeImageFromAnAlbumTest(){
        given(requestSpecificationAddImageToAlbum,positiveResponseSpecification)
                .post(REMOVE_IMAGE_FROM_ALBUM,albumId)
                .prettyPeek();
    }

    @Order(13)
    @Test
    void delAlbum() {
        given(requestSpecification,positiveResponseSpecification)
                .delete(GET_ALBUM, createAlbumId)
                .prettyPeek();
    }

    @Order(14)
    @Test
    void DeleteImage1Test() {
        given(requestSpecification,positiveResponseSpecification)
                .delete(DELETE_IMAGE, username, uploadedImageId.get(0))
                .prettyPeek();
    }

    @Order(15)
    @Test
    void DeleteImage2Test() {
        given(requestSpecification,positiveResponseSpecification)
                .delete(DELETE_IMAGE, username, uploadedImageId.get(1))
                .prettyPeek();
    }

    @Order(16)
    @Test
    void DeleteImage3Test() {
        given(requestSpecification,positiveResponseSpecification)
                .delete(DELETE_IMAGE, username, uploadedImageId.get(2))
                .prettyPeek();
    }


    private static byte[] getFileContent(String PATH_TO_IMAGE) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
