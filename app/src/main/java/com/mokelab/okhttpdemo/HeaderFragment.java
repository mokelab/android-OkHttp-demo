package com.mokelab.okhttpdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment for post page
 */
public class HeaderFragment extends Fragment {
    private static final int REQUEST_ADD_HEADER = 1000;

    @Bind(R.id.edit_url) EditText mUrlText;
    @Bind(R.id.edit_body) EditText mBodyText;
    @Bind(R.id.spinner_headers) Spinner mHeaderSpinner;
    @Bind(android.R.id.button1) Button mSendButton;
    @Bind(android.R.id.text2) TextView mResultText;

    private OkHttpClient mClient;

    public static HeaderFragment newInstance() {
        HeaderFragment fragment = new HeaderFragment();
        
        Bundle args = new Bundle();
        fragment.setArguments(args);
        
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new OkHttpClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_header, container, false);
    
        ButterKnife.bind(this, root);

        ArrayAdapter<Header> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHeaderSpinner.setAdapter(adapter);
    
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) { return; }
        switch (requestCode) {
        case REQUEST_ADD_HEADER: {
            String key = data.getStringExtra(AddHeaderDialogFragment.EXTRA_KEY);
            String value = data.getStringExtra(AddHeaderDialogFragment.EXTRA_VALUE);

            @SuppressWarnings("unchecked")
            ArrayAdapter<Header> adapter = (ArrayAdapter<Header>) mHeaderSpinner.getAdapter();
            adapter.add(new Header(key, value));
            adapter.notifyDataSetChanged();

            return;
        }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button_header_add)
    void addHeaderClicked() {
        AddHeaderDialogFragment dialog = AddHeaderDialogFragment.newInstance(this, REQUEST_ADD_HEADER);
        dialog.show(getFragmentManager(), "");
    }

    @OnClick(android.R.id.button1)
    void sendClicked() {
        String url = mUrlText.getText().toString();
        String body = mBodyText.getText().toString();
        @SuppressWarnings("unchecked")
        ArrayAdapter<Header> adapter = (ArrayAdapter<Header>) mHeaderSpinner.getAdapter();
        mSendButton.setEnabled(false);

        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), body));
        for (int i = 0 ; i < adapter.getCount() ; ++i) {
            Header header = adapter.getItem(i);
            builder.addHeader(header.mKey, header.mValue);
        }

        Request request = builder.build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseBody = response.body().string();
                showResponse(responseBody);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("Main", "onFailure " + e.getMessage(), e);
                showResponse("Failure \n" + e.getMessage());
            }
        });
    }

    private void showResponse(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSendButton.setEnabled(true);
                mResultText.setText(message);
            }
        });
    }

    private static class Header {
        public String mKey;
        public String mValue;

        public Header(String key, String value) {
            mKey = key;
            mValue = value;
        }

        @Override
        public String toString() {
            return mKey + ":" + mValue;
        }
    }
}
