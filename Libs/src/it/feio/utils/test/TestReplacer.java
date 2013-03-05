package it.feio.utils.test;

import it.feio.utils.file.Replacer;
import java.io.File;



public class TestReplacer {

	public static void main(String[] args) {

//		String[] pattern = {"ciao", "caro", "amico"};
//		String[] replace = {"fancuffia", "pezzente", "ingrato"};
		File of = null;
		
		try {
//			of = Replacer.replace( "C:\\Documents and Settings\\17000026\\Desktop\\feio\\file.txt");
			of = Replacer.replace("C:\\Documents and Settings\\17000026\\Desktop\\feio\\pro.xml", "C:\\Documents and Settings\\17000026\\Desktop\\feio\\pro1.xml");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		

	}
}
