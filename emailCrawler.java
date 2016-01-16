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
 *         I used Jsoup to implement this email crawler. I've tested this
 *         program by some other web sites, like
 *         https://code.google.com/p/crawler4j/, it works. But when I tested
 *         "jana.com", it doesn't. I think the problem is that, for example,
 *         www.jana.com/contact, there are 3 email adds at bottom, but when I
 *         view this page source by chrome, I can find them. I think they are
 *         hidden or they are actually posted by another pages. In a word,
 *         because I don't know much about HTML/CSS/JavaSript, So I cannot find
 *         t hem in my way. But for a normal page, like above I've listed, I can
 *         make it.
 *
 */
public class emailCrawler {
	// used to store email addresses which are grabbed and avoid duplicate.
	private static HashSet<String> emailList = new HashSet<>();

	public static void main(String args[]) throws IOException {
		String url = "http://" + args[0];
		// this list is used to store all of urls which can be discovered in
		// home page. Actually, I can write this program
		// by using DFS so that I can find more discoverable pages on discovered
		// pages. But I don't do it for that it may go into a infinite loop.
		ArrayList<String> urlList = new ArrayList<>();
		Document doc = Jsoup.connect(url).get();
		urlList.add(url);
		// grab links
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
					// add found email address to hashset which means we have
					// printed this email address, and no need to print again if
					// we found in future.
					emailList.add(email);
					System.out.println(email);
				}
			}
		}
	}
}
