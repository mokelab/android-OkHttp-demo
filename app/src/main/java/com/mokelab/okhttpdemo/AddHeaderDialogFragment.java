package com.mokelab.okhttpdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dialog for adding header
 */
public class AddHeaderDialogFragment extends DialogFragment {

    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_VALUE = "value";

    @Bind(R.id.edit_key) EditText mKeyEdit;
    @Bind(R.id.edit_value) EditText mValueEdit;

    public static AddHeaderDialogFragment newInstance(Fragment target, int requestCode) {
        AddHeaderDialogFragment fragment = new AddHeaderDialogFragment();
        fragment.setTargetFragment(target, requestCode);

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_add_header, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.add_header);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button_submit)
    void submitClicked() {
        String key = mKeyEdit.getText().toString();
        String value = mValueEdit.getText().toString();

        if (TextUtils.isEmpty(key)) {
            return;
        }

        if (TextUtils.isEmpty(value)) {
            return;
        }

        Fragment target = getTargetFragment();
        if (target == null) {
            dismiss();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_KEY, key);
        data.putExtra(EXTRA_VALUE, value);

        target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        dismiss();
    }


}
