package com.vincent.handwrite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.OutputStream;

class PaintBoard2 extends View {
    private String TAG = "PaintBoard2";
    private Paint mPaint = null;
    private Bitmap mBitmap = null;
    private Canvas mBitmapCanvas = null;
    private boolean clearCanvas = false;
    public PaintBoard2(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.post(()->{
            Log.d(TAG,"width:"+this.getWidth());
            Log.d(TAG,"height:"+this.getHeight());
            mBitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(), Bitmap.Config.ARGB_8888);
            mBitmapCanvas = new Canvas(mBitmap);
            mBitmapCanvas.drawColor(Color.GRAY);
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(6);
            invalidate();
        });
    }
    private float startX;
    private float startY ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float stopX = event.getX();
                float stopY = event.getY();
                Log.d(TAG,"onTouchEvent-ACTION_MOVE\nstartX is "+startX+
                        " startY is "+startY+" stopX is "+stopX+ " stopY is "+stopY);
                mBitmapCanvas.drawLine(startX, startY, stopX, stopY, mPaint);
                startX = event.getX();
                startY = event.getY();
                invalidate();//call onDraw()
                break;
        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap != null) {
            if(clearCanvas)
            {
                Log.d(TAG,"clear canvas");
                canvas.drawColor(Color.GRAY);
                mBitmapCanvas.drawColor(Color.GRAY);
                clearCanvas = false;
            }
            else
            {
                canvas.drawColor(Color.GRAY);
                canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            }

        }
    }

    public void clear(){
        clearCanvas = true;
        invalidate();
    }

    public void saveBitmap(OutputStream stream) {
        if (mBitmap != null) {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
    }
}
