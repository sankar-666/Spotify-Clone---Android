package com.example.spotify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserHome extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    ListView l1;
    public static String cid,catg;
    String[] cat,cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full sc
        setContentView(R.layout.activity_user_home);

        Button b1 = findViewById(R.id.main);

        l1=findViewById(R.id.cat);
        l1.setOnItemClickListener(this);

        ImageButton i1 = findViewById(R.id.imageButton);

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse) UserHome.this;
        String q = "/viewcategory";
        q=q.replace(" ","%20");
        JR.execute(q);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), View_Random_Music.class));
            }
        });
//
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ViewTournaments.class));
//            }
//        });

//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), IPSettings.class));
//            }
//        });
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),UserHome.class);
        startActivity(b);
    }

    @Override
    public void response(JSONObject jo) {

        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("viewcategory")){
                String status=jo.getString("status");
                Log.d("pearl",status);
                if(status.equalsIgnoreCase("success")){

                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");


                    cat=new String[ja1.length()];
                    cat_id=new String[ja1.length()];


                    for(int i = 0;i<ja1.length();i++)
                    {

                        cat[i]=ja1.getJSONObject(i).getString("category");
                        cat_id[i]=ja1.getJSONObject(i).getString("category_id");




                    }
                    CustomUser clist=new CustomUser(this,cat);
                    l1.setAdapter(clist);

//                    ArrayAdapter<String> ar= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,value);
//                    lv1.setAdapter(ar);


                }

                else {
                    Toast.makeText(getApplicationContext(), "No Result found!", Toast.LENGTH_LONG).show();

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
        cid=cat_id[i];
        catg=cat[i];
        startActivity(new Intent(getApplicationContext(),MusicbasedOn_category.class));
    }
}