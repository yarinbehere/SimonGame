package com.example.simongame;

import android.widget.ImageButton;

public class GameButton
{
    ImageButton button;
    int buttonOnResource;
    int buttonOffResource;
    int buttonSound;

    public GameButton(ImageButton button, int buttonOffResource, int buttonOnResource, int soundResource)
    {
        this.button = button;
        this.buttonOnResource = buttonOnResource;
        this.buttonOffResource = buttonOffResource;
        this.buttonSound = soundResource;
    }

}
