package dhbk.android.testgooglesearchreturn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Thien Nhan on 3/31/2016.
 */
public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.ViewHolder> {
    private File[] mImageFile;

    public ListImageAdapter(File[] holderFile) {
        mImageFile = holderFile;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File imageView = mImageFile[position];
        ImageView imgView = holder.getImageView();
//        setReductImageSize(imgView, imageView.getAbsolutePath());
        Toast.makeText(imgView.getContext(), "NEW", Toast.LENGTH_SHORT).show();
        Picasso.with(imgView.getContext()).load(imageView).into(imgView);

    }


    @Override
    public int getItemCount() {
        return mImageFile.length;
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
