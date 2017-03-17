package com.charlezz.backendlesstest.list;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.charlezz.backendlesstest.R;
import com.charlezz.backendlesstest.data.DeleteResult;
import com.charlezz.backendlesstest.data.User;
import com.charlezz.backendlesstest.data.UserResult;
import com.charlezz.backendlesstest.network.NetworkManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
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
    UserResult result;

    private ProgressDialog mProgressDialog;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        refresh();
    }

    @OnItemClick
    public void onItemClick(final int position) {
        mProgressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait..");
        NetworkManager.getInstance().deleteUser(result.data.get(position).objectId, new Callback<DeleteResult>() {
            @Override
            public void onResponse(Call<DeleteResult> call, Response<DeleteResult> response) {
                mProgressDialog.dismiss();
                refresh();
            }

            @Override
            public void onFailure(Call<DeleteResult> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add(0, 0, 0, "Refresh");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        mProgressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please Wait..");
        listView.setClickable(true);
        adapter.clear();
        NetworkManager.getInstance().getAllUsers(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                result = response.body();
                for (User user : result.data) {
                    adapter.add(user.email);
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                adapter.add("Failure");
                listView.setClickable(false);
                mProgressDialog.dismiss();
            }
        });
    }
}
