package artyomd.search;

import java.util.List;

/**
 * Created by artyomd on 4/17/16.
 */
public interface AsyncDelegate {
    void asyncComplete(List<GObject> data);
}
