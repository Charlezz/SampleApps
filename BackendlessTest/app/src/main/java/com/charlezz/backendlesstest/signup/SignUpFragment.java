package com.charlezz.backendlesstest.signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Charles on 2017. 1. 29..
 */

public class SignUpFragment extends Fragment {

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    ProgressDialog mProgress;

    @OnClick(R.id.check)
    public void onCheckEmailClick() {
        String emailAddress = email.getText().toString();
        if (TextUtils.isEmpty(emailAddress) || !isValidEmail(emailAddress)) {
            Toast.makeText(getActivity(), "incorrect email field", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgress = ProgressDialog.show(getActivity(), "Checking...", "Please Wait for a while");
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
                mProgress.dismiss();
            }
        });
    }

    @OnClick(R.id.signup)
    public void onSignUpClick(View v) {
        boolean isEverythingOk = true;
        mProgress = ProgressDialog.show(getActivity(), "", "Please Wait for a while");
        String emailAddress = email.getText().toString();

        String pw1 = password1.getText().toString();
        String pw2 = password2.getText().toString();
        String strName = name.getText().toString();

        if (TextUtils.isEmpty(pw1) || TextUtils.isEmpty(pw2) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(strName)) {
            Toast.makeText(getActivity(), "Fill the blank", Toast.LENGTH_SHORT).show();
            isEverythingOk = false;
        } else if (!pw1.equals(pw2)) {
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            isEverythingOk = false;
        } else if (check.isEnabled()) {
            Toast.makeText(getActivity(), "please check email first", Toast.LENGTH_SHORT).show();
            isEverythingOk = false;
        }

        if (isEverythingOk) {
            User user = new User();
            user.email = emailAddress;
            user.password = pw1;
            user.name = strName;
            NetworkManager.getInstance().postUser(user, new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful()) {
                        Log.e(TAG, response.message());
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                Log.e(TAG, "" + response.errorBody().string());
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    mProgress.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                    mProgress.dismiss();
                }
            });
        } else {
            mProgress.dismiss();
        }
    }

    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }
}
