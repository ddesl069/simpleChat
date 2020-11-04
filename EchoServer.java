// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
	ServerConsole sv;
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  if(msg.toString().startsWith("#login ")) {
		  if(client.getInfo("login_id")==null) {
			  client.setInfo("login_id", msg.toString().substring(7));
			  this.sendToAllClients(client.getInfo("login_id")+ "has id");
			  sv.display(client.getInfo("login_id")+ "has logged in");
		  }else {
			  try {
				  client.close();
			  }catch (IOException e) {}
		  }
	  }else {
		  this.sendToAllClients(client.getInfo("login_id")+ ":"+ sv);
	  }
	  
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
	  sendToAllClients("Connected to client " +client.toString() );
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(
    ConnectionToClient client) {
	  sendToAllClients(client.toString()+"is diconected");
  }

  /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
	  sendToAllClients("client has disconected");
  }
  public void setConsole() {
	  this.sv = sv;
  }
  public void function(String msg) throws IOException {
	  if(msg.equals("#quit")) {
		  close();
	  }else if (msg.equals("#stop")) {
		  stopListening();
	  }else if(msg.equals("#close")) {
		  close();
	  }else if(msg.equals("sethost")) {
		if(isListening()==false) {
			if(msg.length()>= 10) {
				setPort(Integer.parseInt(msg.substring(9)));
			}else {
				sv.display("not a valid host");
			}
		}  
	  }else if(msg.equals("#start")) {
		  if(isListening()==false) {
			  listen();
		  }else {
			  sv.display("server is already on");
		  }
	  }else if(msg.equals("#getport")) {
		  sv.display(Integer.toString(getPort()));
	  }
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
