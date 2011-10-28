package local.bin;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;


public class tab extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabview);

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

	    tabHost.setCurrentTab(0);
	}
}
