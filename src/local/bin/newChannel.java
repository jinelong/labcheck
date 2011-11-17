package local.bin;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * 
 * author Reid Starn 
 * date: Nov.09,2011
 * 
 * 
 */

public class newChannel {


	public static Document dom;
	
	
	public  ArrayList<article>  parseNews(String feed) {
		ArrayList<article> articles = new ArrayList<article>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();		
		

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			URL url = new URL(feed);
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			dom = db.parse(is);


		}catch(ParserConfigurationException pce) {
			;
		}catch(SAXException se) {
			;
		}catch(IOException ioe) {
			;
		}

	//	Log.d("Parser", "Obtained dom");
		if(dom == null)
		{
		//	Log.d("Parser", "dom is null bro");
		}
		Element root = dom.getDocumentElement();

		//Log.d("Parser", "obtained document element");

		NodeList nl = root.getElementsByTagName("item");

		//Log.d("Parser", "Obtained nl");


		if(nl != null && nl.getLength() > 0) {
			for(int i = 0; i < nl.getLength(); i++) {

				//get the news info
				Element news_element = (Element)nl.item(i);

				//make a new artical element
				article new_article = getArticleInfo(news_element);

				//add to list
				articles.add(new_article);
			}
		}
		return articles;
	}

	public  article getArticleInfo(Element e) {
		String date = getTextValue(e, "pubDate");
		String title = getTextValue(e, "title");
		String desc =  getTextValue(e, "description");
		//Log.d("desc", desc);
		String link = getTextValue(e, "link");

		article a = new article(date, title.trim(), desc.trim(), link.trim());
		return a;

	}

	private static String getTextValue(Element ele, String tagName) {
		String textVal = "";
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			NodeList nnl = el.getChildNodes();
			for(int i = 0; i < nnl.getLength(); i++) {
				if(nnl.item(i).getNodeValue() == null ) 
					textVal += '\'';
				else
					textVal += nnl.item(i).getNodeValue();

			}
		}

		return textVal;
	}
/*
	public static void main(String []da){


		news a = new news();
		final String feed = "http://www.purdue.edu/newsroom/rss/FeaturedNews.xml";
		a.parseNews(feed);

		for(int i=0; i<articles.size();i++ ){

			System.out.println("title: " +  articles.get(i).title);
			System.out.println("date: " +  articles.get(i).date);
			System.out.println("story: " +  articles.get(i).story);
			System.out.println("link: " +  articles.get(i).link);

		}

	}
	*/
}