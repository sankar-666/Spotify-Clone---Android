package com.example.spotify;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Custimage extends ArrayAdapter<String>  {

	 private Activity context;       //for to get current activity context
	    SharedPreferences sh;
	private String[] image;
	private String[] music;
	private String[] cat;


	
	 public Custimage(Activity context, String[] images, String[] music, String[] category) {
	        //constructor of this class to get the values from main_activity_class

	        super(context, R.layout.cust_images, images);
	        this.context = context;
	        this.image = images;
		    this.music = music;
		 	this.cat = category;

	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	                 //override getView() method

	        LayoutInflater inflater = context.getLayoutInflater();
	        View listViewItem = inflater.inflate(R.layout.cust_images, null, true);
			//cust_list_view is xml file of layout created in step no.2

	        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView1);
	        TextView t1=(TextView)listViewItem.findViewById(R.id.song);
	        TextView t2=(TextView)listViewItem.findViewById(R.id.catg);

//			TextView t2=(TextView)listViewItem.findViewById(R.id.textView5);
//			TextView t3=(TextView)listViewItem.findViewById(R.id.textView6);
//			TextView t4=(TextView)listViewItem.findViewById(R.id.textView7);
			t1.setText(music[position]);
			t2.setText(cat[position]);
//			t3.setText(qtys[position]);
//			t4.setText(rate[position]);
	        sh=PreferenceManager.getDefaultSharedPreferences(getContext());
	        
	       String pth = "http://"+sh.getString("ip", "")+"/"+image[position];
	       pth = pth.replace("~", "");
//	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();
	        
	        Log.d("-------------", pth);
	        Picasso.with(context)
	                .load(pth)
	                .placeholder(R.drawable.ic_launcher_background)
	                .error(R.drawable.ic_launcher_background).into(im);
	        
	        return  listViewItem;
	    }

		private TextView setText(String string) {
			// TODO Auto-generated method stub
			return null;
		}
}