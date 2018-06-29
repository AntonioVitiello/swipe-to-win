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
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.InputStream;

/**
 * Created by Antonio Vitiello on 29/06/2018.
 */
public class PorterDuffView extends AppCompatImageView {
    private Bitmap aboveBitmap, drawBitmap, scaledAboveBitmap;
    private Paint mPaint;
    private float mX, mY;
    private Path mPath = new Path();
    private Canvas mDrawCanvas = new Canvas();
    public int mScreenWidth;
    public int mScreenHeigth;
    int[] mAboveResIds = new int[1];
    int[] mBelowResIds = new int[1];
    // Bounds of the canvas in float Used to set bounds of member initial and background
    private Rect mScreenRect = new Rect();
    private int[] mPorterDuffAttrs = R.styleable.PorterDuffAttrs;


    public PorterDuffView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PorterDuffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PorterDuffView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    private void readCustomProperties(TypedArray typedArray) {
        try {
            mAboveResIds[0] = typedArray.getResourceId(R.styleable.PorterDuffAttrs_aboveSrc, -1);
            mBelowResIds[0] = typedArray.getResourceId(R.styleable.PorterDuffAttrs_belowSrc, -1);
        } finally {
            typedArray.recycle();
        }
    }

    private void updateView() {
        setBackgroundResource(mBelowResIds[0]);
        initPaint();
        initBitmap();
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
        InputStream rawStream = getResources().openRawResource(mAboveResIds[0]);
        aboveBitmap = BitmapFactory.decodeStream(rawStream, null, options).copy(Bitmap.Config.ARGB_8888, true);
        options.inSampleSize = 4;    //if picture very large it can optimize 1/2 memory
        scaledAboveBitmap = Bitmap.createScaledBitmap(aboveBitmap, mScreenWidth, mScreenHeigth, true);
        mDrawCanvas = new Canvas();
        drawBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeigth, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
        mDrawCanvas.setBitmap(drawBitmap);
        mDrawCanvas.drawBitmap(scaledAboveBitmap, mScreenRect, mScreenRect, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        mScreenHeigth = MeasureSpec.getSize(heightMeasureSpec);
        mScreenRect.set(0, 0, mScreenWidth, mScreenHeigth);
        updateView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mDrawCanvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(drawBitmap, mScreenRect, mScreenRect, null);
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

    public void setAboveResource(@DrawableRes int resId) {
        mAboveResIds[0] = resId;
    }

    public void setBelowResource(@DrawableRes int resId) {
        mBelowResIds[0] = resId;
    }

}