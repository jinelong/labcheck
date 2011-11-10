package local.bin;
import java.io.File;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class tab extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabview);
	    if((new File("/sdcard/kiosk")).mkdirs())
    		Log.d("search", "dir created");
	    
	    
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    // Initialize a TabSpec for each tab and add it to the TabHost
		   /* spec = tabHost.newTabSpec("artists").setIndicator("LabCheck",
		                      res.getDrawable(R.drawable.ic_tab_artists))
		                  .setContent(intent);
		    tabHost.addTab(spec);
		    */
	    
	    intent = new Intent().setClass(this, LabCheckActivity.class);
	  
	    spec = tabHost.newTabSpec("lab").setIndicator("LabCheck",
                res.getDrawable(R.drawable.labtab)).setContent(intent);
        tabHost.addTab(spec);

	    // Do the same for the other tabs
        intent = new Intent().setClass(this, DiningMenu.class);
  	  
	    spec = tabHost.newTabSpec("menu").setIndicator("DiningMenu",
                res.getDrawable(R.drawable.menutab)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, newsActivity.class);
    	  
	    spec = tabHost.newTabSpec("news").setIndicator("PurdueNews",
                res.getDrawable(R.drawable.newstab)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, bookSearch.class);
        
        spec = tabHost.newTabSpec("searh").setIndicator("bookSearch",
                res.getDrawable(R.drawable.searchtab)).setContent(intent);
        tabHost.addTab(spec);
        

	    tabHost.setCurrentTab(0);
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		
		
		menu.add(0, 1, 1, "About");
		menu.add(0, 2, 2, "quit");
	//	menu.add(0, 3, 3, "news");
	
		return super.onCreateOptionsMenu(menu);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case 2:
			finish();
			break;

		case 1:
			
			Intent gotoAbout = new Intent();
			gotoAbout.setClass(tab.this, about.class);
			this.startActivity(gotoAbout);

			break;
		// about
		}
		return super.onOptionsItemSelected(item);
	}
	
}
