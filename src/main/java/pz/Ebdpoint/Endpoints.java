package pz.Ebdpoint;

public class Endpoints {
    public static final String UPLOAD_IMAGE="/image";
    public static final String NONEXISTEN_IMAGE=UPLOAD_IMAGE+"/m5t78NO";
    public static final String FAFORITE_NONEXISTEN_IMAGE=NONEXISTEN_IMAGE+"/favorite";
    public static final String GET_ACCOUNT="/account/{username}";
    public static final String CREATE_ALBUM="/album";
    public static final String GET_ALBUM=CREATE_ALBUM+"/{albumHash}";
    public static final String IMAGE_TO_ALBUM=GET_ALBUM+"/add";
    public static final String REMOVE_IMAGE_FROM_ALBUM=GET_ALBUM+"/remove_images";
    public static final String DELETE_IMAGE = GET_ACCOUNT + UPLOAD_IMAGE+"/{imageDeleteHash}";
    public static final String FAFORITE_ALBUM=GET_ALBUM+"/favorite";
    public static final String GET_IMAGE=UPLOAD_IMAGE+"/{imageHash}";
    public static final String IMAGE_FAFORITE = GET_IMAGE+ "/favorite";
    public static final String NONEXISTEN_ALBUM="/album/L2AMlFb/favorite";
}
