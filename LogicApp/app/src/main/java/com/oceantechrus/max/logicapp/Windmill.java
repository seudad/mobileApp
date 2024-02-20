package com.oceantechrus.max.logicapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import static java.lang.Math.PI;
import static java.lang.Math.atan;

public class Windmill extends View {
    private Paint mWindmillPaint;
    private Path mWindPath;
    private Path mPillarPath;
    private int width,height;
    private int mWindmillColor;
    private float mWindLengthPercent;
    private Point mCenterPoint;
    private float x1,y1,x2,y2,x3,y3,x4,y4,x5,y5;
    private double rad1,rad2,rad3,rad4;
    private double r1,r2,r3,r4;
    private ObjectAnimator mAnimator;
    private float angle;
    private int windSpeed = 1;


    private int mDefualtSize;
    private Context mContext;

    private final float DEFAULT_DIP = 120;
    private static final String TAG = "Windmill";

    public Windmill(Context context) {
        this(context,null);
    }

    public Windmill(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Windmill(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //Log.d(TAG, "Windmill: "+"жћ„йЂ е‡Ѕж•°");
        mContext = context;
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        initAttrs(attrs);


        mWindmillPaint = new Paint();
        mCenterPoint = new Point();
        mWindmillPaint.setStyle(Paint.Style.FILL);
        mWindmillPaint.setStrokeWidth(2);
        mWindmillPaint.setAntiAlias(true);
        mWindmillPaint.setColor(mWindmillColor);

        mAnimator = ObjectAnimator.ofFloat(this,"angle",0,(float)(2*PI));//
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());

    }

    private void initAttrs(AttributeSet attrs){
        mDefualtSize = dipToPx(DEFAULT_DIP);
        if(attrs != null){
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.Windmill);
            mWindLengthPercent = typedArray.getFloat(R.styleable.Windmill_windLengthPerent,0.33f);
            mWindmillColor = typedArray.getColor(R.styleable.Windmill_windmillColors, Color.WHITE);
            typedArray.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(myMeasure(widthMeasureSpec,mDefualtSize),myMeasure(heightMeasureSpec,mDefualtSize));
        //Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {//w , h еЌідёєonMeasureи®Ўз®—е‡єжќҐзљ„еЂј
        width = w;
        height = h;
        mCenterPoint.x = (int) (w * mWindLengthPercent);
        mCenterPoint.y = (int) (w * mWindLengthPercent);
        setBladeLocate();
        //Log.d(TAG, "onSizeChanged: ");
    }



    private void setBladeLocate() {

        x1 = mCenterPoint.x;
        y1 = mCenterPoint.y;

        //radian(еј§еє¦)
        rad1 = atan(width/15/(width/30));
        rad2 = atan(width/6/(width/30));
        rad3 = PI/2;
        rad4 = atan(mCenterPoint.y/2/(-width/30))+PI;


        //r дёєж–њиѕ№й•їеє¦,дёЋдёЉйќўи¦ЃеЇ№еє”
        r1 = Math.hypot(width/30,width/15);
        r2 = Math.hypot(width/30,width/6);
        r3 = Math.hypot(0,mCenterPoint.y);
        r4 = Math.hypot(width/30,mCenterPoint.y/2);





    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawWind(canvas);
        drawPillar(canvas);

        canvas.restore();
        //Log.d(TAG, "onDraw: ");


    }

    private void drawPillar(Canvas canvas) {
        mPillarPath = new Path();
        mPillarPath.moveTo(mCenterPoint.x-width/90,mCenterPoint.y-width/90);
        mPillarPath.lineTo(mCenterPoint.x+width/90,mCenterPoint.y-width/90);//иїћзєї
        mPillarPath.lineTo(mCenterPoint.x+width/35,height-height/35);
        mPillarPath.quadTo(mCenterPoint.x,height,mCenterPoint.x-width/35,height-height/35);//иґќеЎће°”ж›ІзєїпјЊжЋ§е€¶з‚№е’Њз»€з‚№
        mPillarPath.close();//й—­еђ€е›ѕеЅў
        canvas.drawPath(mPillarPath,mWindmillPaint);

    }
    private void drawWind(Canvas canvas) {
        mWindPath = new Path();
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,width/40,mWindmillPaint);
        mWindPath.moveTo(x1,y1);
        x2 = mCenterPoint.x + (float) (r1 * Math.cos(rad1 + angle));
        y2 = mCenterPoint.y + (float) (r1 * Math.sin(rad1 + angle));
        x3 = mCenterPoint.x + (float) (r2 * Math.cos(rad2 + angle));
        y3 = mCenterPoint.y + (float) (r2 * Math.sin(rad2 + angle));
        x4 = mCenterPoint.x + (float) (r3 * Math.cos(rad3 + angle));
        y4 = mCenterPoint.y + (float) (r3 * Math.sin(rad3 + angle));
        x5 = mCenterPoint.x + (float) (r4 * Math.cos(rad4 + angle));
        y5 = mCenterPoint.y + (float) (r4 * Math.sin(rad4 + angle));


        mWindPath.cubicTo(x2,y2,x3,y3,x4,y4);
        mWindPath.quadTo(x5,y5,x1,y1);
        canvas.drawPath(mWindPath,mWindmillPaint);
        canvas.rotate(120,mCenterPoint.x,mCenterPoint.y);
        canvas.drawPath(mWindPath,mWindmillPaint);
        canvas.rotate(120,mCenterPoint.x,mCenterPoint.y);
        canvas.drawPath(mWindPath,mWindmillPaint);
        canvas.rotate(120,mCenterPoint.x,mCenterPoint.y);
    }


    private int myMeasure(int measureSpec, int defaultSize) {
        int mode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = defaultSize;
        if(mode == MeasureSpec.EXACTLY ){
            result = specSize;
        }else if(mode == MeasureSpec.AT_MOST){
            result = Math.min(specSize,defaultSize);
        }
        return result;
    }




    public void setWindSpeed(int windSpeed) {
        if(windSpeed == 0){
            windSpeed = 1;
        }
        this.windSpeed = windSpeed;
    }
    private int i = 1;

    public void setAngle(float angle){
        this.angle = angle;
        invalidate();
    }
    public void startAnimation(){
        mAnimator.setDuration((long) (10000/(windSpeed*0.80)));
        mAnimator.start();
    }
    public void stopAnimation(){
        clearAnimation();
    }

    private int dipToPx(float dip) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int)((dip*density)+0.5f);
    }
}
