package app.com.ark.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;

/**
 * Created by ark on 7/29/2015.
 */
public class MovieCursorAdapter extends CursorAdapter {


    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewholder = (ViewHolder) view.getTag();

        // Read Movie poster from cursor
        byte[] poster_url = cursor.getBlob(Constant.COL_MOVIE_POSTER_PATH);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(poster_url);
        Bitmap theImage= BitmapFactory.decodeStream(imageStream);
        viewholder.posterView.setImageBitmap(theImage);
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView posterView;

        public ViewHolder(View view) {
            posterView = (ImageView) view.findViewById(R.id.list_item_movie_imageview);
        }
    }

}