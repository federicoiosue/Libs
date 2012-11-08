package it.autostrade.feio.test;

import java.io.File;

import it.autostrade.feio.utils.Replacer;



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
