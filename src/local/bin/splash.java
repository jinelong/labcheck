package local.bin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



public class splash extends Activity {
	private TextView t = null;
	final String PATH = "/sdcard/";
	final String ADDR = "https://www.purdue.edu/apps/ics/LabMap";
	
	private final int EARHART = 2;
	private final int FORD = 10;
	private final int HILLENBRAND = 9;
	private final int WILEY = 4;
	private final int WINDSOR = 11;
	
	
	public boolean DownloadFromUrl(String imageURL, String fileName) {  //this is the downloader method
        try {
                URL url = new URL(imageURL); //you can write here any link
                File file = new File(fileName);

                long startTime = System.currentTimeMillis();
                Log.d("downloader", "download begining");
                Log.d("downloader", "download url:" + url);
                Log.d("downloader", "downloaded file name:" + fileName);
                /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();
                ucon.setConnectTimeout(1000);
                /*
                 * Define InputStreams to read from the URLConnection.
                 */
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                /*
                 * Read bytes to the Buffer until there is nothing more to read(-1).
                 */
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                }

                /* Convert the Bytes read to a String. */
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.close();
                Log.d("downloader", "download ready in"
                                + ((System.currentTimeMillis() - startTime) / 1000)
                                + " sec");
                return true;

        } catch (IOException e) {
                Log.d("downloader", "Error: " + e);
                return false;
        }

}
	
	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	Runnable a = new Runnable(){
		
		public void run(){
			//if(isOnline() && DownloadFromUrl(ADDR, PATH)){
			if(isOnline()){
				
				//start downloading stuff
					final Calendar c = Calendar.getInstance();
			        int mYear = c.get(Calendar.YEAR);
			        int mMonth = c.get(Calendar.MONTH)+1;
			        int mDay = c.get(Calendar.DAY_OF_MONTH);
			   
			        //download today's menu from 5 dining courts
			    	
			        String 
			        downloadAddr = "http://www.housing.purdue.edu/Menus/menu.aspx?hallID="+EARHART+"&date=" + mMonth + "/" + mDay + "/"+ mYear;
			        DownloadFromUrl(downloadAddr, PATH+"menu"+EARHART);

			        downloadAddr = "http://www.housing.purdue.edu/Menus/menu.aspx?hallID="+FORD+"&date=" + mMonth + "/" + mDay + "/"+ mYear;
			        DownloadFromUrl(downloadAddr, PATH+"menu"+FORD);
			        
			        downloadAddr = "http://www.housing.purdue.edu/Menus/menu.aspx?hallID="+HILLENBRAND+"&date=" + mMonth + "/" + mDay + "/"+ mYear;
			        DownloadFromUrl(downloadAddr, PATH+"menu"+HILLENBRAND);

			        downloadAddr = "http://www.housing.purdue.edu/Menus/menu.aspx?hallID="+WILEY+"&date=" + mMonth + "/" + mDay + "/"+ mYear;
			        DownloadFromUrl(downloadAddr, PATH+"menu"+WILEY);

			        downloadAddr = "http://www.housing.purdue.edu/Menus/menu.aspx?hallID="+WINDSOR+"&date=" + mMonth + "/" + mDay + "/"+ mYear;
			        DownloadFromUrl(downloadAddr, PATH+"menu"+WINDSOR);
			       
			        Toast.makeText(splash.this,"Menu downloaded", Toast.LENGTH_LONG).show();
				
			        downloadAddr = "https://www.purdue.edu/apps/ics/LabMap";
			        if(!DownloadFromUrl(downloadAddr, PATH+"lab")){
			        	
			        	runOnUiThread(new Runnable(){
							public void run() {
								Toast.makeText(splash.this,"cannot connect to ITAP server, use cache file", Toast.LENGTH_LONG).show();
							}
							});
			        }
			        
			        
			        startActivity(new Intent().setClass(splash.this,tab.class));
			        finish();
			}else{
				
				runOnUiThread(new Runnable(){
					public void run() {
						Toast.makeText(splash.this,"you are in offline mode", Toast.LENGTH_LONG).show();
					}
					});
				
				startActivity(new Intent().setClass(splash.this,tab.class));
		        finish();	
			}
			
		}
		
	};


	
	Handler welcome = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		t = (TextView)findViewById(R.id.progress);
			
		welcome.postDelayed(a,1000);
		//welcome.removeCallbacks(a);
		
		}
}
