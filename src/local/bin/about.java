package local.bin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Jin
 *
 */

public class about extends Activity  {
		
	TextView text = null;
	Button ok_about = null;
	Button eggB = null;
	double oldx= 0;
	double oldy = 0;
	boolean egg = false;
	int counter = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		
		text = (TextView)findViewById(R.id.about);
		text.setTextSize(15);
		text.setText("happy birthday reid");
		ok_about = (Button)findViewById(R.id.ok_about);
		ok_about.setText("OK");
		eggB = (Button)findViewById(R.id.egg);
		eggB.setBackgroundColor(Color.BLACK);
		eggB.setVisibility(View.VISIBLE);
		
		ok_about.setOnClickListener(new onClick());
		eggB.setOnTouchListener(new onEgg());
		 
	}
	
	
class onClick implements OnClickListener{
		
		@Override
		public void onClick(View v){
			finish();
				
		}
	}



class onEgg implements OnTouchListener{
	// TODO Auto-generated method stub
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(counter>21)	return false;
		double x = event.getX();
		double y = event.getY();
		Log.d("about", "in touch");
		
		if(!egg){
			if(x!=oldx || y!=oldy){
				 oldx = x;
				 oldy = y;
				 Log.d("about", ""+counter);
				counter ++;
				
				if(counter > 20)
					egg = true;
			}
		}
		else{
			String url = "http://www.mememaker.net/images/public/201111110603513359.jpg	";
			 Intent i = new Intent(Intent.ACTION_VIEW, 
				       Uri.parse(url));
				startActivity(i);
		}
		
		return true;
	}
	
	
}
		
}
	