package com.example.spotify;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomUser extends ArrayAdapter<String>  {

	 private Activity context;       //for to get current activity context
	    SharedPreferences sh;
	private String[] name;





	 public CustomUser(Activity context, String[] n) {
	        //constructor of this class to get the values from main_activity_class

	        super(context, R.layout.cust_user,n);
	        this.context = context;

		    this.name = n;



	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	                 //override getView() method

	        LayoutInflater inflater = context.getLayoutInflater();
	        View listViewItem = inflater.inflate(R.layout.cust_user, null, true);
			//cust_list_view is xml file of layout created in step no.2

//	        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView1);
	        TextView t1=(TextView)listViewItem.findViewById(R.id.names);


//			TextView t3=(TextView)listViewItem.findViewById(R.id.textView6);
//			TextView t4=(TextView)listViewItem.findViewById(R.id.textView7);
			t1.setText(name[position]);

//			t3.setText(qtys[position]);
//			t4.setText(rate[position]);

//	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();

	        return  listViewItem;
	    }

		private TextView setText(String string) {
			// TODO Auto-generated method stub
			return null;
		}
}