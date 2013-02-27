package it.autostrade.feio.test;

import it.autostrade.feio.utils.net.HeaderInfo;
import java.util.HashMap;
import java.util.Map;

public class TestHeaderInfo {

	private final static String IE = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR";
	private final static String CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.812.0 Safari/535.1";
	private final static String CHROME24 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.60 Safari/537.17";
	private final static String FIREFOX16 = "Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1";
	private final static String FIREFOX4b8 = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0b8) Gecko/20100101 Firefox/4.0b8";
	private final static String SAFARI504 = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_7; ja-jp) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27";

	public static void main(String[] args) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("User-Agent", SAFARI504);

		Map<String, String> browser = HeaderInfo.getBrowser(headerMap);
		System.out.println(HeaderInfo.getBrowserToString(headerMap));
	}

}