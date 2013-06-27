package it.feio.utils.net;

import java.io.*;
import java.net.*;
import org.apache.log4j.*;

public class HttpClient {
	private static Logger log;
	
	private int timeout = -1;
	private String host;
	private int port = 80;
	private String post;
	//private int statusCode, contentLength;
	//private String statusMessage, contentType;
	private StringBuffer body;
	private File spooler;
	//private boolean ascii;

	public HttpClient() {
		//this.log = Logger.getLogger(this.getClass().getName());
	}

	public HttpClient(String host, int port) {
		this();
		this.setHost(host);
		if (port != -1)
			this.setPort(port);
	}

	public HttpClient(URL url) {
		this(url.getHost(), url.getPort());
	}

	public HttpClient(String address) throws MalformedURLException {
		this(new URL(address));
	}

	public HttpResponse doPost(String uri, String params) throws IOException {
		return doInner(uri, params);
	}

	public HttpResponse doGet(String uri) throws IOException {
		return doInner(uri, null);
	}

	public HttpResponse doInner(String uri, String postData) throws IOException {
		HttpResponse response = new HttpResponse();
		body = new StringBuffer();
		Socket s = null;
		BufferedReader is = null;
		PrintWriter os = null;

		try {
			s = new Socket(getHost(), getPort());
			s.setSoTimeout(getTimeout());
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream());
			//
			os.println((postData != null ? "POST " : "GET ") + uri + " HTTP/1.0");
			os.println("Host: " + getHost());
			os.println("Pragma:no-cache");
			if (postData != null) {
				// os.println("Referer: " + referer);
				os.println("Content-Type: application/x-www-form-urlencoded");
				os.println("Content-Length: " + postData.length());
			}
			os.println("");
			if (postData != null) {
				os.print(postData);
			}
			os.flush();
			String st;
			// Header e status
			String header = "";
			do {
				st = is.readLine();
				header = header + "\n" + st;
				if (st.startsWith("HTTP/1.")) {
					response.setStatusCode((new Integer(st.substring(9, 12))).intValue());
					response.setStatusMessage(st.substring(12));
				} else if (st.toUpperCase().startsWith("CONTENT-TYPE:")) {
					response.setContentType(st.substring(st.indexOf(' ') + 1));
				} else if (st.toUpperCase().startsWith("CONTENT-LENGTH:")) {
					response.setContentLength((new Integer(st.substring(st.indexOf(' ') + 1))).intValue());
				}
			} while (st != null && st.length() > 0);

			// Body
			if (response.isAscii()) {
				// Testo
				while ((st = is.readLine()) != null) {
					body.append(st);
					body.append("\n");
				}
				response.setBody(body.toString());
			} else {
				// Binario ?
				response.setSpooler(spooler);
				FileOutputStream fos = new FileOutputStream(spooler);
				byte[] buffer = new byte[1000];
				int len = 0;
				InputStream ibs = s.getInputStream();
				while ((len = ibs.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();
				fos.close();
			}
		} finally {
			if (os != null)
				os.close();
			if (is != null)
				is.close();
			if (s != null)
				s.close();
		} 
		return response; 
	}

	public static void main(String[] args) {
		/*
		 * HttpClient localClient = new HttpClient("uvt6", 8080);
		 * localClient.setTimeout(10000); try { HttpResponse res =
		 * localClient.doGet("/Anticipazione/noanswerservlet");
		 * System.out.println(res.getStatusCode() + res.getStatusMessage());
		 * System.out.println(res.getBody()); } catch(Exception ex) {
		 * ex.printStackTrace(); }
		 */
		//Category cat = Category.getInstance(HttpClient.class.getName());
		HttpClient httpClient = new HttpClient("ttssrv3", 80);
		httpClient.setTimeout(10000);
		try {
			// HttpResponse res =
			// httpClient.doGet("/Loquendo/loquendoservlet?nomefile=prova&testo=Ciao mi chiamo piero&option=1");
			HttpResponse res = httpClient.doPost("/Loquendo/loquendoservlet", "nomefile=prova&testo=Ciao mi chiamo piero&option=1");
			log.error(res.getStatusCode() + res.getStatusMessage());
			log.error(res.getBody());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		/*
		 * httpClient = new HttpClient("ttssrv3", 80);
		 * httpClient.setTimeout(50000); try { httpClient.setSpooler(new
		 * File("C:\\Temp\\Spool.wav")); HttpResponse res =
		 * httpClient.doGet("/TTS-audio/prova.wav");
		 * System.out.println(res.getStatusCode() + res.getStatusMessage()); }
		 * catch(Exception ex) { ex.printStackTrace(); }
		 */
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public void setSpooler(File spooler) {
		this.spooler = spooler;
	}
}
