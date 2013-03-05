package it.feio.utils.file;

import java.io.File;
import java.io.InputStream;
import org.mozilla.universalchardet.UniversalDetector;


public class EncodingDetector {

	public static String detect(File file) throws java.io.IOException {
		return detect(file.getName());
	}

	public static String detect(String fileName) throws java.io.IOException {
		InputStream is = new java.io.FileInputStream(fileName);
		return detect(is);
	}

	public static String detect(InputStream is) throws java.io.IOException {
		byte[] buf = new byte[4096];

		UniversalDetector detector = new UniversalDetector(null);
		int nread;
		while ((nread = is.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();

		// detector.reset();
		return encoding;
	}
}