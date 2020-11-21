package com.example.simongame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private final String FILENAME = "scoreboard.txt";
    private ArrayList<Player> playersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_screen);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        playersList = new ArrayList<Player>();

        FileInputStream fin = null;
        try {
            fin = openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            String tempGameData;
            while ((tempGameData = reader.readLine()) != null)
            {
                String[] values = tempGameData.split(",");
                Player player = new Player(values[0], values[1], values[2]);
                playersList.add(player);
            }
            //Sorting scoreboard (descending order)
            Collections.sort(playersList);
            Collections.reverse(playersList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        myRecyclerAdapter = new RecyclerAdapter(playersList, this);
        recyclerView.setAdapter(myRecyclerAdapter);

    }

    public void pressedClose(View view) {
        super.onBackPressed();
    }
}
