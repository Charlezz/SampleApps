package charlezz.openglesstudy;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import charlezz.openglesstudy.views.basic_gl.MainGLSurfaceView;

/**
 * Created by Charles on 3/23/17.
 */

public class PracticeActivity extends AppCompatActivity {
    public static final String TAG = PracticeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        getSupportActionBar().hide();
        Intent intent = getIntent();
        if (intent != null) {
            String className = intent.getStringExtra(MainActivity.KEY_MENU);
            if (!TextUtils.isEmpty(className)) {
                try {
                    Class<?> mClass = Class.forName(className);
                    Constructor<?> constructor = mClass.getConstructor(Context.class);
                    View view = (View) constructor.newInstance(new Object[]{this});
                    setContentView(view);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                setContentView(new MainGLSurfaceView(this));
            }
        }


    }
}
