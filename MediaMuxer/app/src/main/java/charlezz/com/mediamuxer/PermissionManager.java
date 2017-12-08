package charlezz.com.mediamuxer;

import android.Manifest;
import android.content.Context;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 12/10/2017.
 */

public class PermissionManager {

	private static PermissionManager instance;

	public static PermissionManager getInstance(){
		if(instance==null){
			instance = new PermissionManager();
		}
		return instance;
	}

	public void checkPermission(Context context, PermissionListener listener){
		new TedPermission(context)
				.setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.setPermissionListener(listener)
				.check();
	}
}
