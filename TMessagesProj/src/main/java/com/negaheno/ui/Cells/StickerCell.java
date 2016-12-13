/*
 * This is the source code of Telegram for Android v. 2.0.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package com.negaheno.ui.Cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.negaheno.messenger.TLRPC;
import com.negaheno.ui.Components.FrameLayoutFixed;
import com.negaheno.android.AndroidUtilities;
import com.negaheno.messenger.R;
import com.negaheno.ui.Components.BackupImageView;

public class StickerCell extends FrameLayoutFixed {

    private BackupImageView imageView;

    public StickerCell(Context context) {
        super(context);

        imageView = new BackupImageView(context);
        imageView.imageReceiver.setAspectFit(true);
        imageView.imageReceiver.setDisableRecycle(true);
        imageView.processDetach = false;
        addView(imageView);
        LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
        layoutParams.width = AndroidUtilities.dp(66);
        layoutParams.height = AndroidUtilities.dp(66);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.topMargin = AndroidUtilities.dp(5);
        imageView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(76) + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(78), MeasureSpec.EXACTLY));
    }

    @Override
    public void setPressed(boolean pressed) {
        if (imageView.imageReceiver.getPressed() != pressed) {
            imageView.imageReceiver.setPressed(pressed);
            imageView.invalidate();
        }
        super.setPressed(pressed);
    }

    public void setSticker(TLRPC.Document document, int side) {
        if (document != null) {
            document.thumb.location.ext = "webp";
            imageView.setImage(document.thumb.location, null, (Drawable) null);
        }
        if (side == -1) {
            setBackgroundResource(R.drawable.stickers_back_left);
            setPadding(AndroidUtilities.dp(7), 0, 0, 0);
        } else if (side == 0) {
            setBackgroundResource(R.drawable.stickers_back_center);
            setPadding(0, 0, 0, 0);
        } else if (side == 1) {
            setBackgroundResource(R.drawable.stickers_back_right);
            setPadding(0, 0, AndroidUtilities.dp(7), 0);
        } else if (side == 2) {
            setBackgroundResource(R.drawable.stickers_back_all);
            setPadding(AndroidUtilities.dp(3), 0, AndroidUtilities.dp(3), 0);
        }
        if (getBackground() != null) {
            getBackground().setAlpha(230);
        }
    }
}
