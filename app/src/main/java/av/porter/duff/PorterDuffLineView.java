package av.porter.duff;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Antonio Vitiello on 29/06/2018.
 */
public class PorterDuffLineView extends View {
    private Paint mPaint;
    private float mX, mY;
    private Path mPath = new Path();
    public int mScreenWidth;
    public int mScreenHeigth;
    @DrawableRes
    int mImageResId;

    // Bounds of the canvas in float Used to set bounds of member initial and background
    private Rect mScreenRect = new Rect();
    private int[] mPorterDuffAttrs = R.styleable.PorterDuffAttrs;


    public PorterDuffLineView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PorterDuffLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PorterDuffLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    mPorterDuffAttrs,
                    0, 0);
            readCustomProperties(typedArray);
        }
        updateView();
    }

    private void readCustomProperties(TypedArray typedArray) {
        try {
            mImageResId = typedArray.getResourceId(R.styleable.PorterDuffAttrs_aboveSrc, -1);
        } finally {
            typedArray.recycle();
        }
    }

    private void updateView() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeigth = metrics.heightPixels;
        mScreenRect.set(0, 0, mScreenWidth, mScreenHeigth);

        setBackgroundResource(mImageResId);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //mPaint.setAlpha(0);
        mPaint.setDither(true);
        //mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(120);
        mPaint.setColor(getResources().getColor(R.color.white));
        mPath.reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                mX = x;
                mY = y;
                //invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(mX, mY, x, y);
                mX = x;
                mY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(mX, mY);
//                invalidate();
                break;
        }

        return true;
    }

    public void setBelowResource(@DrawableRes int resId) {
        mImageResId = resId;
    }

}