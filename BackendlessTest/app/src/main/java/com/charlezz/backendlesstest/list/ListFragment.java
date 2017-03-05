package com.charlezz.backendlesstest.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.charlezz.backendlesstest.R;
import com.charlezz.backendlesstest.data.RetrieveUsersResult;
import com.charlezz.backendlesstest.data.User;
import com.charlezz.backendlesstest.network.NetworkManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Charles on 2017. 1. 29..
 */

public class ListFragment extends Fragment {
    @BindView(R.id.listView)
    ListView listView;

    ArrayAdapter<String> adapter;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        NetworkManager.getInstance().getAllUsers(new Callback<RetrieveUsersResult>() {
            @Override
            public void onResponse(Call<RetrieveUsersResult> call, Response<RetrieveUsersResult> response) {
                RetrieveUsersResult result = response.body();
                for (User user : result.data) {
                    adapter.add(user.email);
                }
            }

            @Override
            public void onFailure(Call<RetrieveUsersResult> call, Throwable t) {

            }
        });

    }
}
