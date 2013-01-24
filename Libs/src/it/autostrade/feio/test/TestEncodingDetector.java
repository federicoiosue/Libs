package it.autostrade.feio.test;


import it.autostrade.feio.utils.file.EncodingDetector;

public class TestEncodingDetector {

	public static void main(String[] args) throws java.io.IOException {
		String fileName = "provaDwnlXml.xml";
		String encoding = EncodingDetector.detect(fileName);
		System.out.println(encoding);
	}
}
