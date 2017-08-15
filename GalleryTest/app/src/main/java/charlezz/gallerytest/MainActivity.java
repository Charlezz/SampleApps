package charlezz.gallerytest;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

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
		Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

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
			Log.e(TAG,"Error occured");
		} else if (imageCursor.moveToFirst()) {
			do {
				String filePath = imageCursor.getString(dataColumnIndex);
				result.add(filePath);
			} while (imageCursor.moveToNext());
		} else {
			Log.e(TAG,"imageCursor is empty");
			// imageCursor가 비었습니다.
		}
		imageCursor.close();
		return result;
	}

	@OnItemClick(R.id.gridView)
	public void onItemClick(int position) {
//        Intent intent = new Intent(MainActivity.this, ViewerActivity.class);
//        intent.putExtra(ViewerActivity.KEY_IMAGE, (String) mGalleryAdapter.getItem(position));
//        startActivity(intent);



//		final ProgressDialog dialog = new ProgressDialog(this);
//		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		dialog.setIndeterminate(false);
//		dialog.show();
//		String path = (String) mGalleryAdapter.getItem(position);
//		File file = new File(path);
//		NetworkManager.getInstance().uploadImage(file, new OnProgressListener() {
//			@Override
//			public void onProgress(int progress) {
//				dialog.setProgress(progress);
//				dialog.setSecondaryProgress(50);
//				Log.e(TAG, "progress:" + progress);
//			}
//
//			@Override
//			public void onFinish() {
//				Log.e(TAG, "finished");
//			}
//		}, new Callback<ResponseBody>() {
//			@Override
//			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//				if (response.isSuccessful()) {
//					Log.e(TAG, "success");
//				} else {
//					Log.e(TAG, "not success");
//				}
//				dialog.dismiss();
//			}
//
//			@Override
//			public void onFailure(Call<ResponseBody> call, Throwable t) {
//				Log.e(TAG, "onFailure");
//				dialog.dismiss();
//			}
//		});
//
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//		intent.setAction(Intent.ACTION_GET_CONTENT);
//		startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
	}
}
