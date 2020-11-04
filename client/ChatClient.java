// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    
  }

  
  //Instance methods ************************************************
   public void function() throws IOException {
	   openConnection();
	    handleMessageFromClientUI("#loginID" + loginID);
   }
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#")) {
    		holdMessage(message);
    	}
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		clientUI.display("Conncection is closed");
		quit();
		
	}
	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		System.out.print("Unable to connect to client");
	}
	void holdMessage(String message) throws IOException{
		if(message.equals("#quit")) {
			quit();
		}else if(message.equals("#logoff")) {
			closeConnection();
		}else if(message.equals("#sethost")) {
			if(isConnected()==false) {
				setHost(message.substring(9));
			}else {
				clientUI.display("you are still connected");
			}
		}else if(message.equals("#setport")) {
			if(isConnected() == false) {
				setPort(Integer.parseInt(message.substring(9)));
			}else {
				clientUI.display("you are still connected");
			}
		}else if(message.equals("#login")) {
			if(isConnected()== false) {
				openConnection();
				handleMessageFromClientUI("#loginID" + loginID);
			}else {
				clientUI.display("error already connnected");
			}
		}else if(message.equals("#gethost")) {
			getHost();
		}else if(message.equals("#getport")) {
			getPort();
		}
	}
}
//End of ChatClient class
