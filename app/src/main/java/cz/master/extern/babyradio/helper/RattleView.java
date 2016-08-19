package cz.master.extern.babyradio.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Random;

import cz.master.extern.babyradio.R;

public class RattleView extends View implements SensorEventListener {


    private final int mTouchSlop;
    // Bounds of screen
    private int xMin = 0;
    private int yMin = 0;
    private int xMax;
    private int yMax;

    private float ballX1 = 50;
    private float ballY1 = 50;
    private float ballSpeedX1 = 3;
    private float ballSpeedY1 = 4;
    private float maxSpeed = 20;
    private RectF ballBounds1;
    private Paint ballColor1;
    // Ball Attribute 2
    private float ballX2 = 250;
    private float ballY2 = 50;
    private float ballSpeedX2 = 5;
    private float ballSpeedY2 = 3;

    // Ball Attribute 3
    private float ballX3 = 440;
    private float ballY3 = 90;
    private float ballSpeedX3 = 4;
    private float ballSpeedY3 = 2;
    private RectF ballBounds2;
    private Paint ballColor2;
    // For touch input
    private float previousX;
    private float previousY;
    private float scalor = 5.0f;
    // For game elements

    // Enemy Ball
    private float enemyRadius = 30;
    private float enemyX = 200;
    private float enemyY = 300;
    private float enemySpeedX = 3;
    private float enemySpeedY = 5;
    private RectF enemyBallBounds;
    private Paint enemyColor;
    Context context;
    Bitmap backgroundBitmap;
    Bitmap scaledBackgroundBitmap;
    Paint paintForBackground;
    Resources res;

    volatile Bitmap ball1, ball2, ball3, ball4, ball5, ball6, ball7, ball8, ball9;
    Bitmap scaledBall1, scaledBall2, scaledBall3, scaledBall4, scaledBall5;

    private volatile int sprite1Rotation = 0;
    public volatile Rect rectBall1, rectBall2, rectBall3, rectBall4, rectBall5, rectBall6, rectBall7, rectBall8,
            rectBall9;
    public volatile Point point1, point2, point3, point4, point5, point6, point7, point8, point9;
    public volatile Matrix matrix1, matrix2, matrix3, matrix4, matrix5, matrix6, matrix7, matrix8, matrix9;
    private Point lastCollision;

    private boolean isSensorShowMoving = false;

    public RattleView(Context context) {
        super(context);
        // Initialize game elements
        this.context = context;
        enemyBallBounds = new RectF();

        ballBounds1 = new RectF();
        ballColor1 = new Paint(Paint.FILTER_BITMAP_FLAG);
        enemyColor = new Paint();

        ballBounds2 = new RectF();
        ballColor2 = new Paint();

        res = getResources();
        backgroundBitmap = BitmapFactory.decodeResource(res, R.drawable.bg_restmoment);
        paintForBackground = new Paint(Paint.FILTER_BITMAP_FLAG);
        ball1 = BitmapFactory.decodeResource(res, R.drawable.planet1);
        ball2 = BitmapFactory.decodeResource(res, R.drawable.planet2);
        ball3 = BitmapFactory.decodeResource(res, R.drawable.planet3);
        ball4 = BitmapFactory.decodeResource(res, R.drawable.planet4);
        ball5 = BitmapFactory.decodeResource(res, R.drawable.planet5);
        ball6 = BitmapFactory.decodeResource(res, R.drawable.planet6);
        ball7 = BitmapFactory.decodeResource(res, R.drawable.planet7);
        ball8 = BitmapFactory.decodeResource(res, R.drawable.planet8);
        this.setFocusableInTouchMode(true);
        rectBall1 = new Rect(0, 0, ball1.getWidth(), ball1.getHeight());
        rectBall2 = new Rect(0, 0, ball2.getWidth(), ball2.getHeight());
        rectBall3 = new Rect(0, 0, ball3.getWidth(), ball3.getHeight());
        rectBall4 = new Rect(0, 0, ball4.getWidth(), ball4.getHeight());
        rectBall5 = new Rect(0, 0, ball5.getWidth(), ball5.getHeight());
        rectBall6 = new Rect(0, 0, ball6.getWidth(), ball6.getHeight());
        rectBall7 = new Rect(0, 0, ball7.getWidth(), ball7.getHeight());
        rectBall8 = new Rect(0, 0, ball8.getWidth(), ball8.getHeight());
        point1 = new Point(-1, -1);
        point2 = new Point(-1, -1);
        point3 = new Point(-1, -1);
        point4 = new Point(-1, -1);
        point5 = new Point(-1, -1);
        point6 = new Point(-1, -1);
        point7 = new Point(-1, -1);
        point8 = new Point(-1, -1);
        matrix1 = new Matrix();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }//end of function

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    // When view is first created (or changed) set ball's X & Y max and position.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        xMax = w - 1;
        yMax = h - 1;
        try {
            if (scaledBackgroundBitmap == null && backgroundBitmap != null)
                scaledBackgroundBitmap = Bitmap.createBitmap(backgroundBitmap, 0, 0, w, h);
        } catch (Exception e) {

        }

        Handler hn = new Handler();
        hn.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 0);
    }//end of onSizeChanged

    public void onDraw(Canvas canvas) {
        if (scaledBackgroundBitmap != null)
            canvas.drawBitmap(scaledBackgroundBitmap, 0, 0, paintForBackground);
        else {
            canvas.drawBitmap(backgroundBitmap, 0, 0, paintForBackground);
        }
        drawBall(canvas, point1, rectBall1, ball1);
        drawBall(canvas, point2, rectBall2, ball2);
        drawBall(canvas, point3, rectBall3, ball3);
        drawBall(canvas, point4, rectBall4, ball4);
        drawBall(canvas, point5, rectBall5, ball5);
        drawBall(canvas, point6, rectBall6, ball6);
//        drawBall(canvas, point7, rectBall7, ball7);
//        drawBall(canvas, point8, rectBall8, ball8);
        sprite1Rotation += 3;
    }

    private void drawBall(Canvas canvas, Point pointFOrDraw, Rect rectForDraw, Bitmap bitmapForDraw) {
        matrix1.reset();
        matrix1.postTranslate((float) (pointFOrDraw.x), (float) (pointFOrDraw.y));
        matrix1.postRotate(sprite1Rotation,
                (float) (pointFOrDraw.x + rectForDraw.width() / 2.0),
                (float) (pointFOrDraw.y + rectForDraw.width() / 2.0));
        canvas.drawBitmap(bitmapForDraw, matrix1, null);
    }


    boolean ballTouch1 = false,
            ballTouch2 = false, ballTouch3 = false, ballTouch4 = false, ballTouch5 = false,
            ballTouch6 = false, ballTouch7 = false, ballTouch8 = false;
    private int xTouch, preXTouch;
    private int yTouch, preYTouch;

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xTouch = (int) event.getX(0);
        yTouch = (int) event.getY(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preXTouch = xTouch;
                preYTouch = yTouch;
                Rect r1 = new Rect(point1.x, point1.y, point1.x + rectBall1.width(), point1.y + rectBall1.height());
                if (r1.contains(xTouch, yTouch)) {
                    Log.d("Ball1", "Touch");
                    ballTouch1 = true;
                }
                r1 = new Rect(point2.x, point2.y, point2.x + rectBall2.width(), point2.y + rectBall2.height());
                if (r1.contains(xTouch, yTouch)) {

                    ballTouch2 = true;
                }
                r1 = new Rect(point3.x, point3.y, point3.x + rectBall3.width(), point3.y + rectBall3.height());
                if (r1.contains(xTouch, yTouch)) {
                    Log.d("Ball3", "Touch");
                    ballTouch3 = true;
                }
                r1 = new Rect(point4.x, point4.y, point4.x + rectBall4.width(), point4.y + rectBall4.height());
                if (r1.contains(xTouch, yTouch)) {
                    Log.d("Ball4", "Touch");
                    ballTouch4 = true;
                }
                r1 = new Rect(point5.x, point5.y, point5.x + rectBall5.width(), point5.y + rectBall5.height());
                if (r1.contains(xTouch, yTouch)) {
                    Log.d("Ball5", "Touch");
                    ballTouch5 = true;
                }
                r1 = new Rect(point6.x, point6.y, point6.x + rectBall6.width(), point6.y + rectBall6.height());
                if (r1.contains(xTouch, yTouch)) {
                    Log.d("Ball6", "Touch");
                    ballTouch6 = true;
                }
                r1 = new Rect(point7.x, point7.y, point7.x + rectBall7.width(), point7.y + rectBall7.height());
                if (r1.contains(xTouch, yTouch)) {
                    Log.d("Ball7", "Touch");
                    ballTouch7 = true;
                }
                r1 = new Rect(point8.x, point8.y, point8.x + rectBall8.width(), point8.y + rectBall8.height());
                if (r1.contains(xTouch, yTouch)) {
                    Log.d("Ball8", "Touch");
                    ballTouch8 = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int distY = Math.abs(yTouch - preYTouch);
                final int distX = Math.abs(xTouch - preXTouch);
                if (ballTouch1) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point1.x + deltaX) > 0 &&
                                ((point1.x + deltaX) < sprite1MaxX)
                                && (point1.y + deltaY) > 0 &&
                                ((point1.y + deltaY) < sprite1MaxY)) {

                            point1.x = point1.x + (deltaX);
                            point1.y = point1.y + deltaY;
                        }
                    }
                } else if (ballTouch2) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point2.x + deltaX) > 0 &&
                                ((point2.x + deltaX) < sprite2MaxX)
                                && (point2.y + deltaY) > 0 &&
                                ((point2.y + deltaY) < sprite2MaxY)) {

                            point2.x = point2.x + (deltaX);
                            point2.y = point2.y + deltaY;
                        }
                    }
                } else if (ballTouch3) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point3.x + deltaX) > 0 &&
                                ((point3.x + deltaX) < sprite3MaxX)
                                && (point3.y + deltaY) > 0 &&
                                ((point3.y + deltaY) < sprite3MaxY)) {

                            point3.x = point3.x + (deltaX);
                            point3.y = point3.y + deltaY;
                        }
                    }
                } else if (ballTouch4) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point4.x + deltaX) > 0 &&
                                ((point4.x + deltaX) < sprite4MaxX)
                                && (point4.y + deltaY) > 0 &&
                                ((point4.y + deltaY) < sprite4MaxY)) {

                            point4.x = point4.x + (deltaX);
                            point4.y = point4.y + deltaY;
                        }
                    }
                } else if (ballTouch5) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point5.x + deltaX) > 0 &&
                                ((point5.x + deltaX) < sprite5MaxX)
                                && (point5.y + deltaY) > 0 &&
                                ((point5.y + deltaY) < sprite5MaxY)) {

                            point5.x = point5.x + (deltaX);
                            point5.y = point5.y + deltaY;
                        }
                    }
                } else if (ballTouch6) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point6.x + deltaX) > 0 &&
                                ((point6.x + deltaX) < sprite6MaxX)
                                && (point6.y + deltaY) > 0 &&
                                ((point6.y + deltaY) < sprite6MaxY)) {

                            point6.x = point6.x + (deltaX);
                            point6.y = point6.y + deltaY;
                        }
                    }
                } else if (ballTouch7) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point7.x + deltaX) > 0 &&
                                ((point7.x + deltaX) < sprite7MaxX)
                                && (point7.y + deltaY) > 0 &&
                                ((point7.y + deltaY) < sprite7MaxY)) {

                            point7.x = point7.x + (deltaX);
                            point7.y = point7.y + deltaY;
                        }
                    }
                } else if (ballTouch8) {
                    if (distX > mTouchSlop || distY > mTouchSlop) {
                        int deltaX = (xTouch - preXTouch);
                        int deltaY = (yTouch - preYTouch);
                        if ((point8.x + deltaX) > 0 &&
                                ((point8.x + deltaX) < sprite8MaxX)
                                && (point8.y + deltaY) > 0 &&
                                ((point8.y + deltaY) < sprite8MaxY)) {

                            point8.x = point8.x + (deltaX);
                            point8.y = point8.y + deltaY;
                        }
                    }
                }
                preXTouch = xTouch;
                preYTouch = yTouch;
                break;
            case MotionEvent.ACTION_UP:
                ballTouch1 = false;
                ballTouch2 = false;
                ballTouch3 = false;
                ballTouch4 = false;
                break;
        }
        return true;
    }

    private Point getRandomVelocity() {
        Random r = new Random();
        int min = 1;
        int max = 5;
        int x = r.nextInt(max - min + 1) + min;
        int y = r.nextInt(max - min + 1) + min;
        return new Point(x, y);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        float f = sensorEvent.values[0];
        float f2 = sensorEvent.values[1];
        float f3 = sensorEvent.values[2];

        mGravity = sensorEvent.values.clone();
        // Shake detection
        Double x = Double.valueOf(mGravity[0]);
        Double y = Double.valueOf(mGravity[1]);
        Double z = Double.valueOf(mGravity[2]);
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        // Make this higher or lower according to how much
        // motion you want to detect
        Log.d("mAccel", mAccel + "");
        if (mAccel >= 0.01) {
            // do something
            //isSensorShowMoving = true;
        } else {
//            isSensorShowMoving = false;
        }

        if (isSensorShowMoving) {
            frame.removeCallbacks(frameUpdate);
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    }//end of onSensorChange

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }//end of onAccuracyChanged

    private Point getRandomPoint() {
        Random r = new Random();
        int minX = 0;
        int maxX = getWidth() -
                rectBall1.width();
        int x = 0;
        int minY = 0;
        int maxY = getHeight() -
                rectBall1.height();
        int y = 0;
        x = r.nextInt(maxX - minX + 1) + minX;
        y = r.nextInt(maxY - minY + 1) + minY;
        return new Point(x, y);
    }


    private Handler frame = new Handler();
    //Velocity includes the speed and the direction of our sprite motion
    private volatile Point sprite1Velocity;
    private volatile Point sprite2Velocity;
    private volatile Point sprite3Velocity;
    private volatile Point sprite4Velocity;
    private volatile Point sprite5Velocity;
    private volatile Point sprite6Velocity;
    private volatile Point sprite7Velocity;
    private Point sprite8Velocity;
    private volatile int sprite1MaxX;
    private volatile int sprite1MaxY;
    private volatile int sprite2MaxX;
    private volatile int sprite2MaxY;
    private volatile int sprite3MaxX;
    private volatile int sprite3MaxY;
    private volatile int sprite4MaxX;
    private volatile int sprite4MaxY;
    private volatile int sprite5MaxX;
    private volatile int sprite5MaxY;
    private volatile int sprite6MaxX;
    private volatile int sprite6MaxY;
    private volatile int sprite7MaxX;
    private volatile int sprite7MaxY;
    private volatile int sprite8MaxX;
    private volatile int sprite8MaxY;
    //Divide the frame by 1000 to calculate how many times per second the screen will update.
    private static final int FRAME_RATE = 10; //50 frames per second

    synchronized public void initGfx() {
        //Select two random points for our initial sprite placement.
        //The loop is just to make sure we don't accidentally pick
        //two points that overlap.
        Point p1, p2;
        p1 = new Point(128, 50);
        p2 = new Point(400, 5);
        point3 = new Point(7, 400);
        point4 = new Point(355, 400);
        point5 = new Point(77, 200);
        point6 = new Point(700, 300);
        point7 = new Point(7, 400);
        point8 = new Point(7, 400);

        setSprite1(p1.x, p1.y);
        setSprite2(p2.x, p2.y);
        //Give the asteroid a random velocity
        sprite1Velocity = getRandomVelocity();
        //Fix the ship velocity at a constant speed for now
        sprite2Velocity = getRandomVelocity();
        sprite3Velocity = getRandomVelocity();
        sprite4Velocity = getRandomVelocity();
        sprite5Velocity = getRandomVelocity();
        sprite6Velocity = getRandomVelocity();
        sprite7Velocity = getRandomVelocity();
        sprite8Velocity = getRandomVelocity();
        //Set our boundaries for the sprites
        sprite1MaxX = getWidth() -
                rectBall1.width();
        sprite1MaxY = getHeight() -
                rectBall1.height();
        sprite2MaxX = getWidth() -
                rectBall2.width();
        sprite2MaxY = getHeight() -
                rectBall2.height();


        sprite3MaxX = getWidth() -
                rectBall3.width();
        sprite3MaxY = getHeight() -
                rectBall3.height();
        sprite4MaxX = getWidth() -
                rectBall4.width();
        sprite4MaxY = getHeight() -
                rectBall4.height();

        sprite5MaxX = getWidth() -
                rectBall5.width();
        sprite5MaxY = getHeight() -
                rectBall5.height();
        sprite6MaxX = getWidth() -
                rectBall6.width();
        sprite6MaxY = getHeight() -
                rectBall6.height();

        sprite7MaxX = getWidth() -
                rectBall7.width();
        sprite7MaxY = getHeight() -
                rectBall7.height();
        sprite8MaxX = getWidth() -
                rectBall8.width();
        sprite8MaxY = getHeight() -
                rectBall8.height();


        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    private void setSprite2(int x, int y) {
        point2.x = x;
        point2.y = y;
    }

    private void setSprite1(int x, int y) {
        point1.x = x;
        point1.y = y;
    }

    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            frame.removeCallbacks(frameUpdate);
            //First get the current positions of both sprites
            Rect r1 = new Rect(point1.x, point1.y, point1.x
                    + rectBall1.width(), point1.y + rectBall1.height());
            Rect r2 = new Rect(point2.x, point2.y, point2.x +
                    rectBall2.width(), point2.y + rectBall2.height());
            Rect r3 = new Rect(point3.x, point3.y, point3.x
                    + rectBall3.width(), point3.y + rectBall3.height());
            Rect r4 = new Rect(point4.x, point4.y, point4.x +
                    rectBall4.width(), point4.y + rectBall4.height());
            Rect r5 = new Rect(point5.x, point5.y, point5.x
                    + rectBall5.width(), point5.y + rectBall5.height());
            Rect r6 = new Rect(point6.x, point6.y, point6.x +
                    rectBall6.width(), point6.y + rectBall6.height());
            Rect r7 = new Rect(point7.x, point7.y, point7.x
                    + rectBall7.width(), point7.y + rectBall7.height());
            Rect r8 = new Rect(point8.x, point8.y, point8.x +
                    rectBall8.width(), point8.y + rectBall8.height());
            boolean isCollide = checkForCollision(point1, point2, rectBall1, rectBall2, ball1, ball2);
            if (isCollide) {
                Point temp1Velocity = new Point(sprite1Velocity);
                sprite1Velocity.x = sprite2Velocity.x;
                sprite1Velocity.y = sprite2Velocity.y;
                sprite2Velocity = new Point(temp1Velocity);


//                if (r1.right > r2.left && ((r1.right - r2.left) / r1.width()) * 100 > 25 && r1.top < r2.bottom) {
//                    point1.x -= Math.ceil(r1.right - r2.left);
//                }
//                else {
//                    point2.x -= Math.ceil(r2.right - r1.left);
//                }
//                if (point1.y < point2.y) {
//                    point1.y -= Math.ceil(r1.bottom - r2.top);
//                } else {
//                    point2.y -= Math.ceil(r2.bottom - r1.top);
//                }
                if (sprite1Velocity.equals(sprite2Velocity)) {
                    sprite1Velocity.negate();
                }
            }
            isCollide = checkForCollision(point1, point3, rectBall1, rectBall3, ball1, ball3);
            if (isCollide) {
                Point temp1Velocity = new Point(sprite1Velocity);
                sprite1Velocity.x = sprite3Velocity.x;
                sprite1Velocity.y = sprite3Velocity.y;
                sprite3Velocity = new Point(temp1Velocity);
//                if (r1.right > r3.left && ((r1.right - r3.left) / r1.width()) * 100 > 25 && r1.top < r3.bottom) {
//                    point1.x -= Math.ceil(r1.right - r3.left);
//                }
//                else {
//                    point3.x -= Math.ceil(r3.right - r1.left);
//                }
//                if (point1.y < point3.y) {
//                    point1.y -= Math.ceil(r1.bottom - r3.top);
//                } else {
//                    point3.y -= Math.ceil(r3.bottom - r1.top);
//                }
                if (sprite1Velocity.equals(sprite3Velocity)) {
                    sprite1Velocity.negate();
                }
            }
            isCollide = checkForCollision(point1, point4, rectBall1, rectBall4, ball1, ball4);
            if (isCollide) {
                Point temp1Velocity = new Point(sprite1Velocity);
                sprite1Velocity.x = sprite4Velocity.x;
                sprite1Velocity.y = sprite4Velocity.y;
                sprite4Velocity = new Point(temp1Velocity);
//                if (r1.right > r4.left && ((r1.right - r4.left) / r1.width()) * 100 > 18 && r1.top < r4.bottom) {
//                    point1.x -= Math.ceil(r1.right - r4.left);
//                }
//                else {
//                    point4.x -= Math.ceil(r4.right - r1.left);
//                }
//                if (point1.y < point4.y) {
//                    point1.y -= Math.ceil(r1.bottom - r4.top);
//                } else {
//                    point2.y -= Math.ceil(r4.bottom - r1.top);
//                }
                if (sprite1Velocity.equals(sprite4Velocity)) {
                    sprite1Velocity.negate();
                }
            }


            isCollide = checkForCollision(point1, point5, rectBall1, rectBall5, ball1, ball6);
            if (isCollide) {
                Point temp1Velocity = new Point(sprite1Velocity);
                sprite1Velocity.x = sprite5Velocity.x;
                sprite1Velocity.y = sprite5Velocity.y;
                sprite5Velocity = new Point(temp1Velocity);
//                if (r1.right > r5.left && ((r1.right - r5.left) / r1.width()) * 100 > 25 && r1.top < r5.bottom) {
//                    point1.x -= Math.ceil(r1.right - r5.left);
//                }
//                else {
//                    point5.x -= Math.ceil(r5.right - r1.left);
//                }
//                if (point1.y < point5.y) {
//                    point1.y -= Math.ceil(r1.bottom - r5.top);
//                } else {
//                    point5.y -= Math.ceil(r5.bottom - r1.top);
//                }
                if (sprite1Velocity.equals(sprite5Velocity)) {
                    sprite1Velocity.negate();
                }
            }
            isCollide = checkForCollision(point1, point6, rectBall1, rectBall6, ball1, ball6);
            if (isCollide) {
                Point temp1Velocity = new Point(sprite1Velocity);
                sprite1Velocity.x = sprite6Velocity.x;
                sprite1Velocity.y = sprite6Velocity.y;
                sprite6Velocity = new Point(temp1Velocity);
                if (sprite1Velocity.equals(sprite6Velocity)) {
                    sprite1Velocity.negate();
                }
            }
            isCollide = checkForCollision(point1, point7, rectBall1, rectBall7, ball1, ball7);
            if (false && isCollide) {
                Point temp1Velocity = new Point(sprite1Velocity);
                sprite1Velocity.x = sprite7Velocity.x;
                sprite1Velocity.y = sprite7Velocity.y;
                sprite7Velocity = new Point(temp1Velocity);
                if (sprite1Velocity.equals(sprite7Velocity)) {
                    sprite1Velocity.negate();
                }
            }
            isCollide = checkForCollision(point1, point8, rectBall1, rectBall8, ball1, ball8);
            if (false && isCollide) {
                Point temp1Velocity = new Point(sprite1Velocity);
                sprite1Velocity.x = sprite8Velocity.x;
                sprite1Velocity.y = sprite8Velocity.y;
                sprite8Velocity = new Point(temp1Velocity);
                if (sprite1Velocity.equals(sprite8Velocity)) {
                    sprite1Velocity.negate();
                }
            }
            //*******Now for 2 ball
            isCollide = checkForCollision(point2, point3, rectBall2, rectBall3, ball2, ball3);
            if (isCollide) {
                Point temp2Velocity = new Point(sprite2Velocity);
                sprite2Velocity.x = sprite3Velocity.x;
                sprite2Velocity.y = sprite3Velocity.y;
                sprite3Velocity = new Point(temp2Velocity);
//                if (point2.x < point3.x) {
//                    point2.x -= Math.ceil(point3.x - point2.x);
//                } else {
//                    point3.x -= point2.x - point3.x;
//                }
//                if (point2.y < point3.y) {
//                    point2.y -= Math.ceil(point3.y - point2.y);
//                } else {
//                    point3.y -= point2.y - point3.y;
//                }
                if (sprite2Velocity.equals(sprite3Velocity)) {
                    sprite2Velocity.negate();
                }
            }
            isCollide = checkForCollision(point2, point4, rectBall2, rectBall4, ball2, ball4);
            if (isCollide) {
                Point temp2Velocity = new Point(sprite2Velocity);
                sprite2Velocity.x = sprite4Velocity.x;
                sprite2Velocity.y = sprite4Velocity.y;
                sprite4Velocity = new Point(temp2Velocity);
//                if (point2.x < point4.x) {
//                    point2.x -= Math.ceil(point4.x - point2.x);
//                } else {
//                    point4.x -= point2.x - point4.x;
//                }
//                if (point2.y < point4.y) {
//                    point2.y -= Math.ceil(point4.y - point2.y);
//                } else {
//                    point4.y -= point2.y - point4.y;
//                }
                if (sprite2Velocity.equals(sprite4Velocity)) {
                    sprite2Velocity.negate();
                }
            }
            isCollide = checkForCollision(point2, point5, rectBall2, rectBall5, ball2, ball5);
            if (isCollide) {
                Point temp2Velocity = new Point(sprite2Velocity);
                sprite2Velocity.x = sprite5Velocity.x;
                sprite2Velocity.y = sprite5Velocity.y;
                sprite5Velocity = new Point(temp2Velocity);
//                if (point2.x < point3.x) {
//                    point2.x -= Math.ceil(point5.x - point2.x);
//                } else {
//                    point5.x -= point2.x - point5.x;
//                }
//                if (point2.y < point3.y) {
//                    point2.y -= Math.ceil(point5.y - point2.y);
//                } else {
//                    point5.y -= point2.y - point5.y;
//                }
                if (sprite2Velocity.equals(sprite5Velocity)) {
                    sprite2Velocity.negate();
                }
            }
            isCollide = checkForCollision(point2, point6, rectBall2, rectBall6, ball2, ball6);
            if (isCollide) {
                Point temp2Velocity = new Point(sprite2Velocity);
                sprite2Velocity.x = sprite6Velocity.x;
                sprite2Velocity.y = sprite6Velocity.y;
                sprite6Velocity = new Point(temp2Velocity);
                if (sprite2Velocity.equals(sprite6Velocity)) {
                    sprite2Velocity.negate();
                }
            }
            isCollide = checkForCollision(point2, point7, rectBall2, rectBall7, ball2, ball7);
            if (false && isCollide) {
                Point temp2Velocity = new Point(sprite2Velocity);
                sprite2Velocity.x = sprite7Velocity.x;
                sprite2Velocity.y = sprite7Velocity.y;
                sprite7Velocity = new Point(temp2Velocity);
                if (sprite2Velocity.equals(sprite7Velocity)) {
                    sprite2Velocity.negate();
                }
            }
            isCollide = checkForCollision(point2, point8, rectBall2, rectBall8, ball2, ball8);
            if (false && isCollide) {
                Point temp2Velocity = new Point(sprite2Velocity);
                sprite2Velocity.x = sprite8Velocity.x;
                sprite2Velocity.y = sprite8Velocity.y;
                sprite8Velocity = new Point(temp2Velocity);
                if (sprite2Velocity.equals(sprite8Velocity)) {
                    sprite2Velocity.negate();
                }
            }

            //***************Ball 3
            isCollide = checkForCollision(point3, point4, rectBall3, rectBall4, ball3, ball4);
            if (isCollide) {
                Point temp3Velocity = new Point(sprite3Velocity);
                sprite3Velocity.x = sprite4Velocity.x;
                sprite3Velocity.y = sprite4Velocity.y;
                sprite4Velocity = new Point(temp3Velocity);
//                if (point3.x < point4.x) {
//                    point3.x -= Math.ceil(point4.x - point3.x);
//                } else {
//                    point4.x -= point3.x - point4.x;
//                }
//                if (point3.y < point4.y) {
//                    point3.y -= Math.ceil(point4.y - point3.y);
//                } else {
//                    point4.y -= point3.y - point4.y;
//                }
                if (sprite3Velocity.equals(sprite4Velocity)) {
                    sprite3Velocity.negate();
                }
            }
            isCollide = checkForCollision(point3, point5, rectBall3, rectBall5, ball3, ball5);
            if (isCollide) {
                Point temp3Velocity = new Point(sprite3Velocity);
                sprite3Velocity.x = sprite5Velocity.x;
                sprite3Velocity.y = sprite5Velocity.y;
                sprite5Velocity = new Point(temp3Velocity);
//                if (point3.x < point5.x) {
//                    point3.x -= Math.ceil(point5.x - point3.x);
//                } else {
//                    point5.x -= point3.x - point5.x;
//                }
//                if (point3.y < point5.y) {
//                    point3.y -= Math.ceil(point5.y - point3.y);
//                } else {
//                    point5.y -= point3.y - point5.y;
//                }
                if (sprite3Velocity.equals(sprite5Velocity)) {
                    sprite3Velocity.negate();
                }
            }
            isCollide = checkForCollision(point3, point6, rectBall3, rectBall6, ball3, ball6);
            if ( isCollide) {
                Point temp3Velocity = new Point(sprite3Velocity);
                sprite3Velocity.x = sprite6Velocity.x;
                sprite3Velocity.y = sprite6Velocity.y;
                sprite6Velocity = new Point(temp3Velocity);
                if (sprite3Velocity.equals(sprite6Velocity)) {
                    sprite3Velocity.negate();
                }
            }
            isCollide = checkForCollision(point3, point7, rectBall3, rectBall7, ball3, ball7);
            if (false && isCollide) {
                Point temp3Velocity = new Point(sprite3Velocity);
                sprite3Velocity.x = sprite7Velocity.x;
                sprite3Velocity.y = sprite7Velocity.y;
                sprite7Velocity = new Point(temp3Velocity);
                if (sprite3Velocity.equals(sprite7Velocity)) {
                    sprite3Velocity.negate();
                }
            }
            isCollide = checkForCollision(point3, point8, rectBall3, rectBall8, ball3, ball8);
            if (false && isCollide) {
                Point temp3Velocity = new Point(sprite3Velocity);
                sprite3Velocity.x = sprite8Velocity.x;
                sprite3Velocity.y = sprite8Velocity.y;
                sprite8Velocity = new Point(temp3Velocity);
                if (sprite3Velocity.equals(sprite8Velocity)) {
                    sprite3Velocity.negate();
                }
            }

            //**************Ball 4
            isCollide = checkForCollision(point4, point5, rectBall4, rectBall5, ball4, ball5);
            if (isCollide) {
                Point temp4Velocity = new Point(sprite4Velocity);
                sprite4Velocity.x = sprite5Velocity.x;
                sprite4Velocity.y = sprite5Velocity.y;
//                if (point4.x < point5.x) {
//                    point4.x -= Math.ceil(point5.x - point4.x);
//                } else {
//                    point5.x -= point4.x - point5.x;
//                }
//                if (point4.y < point5.y) {
//                    point4.y -= Math.ceil(point5.y - point4.y);
//                } else {
//                    point5.y -= point4.y - point5.y;
//                }
                sprite5Velocity = new Point(temp4Velocity);
                if (sprite4Velocity.equals(sprite5Velocity)) {
                    sprite4Velocity.negate();
                }
            }
            isCollide = checkForCollision(point4, point6, rectBall4, rectBall6, ball4, ball6);
            if ( isCollide) {
                Point temp4Velocity = new Point(sprite4Velocity);
                sprite4Velocity.x = sprite6Velocity.x;
                sprite4Velocity.y = sprite6Velocity.y;
                sprite6Velocity = new Point(temp4Velocity);
                if (sprite4Velocity.equals(sprite6Velocity)) {
                    sprite4Velocity.negate();
                }
            }
            isCollide = checkForCollision(point4, point7, rectBall4, rectBall7, ball4, ball7);
            if (false && isCollide) {
                Point temp4Velocity = new Point(sprite4Velocity);
                sprite4Velocity.x = sprite7Velocity.x;
                sprite4Velocity.y = sprite7Velocity.y;
                sprite7Velocity = new Point(temp4Velocity);
                if (sprite4Velocity.equals(sprite7Velocity)) {
                    sprite4Velocity.negate();
                }
            }
            isCollide = checkForCollision(point4, point8, rectBall4, rectBall8, ball4, ball8);
            if (false && isCollide) {
                Point temp4Velocity = new Point(sprite4Velocity);
                sprite4Velocity.x = sprite8Velocity.x;
                sprite4Velocity.y = sprite8Velocity.y;
                sprite8Velocity = new Point(temp4Velocity);
                if (sprite4Velocity.equals(sprite8Velocity)) {
                    sprite4Velocity.negate();
                }
            }
            //**********Ball 5
            isCollide = checkForCollision(point5, point6, rectBall5, rectBall6, ball5, ball6);
            if ( isCollide) {
                Point temp4Velocity = new Point(sprite5Velocity);
                sprite5Velocity.x = sprite6Velocity.x;
                sprite5Velocity.y = sprite6Velocity.y;
                sprite6Velocity = new Point(temp4Velocity);
                if (sprite5Velocity.equals(sprite6Velocity)) {
                    sprite5Velocity.negate();
                }
            }
            isCollide = checkForCollision(point5, point7, rectBall5, rectBall7, ball5, ball7);
            if (false && isCollide) {
                Point temp5Velocity = new Point(sprite5Velocity);
                sprite5Velocity.x = sprite7Velocity.x;
                sprite5Velocity.y = sprite7Velocity.y;
                sprite7Velocity = new Point(temp5Velocity);
                if (sprite5Velocity.equals(sprite7Velocity)) {
                    sprite5Velocity.negate();
                }
            }
            isCollide = checkForCollision(point5, point8, rectBall5, rectBall8, ball5, ball8);
            if (false && isCollide) {
                Point temp5Velocity = new Point(sprite5Velocity);
                sprite5Velocity.x = sprite8Velocity.x;
                sprite5Velocity.y = sprite8Velocity.y;
                sprite8Velocity = new Point(temp5Velocity);
                if (sprite5Velocity.equals(sprite8Velocity)) {
                    sprite5Velocity.negate();
                }
            }
            //*********Ball 6
            isCollide = checkForCollision(point6, point7, rectBall6, rectBall7, ball6, ball7);
            if (false && isCollide) {
                Point temp6Velocity = new Point(sprite6Velocity);
                sprite6Velocity.x = sprite7Velocity.x;
                sprite6Velocity.y = sprite7Velocity.y;
                sprite7Velocity = new Point(temp6Velocity);
                if (sprite6Velocity.equals(sprite7Velocity)) {
                    sprite6Velocity.negate();
                }
            }
            isCollide = checkForCollision(point6, point8, rectBall6, rectBall8, ball6, ball8);
            if (false && isCollide) {
                Point temp6Velocity = new Point(sprite6Velocity);
                sprite6Velocity.x = sprite8Velocity.x;
                sprite6Velocity.y = sprite8Velocity.y;
                sprite8Velocity = new Point(temp6Velocity);
                if (sprite6Velocity.equals(sprite8Velocity)) {
                    sprite6Velocity.negate();
                }
            }
            //**********Ball7

            isCollide = checkForCollision(point7, point8, rectBall7, rectBall8, ball7, ball8);
            if (false && isCollide) {
                Point temp7Velocity = new Point(sprite7Velocity);
                sprite7Velocity.x = sprite8Velocity.x;
                sprite7Velocity.y = sprite8Velocity.y;
                sprite8Velocity = new Point(temp7Velocity);
            }
            //*************************************
            Point sprite1 = new Point
                    (point1.x,
                            (point1.y));
            Point sprite2 = new Point
                    (point2.x, point2.y);
            //Now calc the new positions.
            //Note if we exceed a boundary the direction of the velocity gets reversed.
            sprite1.x = sprite1.x + sprite1Velocity.x;
            if (sprite1.x > sprite1MaxX) {
                sprite1Velocity.x *= -1;
                sprite1.x = sprite1MaxX - 1;
            }
            if (sprite1.x < 5) {
                sprite1Velocity.x *= -1;
                sprite1.x = 5;
            }
            sprite1.y = sprite1.y + sprite1Velocity.y;
            if (sprite1.y > sprite1MaxY) {
                sprite1Velocity.y *= -1;
                sprite1.y = sprite1MaxY - 1;
            }
            if (sprite1.y < 5) {
                sprite1Velocity.y *= -1;
                sprite1.y = 5;
            }
            sprite2.x = sprite2.x + sprite2Velocity.x;
            if (sprite2.x > sprite2MaxX) {
                sprite2Velocity.x *= -1;
                sprite2.x = sprite2MaxX - 1;
            }
            if (sprite2.x < 5) {
                sprite2Velocity.x *= -1;
                sprite2.x = 5;
            }
            sprite2.y = sprite2.y + sprite2Velocity.y;
            if (sprite2.y > sprite2MaxY) {
                sprite2Velocity.y *= -1;
                sprite2.y = sprite2MaxY - 1;
            }
            if (sprite2.y < 5) {
                sprite2Velocity.y *= -1;
                sprite2.y = 5;
            }

//after two
            point3.x = point3.x + sprite3Velocity.x;
            if (point3.x > sprite3MaxX) {
                sprite3Velocity.x *= -1;
                point3.x = sprite3MaxX - 1;
            }
            if (point3.x < 5) {
                sprite3Velocity.x *= -1;
                point3.x = 5;
            }
            point3.y = point3.y + sprite3Velocity.y;
            if (point3.y > sprite3MaxY) {
                sprite3Velocity.y *= -1;
                point3.y = sprite3MaxY - 1;
            }
            if (point3.y < 5) {
                sprite3Velocity.y *= -1;
                point3.y = 5;
            }

            point4.x = point4.x + sprite4Velocity.x;
            if (point4.x > sprite4MaxX) {
                sprite4Velocity.x *= -1;
                point4.x = sprite4MaxX - 1;
            }
            if (point4.x < 5) {
                sprite4Velocity.x *= -1;
                point4.x = 5;
            }
            point4.y = point4.y + sprite4Velocity.y;
            if (point4.y > sprite4MaxY) {
                sprite4Velocity.y *= -1;
                point4.y = sprite4MaxY - 1;
            }
            if (point4.y < 5) {
                sprite4Velocity.y *= -1;
                point4.y = 5;
            }

            point5.x = point5.x + sprite5Velocity.x;
            if (point5.x > sprite5MaxX) {
                sprite5Velocity.x *= -1;
                point5.x = sprite5MaxX - 1;
            }
            if (point5.x < 5) {
                sprite5Velocity.x *= -1;
                point5.x = 5;
            }
            point5.y = point5.y + sprite5Velocity.y;
            if (point5.y > sprite5MaxY) {
                sprite5Velocity.y *= -1;
                point5.y = sprite5MaxY;
            }
            if (point5.y < 5) {
                sprite5Velocity.y *= -1;
                point5.y = 5;
            }
            point6.x = point6.x + sprite6Velocity.x;
            if (point6.x > sprite6MaxX || point6.x < 5) {
                sprite6Velocity.x *= -1;
            }
            point6.y = point6.y + sprite6Velocity.y;
            if (point6.y > sprite6MaxX || point6.y < 5) {
                sprite6Velocity.y *= -1;
            }
            if (point6.x == 0) {
                point6.x = 1;
            }

            if (point6.y == 0) {
                point6.y = 1;
            }

            point7.x = point7.x + sprite7Velocity.x;
            if (point7.x > sprite7MaxX || point7.x < 5) {
                sprite7Velocity.x *= -1;
            }
            point7.y = point7.y + sprite7Velocity.y;
            if (point7.y > sprite7MaxY || point7.y < 5) {
                sprite7Velocity.y *= -1;
            }
            if (point7.x == 0) {
                point7.x = 1;
            }

            if (point7.y == 0) {
                point7.y = 1;
            }
            point8.x = point8.x + sprite8Velocity.x;
            if (point8.x > sprite8MaxX || point8.x < 5) {
                sprite8Velocity.x *= -1;
            }

            point8.y = point8.y + sprite8Velocity.y;
            if (point8.y > sprite8MaxX || point8.y < 5) {
                sprite8Velocity.y *= -1;
            }
            if (point8.x == 0) {
                point8.x = 1;
            }

            if (point8.y == 0) {
                point8.y = 1;
            }
            //********
            setSprite1(sprite1.x,
                    sprite1.y);
            setSprite2(sprite2.x, sprite2.y);
            invalidate();
            if (true || isSensorShowMoving) {
                frame.removeCallbacks(frameUpdate);
                frame.postDelayed(frameUpdate, FRAME_RATE);
            }//end of if
        }
    };//end of hanndler

    private void afterCollide() {

    }

    private boolean checkForCollision(Point sprite1, Point sprite2, Rect sprite1Bounds, Rect sprite2Bounds, Bitmap bm1, Bitmap bm2) {
        if (sprite1.x < 0 && sprite2.x < 0 && sprite1.y < 0 && sprite2.y < 0)
            return false;
        Rect r1 = new Rect(sprite1.x, sprite1.y, sprite1.x
                + sprite1Bounds.width(), sprite1.y + sprite1Bounds.height());
        Rect r2 = new Rect(sprite2.x, sprite2.y, sprite2.x +
                sprite2Bounds.width(), sprite2.y + sprite2Bounds.height());
        Rect r3 = new Rect(r1);
        if (r1.intersect(r2)) {

            return true;
        }

        lastCollision = new Point(-1, -1);
        return false;
    }

}//end of class
