package dhbk.android.testgooglesearchreturn.ClassHelp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import dhbk.android.testgooglesearchreturn.R;

/**
 * Created by Thien Nhan on 3/31/2016.
 */
public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.ViewHolder> {
    private List<File> mImageFile;

    public ListImageAdapter(List<File> holderFile) {
        mImageFile = holderFile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File imageView = mImageFile.get(position);
        ImageView imgView = holder.getImageView();
//        setReductImageSize(imgView, imageView.getAbsolutePath());
        Picasso.with(imgView.getContext()).load(imageView).into(imgView);

    }


    @Override
    public int getItemCount() {
        return mImageFile.size();
    }

    //associate imageview and recycleview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageItem);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
