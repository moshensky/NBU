/******************************************************************************************************************
* File:EventManagerInterface.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 1 (ajl).
*
* Description: This class is the interface definition for the event manager services that are available to
* 			   participants
*
* Parameters: None
*
* Internal Methods: None
*
******************************************************************************************************************/
package EventPackage;

import java.rmi.*;

public interface RMIEventManagerInterface extends Remote
{

	/***************************************************************************
	* INTERFACE:: Register
	* Purpose: This interface is used to access the participant registration
	* 		   service on the EventManager
	*
	* Arguments: None
	*
	* Returns: long integer registration number
	*
	* Exceptions: RemoteException
	*
	****************************************************************************/

	public long Register() throws java.rmi.RemoteException;

	/***************************************************************************
	* INTERFACE:: UnRegister
	* Purpose:This interface is used to access the participant un-registration
	* 		   service on the EventManager
	*
	* Arguments: long integer registration number
	*
	* Returns: None
	*
	* Exceptions: RemoteException
	*
	****************************************************************************/

	public void UnRegister(long SenderID) throws java.rmi.RemoteException;

	/***************************************************************************
	* INTERFACE:: SendEvent
	* Purpose: This interface is used by participant to access the event sending
	* 		   service on the EventManager
	*
	* Arguments: Event object (see the class: Event.java)
	*
	* Returns: None
	*
	* Exceptions: RemoteException
	*
	****************************************************************************/

	public void SendEvent(Event m ) throws java.rmi.RemoteException;

	/***************************************************************************
	* INTERFACE:: GetEvent
	* Purpose: This interface is used to allow the participant access the event
	*		   queue on the EventManager
	*
	* Arguments: long integer registration number
	*
	* Returns: EventQueue object (see the class: EventQueue.java)
	*
	* Exceptions: RemoteException
	*
	****************************************************************************/

	public EventQueue GetEventQueue(long SenderID) throws java.rmi.RemoteException;

} // class
