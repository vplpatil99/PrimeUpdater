package Prime.ProgramUpdater;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;


/**
 *
 * @author Administrator
 */
public class ConnectionDB {
	String ApplicationPath="D:\\ConsumptionReport\\";
    Connection getConnectionDB(){
    // Create a variable for the connection string.  
      //String connectionUrl = "jdbc:sqlserver://prime-1;databaseName=prime_003_new;user=sa;password=";
      String connectionUrl = "jdbc:sqlserver://"+getConnectionURL();

      // Declare the JDBC objects.  
      Connection con = null;  


      try {  
         // Establish the connection.  
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
         con = DriverManager.getConnection(connectionUrl);
    	  //mysql
    	  //Class.forName("com.mysql.jdbc.Driver");  
    	  //con=DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12221294","sql12221294","cvtWuX94tg");
      }  
      catch (Exception e) {  
         e.printStackTrace();  
      }
      return con;
   }  
    
	public String getConnectionURL()
	{
		String City_Name="";
		String s="";
		try {
		
	 	File file =new File(ApplicationPath+"ServerConfig.txt");
		Scanner sc = new Scanner(file);
		 
		    while (sc.hasNextLine()) {
		    if((s=sc.nextLine()).contains("ConnectionURL"))	
		    {			    
		      City_Name=s.substring(s.indexOf('=')+1);
		    }
		  }
		}catch(Exception e)
		{
			
		}

		 return City_Name;
	}
	public String getCityName()
	{
		String City_Name="";
		String s="";
		try {
		
	 	File file =new File(ApplicationPath+"ServerConfig.txt");
		Scanner sc = new Scanner(file);
		 
		    while (sc.hasNextLine()) {
		    if((s=sc.nextLine()).contains("City_Name"))	
		    {			    
		      City_Name=s.substring(s.indexOf('=')+1);
		    }
		  }
		}catch(Exception e)
		{
			
		}

		 return City_Name;
	}
	
	Connection getConnectionTallyserverDB(){
	    // Create a variable for the connection string.  
	      //String connectionUrl = "jdbc:sqlserver://prime-1;databaseName=prime_003_new;user=sa;password=";
	      String connectionUrl = "jdbc:sqlserver://27.106.125.26;databaseName=tallyDB;user=sa;password=optimalrx";

	      // Declare the JDBC objects.  
	      Connection con = null;  


	      try {  
	         // Establish the connection.  
	         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
	         con = DriverManager.getConnection(connectionUrl);
	    	  //mysql
	    	  //Class.forName("com.mysql.jdbc.Driver");  
	    	  //con=DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12221294","sql12221294","cvtWuX94tg");
	      }  
	      catch (Exception e) {  
	         e.printStackTrace();  
	      }
	      return con;
	   }  

}
