package com.learnoset.musicplayerul;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private MediaPlayer mediaPlayer;
    private Button playButton,skipPrevious,skipNext,stopSong;
    private ImageView albumArtImageView;
    private TextView musikNameView,timeElapsedView,totalDurationView;
    private SeekBar seekBar;
    private int currentSongIndex = 0;
    private int[] songs = {R.raw.sawbones, R.raw.lost, R.raw.fakeid,R.raw.demetori,R.raw.s3rl,R.raw.porcelain};
    private int[] albumArt = {R.drawable.sawbones, R.drawable.lost, R.drawable.fake_id,R.drawable.demetori,R.drawable.mtc,R.drawable.miw};



    private String[] musikName = {"Qoiet - saw BONES","RIP Kenny - Lost", "Riton & Kah-Lo - Fake ID","Demetori-ネクロファンタジア","MTC-S3RL","Motionless In White - Porcelain"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);
        playButton = findViewById(R.id.play_button);
        skipNext = findViewById(R.id.skip_next);
        skipPrevious = findViewById(R.id.skip_previous);
        stopSong = findViewById(R.id.stop);
        albumArtImageView = findViewById(R.id.album_art);
        musikNameView = findViewById(R.id.musikName);
        albumArtImageView.setClipToOutline(true);
        seekBar = findViewById(R.id.seekBar);
        timeElapsedView = findViewById(R.id.time_elapsed);
        totalDurationView = findViewById(R.id.total_duration);
        albumArtImageView.setImageResource(albumArt[currentSongIndex]);
        musikNameView.setText(musikName[currentSongIndex]);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.pause();
                playButton.setText("Play");
                seekBar.setMax(mediaPlayer.getDuration());
                mHandler.postDelayed(mUpdateSeekBar, 100);
            }
        });




        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setText("Resume Music");
                } else {
                    mediaPlayer.start();
                    playButton.setText("Pause Music");
                }
            }
        });




        skipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSongIndex++;
                if (currentSongIndex == songs.length) {
                    currentSongIndex = 0;
                }

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(MainActivity.this, songs[currentSongIndex]);
                seekBar.setMax(mediaPlayer.getDuration());
                albumArtImageView.setImageResource(albumArt[currentSongIndex]);
                musikNameView.setText(musikName[currentSongIndex]);
                mediaPlayer.start();
                playButton.setText("Pause Music");
            }
        });

        skipPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSongIndex--;
                if (currentSongIndex < 0) {
                    currentSongIndex = songs.length - 1;
                }

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(MainActivity.this, songs[currentSongIndex]);
                seekBar.setMax(mediaPlayer.getDuration());
                albumArtImageView.setImageResource(albumArt[currentSongIndex]);
                musikNameView.setText(musikName[currentSongIndex]);
                mediaPlayer.start();
                playButton.setText("Pause Music");
            }
        });
        stopSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setText("Play");
                }else {
                    playButton.setText("Play");
                }
                mediaPlayer.seekTo(0);

            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
                playButton.setText("Pause Musik");
            }
        });

    }




    private Runnable mUpdateSeekBar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            int currentPosition = mediaPlayer.getCurrentPosition();
            int totalDuration = mediaPlayer.getDuration();
            timeElapsedView.setText(millisecondsToString(currentPosition));
            totalDurationView.setText(millisecondsToString(totalDuration));
            mHandler.postDelayed(this, 100);
        }
    };
    public static String millisecondsToString(int milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        mHandler.removeCallbacks(mUpdateSeekBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
