package nl.teamone.settingsun.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import nl.teamone.settingsun.R;

public class BoardTileView extends ImageView {

	private Block mBlock;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public BoardTileView(Context context, Block block,  RelativeLayout.LayoutParams layoutParams, int backgroundRes) {
		super(context);
        mBlock = block;
        setLayoutParams(layoutParams);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(getResources().getDrawable(backgroundRes).mutate());
        } else {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                setBackground(getResources().getDrawable(backgroundRes).mutate());
            } else {
                setBackground(getResources().getDrawable(backgroundRes, null).mutate());
            }
        }
	}

    public Block getBlock() {
        return mBlock;
    }

}