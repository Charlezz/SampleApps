package charlezz.gallerytest;

import android.Manifest;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static charlezz.gallerytest.R.id.delete;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final float IMAGE_STANDARD_WIDTH = 100f;

    @BindView(R.id.gridView)
    GridView gridView;

    GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        new TedPermission(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        int space = 5;

                        DisplayMetrics dm = getResources().getDisplayMetrics();
                        int columnCount = (int) (dm.densityDpi / IMAGE_STANDARD_WIDTH);

                        gridView.setNumColumns(columnCount);
                        gridView.setVerticalSpacing(space);
                        gridView.setHorizontalSpacing(space);
                        gridView.setPadding(space, space, space, space);

                        mGalleryAdapter = new GalleryAdapter((dm.widthPixels - (space * (columnCount + 1))) / columnCount);
                        gridView.setAdapter(mGalleryAdapter);
                        mGalleryAdapter.setitems(fetchAllImages());
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .check();
    }

    ArrayList<String> fetchAllImages() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor imageCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        ArrayList<String> result = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
        } else if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                result.add(filePath);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
        return result;
    }

    @OnItemClick(R.id.gridView)
    public void onItemClick(int position) {
//
//
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setIndeterminate(false);
//        dialog.show();
//        String path = (String) mGalleryAdapter.getItem(position);
//        File file = new File(path);
//        NetworkManager.getInstance().uploadImage(file, new OnProgressListener() {
//            @Override
//            public void onProgress(int progress) {
//                dialog.setProgress(progress);
//                dialog.setSecondaryProgress(50);
//                Log.e(TAG, "progress:" + progress);
//            }
//
//            @Override
//            public void onFinish() {
//                Log.e(TAG, "finished");
//            }
//        }, new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    Log.e(TAG, "success");
//                } else {
//                    Log.e(TAG, "not success");
//                }
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e(TAG, "onFailure");
//                dialog.dismiss();
//            }
//        });

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case delete:
                SparseBooleanArray sparseBooleanArray = gridView.getCheckedItemPositions();
                for (int i = 0; i < sparseBooleanArray.size(); i++) {
                    int key = sparseBooleanArray.keyAt(i);
                    if (sparseBooleanArray.get(key)) {
                        Log.e(TAG, key + " is checked:" + mGalleryAdapter.getItem(key));
                        File file = new File(mGalleryAdapter.getItem(key));
                        if (file.exists()) {
                            Log.e(TAG, "successfully delete");
                            file.delete();
                            MediaScannerConnection.scanFile(this,
                                    new String[]{file.getPath()},
                                    null,
                                    new MediaScannerConnection.MediaScannerConnectionClient() {
                                        @Override
                                        public void onMediaScannerConnected() {

                                        }

                                        @Override
                                        public void onScanCompleted(String path, Uri uri) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    gridView.clearChoices();
                                                    mGalleryAdapter.setitems(fetchAllImages());
                                                }
                                            });

                                        }
                                    });
//                            MediaScannerConnection.scanFile(this,
//                                    new String[]{mGalleryAdapter.getItem(key)},
//                                    null,
//                                    new MediaScannerConnection.MediaScannerConnectionClient() {
//                                        @Override
//                                        public void onMediaScannerConnected() {
//
//                                        }
//
//                                        @Override
//                                        public void onScanCompleted(String path, Uri uri) {
//                                            if (uri != null) {
//                                                getContentResolver().delete(uri, null, null);
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        mGalleryAdapter.setitems(fetchAllImages());
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    });
                        } else {
                            Log.e(TAG, "no exists file");
                        }
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
