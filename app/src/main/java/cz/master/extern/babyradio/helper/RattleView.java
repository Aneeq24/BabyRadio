package cz.master.extern.babyradio.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
    private float ballSpeedX1 = 5;
    private float ballSpeedY1 = 3;
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

    public RattleView(Context context) {
        super(context);
        // Initialize game elements
        this.context = context;
        enemyBallBounds = new RectF();

        ballBounds1 = new RectF();
        ballColor1 = new Paint();
        enemyColor = new Paint();

        ballBounds2 = new RectF();
        ballColor2 = new Paint();
        this.setFocusableInTouchMode(true);

    }

    // When view is first created (or changed) set ball's X & Y max and position.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        xMax = w - 1;
        yMax = h - 1;

    }

    public void onDraw(Canvas canvas) {
        // Draw ball
        ballBounds1.set(ballX1 - ballRadius, ballY1 - ballRadius, ballX1 + ballRadius, ballY1 + ballRadius);
        ballColor1.setColor(Color.GREEN);
        canvas.drawOval(ballBounds1, ballColor1);

        ballBounds2.set(ballX2 - ballRadius, ballY2 - ballRadius, ballX2 + ballRadius, ballY2 + ballRadius);
        ballColor2.setColor(Color.RED);
        canvas.drawOval(ballBounds2, ballColor2);
        // Draw enemy
//        enemyBallBounds.set(enemyX - enemyRadius, enemyY - enemyRadius, enemyX + enemyRadius, enemyY + enemyRadius);
//        enemyColor.setColor(Color.RED);
//        canvas.drawOval(enemyBallBounds, enemyColor);
        // Perform position calculations
        updateBall1();
        updateBall2();
        //updateEnemy();
        // Delay for the old human eyes to catch up
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
        if (ballX1 + ballRadius > xMax) {
            ballSpeedX1 = -ballSpeedX1;
            ballX1 = xMax - ballRadius;
            // enemyWallCollision();
        } else if (ballX1 - ballRadius < xMin) {
            ballSpeedX1 = -ballSpeedX1;
            ballX1 = xMin + ballRadius;
            //  enemyWallCollision();
        }
        // Detect Wall Collision on vertical plane
        if (ballY1 + ballRadius > yMax) {
            ballSpeedY1 = -ballSpeedY1;
            ballY1 = yMax - ballRadius;
            //enemyWallCollision();
        } else if (ballY1 - ballRadius < yMin) {
            ballSpeedY1 = -ballSpeedY1;
            ballY1 = yMin + ballRadius;
            // enemyWallCollision();
        }
    }

    //******

    public void updateBall2() {
        // ball x & y change based on ball speed
        ballX2 += ballSpeedX2;
        ballY2 += ballSpeedY2;

        // Detect Wall Collision on horizontal plane
        if (ballX2 + ballRadius > xMax) {
            ballSpeedX2 = -ballSpeedX2;
            ballX2 = xMax - ballRadius;
            // enemyWallCollision();
        } else if (ballX2 - ballRadius < xMin) {
            ballSpeedX2 = -ballSpeedX2;
            ballX2 = xMin + ballRadius;
            //  enemyWallCollision();
        }
        // Detect Wall Collision on vertical plane
        if (ballY2 + ballRadius > yMax) {
            ballSpeedY2 = -ballSpeedY2;
            ballY2 = yMax - ballRadius;
            //enemyWallCollision();
        } else if (ballY2 - ballRadius < yMin) {
            ballSpeedY2 = -ballSpeedY2;
            ballY2 = yMin + ballRadius;
            // enemyWallCollision();
        }
        float diffX = ballX1 - ballX2;
        float diffY = ballY1 - ballY2;
        // Square root each difference squared
        float diff = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));
        if (diff <= (ballRadius + ballRadius)) {

            if (ballX2 + ballRadius > ballX1) {
                ballSpeedX2 = -ballSpeedX2;
                ballX2 = (-ballX1 - ballRadius);
                // enemyWallCollision();
            } else if (ballX2 - ballRadius < ballX1) {
                ballSpeedX2 = -ballSpeedX2;
                ballX2 = (-ballX1 + ballRadius);
                //  enemyWallCollision();
            }
            // Detect Wall Collision on vertical plane
            if (ballY2 + ballRadius > ballY1) {
                ballSpeedY2 = -ballSpeedY2;
                ballY2 = (-ballY1 - ballRadius);
                //enemyWallCollision();
            } else if (ballY2 - ballRadius < ballY1) {
                ballSpeedY2 = -ballSpeedY2;
                ballY2 = (-ballY1 + ballRadius);
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
