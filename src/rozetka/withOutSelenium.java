package rozetka;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.testng.annotations.*;
import org.w3c.dom.Document;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.NodeList;
import org.json.JSONObject;

public class withOutSelenium {

	private URL url;
	private Document doc;

	@BeforeClass
	public void setUp() throws Exception {
		String productArticle = "5000299223017";
		String baseUrl = "http://rozetka.com.ua/search/?text="+productArticle;
		url = new URL(baseUrl);
	}

	@Test
	private void searchProductByArticleWithoutSelenium() throws Exception {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//System.out.println(response.toString());
		TagNode tagNode = new HtmlCleaner().clean(response.toString());
		doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
		XPath xpath = XPathFactory.newInstance().newXPath();

		assertEquals("Ром Captain Morgan Spiced Gold 0.7 л 35% (5000299223017) ",
				(String) xpath.evaluate(".//*[@class='detail-title']",
						doc, XPathConstants.STRING));
        ////Show USDproduct price///////
        NodeList n = doc.getElementsByTagName("script");//6
        for(int i = 0; i< n.getLength();i++)
        {
            if (n.item(i).getTextContent().indexOf("dataLayer.push") == 5)
            {
                String original = n.item(i).getTextContent();
                String json = original.substring(original.indexOf("(")+1, original.indexOf(");"));

                JSONObject obj = new JSONObject(json);
                String pageName = obj.getString("productPrice");
                System.out.println("Price in USD = " + pageName);
            }
        }
	}

	@Test(dependsOnMethods = "searchProductByArticleWithoutSelenium")
	private void countCommentsWithoutSelenium() throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		assertEquals("49", (String) xpath.evaluate(".//*[@id='tabs']/li[4]/a/span",
				doc, XPathConstants.STRING));
	}

	@Test(dependsOnMethods = "searchProductByArticleWithoutSelenium")
	private void countRatingWithoutSelenium() throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		assertEquals("width:100%",
				(String) xpath.evaluate(".//*[@class='g-rating-stars-i-medium']/@style",
						doc, XPathConstants.STRING));
	}
}
