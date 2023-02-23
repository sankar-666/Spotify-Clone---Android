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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Add_PlayList extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView lv1;
    String [] music,path,name,play_id,value;
    public static String song,lyr,pid,playlist;

    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_add_play_list);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        EditText e1 = findViewById(R.id.search);
        Button b1 = findViewById(R.id.main);

        lv1=(ListView)findViewById(R.id.cat);
        lv1.setOnItemClickListener(this);

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Add_PlayList.this;
        String q = "/viewplaylist?lid="+sh.getString("log_id","");
        q=q.replace(" ","%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Th"+sh.getString("log_id","")+playlist, Toast.LENGTH_SHORT).show();
                playlist = e1.getText().toString();

                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse) Add_PlayList.this;
                String q = "/addplaylist?lid="+sh.getString("log_id","")+"&name="+playlist;
                q=q.replace(" ","%20");
                JR.execute(q);



            }
        });
    }

    @Override
    public void response(JSONObject jo) {

        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("viewplaylist")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                if(status.equalsIgnoreCase("success")){

                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");


                    name=new String[ja1.length()];
                    play_id=new String[ja1.length()];

                    value=new String[ja1.length()];




                    for(int i = 0;i<ja1.length();i++)
                    {



                        name[i]=ja1.getJSONObject(i).getString("name");
                        play_id[i]=ja1.getJSONObject(i).getString("playlist_id");


                        value[i]="Music: "+name[i];




                    }
				CustomPlaylist clist=new CustomPlaylist(this,name);
				 lv1.setAdapter(clist);

//                    ArrayAdapter<String> ar= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                    lv1.setAdapter(ar);


                }


            }

            if(method.equalsIgnoreCase("addplaylist")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                if(status.equalsIgnoreCase("success")) {
                    startActivity(new Intent(getApplicationContext(), Add_PlayList.class));
                }else if(status.equalsIgnoreCase("duplicate")) {
                    Toast.makeText(getApplicationContext(), "This Playlist Already Exist!", Toast.LENGTH_SHORT).show();
                }
                }

            if(method.equalsIgnoreCase("add_songs_toplaylist")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                if(status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Successfully Added to Playlist", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), View_Random_Music.class));
                }else if(status.equalsIgnoreCase("duplicate")) {
                    Toast.makeText(getApplicationContext(), "This Song is Already Added!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), View_Random_Music.class));
                }
                }

        }catch (Exception e)
        {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pid=play_id[i];

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) Add_PlayList.this;
        String q = "/add_songs_toplaylist?pid="+pid+"&mid="+View_Random_Music.mid;
        q=q.replace(" ","%20");
        JR.execute(q);


//        startActivity(new Intent(getApplicationContext(), Add_PlayList.class));

    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }


}