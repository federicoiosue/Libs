package it.feio.utils.test;

import java.util.Calendar;
import java.util.Date;



public class TestScratch {

	
	public static void main(String[] args) {

		Date n = new Date(0);
		System.out.println(n);
		n = new Date();
		System.out.println(n);
		
		Calendar c = Calendar.getInstance();
		c.setTime(n);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		System.out.println(c.getTime());
		
		c.setTime(n);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		System.out.println(c.getTime());
	}

}



class Persona {

	public Persona() {}

	public Persona(String name, String surname, String address, int age) {
		super();
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.age = age;
	}

	String name;
	String surname;
	String address;
	int age;
}
