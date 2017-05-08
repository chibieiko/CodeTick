package com.sankari.erika.codetick.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sankari.erika.codetick.R;

/**
 * Created by erika on 4/23/2017.
 */

public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private Context context;
    private boolean hasSkip;

    public CustomDividerItemDecoration(Drawable divider, Context context, boolean hasSkip) {
        this.divider = divider;
        this.context = context;
        this.hasSkip = hasSkip;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        outRect.top = divider.getIntrinsicHeight();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (hasSkip && i != 0 && i != childCount - 1) {
                drawLine(c, parent, parent.getChildAt(i));
            } else if (!hasSkip) {
                drawLine(c, parent, parent.getChildAt(i));
            }
        }
    }

    private void drawLine(Canvas c, RecyclerView parent, View view) {
        View child = view;

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int dividerLeft = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        int dividerRight = parent.getWidth() - (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        int dividerTop = child.getBottom() + params.bottomMargin;
        int dividerBottom = dividerTop + divider.getIntrinsicHeight();

        divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
        divider.draw(c);
    }
}
