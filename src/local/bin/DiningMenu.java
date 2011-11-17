package local.bin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import local.bin.LabCheckActivity.SpinnerOnSelectedListener;
import local.bin.LabCheckActivity.seqComparator;

import org.apache.http.util.ByteArrayBuffer;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DiningMenu  extends ListActivity {
	
	private final int EARHART = 2;
	private final int FORD = 10;
	private final int HILLENBRAND = 9;
	private final int WILEY = 4;
	private final int WINDSOR = 11;
	
	String currentDC = "Earhart";
	
	//default earhart
	double currentLA = 40.42575;
	double currentLO = -86.92499;
	private CheckBox vege = null;
	boolean vegeOnly = false;
	private final String PATH = "/sdcard/";
	int currentChoice = EARHART;
	
	public ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	Spinner selectDC ;
	ArrayList<diningCourt> diningList = new ArrayList<diningCourt>();
	ArrayAdapter<CharSequence> adapter;
	Button showDC ;
	
	
	
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

	}//downloader
	
	enum diningCourtID {EAR, FORD, HILL, WILEY, WIN};

	public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}

	class buttonListen implements OnClickListener{
			
			@Override
			public void onClick(View v){
			
				
				if(!isOnline()){
					Toast.makeText(DiningMenu.this,"you are offline, cannot display location", Toast.LENGTH_LONG).show();
					return;
				}
				
				
				Intent gotoMapview = new Intent();
				gotoMapview.setClass(DiningMenu.this, mapview.class);
				
				
			
				gotoMapview.putExtra("status", " ");
				gotoMapview.putExtra("name",currentDC+" Dining Court");
				gotoMapview.putExtra("la", currentLA);
				gotoMapview.putExtra("lo",currentLO);
				
				startActivity(gotoMapview);
			}	
	}
	
	
	
	void updateList( int type ) {

		list.clear();
		int index = 0;
		currentChoice = type;
		switch (type){
			case EARHART: index = 0;currentLA = 40.42575; currentLO = -86.92499;currentDC = "Earhart";break;//40.42575	-86.92499
			case FORD: index = 1;currentLA = 40.43211;currentLO = -86.91965; currentDC = "Ford";break;//40.43211	-86.91965
			case HILLENBRAND: index = 2;currentLA = 40.42663; currentLO = -86.92670;currentDC = "Hillenbrand";break;//40.42663	-86.92670
			case WILEY: index = 3 ;currentLA = 40.42945;currentLO = -86.92082;currentDC = "Wiley";break;//40.42945	-86.92082
			case WINDSOR: index = 4;currentLA = 40.42681; currentLO = -86.92097;currentDC = "Windsor";break;//40.42681	-86.92097
		}
		
			try {
				
				for (int i =0; i< diningList.get(index).place.size(); i++){
					HashMap<String, String> tempHash = null;				
					
					String placeName = diningList.get(index).place.get(i).name ;
					String menu = "";
					
					for(int j=0;j<diningList.get(index).place.get(i).food.size();j++){
						if(vegeOnly){
							if(diningList.get(index).place.get(i).food.get(j).contains("*"))
								menu += diningList.get(index).place.get(i).food.get(j)+"\n";
						}
						else
							menu += diningList.get(index).place.get(i).food.get(j)+"\n";
						
					}
					if(!menu.equals("")){
						tempHash = new HashMap<String, String>();
						tempHash.put("place", placeName);
						tempHash.put("menu", menu);
								
						list.add(tempHash);
						
					}
						
					
					Log.d("menu",placeName);
					Log.d("menu",menu);
					
					
	
				}

			} catch (Exception e) {
				;
			}
			
	
		
    	
		SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.dining_list_config, new String[] { "place", "menu" },new int[] { R.id.place, R.id.dish});
		setListAdapter(listAdapter);

	}

	
	
    
    private void read(String fileName, int id) throws IOException {

	final String newStand = "<div id=\"mealStation\" class=\"meal\">";
    		final String endStand = "</div>";
    		final String newDish = "<li>";
    		final String newDishEnd = "</li>";

    		String dcName = null;
    		switch (id){
	    		case EARHART: dcName = "Earhart";break;
	    		case FORD:dcName = "Ford";break;
	    		case HILLENBRAND: dcName = "Hillenbrand";break;
	    		case WINDSOR: dcName = "Windsor";break;
	    		case WILEY: dcName = "Wiley";break;
    		}
    		diningList.add(new diningCourt(dcName));
    		int index = 0;
    		if(diningList.size()!=0)
    			index = diningList.size()-1;
    		
    		int secondIndex = 0; 
    	  try{

    		  FileInputStream fstream = new FileInputStream(fileName);
    		  DataInputStream in = new DataInputStream(fstream);
    		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
    		  String strLine;
    		  //Read File Line By Line

    		  int olde= 0;
    		  int oldd = 0;

    		  boolean entryStart = false;

    		  while ((strLine = br.readLine()) != null)   {

    			  String buffer = new String(strLine);

    			  if(buffer.contains("There are no items available for this meal")){
    				  

		        	Toast t = Toast.makeText(DiningMenu.this,"not able to fetch content, are you sure dining courts are open?", Toast.LENGTH_SHORT);
		        	t.setGravity(Gravity.CENTER, 0, 0);
		        	t.show();
    				  return;
    				  
    			  }
    			  
    			  
    			 int  s=buffer.indexOf(newStand, olde);
    			 int ds = buffer.indexOf(newDish,olde);

    			 int e,de;

    			 while(s!=-1 && ds!=-1 || entryStart){
    				// System.out.println(counter);
    				 entryStart = true;
    				 //System.out.println("s: "+ s + " ds: " + ds);
    				 if(s<ds && s!=-1){
    					 e=buffer.indexOf(endStand, s);
    					 String standName = strLine.substring(s+newStand.length(), e);

    					 System.out.println(standName);
    					 diningList.get(index).addPlace(standName);
    					 Log.d("menu" , standName + " added to " + diningList.get(index).name);
    					 
    					 if(diningList.get(index).place.size()!=0)
    						 secondIndex = diningList.get(index).place.size()-1;
    					 else
    						 secondIndex=  0;

    					 olde = e+1;
    					 s=buffer.indexOf(newStand, olde);

    				 }else if (s==-1 && ds != -1){
    					 entryStart = false;
    					 while(ds!=-1){
    						 de=buffer.indexOf(newDishEnd, ds);
    						 String dishName = strLine.substring(ds+newDish.length(), de);

    						 System.out.println("\t"+dishName);
    						 diningList.get(index).place.get(secondIndex).addFood(dishName);
    						 Log.d("menu",diningList.get(index).place.get(secondIndex).name+"has food: "+dishName);

    						 oldd = de+1;
    						 ds=buffer.indexOf(newDish, oldd);
    					 }

    				 }//else if
    				 else{
    					 de=buffer.indexOf(newDishEnd, ds);
    					 String dishName = strLine.substring(ds+newDish.length(), de);

    					 System.out.println("\t"+dishName);
    					 diningList.get(index).place.get(secondIndex).addFood(dishName);
						 Log.d("menu",diningList.get(index).place.get(secondIndex)+"has food: "+dishName);

    					 oldd = de+1;
    					 ds=buffer.indexOf(newDish, oldd);

    				 }

    			 }
    			 olde = 0;
    			 oldd= 0;
    			 }//while

    			  //Close the input stream
    			  in.close();
    	   }catch (Exception e){//Catch exception if any
    		   	System.err.println("Error: " + e.getMessage());

    	   }

          
      }//read
   
    class SpinnerOnSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			switch(arg2){
				case 0:	updateList(EARHART);break;
				case 1:	updateList(FORD);break;
				case 2:	updateList(HILLENBRAND);break;
				case 3: updateList(WILEY);break;
				case 4: updateList(WINDSOR);break;
			}
			
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	}
    
	 public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.dining);
	        
	    	vege = (CheckBox)findViewById(R.id.check2);
	    	vege.setOnCheckedChangeListener(new OnCheckedChangeListener()
	         {
	             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	             {
	                 if ( isChecked )
	                	 vegeOnly = true;
	                 	
	                 else
	                	 vegeOnly = false;
	                 
	             	updateList(currentChoice);
	              
	             }
	         });

	        
	        showDC = (Button)findViewById(R.id.showDiningCourt);
	        showDC.setOnClickListener(new buttonListen());
	        
	        adapter = ArrayAdapter.createFromResource(this, R.array.diningCourts, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        
	        
	        selectDC = (Spinner)findViewById(R.id.dining_spinner);
	        selectDC.setAdapter(adapter);
	        selectDC.setPrompt("Select Dining Court");
	        selectDC.setOnItemSelectedListener(new SpinnerOnSelectedListener());
	    
	        /*
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
	       
	        */
	        try{
	        	read(PATH+"menu"+EARHART, EARHART);
	        	read(PATH+"menu"+FORD, FORD);
	        	read(PATH+"menu"+HILLENBRAND, HILLENBRAND);
	        	read(PATH+"menu"+WILEY, WILEY);
	        	read(PATH+"menu"+WINDSOR, WINDSOR);
	        	
	        }catch(Exception e){
	        	e.printStackTrace();
	        	
	        }
	       
	        
	 }
	
	//URL format:
	//http://www.housing.purdue.edu/Menus/menu.aspx?hallID=9&date=10/21/2011
	

}
	