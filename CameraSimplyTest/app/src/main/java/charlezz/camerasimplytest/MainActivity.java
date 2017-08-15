package charlezz.camerasimplytest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static charlezz.camerasimplytest.R.id.imageView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_THUMBNAIL = 1;
    private static final int REQUEST_BIG_PHOTO = 2;
    private static final int REQUEST_PICK_FROM_GALLERY = 3;

    @BindView(imageView)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_THUMBNAIL && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_BIG_PHOTO && resultCode == RESULT_OK) {
//            galleryAddPic();
//            int targetW = mImageView.getWidth();
//            int targetH = mImageView.getHeight();
//
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//            int photoW = bmOptions.outWidth;
//            int photoH = bmOptions.outHeight;
//
//            int scaleFactor = 1;
//            if ((targetW > 0) || (targetH > 0)) {
//                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//            }
//
//            bmOptions.inJustDecodeBounds = false;
//            bmOptions.inSampleSize = scaleFactor;
//            bmOptions.inPurgeable = true;
//
//            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//            mImageView.setImageBitmap(bitmap);

            ImageLoader imageLoader = ImageLoader.getInstance();

            File imageFile = new File(mCurrentPhotoPath);
            File moveImageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), imageFile.getName());
            imageFile.renameTo(moveImageFile);
            imageLoader.displayImage("file://" + moveImageFile.getAbsolutePath(), mImageView);

            galleryAddPic(moveImageFile);
        } else if (requestCode == REQUEST_BIG_PHOTO && resultCode != RESULT_OK) {
            if (mCurrentPhotoPath != null) {
                File file = new File(mCurrentPhotoPath);
                if (file.exists()) {
                    file.delete();
                }
            }
            mCurrentPhotoPath = null;
        } else if (requestCode == REQUEST_PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri picture = data.getData();
            mImageView.setImageURI(picture);
        }
    }


    String mCurrentPhotoPath = null;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.e(TAG, "directory:" + storageDir.getAbsolutePath());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e(TAG, "mCurrentPhotoPath:" + mCurrentPhotoPath);
        return image;
    }


    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "charlezz.camerasimplytest.fileprovider", photoFile);
                List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_BIG_PHOTO);
            }
        }
    }

    @OnClick(R.id.take)
    public void OnTakeClick() {
        Log.e(TAG, "OnTakeClicked");
        takePhoto();
    }

    private void galleryAddPic() {
        File f = new File(mCurrentPhotoPath);
        galleryAddPic();
    }

    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @OnClick(R.id.pick)
    public void OnPickClick() {
        pickFromGallery();
    }

    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_PICK_FROM_GALLERY);
    }


}
