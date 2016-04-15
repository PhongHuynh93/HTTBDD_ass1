package dhbk.android.testgooglesearchreturn.ClassHelp;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dhbk.android.testgooglesearchreturn.R;

/**
 * Created by Thien Nhan on 4/15/2016.
 */
public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.ViewHolder> {
    private static final String root = Environment.DIRECTORY_DCIM + "/Camera/tripGallery";
    private String[] mImageFile;
    private String[] mName;
    private String[] mDate;

    public SavedListAdapter(String[] holderFile, String[] name, String[] date) {
        mImageFile = holderFile;
        mName = name;
        mDate = date;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imgName = root + mImageFile[position];
        String name = mName[position];
        String date = mDate[position];

        ImageView imgView = holder.getImageView();
        TextView nameView = holder.getTxtNameView();
        TextView dateView = holder.getTxtDateView();
//        setReductImageSize(imgView, imageView.getAbsolutePath());
        Picasso.with(imgView.getContext()).load(imgName).into(imgView);
        nameView.setText(name);
        dateView.setText(date);
    }

    @Override
    public int getItemCount() {
        return mImageFile.length;
    }

    //associate imageview and recycleview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtName, txtDate;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageItem);
            txtName = (TextView) v.findViewById(R.id.name);
            txtDate = (TextView) v.findViewById(R.id.date);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTxtNameView() {
            return txtName;
        }

        public TextView getTxtDateView() {
            return txtDate;
        }
    }
}