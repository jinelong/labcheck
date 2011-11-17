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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class libraryParser {
	
	final String titleToken = "class=\"title\">";
	final String titleHighlightStart = "<span class=\"highlight\">";
	final String titleHighlightEnd = "</span>";
	final String authorMark = "Find/Author/Home?author=";
	final String recordID = "class=\"accessible-text\"";
	final String path = "/sdcard/kiosk/";
	final String isbn = "<script>GBS_insertPreviewButtonPopup('ISBN:";

	

	ArrayList<searchBookResult> retval = new ArrayList<searchBookResult> ();

	public ArrayList<searchBookResult>   bookSearch(String query){
		retval.clear();
		int counter = 0;
		
		

		String queryAddr = "http://catalog.lib.purdue.edu/Find/Search/Home?lookfor=";
		String searchResultPage = query+".htm";

		Scanner parseQuery = new Scanner(query);
		while(parseQuery.hasNext()){
			queryAddr += parseQuery.next();

			if(parseQuery.hasNext())
				queryAddr+= "+";

		}
		Log.d ("search", "query in parser: " + query);
		Log.d ("search", "query URL: " + queryAddr);
		queryAddr += "&type=all&submit=Find";
		
		DownloadFromUrl(queryAddr ,path + searchResultPage);

		//Pattern titlePattern = Pattern.compile("(\\s+)?<span class=\"highlight\">(\\s+)?</span>(\\s+)?");

		  try{
			  Log.d("search", "path: " + path+searchResultPage);
			  System.out.println(path+searchResultPage);
			  FileInputStream fstream = new FileInputStream(path+searchResultPage);

			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line

			  while ((strLine = br.readLine()) != null)   {
				//  Log.d("search", "line: " + strLine);
				  
				  String author = "";
				  String year = "";
				  String realtitle = "";
				  String bookISBN= "";

				  if(strLine.contains("class=\"title\">")){
					  //System.out.println(strLine);
					  //System.out.println("found title");
					  
					  Log.d("search", "title found");
					  realtitle = "";
					  int titleStart = strLine.indexOf(titleToken) + titleToken.length();
					  int titleEnd = strLine.indexOf("</a>");
					  //System.out.println("strLine legnth: " + strLine.length() + " start: " + titleStart + " end: " + titleEnd);
					  String titleWithHighLight = strLine.substring(titleStart, titleEnd);
					  Log.d("search", titleWithHighLight);
					  if(titleWithHighLight.contains(titleHighlightStart)){
						  
						  int s1 = titleWithHighLight.indexOf(titleHighlightStart);
						  int s2 = titleWithHighLight.indexOf(titleHighlightEnd, s1);
						  Log.d("search", "s1: " + s1);
						  Log.d("search", "s2: " + s2);
						  //System.out.println("s1: " + s1 + " s2 " + s2);

						  realtitle += titleWithHighLight.substring(0, s1);
						  realtitle += titleWithHighLight.substring(s1+titleHighlightStart.length(), s2);
						  realtitle += titleWithHighLight.substring(s2+titleHighlightEnd.length(), titleWithHighLight.length());
						  
						  Log.d("search", "if: " + realtitle);
						  
					  }else{
						  //System.out.println("awesome,  no hightlight");
						  realtitle = titleWithHighLight;
						  Log.d("search", realtitle);
					  }
					 
					  
					  String rtitle = realtitle.substring(0, realtitle.length()-1);
					  rtitle = rtitle.trim();
					
					  
					  retval.add(new searchBookResult(rtitle));
					  counter ++;
					  
					  Log.d("search", "title: " + retval.get(counter-1).title);

				  }//if contains title
				  else if(strLine.contains(authorMark)){
					  Log.d("search", "author found");
					  int authorStart = strLine.indexOf(">") + 1;
					  int authorEnd = strLine.indexOf("</a>");
					  author = strLine.substring(authorStart, authorEnd).trim();

					  if(author.length()==0)
						  	author = "UNKNOWN";

					  if(counter > 0)
						  retval.get(counter-1).author = author;
					  
					  //System.out.println("by " + author);
					  Log.d("search", "author: " + author);
				  }else if(strLine.contains("Published")){
					  //System.out.println("found date");
					  Log.d("search", "data found");
					  int s = strLine.indexOf("Published");
					  int yearStart = strLine.indexOf("</b>",s) + 4;
					  int yearEnd = strLine.indexOf("</div>",s) ;
					  year = strLine.substring(yearStart, yearEnd).trim();

					  if(counter > 0)
						  retval.get(counter-1).year = year;
					  
					  Log.d("search", "published: " + year);
					  
//					/  System.out.println("published: " + year + "\n");

				  }else if(strLine.contains(recordID)){
					  Log.d("search", "ID found");
							int s = strLine.indexOf(recordID) + recordID.length();
							int idStart = strLine.indexOf("for=\"", s) + 5;
							int idEnd = strLine.indexOf("\">Select Record", idStart);

							String ID = strLine.substring(idStart, idEnd).trim();
							if(counter > 0)
								  retval.get(counter-1).id = ID;
							
							 Log.d("search", "ID: " + ID);
							//System.out.println("ID: "  + ID);
							//bookISBN = getISBN (ID);
							if(counter > 0)
								  //retval.get(counter-1).isbn = bookISBN;
								retval.get(counter-1).isbn = "TBD";
							
							 Log.d("search", "isbn: " + bookISBN);
							//getCoverImg (bookISBN, ID);

				  }

				 }//while_readLine

				  //Close the input stream
				  in.close();
		   }catch (Exception e){//Catch exception if any
			   	System.err.println("Error: " + e.getMessage());
		   }
		   return retval;
	}//search book
	
	
	public void getCoverImg(String isb, String id){

		//get the bookcover
		//fileDownload("http://catalog.lib.purdue.edu/Find/bookcover.php?isn=9781848829039", "F:\\", "1.jpg");

		//http://catalog.lib.purdue.edu/Find/bookcover.php?isn=0387952608

		String imgAddr = "http://catalog.lib.purdue.edu/Find/bookcover.php?isn=" + isb;
		DownloadFromUrl(imgAddr, path+id+".jpg");


	}
	public String getISBN(String id){


		//download this file to get ISBN
		//String url= "http://catalog.lib.purdue.edu/Find/Record/1138815/Description";
		//fileDownload(url, "F:\\", "bookDes");
		//get isbn from this line:         		<script>GBS_insertPreviewButtonPopup('ISBN:0387950451',800,800);</script> 



		String desUrl= "http://catalog.lib.purdue.edu/Find/Record/" +id +"/Description";	
		String fileName = id+"Des";
		DownloadFromUrl(desUrl ,path+fileName);

		String retval = "";

		  try{

			  System.out.println(path+fileName);
			  FileInputStream fstream = new FileInputStream(path+fileName);

			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line




			  while ((strLine = br.readLine()) != null)   {


				  if(strLine.contains(isbn)){
					  int isbnStart = strLine.indexOf(isbn) + isbn.length();
					  int isbnEnd = strLine.indexOf("'", isbnStart);

					  retval = strLine.substring(isbnStart, isbnEnd);
//					 / System.out.println("the isbn is " + retval);
					 break;
				  }
				 }//while_readLine

				  //Close the input stream
				  in.close();
		   }catch (Exception e){//Catch exception if any
			   	System.err.println("Error: " + e.getMessage());

		   }

		  return retval;



	}
	/*
	public static void main(String [] a){
		libraryParser p = new libraryParser();
		String query = "data mining compression";
		p.bookSearch (query);

	}
*/



	//final static int size=1024;
	public void DownloadFromUrl(String imageURL, String fileName) {  //this is the downloader method
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

        } catch (IOException e) {
                Log.d("downloader", "Error: " + e);
        }

}

}
