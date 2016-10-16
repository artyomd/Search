package artyomd.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artyomd on 4/17/16.
 */
public class GObjectAdapter extends RecyclerView.Adapter {

    private List<GObject> data;
    private final int VIEW_ITEM = 1;
    private final int VIEW_IMG = 2;
    private final int VIEW_PROG = 0;
    private Context context;


    GObjectAdapter(Context context) {
        data = new ArrayList<>();
        this.context = context;
    }

    public List<GObject> getData() {
        return data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            vh = new MyViewHolder(itemView);
        } else if (viewType == VIEW_IMG) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
            vh = new ImageViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            GObject object = data.get(position);
            ((MyViewHolder) holder).title.setText(object.getTitle());
            ((MyViewHolder) holder).url.setText(object.getURL());

        } else if (holder instanceof ImageViewHolder) {
            GObject object = data.get(position);
            ((ImageViewHolder) holder).imgView.setImageBitmap(object.getBmp());
            ((ImageViewHolder) holder).tView.setText(object.getTitle());
            ((ImageViewHolder) holder).URL = object.getURL();
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) != null) {
            if (data.get(position).getBmp() == null) {
                return VIEW_ITEM;
            } else {
                return VIEW_IMG;
            }
        } else {
            return VIEW_PROG;
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, url;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            url = (TextView) view.findViewById(R.id.url);
            url.setMovementMethod(LinkMovementMethod.getInstance());

        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgView;
        public TextView tView;
        public String URL;
        public CardView card;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.imageView);
            card=(CardView) itemView.findViewById(R.id.card);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(URL));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
            tView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
