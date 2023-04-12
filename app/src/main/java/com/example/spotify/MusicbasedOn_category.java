package com.example.spotify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MusicbasedOn_category extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView lv1;
    String [] music,path,lyrics,mus_id,value,image,category;
    public static String song,lyr,mid,img,mus;

    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_musicbased_on_category);

        TextView t1 = findViewById(R.id.cathead);
        t1.setText("Songs Based on\t"+UserHome.catg);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        lv1=(ListView)findViewById(R.id.music);
        lv1.setOnItemClickListener(this);

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) MusicbasedOn_category.this;
        String q = "/songs_basedon_catg?catid="+UserHome.cid;
        q=q.replace(" ","%20");
        JR.execute(q);
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("songs_basedon_catg")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                if(status.equalsIgnoreCase("success")){

                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");


                    music=new String[ja1.length()];
                    path=new String[ja1.length()];
                    lyrics=new String[ja1.length()];
                    mus_id=new String[ja1.length()];
                    value=new String[ja1.length()];
                    image=new String[ja1.length()];
                    category=new String[ja1.length()];



                    for(int i = 0;i<ja1.length();i++)
                    {



                        music[i]=ja1.getJSONObject(i).getString("music");
                        path[i]=ja1.getJSONObject(i).getString("path");
                        lyrics[i]=ja1.getJSONObject(i).getString("lyrics");
                        mus_id[i]=ja1.getJSONObject(i).getString("music_id");
                        image[i]=ja1.getJSONObject(i).getString("image");
                        category[i]=ja1.getJSONObject(i).getString("category");



                        value[i]="Music: "+music[i];




                    }
//				Custimage clist=new Custimage(this,photo);
//				 lv1.setAdapter(clist);

//                    ArrayAdapter<String> ar= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                    lv1.setAdapter(ar);

                    Custimage clist=new Custimage(this,image,music,category);
                    lv1.setAdapter(clist);


                }

                else {
                    Toast.makeText(getApplicationContext(), "No Songs Available in this Category!", Toast.LENGTH_SHORT).show();

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

        startActivity(new Intent(getApplicationContext(), MusicPlayCatg.class));

//        final CharSequence[] items = {"Play", "Lyrics"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MusicbasedOn_category.this);
//        // builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (items[item].equals("Play")) {
//
//
//                } else if (items[item].equals("Lyrics")) {
//                    startActivity(new Intent(getApplicationContext(), Music_Lyrics.class));
//
//                }
//
//            }
//
//        });
//        builder.show();
    }
}