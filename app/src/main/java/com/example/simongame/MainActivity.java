package com.example.simongame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private Button playButton;
    private Button scoreboardButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        final Intent startGame = new Intent(this, SimonActivity.class);
        final Intent scoreboardIntent = new Intent(this, ScoreboardActivity.class);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame);
            }
        });

        scoreboardButton = (Button) findViewById(R.id.scoreboardButton);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(scoreboardIntent);
            }
        });

        exitButton = (Button) findViewById(R.id.exitButton);
        final Context context = this;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { new AlertDialog.Builder(context)
                    .setTitle("Quit the game")
                    .setMessage("Are you sure?")
                    .setPositiveButton(R.string.posButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // Exit
                            System.exit(0);
                        }
                    })
                    .setNegativeButton(R.string.negButton, null)
                    .show();
            }
        });
    }//onCreate


}//MainActivity
