package app.com.ark.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ark on 7/29/2015.
 */
public class MovieAdapterArrary extends ArrayAdapter<Movie> {

    private Context mContext;
    private int mResource;
    private int mImageViewResourceId;
    public MovieAdapterArrary(Context context, int resource, int ImageViewResourceId, List<Movie> objects) {
        super(context, resource, ImageViewResourceId, objects);
        mContext = context;
        mResource = resource;
        mImageViewResourceId = ImageViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView=inflater.inflate(mResource, null,true);
            view = (ImageView) rowView.findViewById(mImageViewResourceId);
        }
        String url = getItem(position).getmPoster_path();

        Picasso.with(mContext).load(url).into(view);
        return view;
    }


}
