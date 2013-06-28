/******************************************************************************************************************
* File: EventManagerInterface.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class provides an interface to the event manager for participants (processes), enabling them to
*			   to send and receive events between participants. A participant is any thing (thread, object, process)
*			   that instantiates an EventEventManagerInterface object - this automatically attempts to register that
*			   entity with the event manager
*
* Parameters: None
*
* Internal Methods: SendEvent - Sends an event to the event manager
*					GetEventQueue - Gets a participants event queue from the event manager.
*					GetMyId - Gets a participants registration ID
*				    GetRegistrationTime - Gets the point in time when a participant registered with the
*										  event manager
*
******************************************************************************************************************/
package EventPackage;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class EventManagerInterface
{
	private long ParticipantId = -1;			// This processes ID
	private RMIEventManagerInterface em = null;	// Event manager interface object
	private String DEFAULTPORT = "1099";		// Default event manager port

	/***************************************************************************
	* Exceptions::
	*
	****************************************************************************/

	class SendEventException extends Exception
	{
		SendEventException()
		{ super(); }

		SendEventException(String s)
		{ super(s); }

	} // Exception

	class GetEventException extends Exception
	{
		GetEventException()
		{ super(); }

		GetEventException(String s)
		{ super(s); }

	} // Exception

	class ParticipantAlreadyRegisteredException extends Exception
	{
		ParticipantAlreadyRegisteredException()
		{ super(); }

		ParticipantAlreadyRegisteredException(String s)
		{ super(s); }

	} // Exception

	class ParticipantNotRegisteredException extends Exception
	{
		ParticipantNotRegisteredException()
		{ super(); }

		ParticipantNotRegisteredException(String s)
		{ super(s); }

	} // Exception

	class LocatingEventManagerException extends Exception
	{
		LocatingEventManagerException()
		{ super(); }

		LocatingEventManagerException(String s)
		{ super(s); }

	} // Exception

	class LocalHostIpAddressException extends Exception
	{
		LocalHostIpAddressException()
		{ super(); }

		LocalHostIpAddressException(String s)
		{ super(s); }

	} // Exception

	class RegistrationException extends Exception
	{
		RegistrationException()
		{ super(); }

		RegistrationException(String s)
		{ super(s); }

	} // Exception

	/***************************************************************************
	* CONSTRUCTOR:: EventManagerInterface()
	* Purpose: This method registers participants with the event manager. This
	*	   	   instantiation should be used when the event manager is on the
	*		   local machine, using the default port (1099).
	*
	* Arguments: None.
	*
	* Returns: None.
	*
	* Exceptions: LocatingEventManagerException, RegistrationException,
	*			  ParticipantAlreadyRegisteredException
	*
	****************************************************************************/

	public EventManagerInterface() throws LocatingEventManagerException, RegistrationException, ParticipantAlreadyRegisteredException
	{
		// First we check to see if the participant is already registered. If not
		// we go on with the registration. If the are, we throw an exception.

		if (ParticipantId == -1)
		{
			try
			{
				em = (RMIEventManagerInterface) Naming.lookup( "EventManager" );

			} // try

	    	catch (Exception e)
	    	{
				throw new LocatingEventManagerException( "Event manager not found on local machine at default port (1099)" );

	    	} // catch

		   	try
		   	{
				ParticipantId = em.Register();

			} // try

			catch (Exception e)
			{
				throw new RegistrationException( "Error registering participant " + ParticipantId );

			} // catch

		} else {

			throw new ParticipantAlreadyRegisteredException( "Participant already registered " + ParticipantId );

		} // if

	} // EventManagerInterface

	/***************************************************************************
	* CONSTRUCTOR:: EventManagerInterface( String IPAddress )
	* Purpose: This method registers participants with the event manager at a
	* 		   specified IP address. This instantiation is used  when the
	*		   EventManager is not on a local machine.
	*
	* Arguments: None.
	*
	* Returns: long integer - the participants id
	*
	* Exceptions: LocatingEventManagerException, RegistrationException,
	*			  ParticipantAlreadyRegisteredException
	*
	****************************************************************************/

	public EventManagerInterface( String ServerIpAddress ) throws LocatingEventManagerException,
	RegistrationException, ParticipantAlreadyRegisteredException
	{
		// Assumes that the event manager is on another machine. The user must provide the IP
		// address of the event manager and the port number

		String EMServer = "//" + ServerIpAddress + ":" + DEFAULTPORT + "/EventManager";

		if (ParticipantId == -1)
		{
			try
			{
				em = (RMIEventManagerInterface) Naming.lookup( EMServer );

			} // try

	    	catch (Exception e)
	    	{
				throw new LocatingEventManagerException( "Event manager not found on machine at:" + ServerIpAddress + "::" + e );

	    	} // catch

		   	try
		   	{
				ParticipantId = em.Register();

			} // try

			catch (Exception e)
			{
				throw new RegistrationException( "Error registering participant " + ParticipantId );

			} // catch

		} else {

			throw new ParticipantAlreadyRegisteredException( "Participant already registered " + ParticipantId );

		} // if

	} // EventManagerInterface

	/***************************************************************************
	* CONCRETE METHOD:: GetMyId
	* Purpose: This method allows participants to get their participant Id.
	*
	* Arguments: None.
	*
	* Returns: long integer - the participants id
	*
	* Exceptions: ParticipantNotRegisteredException
	*
	****************************************************************************/

	public long GetMyId() throws ParticipantNotRegisteredException
	{
		if (ParticipantId != -1)
		{
			return ParticipantId;

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // GetMyId

	/***************************************************************************
	* CONCRETE METHOD:: GetRegistrationTime
	* Purpose: This method allows participants to obtain the time of registration.
	*
	* Arguments: None.
	*
	* Returns: String time stamp in the format: yyyy MM dd::hh:mm:ss:SSS
	*											yyyy = year
	*											MM = month
	*											dd = day
	*											hh = hour
	*											mm = minutes
	*											ss = seconds
	*											SSS = milliseconds
	*
	* Exceptions: ParticipantNotRegisteredException
	*
	****************************************************************************/

	public String GetRegistrationTime() throws ParticipantNotRegisteredException
	{
		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

		if (ParticipantId != -1)
		{
			TimeStamp.setTimeInMillis(ParticipantId);
			return ( TimeStampFormat.format(TimeStamp.getTime()) );

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // GetRegistrationTime

	/***************************************************************************
	* CONCRETE METHOD:: SendEvent
	* Purpose: This method sends an event to the event manager.
	*
	* Arguments: Event object.
	*
	* Returns: None.
	*
	* Exceptions: ParticipantNotRegisteredException, SendEventException
	*
	****************************************************************************/

	public void SendEvent( Event evt ) throws ParticipantNotRegisteredException, SendEventException
	{
		if (ParticipantId != -1)
		{
		   	try
	    	{
				evt.SetSenderId( ParticipantId );
				em.SendEvent( evt );

	    	} // try

			catch (Exception e)
			{
				 throw new SendEventException( "Error sending event" + e );

			} // catch

		} else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // SendEvent

	/***************************************************************************
	* CONCRETE METHOD:: GetEvent
	* Purpose: This method sends an event to the event manager.
	*
	* Arguments: None.
	*
	* Returns: Event object.
	*
	* Exceptions: ParticipantNotRegisteredException, GetEventException
	*
	****************************************************************************/

	public EventQueue GetEventQueue() throws ParticipantNotRegisteredException, GetEventException
	{
		EventQueue eq = null;

		if (ParticipantId != -1)
		{
	    	try
	    	{
				eq = em.GetEventQueue(ParticipantId);

	    	} // try

	    	catch (Exception e)
	    	{
				 throw new GetEventException( "Error getting event" + e );

	    	} // catch

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

		return eq;

	} // GetEventQueue

	/***************************************************************************
	* CONCRETE METHOD:: UnRegister
	* Purpose: This method is called when the object is no longer used. Essentially
	* this method unregisters participants from the event manager. It is important
	* that participants actively unregister with the event manager. Failure to do
	* so will cause unconnected queues to fill up with messages over time. This
	* will result in a memory leak and eventual failure of the event manager.
	*
	* Arguments: None.
	*
	* Returns: None.
	*
	* Exceptions: None
	*
	****************************************************************************/

	public void UnRegister() throws ParticipantNotRegisteredException, RegistrationException
	{
		if (ParticipantId != -1)
		{
		   	try
		   	{
				em.UnRegister(ParticipantId);

		   	} // try

		   	catch (Exception e)
		   	{
				throw new RegistrationException( "Error unregistering" + e );

		    } // catch

	    } else {

			throw new ParticipantNotRegisteredException( "Participant not registered" );

		} // if

	} // UnRegister

} // EventManagerInterface