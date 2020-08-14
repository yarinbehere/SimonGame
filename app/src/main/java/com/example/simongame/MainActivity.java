package com.example.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    /*
    Attributes
     */
    final long clickTimer = 500;
    final int maximumTurns = 10;
    private ArrayList<Integer> gameColorOrder;
    private Boolean SimonTurn;
    private int currentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        final ImageButton redImageButton = (ImageButton) findViewById(R.id.redButton);
        redImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redImageButton.setImageResource(R.drawable.red_on);
                new CountDownTimer(clickTimer, 1) { // adjust the milli seconds here

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        redImageButton.setImageResource(R.drawable.red_off);
                    }
                }.start();
            }
        });

        final ImageButton yellowImageButton = (ImageButton) findViewById(R.id.yellowButton);
        yellowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowImageButton.setImageResource(R.drawable.yellow_on);
                new CountDownTimer(clickTimer, 1) { // adjust the milli seconds here

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        yellowImageButton.setImageResource(R.drawable.yellow_off);
                    }
                }.start();
            }
        });

        final ImageButton blueImageButton = (ImageButton) findViewById(R.id.blueButton);
        blueImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueImageButton.setImageResource(R.drawable.blue_on);
                new CountDownTimer(clickTimer, 1) { // adjust the milli seconds here

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        blueImageButton.setImageResource(R.drawable.blue_off);
                    }
                }.start();
            }
        });

        final ImageButton greenImageButton = (ImageButton) findViewById(R.id.greenButton);
        greenImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greenImageButton.setImageResource(R.drawable.green_on);
                new CountDownTimer(clickTimer, 1) { // adjust the milli seconds here

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        greenImageButton.setImageResource(R.drawable.green_off);
                    }
                }.start();
            }
        });

        generateColorOrder();
        this.SimonTurn = true;
        this.currentScore = 1;

        displaySimonColorsUntil(4);
    }


    void generateColorOrder(){
        this.gameColorOrder = new ArrayList<Integer>(this.maximumTurns);

        Random randColor = new Random();
        randColor.setSeed(System.currentTimeMillis());
        for(int i=0; i<this.maximumTurns; i++){
            Integer randomColor = randColor.nextInt(4);
            gameColorOrder.add(randomColor+1);
        }

        TextView orderText = (TextView) findViewById(R.id.gameOrderText);
        orderText.setText(gameColorOrder.toString());
    }


    void displaySimonColorsUntil(int score) {
        final List<Integer> colorOrderToScore = this.gameColorOrder.subList(0, score);
        TextView orderToScoreText = (TextView) findViewById(R.id.gameOrderToScoreText);
        orderToScoreText.setText(colorOrderToScore.toString());

        for (int i=0; i<score; i++){
            final int finalI = i;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    turnOnColor(Integer.valueOf(colorOrderToScore.get(finalI)));
                }
            }, 4000);   //5 seconds
        }
    }

    void turnOnColor(int colorNumber) {
        final ImageButton redImageButton = (ImageButton) findViewById(R.id.redButton);
        final ImageButton greenImageButton = (ImageButton) findViewById(R.id.greenButton);
        final ImageButton blueImageButton = (ImageButton) findViewById(R.id.blueButton);
        final ImageButton yellowImageButton = (ImageButton) findViewById(R.id.yellowButton);

        if (colorNumber == 1){
            redImageButton.setImageResource(R.drawable.red_on);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    redImageButton.setImageResource(R.drawable.red_off);
                }
            }, 2000);   //5 seconds
        }
        else if (colorNumber == 2){
            greenImageButton.setImageResource(R.drawable.green_on);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    greenImageButton.setImageResource(R.drawable.green_off);
                }
            }, 2000);   //5 seconds
        }
        else if (colorNumber == 3) {
            blueImageButton.setImageResource(R.drawable.blue_on);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    blueImageButton.setImageResource(R.drawable.blue_off);
                }
            }, 2000);   //5 seconds
        }
        else {
            yellowImageButton.setImageResource(R.drawable.yellow_on);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    yellowImageButton.setImageResource(R.drawable.yellow_off);
                }
            }, 2000);   //5 seconds
        }

    }

}
