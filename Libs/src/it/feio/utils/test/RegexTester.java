package it.feio.utils.test;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTester {


	public static void main(String[] args) {
		Console console = System.console();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;		
		String regex = null; 
		String out = null;
		boolean found = false;
		
		while (true) {
			
			if (console == null) {				
				try {
					 line = bufferedReader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				line = console.readLine("%nEnter your regex: ");
				regex = console.readLine("Enter input string to search: ");
			}

			found = false;
			Pattern pattern = Pattern.compile(line);
			Matcher matcher = pattern.matcher(regex);

			while (matcher.find()) {
				out = String.format("Trovato \"%s\" alla posizione %d fino alla %d.%n", matcher.group(), matcher.start(), matcher.end());
				if (console == null) {				
					System.out.println(out);
				} else {
					console.format(out);
				}				
				found = true;
			}
			
			if (!found) {
				out = String.format("Non espressione regolare non trovata.%n");
				if (console == null) {				
					System.out.println(out);
				} else {
					console.format(out);
				}	
			}
			
		}
	}
}
