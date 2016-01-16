import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Weijian Wang
 * 
 * 
 *  I used Jsoup to implement this email crawler. I've tested this program by some web sites, like
 *  https://code.google.com/p/crawler4j/, it works well. But for "jana.com", it doesn't. This is 
 *  because the "email address" on jana.com are "hidden behind" in JavaScript, i.e, it's a dynamic web page. 
 * 
 */
public class emailCrawler {
	// used to store email addresses which are grabbed and avoid duplicate.
	private static HashSet<String> emailList = new HashSet<>();

	public static void main(String args[]) throws IOException {
		String url = "http://" + args[0];
		// this list is used to store all of urls which can be discovered in home page. Actually, I can write this 
		// program by using DFS so that I can find more discoverable pages on pages which we have discovered. 
		// But here, I don't do it for that it may go into a infinite loop.
		ArrayList<String> urlList = new ArrayList<>();
		Document doc = Jsoup.connect(url).get();
		urlList.add(url);
		// grab links, i.e., discoverable pages
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			urlList.add(link.attr("abs:href"));
		}
		printEmail(urlList);
	}

	public static void printEmail(ArrayList<String> urlList) throws IOException {
		for (String url : urlList) {
			Document doc = Jsoup.connect(url).get();
			// email format
			Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+")
					.matcher(doc.select("a").toString());
			while (m.find()) {
				String email = m.group();
				if (!emailList.contains(email)) {
					// add found email address to hashset which means we have printed this email address, 
					// and no need to print again if we found in future.
					emailList.add(email);
					System.out.println(email);
				}
			}
		}
	}
}
