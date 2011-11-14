package local.bin;

public class searchBookResult {

	public String title;
	public String author;
	public String isbn;
	public String year;
	public String id;
	public searchBookResult (String t, String a, String i, String y, String d){
		title = t;
		author =a;
		isbn = i;
		year = y;
		id = d;
	}
	public searchBookResult (String t){
		title = t;
		author = "UNKNOWN";
		isbn = "UNKNOWN";
		year= "UNKNOWN";
		id = "";
	}
}
