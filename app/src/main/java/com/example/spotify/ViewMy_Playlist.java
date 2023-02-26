package com.example.spotify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class ViewMy_Playlist extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView lv1;
    String [] music,path,name,play_id,value;
    public static String song,lyr,pid,names;

    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_view_my_playlist);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        lv1=(ListView)findViewById(R.id.cat);
        lv1.setOnItemClickListener(this );

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) ViewMy_Playlist.this;
        String q = "/viewplaylist?lid="+sh.getString("log_id","");
        q=q.replace(" ","%20");
        JR.execute(q);


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




        }catch (Exception e)
        {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pid=play_id[i];
        names=name[i];

        startActivity(new Intent(getApplicationContext(), View_songs_onPlaylist.class));


    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),Settings.class);
        startActivity(b);
    }
}