package com.mokelab.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.edit_url) EditText mUrlText;
    @Bind(android.R.id.button1) Button mSendButton;
    @Bind(android.R.id.text2) TextView mResultText;

    private OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClient = new OkHttpClient();

        ButterKnife.bind(this);
    }

    @OnClick(android.R.id.button1)
    void sendClicked() {
        String url = mUrlText.getText().toString();
        mSendButton.setEnabled(false);

        Request request = new Request.Builder()
                .url(url)
                .get()
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSendButton.setEnabled(true);
                mResultText.setText(message);
            }
        });
    }
}
