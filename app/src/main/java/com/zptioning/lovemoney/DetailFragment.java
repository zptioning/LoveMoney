package com.zptioning.lovemoney;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ClassName DetailFragment
 * @Author zptioning
 * @Date 2019-11-04 11:37
 * @Version 1.0
 * @Description
 */
public class DetailFragment extends BaseFragment {

    public static DetailFragment getInstance() {
        return new DetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mType = 1;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
