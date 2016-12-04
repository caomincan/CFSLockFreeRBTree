package hw1;

public class Database
{
	String Movie;
	String Direct;
	String Date;
	String Seen;

	void DataBase()
	{
		Movie = null;
		Direct = null;
		Date = null;
		Seen = null; 
	}
	
	void DataBase(String title, String direct, String date, String seen)
	{
		Movie = title;
		Direct = direct;
		Date = date;
		Seen = seen; 
	}
	
		
	void A(String title)
	{
		Movie = title;
	}

	void B(String direct)
	{
		Direct = direct;
	}
	
	void C(String date)
	{
		Date = date;
	}
	void D(String seen)
	{
		Seen = seen; 
	}
	

}