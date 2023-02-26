package com.example.spotify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class MusicPlay extends AppCompatActivity implements JsonResponse {

    ImageView i1;
    ImageButton pauseButton;
    SharedPreferences sh;
    MediaPlayer mediaPlayer;
    SeekBar musicSeekBar;
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_music_play);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        TextView t1 = findViewById(R.id.song);
        t1.setText(View_Random_Music.mus);
         i1 = findViewById(R.id.imageView);

        String pth = "http://"+IPSettings.text+"/"+View_Random_Music.img;
        pth = pth.replace("~", "");
//	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();

        Log.d("-------------", pth);
        Picasso.with(getApplicationContext())
                .load(pth)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background).into(i1);



         mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("http://"+IPSettings.text+"/"+View_Random_Music.song);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageButton playButton = findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        pauseButton = findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
            }
        });

        ImageButton repeatButton = findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });






         musicSeekBar = findViewById(R.id.musicSeekBar);
        musicSeekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // Start playback
                mediaPlayer.start();

                // Update progress bar
                musicSeekBar.setMax(mediaPlayer.getDuration());
                updateSeekBar();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Reset media player
                mediaPlayer.reset();

                // Stop update thread
                stopUpdateSeekBar();
            }
        });






        playButton.setVisibility(View.GONE);


        TextView play = findViewById(R.id.playlist);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Add_PlayList.class));
            }
        });
        ImageButton fav = findViewById(R.id.fav);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) MusicPlay.this;
                String q = "/add_fav?lid="+sh.getString("log_id","")+"&mid="+View_Random_Music.mid;
                q=q.replace(" ","%20");
                JR.execute(q);
            }
        });

    }

    private void updateSeekBar() {

        musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }

    }

    private void stopUpdateSeekBar() {
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void response(JSONObject jo) {
        try {

            String method=jo.getString("method");


            if(method.equalsIgnoreCase("add_fav")) {
                String status = jo.getString("status");
                Log.d("pearl", status);
                if(status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), " Added to Favourite", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MusicPlay.class));
                }else if(status.equalsIgnoreCase("duplicate")) {
                    Toast.makeText(getApplicationContext(), "This Song is Already in Favourite!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), View_Random_Music.class));
                }

            }

        }catch (Exception e)
        {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),View_Random_Music.class);
        startActivity(b);
    }

}