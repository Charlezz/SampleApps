package com.charlezz.backendlesstest.signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.charlezz.backendlesstest.R;
import com.charlezz.backendlesstest.data.User;
import com.charlezz.backendlesstest.data.UserResult;
import com.charlezz.backendlesstest.network.NetworkManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Charles on 2017. 1. 29..
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SignUpFragment.class.getSimpleName();

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
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
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    ProgressDialog mProgress;

    @Override
    public void onClick(View v) {
        mProgress = ProgressDialog.show(getActivity(), "Checking...", "Please Wait for a while");
        if (v.equals(check)) {

            String email = this.email.getText().toString();
            NetworkManager.getInstance().getUserByEmail(email, new Callback<UserResult>() {
                @Override
                public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                    mProgress.dismiss();
                    if (response.body().data.size() >= 1) {
                        Toast.makeText(getActivity(), "Already exists", Toast.LENGTH_SHORT).show();
                        check.setEnabled(true);
                    } else {
                        check.setEnabled(false);
                    }
                }

                @Override
                public void onFailure(Call<UserResult> call, Throwable t) {
                }
            });


        } else if (v.equals(signup)) {
            User user = new User();
        }
    }
}
