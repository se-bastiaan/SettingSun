package nl.teamone.settingsun.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import nl.teamone.settingsun.game.Field;
import nl.teamone.settingsun.utils.PixelUtils;
import nl.teamone.settingsun.utils.PrefUtils;

public class GameView extends View {

    private Paint borderPaint, gridPaint, bottomBorderPaint, blackPaint, yellowPaint, redPaint;
    private Integer bigBorder, smallBorder;
    protected Field field;
    private Rect bigBlock, smallBlock1, smallBlock2, smallBlock3, smallBlock4, block1, block2, block3, block4, block5;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Initialise GameView
     */
    private void init() {
        bigBorder = PixelUtils.getPixelsFromDp(getContext(), 3);
        smallBorder = PixelUtils.getPixelsFromDp(getContext(), 1);

        borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor("#FFFFFF"));
        borderPaint.setStrokeWidth(bigBorder);

        bottomBorderPaint = new Paint();
        bottomBorderPaint.setColor(Color.parseColor("#666666"));
        bottomBorderPaint.setStrokeWidth(bigBorder);

        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#666666"));
        gridPaint.setStrokeWidth(smallBorder);

        blackPaint = new Paint();
        blackPaint.setColor(Color.parseColor("#000000"));

        redPaint = new Paint();
        redPaint.setColor(Color.parseColor("#DD0000"));

        yellowPaint = new Paint();
        yellowPaint.setColor(Color.parseColor("#FFDF00"));

        field = new Field();
    }

    /**
     * Main OnDraw
     * @param canvas {@link android.graphics.Canvas}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float offsetVert = 0;
        float offsetHori = 0;
        float size = 0;
        if(canvas.getHeight() > canvas.getWidth()) {
            size = canvas.getWidth() / 4;
            offsetVert = (canvas.getHeight() - size * 5) / 2;
        } else {
            size = canvas.getHeight() / 5;
            offsetHori = (canvas.getWidth() - size * 4) / 2;
        }

        // Draw grid
        canvas.drawLine(offsetHori, offsetVert + size, canvas.getWidth() - offsetHori, offsetVert + size, gridPaint);
        canvas.drawLine(offsetHori, offsetVert + size * 2, canvas.getWidth() - offsetHori, offsetVert + size * 2, gridPaint);
        canvas.drawLine(offsetHori, offsetVert + size * 3, canvas.getWidth() - offsetHori, offsetVert + size * 3, gridPaint);
        canvas.drawLine(offsetHori, offsetVert + size * 4, canvas.getWidth() - offsetHori, offsetVert + size * 4, gridPaint);
        canvas.drawLine(offsetHori + size, offsetVert, offsetHori + size, canvas.getHeight() - offsetVert, gridPaint);
        canvas.drawLine(offsetHori + size * 2, offsetVert, offsetHori + size * 2, canvas.getHeight() - offsetVert, gridPaint);
        canvas.drawLine(offsetHori + size * 3, offsetVert, offsetHori + size * 3, canvas.getHeight() - offsetVert, gridPaint);

        // Draw border
        canvas.drawLine(offsetHori, offsetVert, canvas.getWidth() - offsetHori, offsetVert, borderPaint);
        canvas.drawLine(canvas.getWidth(), offsetVert, canvas.getWidth() - offsetHori, canvas.getHeight() - offsetVert, borderPaint);
        canvas.drawLine(offsetHori, offsetVert, offsetHori, canvas.getHeight() - offsetVert, borderPaint);
        canvas.drawLine(offsetHori, canvas.getHeight() - offsetVert, offsetHori + size, canvas.getHeight() - offsetVert, borderPaint);
        canvas.drawLine(offsetHori + size * 3, canvas.getHeight() - offsetVert, canvas.getWidth() - offsetHori, canvas.getHeight() - offsetVert, borderPaint);
        canvas.drawLine(offsetHori + size, canvas.getHeight() - offsetVert, offsetHori + size * 3, canvas.getHeight() - offsetVert, bottomBorderPaint);

        int blockRight = (int) (getWidth() - size * 3 - smallBorder - offsetHori);
        int blockBottom = (int) (getHeight() - size * 4 - offsetVert);
        int blockTop = (int) (bigBorder + offsetVert);
        int blockLeft = (int) (bigBorder + offsetHori);
        int sizeInt = (int) size;

        if(bigBlock == null) {
            bigBlock = new Rect(blockLeft, blockTop, (int)(blockRight + size), (int)(blockBottom + size));
        }
        if(smallBlock1 == null) {
            smallBlock1 = new Rect(blockLeft, blockTop, blockRight, blockBottom);
        }
        if(smallBlock2 == null) {
            smallBlock2 = new Rect(blockLeft, blockTop, blockRight, blockBottom);
        }
        if(smallBlock3 == null) {
            smallBlock3 = new Rect(blockLeft, blockTop, blockRight, blockBottom);
        }
        if(smallBlock4 == null) {
            smallBlock4 = new Rect(blockLeft, blockTop, blockRight, blockBottom);
        }
        if(block1 == null) {
            block1 = new Rect(blockLeft, blockTop, blockRight, (int)(blockBottom + size));
        }
        if(block2 == null) {
            block2 = new Rect(blockLeft, blockTop, blockRight, (int)(blockBottom + size));
        }
        if(block3 == null) {
            block3 = new Rect(blockLeft, blockTop, blockRight, (int)(blockBottom + size));
        }
        if(block4 == null) {
            block4 = new Rect(blockLeft, blockTop, blockRight, (int)(blockBottom + size));
        }
        if(block5 == null) {
            block5 = new Rect(blockLeft, blockTop, (int)(blockRight + size), blockBottom);
        }

        bigBlock.offset(sizeInt, 0);
        smallBlock1.offset(sizeInt, sizeInt * 3);
        smallBlock2.offset(sizeInt * 2, sizeInt * 3);
        smallBlock3.offset(sizeInt, sizeInt * 4);
        smallBlock4.offset(sizeInt * 2, sizeInt * 4);
        block2.offset(sizeInt * 3, 0);
        block3.offset(0, sizeInt * 3);
        block4.offset(sizeInt * 3, sizeInt * 3);
        block5.offset(sizeInt, sizeInt* 2);

        canvas.drawRect(bigBlock, redPaint);
        canvas.drawRect(smallBlock1, yellowPaint);
        canvas.drawRect(smallBlock2, yellowPaint);
        canvas.drawRect(smallBlock3, yellowPaint);
        canvas.drawRect(smallBlock4, yellowPaint);
        canvas.drawRect(block1, blackPaint);
        canvas.drawRect(block2, blackPaint);
        canvas.drawRect(block3, blackPaint);
        canvas.drawRect(block4, blackPaint);
        canvas.drawRect(block5, blackPaint);

        /*
        Rect rect = new Rect(400, 400);
        rect.

        canvas.drawRect(new);
        */
    }

    /**
     * Touch event listener
     * @param event {@link android.view.MotionEvent}
     * @return {@link java.lang.Boolean}
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
