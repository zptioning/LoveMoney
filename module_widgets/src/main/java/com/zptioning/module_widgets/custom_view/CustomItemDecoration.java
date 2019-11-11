package com.zptioning.module_widgets.custom_view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private int gap = 2;

    public CustomItemDecoration() {
    }

    public CustomItemDecoration(int gap) {
        this.gap = gap;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
//        int spanCount = ((GridLayoutManager)parent.getLayoutManager()).getSpanCount();
//        if (childAdapterPosition % spanCount == spanCount - 1) {
//            outRect.set(0, 0, 0, 4);
//        } else {
            outRect.set(0, 0, 4, 4);
//        }

    }
}