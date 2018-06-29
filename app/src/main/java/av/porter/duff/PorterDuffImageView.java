package av.porter.duff;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.io.InputStream;

/**
 * Created by Antonio Vitiello on 29/06/2018.
 */
public class PorterDuffImageView extends AppCompatImageView {
    private Bitmap mAboveBitmap, mDrawBitmap, mScaledAboveBitmap;
    private Paint mPaint;
    private float mX, mY;
    private Path mPath = new Path();
    private Canvas mDrawCanvas = new Canvas();
    public int mScreenWidth;
    public int mScreenHeigth;
    private int mScaleRatio;
    @RawRes
    int mAboveResId;
    @DrawableRes
    int mBelowResId;

    // Bounds of the canvas in float Used to set bounds of member initial and background
    private Rect mScreenRect = new Rect();
    private int[] mPorterDuffAttrs = R.styleable.PorterDuffAttrs;


    public PorterDuffImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PorterDuffImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PorterDuffImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
            mAboveResId = typedArray.getResourceId(R.styleable.PorterDuffAttrs_aboveSrc, -1);
            mBelowResId = typedArray.getResourceId(R.styleable.PorterDuffAttrs_belowSrc, -1);
            mScaleRatio = typedArray.getInteger(R.styleable.PorterDuffAttrs_scaleRatio, 1);
        } finally {
            typedArray.recycle();
        }
    }

    private void updateView() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mScreenWidth = metrics.widthPixels;
        mScreenHeigth = metrics.heightPixels;
        mScreenRect.set(0, 0, mScreenWidth, mScreenHeigth);

        setBackgroundResource(mBelowResId);
        initBitmap();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(0);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(140);
        mPath.reset();
    }

    private void initBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        InputStream rawStream = getResources().openRawResource(mAboveResId);
        mAboveBitmap = BitmapFactory.decodeStream(rawStream, null, options).copy(Bitmap.Config.ARGB_8888, true);
        // if picture very large inSampleSize can optimize memory with subsample of the original image, returning a smaller image
        options.inSampleSize = mScaleRatio;
        mScaledAboveBitmap = Bitmap.createScaledBitmap(mAboveBitmap, mScreenWidth, mScreenHeigth, true);
        mDrawCanvas = new Canvas();
        mDrawBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeigth, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
        mDrawCanvas.setBitmap(mDrawBitmap);
        mDrawCanvas.drawBitmap(mScaledAboveBitmap, mScreenRect, mScreenRect, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawCanvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mDrawBitmap, mScreenRect, mScreenRect, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
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
                invalidate();
                break;
        }

        return true;
    }

    public void setAboveResource(@RawRes int resId) {
        mAboveResId = resId;
    }

    public void setBelowResource(@DrawableRes int resId) {
        mBelowResId = resId;
    }

    public void setScaleRatio(int scaleRatio) {
        mScaleRatio = scaleRatio;
    }

}