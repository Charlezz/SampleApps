package charlezz.gallerytest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Charles on 22/05/2017.
 */

public class GalleryItemView extends FrameLayout implements Checkable {
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.checkbox)
    ImageView checkbox;

    Context context;

    public GalleryItemView(@NonNull Context context) {
        super(context);
        this.context = context;
        View v = LayoutInflater.from(context).inflate(R.layout.gallery_itemv_iew_layout, this, true);
        ButterKnife.bind(this, v);
    }

    public void setImage(String path) {
        Glide.with(context).load(path).into(imageView);
    }


    private boolean isCheckd;

    @Override
    public void setChecked(boolean checked) {
        isCheckd = checked;
        if (isCheckd) {
            checkbox.setImageResource(R.drawable.ic_check_box_black_24dp);
        } else {
            checkbox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
    }

    @Override
    public boolean isChecked() {
        return isCheckd;
    }

    @Override
    public void toggle() {
        isCheckd = !isCheckd;
        setChecked(isCheckd);
    }

}
