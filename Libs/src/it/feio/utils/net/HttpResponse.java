package it.feio.utils.net;

import java.io.*;

public class HttpResponse {
  public final static int ST_OK = 200;

  private int statusCode, contentLength;
  private String statusMessage, contentType;
  private String body;
  private File spooler;
  //private boolean ascii;

  public HttpResponse() { }

  public int getStatusCode() { return statusCode; }
  public String getStatusMessage() { return statusMessage; }
  public int getContentLength() { return contentLength; }
  public String getContentType() { return contentType; }
  public String getBody() { return body.toString(); }
  public File getSpooler() { return spooler; }
  public boolean isAscii() { return contentType.startsWith("text"); }
  void setSpooler(File spooler) { this.spooler = spooler; }
  //void setAscii(boolean ascii) { this.ascii = ascii; }
  void setBody(String body) { this.body = body; }
  void setContentLength(int contentLength) { this.contentLength = contentLength; }
  void setContentType(String contentType) { this.contentType = contentType; }
  void setStatusCode(int statusCode) { this.statusCode = statusCode; }
  void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
}
