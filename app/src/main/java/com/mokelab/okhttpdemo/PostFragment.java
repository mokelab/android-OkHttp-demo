package com.mokelab.okhttpdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import okio.BufferedSink;

/**
 * Fragment for post page
 */
public class PostFragment extends Fragment {
    @Bind(R.id.edit_url) EditText mUrlText;
    @Bind(R.id.edit_body) EditText mBodyText;
    @Bind(android.R.id.button1) Button mSendButton;
    @Bind(android.R.id.text2) TextView mResultText;

    private OkHttpClient mClient;

    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        
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
        View root = inflater.inflate(R.layout.fragment_post, container, false);
    
        ButterKnife.bind(this, root);
    
        return root;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    
        ButterKnife.unbind(this);
    }

    @OnClick(android.R.id.button1)
    void sendClicked() {
        String url = mUrlText.getText().toString();
        String body = mBodyText.getText().toString();
        mSendButton.setEnabled(false);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), body))
                .build();

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
}
