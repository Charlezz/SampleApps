package charlezz.com.sharedelementtest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    //Ref : https://github.com/pranaypatel512/SharedElementTransitions

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentA fragmentA = FragmentA.newInstance();
        fragmentA.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_image_trans));
        fragmentA.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));

        final FragmentB fragmentB = FragmentB.newInstance();
        fragmentB.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.change_image_trans));
        fragmentB.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));

        Button test1 = (Button) findViewById(R.id.test1);
        Button test2 = (Button) findViewById(R.id.test2);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentA).commit();
        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                imageView = fragmentA.getImageView();
                transaction.addSharedElement(imageView, "test");
                transaction.replace(R.id.container, fragmentB);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
