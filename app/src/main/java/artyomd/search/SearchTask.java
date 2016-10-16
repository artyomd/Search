package artyomd.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artyomd on 4/15/16.
 */
public class SearchTask extends AsyncTask<String, Void, List<GObject>> {

    private AsyncDelegate delegate;
    private long firstItemID = 1;
    private boolean type = false;

    public SearchTask(AsyncDelegate delegate) {
        this.delegate = delegate;
    }

    public void setFirstItemID(long firstItemID) {
        this.firstItemID = firstItemID;
    }

    public boolean isType() {
        return type;
    }

    public long getFirstItemID() {
        return firstItemID;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    protected List<GObject> doInBackground(String... params) {
        Customsearch.Builder customSearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null);
        customSearch.setApplicationName("Search");
        try {
            com.google.api.services.customsearch.Customsearch.Cse.List list = customSearch.build().cse().list(params[0]);
            list.setKey("AIzaSyBU22SbQswBagy5Qcli8KjyDxMy19WagnE");
            list.setCx("003630389706661019147:i5a1vmvqv4m");
            list.setStart(firstItemID);
            if (type) {
                list.setSearchType("image");
            }
            Search results = list.execute();
            List<GObject> objects = new ArrayList<>();
            if (results.getItems() != null)
                if (type) {
                    for (Result res : results.getItems()) {
                        if (res != null) {
                            Rect rect = new Rect();
                            rect.contains(100, 100, 100, 100);
                            Bitmap bitmap = decodeSampledBitmapFromResource(new URL(res.getImage().getThumbnailLink()), rect, 100, 100);
                            objects.add(new GObject(res.getTitle(), res.getImage().getThumbnailLink(), bitmap));
                        }
                    }
                } else {
                    for (Result result : results.getItems()) {
                        if (result != null)
                            objects.add(new GObject(result.getTitle(), result.getFormattedUrl()));
                    }
                }
            return objects;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<GObject> items) {
        super.onPostExecute(items);
        this.delegate.asyncComplete(items);
    }

    @Nullable
    public static Bitmap decodeSampledBitmapFromResource(URL url, Rect resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), resId, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), resId, options);
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}