
package net.myorb.external.anaconda;

/**
 * issue commands to server using SimpleStreamIO TextSource/Sink features.
 * @author Michael
 */
public class ServerAccess extends net.myorb.data.abstractions.ServerAccess
{


	/**
	 * @param hostName the host running the server
	 * @param portNumber the port number the service listens on
	 */
	public ServerAccess
		(
			String hostName, int portNumber
		)
	{
		super (hostName, portNumber);
	}


	/**
	 * issue STOP command
	 *  to Anaconda server
	 * @param args not used
	 * @throws Exception for IO errors
	 */
	public static void main (String[] args) throws Exception
	{
		ServerAccess access = new ServerAccess ("LOCALHOST", 8080);
		System.out.println (access.issueRequest ("STOP"));
	}


}

