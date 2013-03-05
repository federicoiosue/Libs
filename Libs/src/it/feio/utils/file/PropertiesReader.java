package it.feio.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
	
	public static Properties parse(String configFilePath) throws IOException {
		File configFile = new File(configFilePath);
		FileInputStream fis = new FileInputStream(configFile);
		Properties conf = new Properties();
		conf.load(fis);
		return conf;
	}
	
	
	
}