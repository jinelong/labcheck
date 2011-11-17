package local.bin;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/*
 * 	
 * 	Event News	http://www.purdue.edu/newsroom/rss/EventNews.xml
	Faculty and Staff	http://www.purdue.edu/newsroom/rss/faculty_staff.xml
	Featured News	http://www.purdue.edu/newsroom/rss/FeaturedNews.xml
	General	http://www.purdue.edu/newsroom/rss/general.xml
	Academics	http://www.purdue.edu/newsroom/rss/academics.xml
 * 
 */

public class newsActivity extends ListActivity{

	
	enum newsType {FEATURE, EVENT, FACULTY, GENERAL, ACA }
	
	public ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	ArrayList<article> storyList ;
	
	Spinner selectNews ;
	newChannel myNews = null;
	static String url = "";
	
	ArrayAdapter<CharSequence> adapter;
	
	 public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.news);
	        
	        
	        myNews = new newChannel();
	       
	        adapter = ArrayAdapter.createFromResource(this, R.array.news, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        
	        
	        selectNews = (Spinner)findViewById(R.id.news_spinner);
	        selectNews.setAdapter(adapter);
	        selectNews.setPrompt("Select News Channel");
	        selectNews.setOnItemSelectedListener(new SpinnerOnSelectedListener());
	        
	        updateList(newsType.FEATURE);
	        
	}
	
	 
	 class SpinnerOnSelectedListener implements OnItemSelectedListener{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				/*
				 * <item>Feature</item>
			    <item>Academic</item>
			    <item>Faculty</item>
			    <item>Events</item>
			    <item>General</item>
				 */
				
				switch(arg2){
					case 0:	updateList(newsType.FEATURE);break;
					case 1:	updateList(newsType.ACA);break;
					case 2:	updateList(newsType.FACULTY);break;
					case 3: updateList(newsType.EVENT);break;
					case 4: updateList(newsType.GENERAL);break;
				}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
	 }
	
	 protected void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
		 
		 String url = storyList.get(position).link;
		 Intent i = new Intent(Intent.ACTION_VIEW, 
			       Uri.parse(url));
			startActivity(i);
		 
	    	super.onListItemClick(l, v, position, id);

			// when an entry is clicked, connect tio the client, and setup voice
			// clal
		}
		
	 
	void updateList( newsType  type) {
		
		
		switch (type){
			case FEATURE: url = "http://www.purdue.edu/newsroom/rss/FeaturedNews.xml";  ;break;//40.42575	-86.92499
			case ACA: url = "http://www.purdue.edu/newsroom/rss/academics.xml" ;break;//40.43211	-86.91965
			case EVENT: url = "http://www.purdue.edu/newsroom/rss/EventNews.xml";break;//40.42663	-86.92670
			case GENERAL: url = "http://www.purdue.edu/newsroom/rss/general.xml"; break;//40.42945	-86.92082
			case FACULTY: url = "http://www.purdue.edu/newsroom/rss/faculty_staff.xml";break;//40.42681	-86.92097
		}
		
		storyList = myNews.parseNews(url);
		
		list.clear();
			try {
				
				for (int i =0; i< storyList.size(); i++){
					HashMap<String, String> tempHash = null;				
					
					String title = storyList.get(i).title;
					String story = storyList.get(i).story;
					
					tempHash = new HashMap<String, String>();
					tempHash.put("title", title);
					tempHash.put("story", story);
							
					list.add(tempHash);
	
				}

			} catch (Exception e) {
				;
			}
			
		SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.news_list_config, new String[] { "title", "story" },new int[] { R.id.title, R.id.story});
		setListAdapter(listAdapter);

	}


}
