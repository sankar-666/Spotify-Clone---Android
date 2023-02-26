package com.example.spotify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class View_Random_Music extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {


    ListView lv1;
    String [] music,path,lyrics,mus_id,value,image,category;
    public static String song,lyr,mid,search,img,mus;

    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_view_random_music);

        EditText e1 = findViewById(R.id.search);


        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                search = e1.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) View_Random_Music.this;
                String q = "/searchsong?search="+search;
                q=q.replace(" ","%20");
                JR.execute(q);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        lv1=(ListView)findViewById(R.id.music);
        lv1.setOnItemClickListener(this);

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) View_Random_Music.this;
        String q = "/viewsong";
        q=q.replace(" ","%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("viewsong")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                if(status.equalsIgnoreCase("success")){

                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");


                    music=new String[ja1.length()];
                    path=new String[ja1.length()];
                    lyrics=new String[ja1.length()];
                    mus_id=new String[ja1.length()];
                    image=new String[ja1.length()];
                    category=new String[ja1.length()];
                    value=new String[ja1.length()];




                    for(int i = 0;i<ja1.length();i++)
                    {



                        music[i]=ja1.getJSONObject(i).getString("music");
                        path[i]=ja1.getJSONObject(i).getString("path");
                        lyrics[i]=ja1.getJSONObject(i).getString("lyrics");
                        image[i]=ja1.getJSONObject(i).getString("image");
                        category[i]=ja1.getJSONObject(i).getString("category");
                        mus_id[i]=ja1.getJSONObject(i).getString("music_id");



                        value[i]="Music: "+music[i];




                    }
				Custimage clist=new Custimage(this,image,music,category);
				 lv1.setAdapter(clist);

//                    ArrayAdapter<String> ar= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                    lv1.setAdapter(ar);


                }


            }

//            if(method.equalsIgnoreCase("add_fav")) {
//                String status = jo.getString("status");
//                Log.d("pearl", status);
//                if(status.equalsIgnoreCase("success")) {
//                    Toast.makeText(getApplicationContext(), " Added to Favourite", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), View_Random_Music.class));
//                }else if(status.equalsIgnoreCase("duplicate")) {
//                    Toast.makeText(getApplicationContext(), "This Song is Already in Favourite!", Toast.LENGTH_SHORT).show();
////                    startActivity(new Intent(getApplicationContext(), View_Random_Music.class));
//                }
//
//            }

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
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mid=mus_id[i];
        song=path[i];
        lyr=lyrics[i];
        img=image[i];
        mus=music[i];

        startActivity(new Intent(getApplicationContext(), MusicPlay.class));

//        final CharSequence[] items = {"Play", "Add to Playlist", "Add to Favourite"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(View_Random_Music.this);
//        // builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (items[item].equals("Play")) {
//
//                    MediaPlayer mediaPlayer = new MediaPlayer();
//
//                    try {
//                        mediaPlayer.reset();
//                        mediaPlayer.setDataSource("http://"+IPSettings.text+"/"+song);
//                        Toast.makeText(getApplicationContext(), "http://"+IPSettings.text+"/api"+song, Toast.LENGTH_SHORT).show();
//                        mediaPlayer.prepare();
//                        mediaPlayer.start();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//
//
//                } else if (items[item].equals("Add to Playlist")) {
//
//
//                } else if (items[item].equals("Add to Favourite")) {
//
//
//
//
//                }
//
//            }
//
//        });
//        builder.show();
    }
}