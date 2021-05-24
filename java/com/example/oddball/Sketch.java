package com.example.oddball;

import processing.core.PApplet;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Sketch extends PApplet
{
    Star[] stars = new Star[800];
    AppDataManager appDataManager = new AppDataManager();
    Ball ball = new Ball();
    MainActivity mainActivity = new MainActivity();

    Context context;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences settings;

    float speed = 0;
    int highscore = 0;

    public void setup()
    {
        size(width, height);

        context = this.getActivity().getApplicationContext();
        settings = context.getSharedPreferences(MyPREFERENCES, 0);
        loadScore();

        for(int i = 0; i < stars.length; i ++)
        {
        stars[i] = new Star();
        }
    }

    public void mouseReleased()
    {
        if(appDataManager.playBool == 1) {
            float d = dist(ball.ballX, ball.ballY, mouseX, mouseY);
            if (d < ball.ballRadius) {
                ball.fadeR = random(200, 255);
                ball.fadeG = random(200, 255);
                ball.fadeB = random(200, 255);
                ball.fadeA -= 2;

                if (ball.fadeA <= 20) {
                    ball.fadeA = 20;
                }

                ball.xSpeedBool = (int) (Math.floor(Math.random() * 2) + 1);
                if (ball.xSpeedBool == 1) {
                    ball.xSpeed += -1;
                } else {
                    ball.xSpeed += 1;
                }

                ball.ySpeedBool = (int) (Math.floor(Math.random() * 2) + 1);
                if (ball.ySpeedBool == 1) {
                    ball.ySpeed += -1;
                } else {
                    ball.ySpeed += 1;
                }

                println("[xSpeed: " + ball.xSpeed + "] - [ySpeed: " + ball.ySpeed + "]");
                appDataManager.score = appDataManager.score + 1;
                if (appDataManager.score >= appDataManager.highScore)
                {
                    appDataManager.highScore = appDataManager.score;
                    addScore("highscore", appDataManager.highScore);

                }
                if (speed >= 0.5f) {
                    speed += 0.2f;
                }
            } else {
                appDataManager.miss -= 1;
            }
        }
    }

    public void draw()
    {
        if(speed == 0)
        {
            speed = 0.5f;
        }

        background(0);
        translate(width / 2, height / 2);

        for(int i = 0; i < stars.length; i++)
        {
            stars[i].update();
            stars[i].show();
        }

        translate(-width / 2, -height / 2);

        if(appDataManager.miss > 0) {
            if (appDataManager.playBool == 0 && appDataManager.boardBool == 0) {

                appDataManager.score = appDataManager.score;

                //ball radius
                ball.ballRadius = 200;

                //rgb colours
                ball.fadeR  = 255;
                ball.fadeG = 255;
                ball.fadeB = 255;
                ball.fadeA = 255;

                speed = 0.5f;

                fill(100, 100, 100, 100);
                noStroke();
                rect(100, 100, (width / 2) - 150, height - 325, 50);
                rect(width / 2 + 50, 100, width/ 2 - 150 , height - 325, 50);
                fill(255);
                textSize(75);
                text("PLAY", (width / 4) - 80, height / 2);
                text("LEADERBOARD", ((width / 4) * 3) - 275, height / 2);

                menuClick();
            } else if (appDataManager.playBool == 1 && appDataManager.boardBool == 0) {

                ball.update();
                ball.edges();
                ball.show();

                fill(255);
                textSize(50);
                text("High Score: " + appDataManager.highScore, 25, 50);
                text("Score: " + appDataManager.score, 25, 100);
                text("Misses Left: " + appDataManager.miss, 25, 150);

                if (appDataManager.score >= 25) {
                    ball.noiseMod();
                }

                if (appDataManager.miss == 0)
                {
                    appDataManager.score = appDataManager.score;
                }
            } else if (appDataManager.playBool == 0 && appDataManager.boardBool == 1) {

                fill(255);
                textSize(75);
                text("LEADERBOARD", (width / 2) - 275, 75);
                text("Main Screen", (width / 2) - 275, height - 100);
                fill(100, 100, 100, 100);
                noStroke();
                rect(20, 95, width - 40, height - 375, 50);
                rect(20, height - 225, width - 40, 200, 50);

                fill(255);
                for (int i = 0, y = 105; i < 5; i++, y += 125) {
                    textSize(125);
                    text( + i, 40, y + 100);
                }

                if(mouseY > height - 250)
                {
                    appDataManager.playBool = 0;
                    appDataManager.boardBool = 0;
                    appDataManager.miss = 15;
                }

            }
        }
        else
        {
            fill(255);
            textSize(75);
            text("GAME OVER", (width / 2) - 275, 75);
            text("Final Score: " + appDataManager.score, (width / 2) - 275, 150);
            text("Main Screen", (width / 2) - 275, height - 100);
            fill(100, 100, 100, 100);
            noStroke();
            rect(20, height - 225, width - 40, 200, 50);

            if(mouseY > height - 250)
            {
                appDataManager.playBool = 0;
                appDataManager.boardBool = 0;
                appDataManager.miss = 15;
                appDataManager.score = 0;
            }
        }
    }

    public void loadScore() {//get the score

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        appDataManager.highScore = sharedPreferences.getInt("highscore", 0);
    }

    public void addScore(String key, int value){//save the score

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    @Override
    public void onPause()
    {//save the score
        super.onPause();
        SharedPreferences settings =this.getActivity(). getSharedPreferences(MyPREFERENCES,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putInt("highscore", appDataManager.highScore);
        editor.commit();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (settings != null)
        {
            highscore = settings.getInt("highscore", 0);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        SharedPreferences settings =this.getActivity(). getSharedPreferences(MyPREFERENCES,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putInt("highscore", appDataManager.highScore);
        editor.putInt("highscore", appDataManager.highScore);
        editor.commit();
    }

    public void menuClick()
    {
        if(mouseX > 100 && mouseX < (width / 2) - 150)
        {
            if(mouseY > 100 && mouseY < height - 250) {
                appDataManager.boardBool = 0;
                appDataManager.playBool = 1;
            }
            }
        if(mouseX > (width / 2) + 50 && mouseX < width - 100)
        {
            if(mouseY > 100 && mouseY < height - 250) {
                appDataManager.boardBool = 1;
                appDataManager.playBool = 0;
            }
        }

    }

    public class Star
    {
        public float x, y, z;
        public float pz;

        Star()
        {
            x = random(-width / 2, width / 2);
            y = random(-height / 2, height / 2);
            z = random(width / 2);
            pz = z;
        }

        public void update()
        {
            z = z - speed;

            if(z < 1)
            {
                z = width / 2;
                x = random(-width / 2, width / 2);
                y = random(-height / 2, height / 2);
                pz = z;
            }
        }

        public void show()
        {
            fill(255);
            noStroke();

            float sx = map(x / z, 0, 1, 0, width/2);
            float sy = map(y / z, 0, 1, 0, height/2);

            float r = map(z, 0, width/2, 16, 0);
            ellipse(sx, sy, r, r);

            float px = map(x / pz, 0, 1, 0, width/2);
            float py = map(y / pz, 0, 1, 0, height/2);

            pz = z;

            stroke(255);
            line(px, py, sx, sy);
        }
    }

    public class Ball
    {
        //ball start position
        public float ballX;
        public float ballY;

        //ball speed
        public double xSpeed;
        public double ySpeed;
        public int xSpeedBool;
        public int ySpeedBool;

        //ball radius
        public double ballRadius;

        //rgb colours
        public float fadeR;
        public float fadeG;
        public float fadeB;
        public float fadeA;

        public double xOff;
        public double xIncrement;

        public double angle;
        public double n;

        Ball()
        {
            ballX = 1000;
            ballY = 500;

            //ball speed
            xSpeed = 0;
            ySpeed = 0;
            xSpeedBool = 0;
            ySpeedBool = 0;

            //ball radius
            ballRadius = 200;

            //rgb colours
            fadeR  = 255;
            fadeG = 255;
            fadeB = 255;
            fadeA = 255;

            xOff = 0.01;
            xIncrement = 0.01;

            angle = random(-PI/4, PI/4);

            reset();
        }

        public void update()
        {
            ballX += xSpeed * 2;
            ballY += ySpeed * 2;
        }

        public void reset()
        {
            ballX = 1000;
            ballY = 500;
            xSpeed = (5 * Math.cos(angle));
            ySpeed = (5 * Math.sin(angle));

            if(random(1) < 0.5)
            {
                xSpeed *= -1;
            }
        }

        public void edges()
        {
            if(ballY < 0 || ballY > height)
            {
                ySpeed *= -1;
            }

            if(ballX < 0 || ballX > width )
            {
                xSpeed *= -1;
            }
        }

        public void show()
        {
            fill(fadeR, fadeG, fadeB, fadeA);
            noStroke();
            ellipse(ballX, ballY, (int) ballRadius, (int) ballRadius);
        }

        void noiseMod()
        {
            n = noise((float) xOff) * 350;
            ballRadius = n;
            xOff += xIncrement;

            if(ballRadius >= 200)
            {
                ballRadius = 200;
            }
        }
    }
}
