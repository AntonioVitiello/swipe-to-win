package av.porter.duff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public int screenwidth;
    public int screenheigth;
    private Rect fullRect;

//    int[] belowImageId = new int[]{R.drawable.image1};
//    int[] aboveImageIds = new int[]{R.drawable.image2};
//    int[] belowImageId = new int[]{R.drawable.image_below};
//    int[] aboveImageIds = new int[]{R.drawable.image_above};
//    int[] belowImageId = new int[]{R.drawable.img4};
//    int[] aboveImageIds = new int[]{R.drawable.img3};
    int[] aboveImageIds = new int[]{R.drawable.googlepay_sfondo_2};
    int[] belowImageId = new int[]{R.drawable.pagamenti_header_googlepay};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_nature);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenwidth = metrics.widthPixels;
        screenheigth = metrics.heightPixels;
        Log.e("AAA", String.format("screenwidth=%d, screenheigth=%d", screenwidth, screenheigth));
        fullRect = new Rect(0, 0, screenwidth, screenheigth);
        setContentView(new BelowView(this));
    }

    private class BelowView extends View {
        private Bitmap aboveBitmap, drawBitmap, scaledAboveBitmap;
        private Canvas canvas;
        private Paint mPaint;
        private Path mPath;
        private float mX, mY;

        private BelowView(Context context) {
            super(context);
            setBackgroundResource(belowImageId[0]);
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
            mPath = new Path();
        }

        private void initBitmap() {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Config.ARGB_8888;
            //opt.inPurgeable = true;
            //opt.inInputShareable = true;
            opt.inSampleSize = 16;    //if picture very large it can optimize 1/2 memory
            InputStream rawStream = getResources().openRawResource(aboveImageIds[0]);
            aboveBitmap = BitmapFactory.decodeStream(rawStream, null, opt).copy(Config.ARGB_8888, true);
            scaledAboveBitmap = Bitmap.createScaledBitmap(aboveBitmap, screenwidth, screenheigth, true);
            canvas = new Canvas();
            drawBitmap = Bitmap.createBitmap(screenwidth, screenheigth, Config.ARGB_8888).copy(Config.ARGB_8888, true);
            canvas.setBitmap(drawBitmap);
            canvas.drawBitmap(scaledAboveBitmap, fullRect, fullRect, null);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.canvas.drawPath(mPath, mPaint);
            canvas.drawBitmap(drawBitmap, fullRect, fullRect, null);
        }

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
    }

    public Bitmap centerCrop(Bitmap srcBmp) {
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()) {

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        } else {

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }

        return dstBmp;
    }

}
