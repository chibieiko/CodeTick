package com.sankari.erika.codetick.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sankari.erika.codetick.R;

/**
 * Draws a custom divider line between recycler view items.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * Defines what the dividing line looks like.
     */
    private Drawable divider;

    /**
     * Context.
     */
    private Context context;

    /**
     * Indicates whether not to draw a line under the first item.
     */
    private boolean noFirstLine;

    /**
     * Sets divider, context and no first line.
     *
     * @param divider     drawable resource
     * @param context     context
     * @param noFirstLine true if we don't want a line below the first item, otherwise false
     */
    public CustomDividerItemDecoration(Drawable divider, Context context, boolean noFirstLine) {
        this.divider = divider;
        this.context = context;
        this.noFirstLine = noFirstLine;
    }

    /**
     * Gets item offsets.
     *
     * @param outRect outer rectangle
     * @param view    view
     * @param parent  recycler view
     * @param state   recycler view's state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        outRect.top = divider.getIntrinsicHeight();
    }

    /**
     * Calls drawLine depending on no first line boolean and recycler view item position.
     *
     * @param c      canvas
     * @param parent recycler view
     * @param state  recycler view's state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (noFirstLine && i != 0 && i != childCount - 1) {
                drawLine(c, parent, parent.getChildAt(i));
            } else if (!noFirstLine) {
                drawLine(c, parent, parent.getChildAt(i));
            }
        }
    }

    /**
     * Sets divider's for line and draws it.
     *
     * @param c      canvas
     * @param parent recycler view
     * @param view   view
     */
    private void drawLine(Canvas c, RecyclerView parent, View view) {

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        int dividerLeft = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        int dividerRight = parent.getWidth() - (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        int dividerTop = view.getBottom() + params.bottomMargin;
        int dividerBottom = dividerTop + divider.getIntrinsicHeight();

        divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
        divider.draw(c);
    }
}
