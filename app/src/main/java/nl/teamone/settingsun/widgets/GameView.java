package nl.teamone.settingsun.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import nl.teamone.settingsun.game.Field;
import nl.teamone.settingsun.utils.PixelUtils;

public class GameView extends View {

    Paint borderPaint, gridPaint, bottomBorderPaint;
    protected Field field;

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

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor("#FFFFFF"));
        borderPaint.setStrokeWidth(PixelUtils.getPixelsFromDp(getContext(), 3));

        bottomBorderPaint = new Paint();
        bottomBorderPaint.setColor(Color.parseColor("#666666"));
        bottomBorderPaint.setStrokeWidth(PixelUtils.getPixelsFromDp(getContext(), 3));

        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#666666"));
        gridPaint.setStrokeWidth(PixelUtils.getPixelsFromDp(getContext(), 1));

        field = new Field();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float rowHeight = canvas.getHeight() / 5;
        float columnWidth = canvas.getWidth() / 4;

        // Draw grid
        canvas.drawLine(0, rowHeight, canvas.getWidth(), rowHeight, gridPaint);
        canvas.drawLine(0, rowHeight * 2, canvas.getWidth(), rowHeight * 2, gridPaint);
        canvas.drawLine(0, rowHeight * 3, canvas.getWidth(), rowHeight * 3, gridPaint);
        canvas.drawLine(0, rowHeight * 4, canvas.getWidth(), rowHeight * 4, gridPaint);
        canvas.drawLine(columnWidth, 0, columnWidth, canvas.getHeight(), gridPaint);
        canvas.drawLine(columnWidth * 2, 0, columnWidth * 2, canvas.getHeight(), gridPaint);
        canvas.drawLine(columnWidth * 3, 0, columnWidth * 3, canvas.getHeight(), gridPaint);

        // Draw border
        canvas.drawLine(0, 0, canvas.getWidth(), 0, borderPaint);
        canvas.drawLine(canvas.getWidth(), 0, canvas.getWidth(), canvas.getHeight(), borderPaint);
        canvas.drawLine(0, 0, 0, canvas.getHeight(), borderPaint);
        canvas.drawLine(0, canvas.getHeight(), columnWidth, canvas.getHeight(), borderPaint);
        canvas.drawLine(columnWidth * 3, canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), borderPaint);
        canvas.drawLine(columnWidth, canvas.getHeight(), columnWidth * 3, canvas.getHeight(), bottomBorderPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
