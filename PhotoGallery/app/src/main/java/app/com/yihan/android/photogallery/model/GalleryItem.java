package app.com.yihan.android.photogallery.model;

/**
 * Created by HanYi on 10/21/15.
 */
public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;



    @Override
    public String toString() {
        return getCaption();
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
