package artyomd.search;

import android.graphics.Bitmap;

/**
 * Created by artyomd on 4/17/16.
 */
public class GObject {
    private String title;
    private String URL;
    private Bitmap bmp;

    public GObject(String title, String URL, Bitmap bmp) {
        this.title = title;
        this.URL = URL;
        this.bmp = bmp;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public GObject(String title, String URL) {
        this.title = title;
        this.URL = URL;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }
}
