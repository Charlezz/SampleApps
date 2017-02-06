package com.charlezz.backendlesstest.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.charlezz.backendlesstest.R;
import com.charlezz.backendlesstest.data.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Charles on 2017. 1. 29..
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.id)
    EditText id;
    @BindView(R.id.check)
    Button check;
    @BindView(R.id.password1)
    EditText password1;
    @BindView(R.id.password2)
    EditText password2;
    @BindView(R.id.signup)
    Button signup;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        check.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(check)) {

        } else if (v.equals(signup)) {
            User user = new User();
        }
    }
}
