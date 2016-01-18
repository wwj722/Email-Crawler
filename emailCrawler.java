import java.io.IOException;
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
 */
public class emailCrawler {
	// used to store email addresses and url which are grabbed and avoid
	// duplicate.
	private static HashSet<String> emailList = new HashSet<>();
	private static HashSet<String> urlList = new HashSet<>();

	/**
	 * 
	 * @param args input, like "jana.com"
	 * @throws IOException
	 */

	public static void main(String args[]) throws IOException {
		//add "http://"
		args[0] = "http://" + args[0];
		//grab all links we can get through the whole web site
		findLinks(args[0]);
		//find all email addresses from links we have got above 
		findEmail(urlList);
		//print email address
		for (String email : emailList) {
			System.out.println(email);
		}

	}

	/**
	 * 
	 * @param url the link we are now dealt with
	 * @throws IOException
	 * 
	 */
	public static void findLinks(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		urlList.add(url);
		// grab links which contains "contact", such as jana.com/contact
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			String nowlink = link.attr("abs:href");
			// focus on "contact",such as "jana.com/contact", so that crawl the whole site and not accidentally try to crawl the entire internet.
			if (!urlList.contains(nowlink) && nowlink.contains("contact")) {
				urlList.add(nowlink);
				System.out.println(nowlink);
				findLinks(nowlink);
			}
		}
	}

	/**
	 * 
	 * @param urlList the link we have found through the whole website
	 * @throws IOException
	 */
	public static void findEmail(HashSet<String> urlList) throws IOException {
		for (String url : urlList) {
			Document doc = Jsoup.connect(url).get();

			// email format
			Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+")
					.matcher(doc.select("a").toString());
			while (m.find()) {
				String email = m.group();
				emailList.add(email);
			}
		}

	}
}
