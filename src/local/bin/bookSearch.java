package local.bin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import local.bin.newsActivity.SpinnerOnSelectedListener;
import local.bin.newsActivity.newsType;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class bookSearch extends ListActivity{
	
	
	public ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	ArrayList<searchBookResult> bookList ;
	Button searchGo ;
	EditText searchBar;
	libraryParser search;
	InputMethodManager inputManager;
	
	
	 public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.booksearch);
	        
	       
	        inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        
	        searchGo = (Button)findViewById(R.id.searchgo);
	        searchBar = (EditText)findViewById(R.id.searchbar);
	        searchBar.setOnClickListener(new clearBox());
	        
	        searchGo.setOnClickListener(new buttonListen());
	        
	        search = new libraryParser();
	        
	}
	 class clearBox implements OnClickListener{
			
			@Override
			public void onClick(View v){
				searchBar.setText("");
			}	
	}//button
	 
	class buttonListen implements OnClickListener{
			
			@Override
			public void onClick(View v){
				String query = "";
				
				 
				 inputManager.hideSoftInputFromWindow(searchBar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				  
				if(searchBar.getText().toString().length()>0)
						query = searchBar.getText().toString();
				Log.d("search", "query:	 " + query);
				
				bookList = search.bookSearch(query);
				
				Log.d("search", "result size " + bookList.size());
				updateList();
			}	
	}//button
	
	void updateList(){
		list.clear();
			
		try {
			
			for (int i =0; i< bookList.size(); i++){
				HashMap<String, String> tempHash = null;				
				
				String title = bookList.get(i).title;
				String info = "author: " + bookList.get(i).author+"\npublished: " + bookList.get(i).year + "\nISBN: " + bookList.get(i).isbn;
				
				
				tempHash = new HashMap<String, String>();
				tempHash.put("title", title);
				tempHash.put("info", info);
						
				list.add(tempHash);

			}

		} catch (Exception e) {
			;
		}
		

	SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.searchresult_list_config, new String[] { "title", "info" },new int[] { R.id.title, R.id.info});
	setListAdapter(listAdapter);
	
		
		
	}//updateList
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
	 
		
	 String url = "http://catalog.lib.purdue.edu/Find/Record/" + bookList.get(position).id;
	 Intent i = new Intent(Intent.ACTION_VIEW, 
		       Uri.parse(url));
		startActivity(i);
	 
    	super.onListItemClick(l, v, position, id);

		// when an entry is clicked, connect tio the client, and setup voice
		// clal
	}
	
	 
	

}
