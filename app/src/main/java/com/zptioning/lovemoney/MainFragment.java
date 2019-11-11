package com.zptioning.lovemoney;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ClassName MainFragment
 * @Author zptioning
 * @Date 2019-11-04 11:08
 * @Version 1.0
 * @Description
 */
public class MainFragment extends BaseFragment {


    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mType = 0;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
