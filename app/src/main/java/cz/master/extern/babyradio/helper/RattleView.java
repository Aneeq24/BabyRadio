package cz.master.extern.babyradio.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.ratioresolver.CalculateRatio;

public class RattleView extends View implements SensorEventListener {

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

    Bitmap ball1, ball2, ball3, ball4, ball5;
    Bitmap scaledBall1, scaledBall2, scaledBall3, scaledBall4, scaledBall5;
    private float ballRadius1, ballRadius2, ballRadius3, ballRadius4, ballRadius5;

    private int sprite1Rotation = 0;
    public Rect rectBall1, rectBall2, rectBall3, rectBall4, rectBall5;
    public Point point1, point2, point3, point4, point5, point6, point7;
    public Matrix matrix1, matrix2, matrix3, matrix4;
    private Point lastCollision;

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
        ballRadius3 = (float) (0.5 * Math.sqrt(ball3.getWidth() * ball3.getWidth() + ball3.getHeight() * ball3.getHeight()));
        ballRadius4 = (float) (0.5 * Math.sqrt(ball4.getWidth() * ball4.getWidth() + ball4.getHeight() * ball4.getHeight()));
        ballRadius5 = (float) (0.5 * Math.sqrt(ball5.getWidth() * ball5.getWidth() + ball5.getHeight() * ball5.getHeight()));
        this.setFocusableInTouchMode(true);
        rectBall1 = new Rect(0, 0, ball1.getWidth(), ball1.getHeight());
        rectBall2 = new Rect(0, 0, ball2.getWidth(), ball2.getHeight());
        point1 = new Point(-1, -1);
        point2 = new Point(-1, -1);
        matrix1 = new Matrix();
    }//end of function

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
        if (scaledBall1 == null) {
            scaledBall1 = Bitmap.createBitmap(ball1, 0, 0, (int) (ball1.getWidth() * CalculateRatio.HEIGHT_RATIO), (int) (ball1.getHeight() * CalculateRatio.HEIGHT_RATIO));
        }
        if (scaledBall2 == null) {
            scaledBall2 = Bitmap.createBitmap(ball1, 0, 0, (int) (ball2.getWidth() * CalculateRatio.HEIGHT_RATIO), (int) (ball2.getHeight() * CalculateRatio.HEIGHT_RATIO));
        }
        ballRadius1 = ball1.getWidth();//float) (0.5 * Math.sqrt(scaledBall1.getWidth() * scaledBall1.getWidth() + scaledBall1.getHeight() * scaledBall1.getHeight()));
        ballRadius2 = ball2.getWidth();//(float) (0.5 * Math.sqrt(ball2.getWidth() * ball2.getWidth() + ball2.getHeight() * ball2.getHeight()));
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
        matrix1.reset();
        matrix1.postTranslate((float) (point1.x), (float) (point1.y));
        matrix1.postRotate(sprite1Rotation,
                (float) (point1.x + rectBall1.width() / 2.0),
                (float) (point1.y + rectBall1.width() / 2.0));
        canvas.drawBitmap(ball1, matrix1, null);

        sprite1Rotation += 3;
        canvas.drawBitmap(ball2, point2.x, point2.y, null);

    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
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


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float f = sensorEvent.values[0];
        float f2 = sensorEvent.values[1];
        float f3 = sensorEvent.values[2];
        Log.d("F:f2:f3", f + ":" + f2 + ":" + f3);
    }//end of onSensorChange

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }//end of onAccuracyChanged

    private Handler frame = new Handler();
    //Velocity includes the speed and the direction of our sprite motion
    private Point sprite1Velocity;
    private Point sprite2Velocity;
    private int sprite1MaxX;
    private int sprite1MaxY;
    private int sprite2MaxX;
    private int sprite2MaxY;
    //Divide the frame by 1000 to calculate how many times per second the screen will update.
    private static final int FRAME_RATE = 10; //50 frames per second

    synchronized public void initGfx() {
        //Select two random points for our initial sprite placement.
        //The loop is just to make sure we don't accidentally pick
        //two points that overlap.
        Point p1, p2;
        do {
            p1 = getRandomPoint();
            p2 = getRandomPoint();
        } while (Math.abs(p1.x - p2.x) <
                rectBall1.width());
        setSprite1(p1.x, p1.y);
        setSprite2(p2.x, p2.y);
        //Give the asteroid a random velocity
        sprite1Velocity = getRandomVelocity();
        //Fix the ship velocity at a constant speed for now
        sprite2Velocity = new Point(1, 1);
        //Set our boundaries for the sprites
        sprite1MaxX = getWidth() -
                rectBall1.width();
        sprite1MaxY = getHeight() -
                rectBall1.height();
        sprite2MaxX = getWidth() -
                rectBall2.width();
        sprite2MaxY = getHeight() -
                rectBall2.height();
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
            boolean isCollide = checkForCollision(point1, point2, rectBall1, rectBall2, ball1, ball2);
//

            Point sprite1 = new Point
                    (point1.x,
                            (point1.y));
            Point sprite2 = new Point
                    (point2.x, point2.y);
            //Now calc the new positions.
            //Note if we exceed a boundary the direction of the velocity gets reversed.
            sprite1.x = sprite1.x + sprite1Velocity.x;
            if (sprite1.x > sprite1MaxX || sprite1.x < 5) {
                sprite1Velocity.x *= -1;
            }
            sprite1.y = sprite1.y + sprite1Velocity.y;
            if (sprite1.y > sprite1MaxY || sprite1.y < 5) {
                sprite1Velocity.y *= -1;
            }
            sprite2.x = sprite2.x + sprite2Velocity.x;
            if (sprite2.x > sprite2MaxX || sprite2.x < 5) {
                sprite2Velocity.x *= -1;
            }
            sprite2.y = sprite2.y + sprite2Velocity.y;
            if (sprite2.y > sprite2MaxY || sprite2.y < 5) {
                sprite2Velocity.y *= -1;
            }
            if (isCollide) {
                sprite1.x *= -1;
                sprite2.x *= -1;
            }
            setSprite1(sprite1.x,
                    sprite1.y);
            setSprite2(sprite2.x, sprite2.y);
            invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
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
            for (int i = r1.left; i < r1.right; i++) {
                for (int j = r1.top; j < r1.bottom; j++) {
                    if (bm1.getPixel(i - r3.left, j - r3.top) !=
                            Color.TRANSPARENT) {
                        if (bm2.getPixel(i - r2.left, j - r2.top) !=
                                Color.TRANSPARENT) {
                            lastCollision = new Point(sprite2.x +
                                    i - r2.left, sprite2.y + j - r2.top);
                            return true;
                        }
                    }
                }
            }
        }
        lastCollision = new Point(-1, -1);
        return false;
    }

}//end of class
