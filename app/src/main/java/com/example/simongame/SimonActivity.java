package com.example.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class SimonActivity extends AppCompatActivity implements View.OnClickListener, DifficultyDialog.seekBarListener{

    private final int NUM_BUTTONS = 4;
    //Game difficulty
    private final int EASY_DIFFICULTY = 0;
    private final int MEDIUM_DIFFICULTY = 1;
    private final int HARD_DIFFICULTY = 2;
    //Button click animation delay in mSec
    private final int PLAYER_EFFECT_DELAY = 0;
    private final int SIMON_EASY_EFFECT_DELAY = 2000;
    private final int SIMON_MEDIUM_EFFECT_DELAY = 1000;
    private final int SIMON_HARD_EFFECT_DELAY = 500;
    //Turn String
    private final String SIMON_TURN = "Simon's Turn";
    private final String PLAYER_TURN = "Your Turn";
    private final String GAME_OVER = "Game Over!";
    private final String FILENAME = "scoreboard.txt";

    private SoundPool sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    private int gameLostSound;
    Random random = new Random();
    final Handler handler = new Handler();
    Intent intent;
    String nickname;
    private LowBatteryReceiver lowBatteryReceiver;
    GameButton[] arrGameButton;
    ArrayList<Integer> arrSimonMoves = new ArrayList<Integer>();
    ArrayList<Integer> arrPlayerMoves = new ArrayList<Integer>();
    TextView turnIndicatorText;
    TextView simonMovesText;
    TextView playerMovesText;
    TextView scoreText;

    private final int[] arrClickDelay = {SIMON_EASY_EFFECT_DELAY, SIMON_MEDIUM_EFFECT_DELAY, SIMON_HARD_EFFECT_DELAY};
    private int score = 0;
    private int gameDifficulty = HARD_DIFFICULTY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        turnIndicatorText = (TextView) findViewById(R.id.turnText);
        simonMovesText = (TextView) findViewById(R.id.simonMovesText);
        playerMovesText = (TextView) findViewById(R.id.playerMovesText);
        scoreText = (TextView) findViewById(R.id.scoreText);
        //Load game lost sound
        gameLostSound = sp.load(this, R.raw.lose, 1);
        intent = new Intent(this,NotifyIdleService.class);
        //Initialize game buttons and set onClick listener
        arrGameButton = new GameButton[NUM_BUTTONS];
        initGameButtons();
        for(int i = 0; i < NUM_BUTTONS; i++)
        {
            arrGameButton[i].button.setOnClickListener(this);
        }

        /***
         * Load Difficulty from SP
         */
        SharedPreferences sharedPreferences = getSharedPreferences("gameDifficulty", Context.MODE_PRIVATE);
        int loadedDifficulty= sharedPreferences.getInt("DIFFICULTY", 1);
        this.gameDifficulty = loadedDifficulty;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registerLowBatteryReceiver();
        playSimonTurn();
    }//onCreate

    @Override
    protected void onPause() {
        super.onPause();
        startService(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(lowBatteryReceiver);
        stopService(intent);
    }


    private void registerLowBatteryReceiver()
    {
        lowBatteryReceiver = new LowBatteryReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.content.Intent.ACTION_BATTERY_LOW);
        registerReceiver(lowBatteryReceiver, filter);
    }

    /**
     * The function initialises game buttons array
     * For each button in array the function stores the following information:
     *      View (ImageButton)
     *      Image resource for button ON state
     *      Image resource for button OFF state
     *      Sound resource
     */
    private void initGameButtons()
    {
        ImageButton tempImageButton;

        //Red
        tempImageButton = (ImageButton) findViewById(R.id.redButton);
        arrGameButton[0] = new GameButton(tempImageButton, R.drawable.red_off, R.drawable.red_on, R.raw.do_note);
        //Green
        tempImageButton = (ImageButton) findViewById(R.id.greenButton);
        arrGameButton[1] = new GameButton(tempImageButton, R.drawable.green_off, R.drawable.green_on, R.raw.re);
        //Blue
        tempImageButton = (ImageButton) findViewById(R.id.blueButton);
        arrGameButton[2] = new GameButton(tempImageButton, R.drawable.blue_off, R.drawable.blue_on, R.raw.mi);
        //Yellow
        tempImageButton = (ImageButton) findViewById(R.id.yellowButton);
        arrGameButton[3] = new GameButton(tempImageButton, R.drawable.yellow_off, R.drawable.yellow_on, R.raw.fa);

    }//initGameButtons

    @Override
    public void onClick(View v) {

        int buttonID = NUM_BUTTONS;
        int playerMove = 0;

        switch (v.getId())
        {
            case R.id.redButton:
                buttonID = 0;
                break;

            case R.id.greenButton:
                buttonID = 1;
                break;

            case R.id.blueButton:
                buttonID = 2;
                break;

            case R.id.yellowButton:
                buttonID = 3;
                break;
        }//switch

        arrPlayerMoves.add(buttonID);
        playerMovesText.setText(arrPlayerMoves.toString());
        playerMove = arrPlayerMoves.size() - 1;

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                playSimonTurn();
            }
        };

        // If the player clicked the wrong button
        if(arrSimonMoves.get(playerMove) != buttonID)
        {
            turnIndicatorText.setText(GAME_OVER);
            sp.play(gameLostSound, 1, 1, 1, 0, 1f);
            final View nicknameView = getLayoutInflater().inflate(R.layout.nickname, null);
            new AlertDialog.Builder(this)
                    .setTitle("Game Over!")
                    .setMessage("Your score is:\n" + String.valueOf(score))
                    .setView(nicknameView)
                    .setPositiveButton(R.string.posButton, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editTextNickname = (EditText) nicknameView.findViewById(R.id.editTextNickname);
                            nickname = editTextNickname.getText().toString();
//                            nickname = "Jane";
                            saveScoreBoard();
                            finish();
                        }
                    })
                    .show();
        }
        // If the player clicked the correct button
        else
        {
            executeClickEffects(buttonID, PLAYER_EFFECT_DELAY);
            //If the player repeated full sequence - Simon's turn
            if(arrPlayerMoves.size() == arrSimonMoves.size())
            {
                score++ ;
                scoreText.setText(String.valueOf(score));
                arrPlayerMoves.clear();
                playerMovesText.setText("clear");
                handler.postDelayed(runnable, 1000);
            }
        }

    }//onClick

    public void saveScoreBoard()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        try {
            final String separator = System.getProperty("line.separator");
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write( String.valueOf(score) + "," + nickname + "," + dtf.format(now) + separator);
            writer.close();
            fos.close();
        } catch (Exception e) {
            Toast.makeText(this, "Failed saving to file", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This function executes Simon moves
     * Set indication text for Simon's turn
     * Generate new move
     * Click on buttons according to Simon's moves sequence
     * Set indication text for player's turn
     */
    private void playSimonTurn()
    {
        int msClickDelay = arrClickDelay[gameDifficulty];

        turnIndicatorText.setText(SIMON_TURN);
        setClickableGameButtons(false);
        appendNextMove();
        simonMovesText.setText(arrSimonMoves.toString()); 

        //Simon clicks buttons with growing delay
        for(int index = 0; index < arrSimonMoves.size(); index++)
        {
            msClickDelay = arrClickDelay[gameDifficulty] * (index + 1);
            executeClickEffects(arrSimonMoves.get(index), msClickDelay);
        }

        //Set text for player's turn after Simon is done playing
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                turnIndicatorText.setText(PLAYER_TURN);
                setClickableGameButtons(true);
            }
        };
        handler.postDelayed(runnable, msClickDelay + 500);
    }//playSymonTurn

    /**
     * This function sets all game buttons clickable attribute to @clickable value
     * @param clickable
     */
    private void setClickableGameButtons(boolean clickable)
    {
        for(int i = 0; i < NUM_BUTTONS; i++)
        {
            arrGameButton[i].button.setClickable(clickable);
        }
    }

    /**
     * Generate next random Simon move and append to moves array
     */
    private void appendNextMove()
    {
        //Generate random number between 0 and 3
        int randomButton = random.nextInt(NUM_BUTTONS * 10) % NUM_BUTTONS;
        //Append new move to Simon moves array
        arrSimonMoves.add(randomButton);
    }


    /**
     * Play button sound
     * Light the pressed button, then dimmer
     * @param buttonID - button index
     */
    private void executeClickEffects(final int buttonID, final int msDelay)
    {
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                playButtonSound(buttonID);
                animateButtonClick(buttonID);
            }
        };

        handler.postDelayed(runnable, msDelay);
    }//executeClickEffect

    /**
     * Play button sound according to button index
     * @param buttonID - button index
     */
    private void playButtonSound(final int buttonID)
    {
        MediaPlayer player = MediaPlayer.create(this, arrGameButton[buttonID].buttonSound);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        player.start();
    }//playButtonSound

    /**
     * Animate button click:
     * Light a button, then dimmer
     * @param buttonID - button ID to animate click
     */
    private void animateButtonClick(final int buttonID)
    {
        arrGameButton[buttonID].button.setImageResource( arrGameButton[buttonID].buttonOnResource);
        final Runnable runnable = new Runnable() {
            public void run() {
                arrGameButton[buttonID].button.setImageResource( arrGameButton[buttonID].buttonOffResource);
            }
        };
        handler.postDelayed(runnable, 300);
    }//animateButtonClick


    @Override
    public void OnChangeSeekBar(int seekBarPrecision)
    {
        this.gameDifficulty = seekBarPrecision;

        /***
         * Save SP
         */
        SharedPreferences sharedPreferences = getSharedPreferences("gameDifficulty", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("DIFFICULTY", this.gameDifficulty); // 1 = Easy, 2 = Normal, 3 = Hard
        editor.apply();
    }//OnChangeSeekBar


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_exit:
                menuActionExit();
                return true;
            case R.id.action_difficulty:
                DialogFragment difficultyDialog = DifficultyDialog.newInstance(this.gameDifficulty);
                difficultyDialog.show(getFragmentManager(),"difficultyDialog");
                return true;
            case R.id.action_scoreboard:
                final Intent scoreboardIntent = new Intent(this, ScoreboardActivity.class);
                startActivity(scoreboardIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // User clicks exit from menu
    private void menuActionExit()
    {
        new AlertDialog.Builder(this)
                .setTitle("Quit the game")
                .setMessage("Your score will not be saved. \nAre you sure?")
                .setPositiveButton(R.string.posButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.negButton, null)
                .show();

    }

}//SimonActivity

