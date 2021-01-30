package com.fscloud.lib_base.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fscloud.lib_base.R;

/**
 * @author Theo
 * @version 1.0
 * @description
 * @time 2018/12/04
 */
public class LoadingDialogFragment extends BaseDialogFragment {
    TextView tipTextView;

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        if (getDialog().getWindow() == null) {
            return;
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.dialog_fragment_loading, null);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tipTextView = view.findViewById(R.id.tipTextView);
        if (getArguments() != null) {
            String tips = getArguments().getString("tips");
            if (!TextUtils.isEmpty(tips)) {
                tipTextView.setText(tips);
            }
        }
    }
}
