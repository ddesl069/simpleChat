import java.util.*;
import common.*;
public class ServerConsole implements ChatIF{

	Scanner fromConsole;
	EchoServer sv;
	public ServerConsole(EchoServer sv) {
		this.sv = sv;
		fromConsole = new Scanner(System.in);
	}
	
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println(">" + message);
		
	}
	public static void main(String[] args) {
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = 5555; //Set port to 5555
	    }
		
	    EchoServer sv = new EchoServer(port);
	    ServerConsole sc = new ServerConsole(sv);
	    sv.setSV(sc);
	    try 
	    {
	      sv.listen(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	    sc.accept();
	}
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        sv.handleMessageFromUser("Server message>"+message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

}
