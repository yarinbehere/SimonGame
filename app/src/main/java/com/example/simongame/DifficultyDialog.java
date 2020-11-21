package com.example.simongame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class DifficultyDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener
{
    private SeekBar seekBar;
    private seekBarListener listener;

    public DifficultyDialog(){
    }

    static DifficultyDialog newInstance(int seekbarProgress)
    {
        DifficultyDialog settingsDialog = new DifficultyDialog();

        Bundle args = new Bundle();
        args.putInt("seekbarProgress", seekbarProgress);
        settingsDialog.setArguments(args);

        return settingsDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        int seekbarProgress = getArguments().getInt("seekbarProgress");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Seek bar settings
        View seekBarView = getActivity().getLayoutInflater().inflate(R.layout.seekbar, null);
        seekBar = (SeekBar) seekBarView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(seekbarProgress);
        listener = (seekBarListener) getActivity();

        builder.setView(seekBarView)
                .setTitle(R.string.difficultyDialogTitle)
                .setPositiveButton(R.string.posButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.OnChangeSeekBar(seekBar.getProgress());
                                dismiss();  //remove dialog from the screen
                            }
                        })
                .setNegativeButton(R.string.negButton,null);
        return builder.create();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar){
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public interface seekBarListener {
        public void OnChangeSeekBar(int seekBarPrecision);
    }

}
