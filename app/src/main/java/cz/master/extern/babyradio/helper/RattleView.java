package cz.master.extern.babyradio.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cz.master.extern.babyradio.R;
import cz.master.extern.babyradio.ratioresolver.CalculateRatio;

public class RattleView extends View implements SensorEventListener {

    // Bounds of screen
    private int xMin = 0;
    private int yMin = 0;
    private int xMax;
    private int yMax;
    // Ball attributes 1
    private float ballRadius = 30;
    private float ballX1 = 50;
    private float ballY1 = 50;
    private float ballSpeedX1 = 7;
    private float ballSpeedY1 = 4;
    private float maxSpeed = 20;
    private RectF ballBounds1;
    private Paint ballColor1;
    // Ball Attribute 2
    private float ballX2 = 250;
    private float ballY2 = 50;
    private float ballSpeedX2 = 5;
    private float ballSpeedY2 = 3;
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
    }

    // When view is first created (or changed) set ball's X & Y max and position.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        xMax = w - 1;
        yMax = h - 1;
        if (scaledBackgroundBitmap == null && backgroundBitmap != null)
            scaledBackgroundBitmap = Bitmap.createBitmap(backgroundBitmap, 0, 0, xMax, yMax);

        if (scaledBall1 == null) {
            scaledBall1 = Bitmap.createBitmap(ball1, 0, 0, (int) (ball1.getWidth() * CalculateRatio.HEIGHT_RATIO), (int) (ball1.getHeight() * CalculateRatio.HEIGHT_RATIO));
        }

        ballRadius1 = (float) (0.5 * Math.sqrt(scaledBall1.getWidth() * scaledBall1.getWidth() + scaledBall1.getHeight() * scaledBall1.getHeight()));
        ballRadius2 = (float) (0.5 * Math.sqrt(ball2.getWidth() * ball2.getWidth() + ball2.getHeight() * ball2.getHeight()));

    }//end of onSizeChanged

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(scaledBackgroundBitmap, 0, 0, paintForBackground);

        canvas.drawBitmap(ball1, ballX1, ballY1, paintForBackground);
        canvas.drawBitmap(ball2, ballX2, ballY2, paintForBackground);
        // Draw ball
//        ballBounds1.set(ballX1 - ballRadius, ballY1 - ballRadius, ballX1 + ballRadius, ballY1 + ballRadius);
//        ballColor1.setColor(Color.GREEN);
//        canvas.drawOval(ballBounds1, ballColor1);
//
//        ballBounds2.set(ballX2 - ballRadius, ballY2 - ballRadius, ballX2 + ballRadius, ballY2 + ballRadius);
//        ballColor2.setColor(Color.RED);
//        canvas.drawOval(ballBounds2, ballColor2);

        updateBall1();
        updateBall2();

        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
        }
        invalidate();
    }

    public void updateBall1() {
        // ball x & y change based on ball speed
        ballX1 += ballSpeedX1;
        ballY1 += ballSpeedY1;

        // Detect Wall Collision on horizontal plane
        if (ballX1 + ballRadius1 > xMax) {
            ballSpeedX1 = -ballSpeedX1;
            ballX1 = xMax - ballRadius1;
            // enemyWallCollision();
        } else if (ballX1 - ballRadius1 < xMin) {
            ballSpeedX1 = -ballSpeedX1;
            ballX1 = xMin + ballRadius1;
            //  enemyWallCollision();
        }
        // Detect Wall Collision on vertical plane
        if (ballY1 + ballRadius1 > yMax) {
            ballSpeedY1 = -ballSpeedY1;
            ballY1 = yMax - ballRadius1;
            //enemyWallCollision();
        } else if (ballY1 - ballRadius1 < yMin) {
            ballSpeedY1 = -ballSpeedY1;
            ballY1 = yMin + ballRadius1;
            // enemyWallCollision();
        }
    }

    //******

    public void updateBall2() {
        // ball x & y change based on ball speed
        ballX2 += ballSpeedX2;
        ballY2 += ballSpeedY2;

        // Detect Wall Collision on horizontal plane
        if (ballX2 + ballRadius2 > xMax) {
            ballSpeedX2 = -ballSpeedX2;
            ballX2 = xMax - ballRadius2;
            // enemyWallCollision();
        } else if (ballX2 - ballRadius2 < xMin) {
            ballSpeedX2 = -ballSpeedX2;
            ballX2 = xMin + ballRadius2;
            //  enemyWallCollision();
        }
        // Detect Wall Collision on vertical plane
        if (ballY2 + ballRadius2 > yMax) {
            ballSpeedY2 = -ballSpeedY2;
            ballY2 = yMax - ballRadius2;
            //enemyWallCollision();
        } else if (ballY2 - ballRadius2 < yMin) {
            ballSpeedY2 = -ballSpeedY2;
            ballY2 = yMin + ballRadius2;
            // enemyWallCollision();
        }
        float diffX = ballX1 - ballX2;
        float diffY = ballY1 - ballY2;
        // Square root each difference squared
        float diff = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));
        if (diff <= (ballRadius1 + ballRadius2)) {

            if (ballX2 + ballRadius2 > ballX1) {
                ballSpeedX2 = -ballSpeedX2;
                ballSpeedX1 = -ballSpeedX2;
                // enemyWallCollision();
            } else if (ballX2 - ballRadius2 < ballX1) {
                ballSpeedX2 = -ballSpeedX2;
                ballSpeedX1 = -ballSpeedX2;
                //  enemyWallCollision();
            }
            // Detect Wall Collision on vertical plane
            if (ballY2 + ballRadius2 > ballY1) {
                ballSpeedY2 = -ballSpeedY2;
                ballSpeedY1 = -ballSpeedY2;
                //enemyWallCollision();
            } else if (ballY2 - ballRadius2 < ballY1) {
                ballSpeedY2 = -ballSpeedY2;
                ballSpeedY1 = -ballSpeedY2;
                // enemyWallCollision();
            }
        }//end of if
    }


    public void updateEnemy() {

        // Update position
        enemyX += enemySpeedX;
        enemyY += enemySpeedY;
        // Detect wall collision and react
        if (enemyX + enemyRadius > xMax) {
            enemySpeedX = -enemySpeedX;
            enemyX = xMax - enemyRadius;
            enemyWallCollision();
        } else if (enemyX - enemyRadius < xMin) {
            enemySpeedX = -enemySpeedX;
            enemyX = xMin + enemyRadius;
            enemyWallCollision();
        }
        if (enemyY + enemyRadius > yMax) {
            enemySpeedY = -enemySpeedY;
            enemyY = yMax - enemyRadius;
            enemyWallCollision();
        } else if (enemyY - enemyRadius < yMin) {
            enemySpeedY = -enemySpeedY;
            enemyY = yMin + enemyRadius;
            enemyWallCollision();
        }
        // Check for ball collisions: help ==> http://devmag.org.za/2009/04/13/basic-collision-detection-in-2d-part-1/
        // Calculate how far centers of circle are from one another
        float diffX = ballX1 - enemyX;
        float diffY = ballY1 - enemyY;
        // Square root each difference squared
        float diff = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));
        if (diff <= (ballRadius + enemyRadius)) {
            ballCollision(this);
        }
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        float currentX = event.getX();
//        float currentY = event.getY();
//        float deltaX, deltaY;
//        float scalingFactor = scalor / ((xMax > yMax) ? yMax : xMax);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                deltaX = currentX - previousX;
//                deltaY = currentY - previousY;
//                ballSpeedX1 += deltaX * scalingFactor;
//                ballSpeedY1 += deltaY * scalingFactor;
//        }
//        // reset to max speed if over
//        radarGun();
//
//        // Save current x, y
//        previousX = currentX;
//        previousY = currentY;
        return true;
    }

    public void ballCollision(View view) {
        pauseBalls();
        // Alert Dialog
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Crash!");
        alertDialog.setMessage("Pick yourself up grasshopper.").setCancelable(false);
        alertDialog.setPositiveButton("Restart...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//				Context context = getContext();
//				Intent intent = new Intent(context, Level1.class);
//				context.startActivity(intent);

            }
        });
        alertDialog.show();
    }

    public void enemyWallCollision() {
        scalor += .4;
        float increment = .2f;
        if (enemySpeedX >= 0) {
            enemySpeedX += increment;
        } else {
            enemySpeedX -= increment;
        }
        if (enemySpeedY >= 0) {
            enemySpeedX += increment;
        } else {
            enemySpeedY -= increment;
        }
    }

    public void radarGun() {
        if (ballSpeedX1 > 0) {
            ballSpeedX1 = ((ballSpeedX1 > maxSpeed) ? maxSpeed : ballSpeedX1);
        } else if (ballSpeedX1 < 0) {
            ballSpeedX1 = ((ballSpeedX1 < -maxSpeed) ? -maxSpeed : ballSpeedX1);
        }
        if (ballSpeedY1 > 0) {
            ballSpeedY1 = ((ballSpeedY1 > maxSpeed) ? maxSpeed : ballSpeedY1);
        } else if (ballSpeedX1 < 0) {
            ballSpeedY1 = ((ballSpeedY1 < -maxSpeed) ? -maxSpeed : ballSpeedY1);
        }
    }

    public void resetBalls() {
        ballRadius = 30;
        ballX1 = ballRadius;
        ballY1 = ballRadius;
        ballSpeedX1 = 3;
        ballSpeedY1 = 5;
        enemyRadius = ballRadius;
        enemyX = xMax / 3;
        enemyY = yMax / 3;
        enemySpeedX = 5;
        enemySpeedY = 3;
    }

    public void pauseBalls() {
        ballRadius = 0;
        ballX1 = ballRadius;
        ballY1 = ballRadius;
        ballSpeedX1 = 0;
        ballSpeedY1 = 0;
        enemyRadius = ballRadius;
        enemyX = 500;
        enemyY = 500;
        enemySpeedX = 0;
        enemySpeedY = 0;
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
}
