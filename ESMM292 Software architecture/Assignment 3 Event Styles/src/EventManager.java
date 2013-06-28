/******************************************************************************************************************
* File:EventManager.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class is the event manager responsible for receiving and distributing events from participants
*			   and all associated house keeping chores. Communication with participants is via RMI. There are
*			   a number of RMI methods that allow participants to register, post events, get events,
*
* Parameters: None
*
* Internal Methods: None
*
******************************************************************************************************************/
import EventPackage.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

@SuppressWarnings("serial")
public class EventManager extends UnicastRemoteObject implements RMIEventManagerInterface
{
	static Vector<EventQueue> EventQueueList;	// This is the list of event queues.
	static RequestLogger l;  					// This is a request logger - Logger is a private inner class

	public EventManager() throws RemoteException
	{
		super();										// Required by RMI
		l = new RequestLogger();						// Screen logging object
		EventQueueList = new Vector<EventQueue>(15, 1);	// Queue for storing events

	} // Constructor

	/***************************************************************************
	* Main
	****************************************************************************/

	public static void main(String args[])
	{
		try
    	{
			// Here we start up the server. We first must instantiate a class of type PolicyDB

			InetAddress LocalHostAddress = InetAddress.getLocalHost();
			String EventManagerIpAddress = LocalHostAddress.getHostAddress();

			EventManager em = new EventManager();
	      	Naming.bind("EventManager", em);

	     	// Finally we notify the user that the server is ready.

			l.DisplayStatistics( "Server IP address::" + EventManagerIpAddress + ". Event manager ready." );

		} // try

		// Potential exceptions

		catch (Exception e)
		{
			l.DisplayStatistics( "Event manager startup error: " + e );

		} // catch

	} // main

	/***************************************************************************
	* Remote METHOD:: Register
	* Purpose: This method registers participants with the event manager.
	*
	* Arguments: None.
	*
	* Returns: long integer - the participants id
	*
	* Exceptions: None
	*
	****************************************************************************/

	synchronized public long Register() throws RemoteException
	{
		// Create a new queue and add it to the list of event queues.

		EventQueue mq = new EventQueue();
		EventQueueList.add( mq );

		l.DisplayStatistics( "Register event. Issued ID = " + mq.GetId() );

		return mq.GetId();

	} // Register

	/***************************************************************************
	* Remote METHOD:: UnRegister
	* Purpose: This method unregisters participants with the event manager.
	*
	* Arguments: long integer - the participants id
	*
	* Returns: None
	*
	* Exceptions: None
	*
	****************************************************************************/

	synchronized public void UnRegister(long id) throws RemoteException
	{
		EventQueue mq;
		boolean found = false;

		// Find the queue for id.

		for ( int i = 0; i < EventQueueList.size(); i++ )
		{
			//Get the queue for id and remove it from the list.

			mq =  EventQueueList.get(i);

			if (mq.GetId() == id)
			{
				mq = EventQueueList.remove(i);
				found = true;

			} // if

		} // for

		if (found)
			l.DisplayStatistics( "Unregistered ID::" + id );
		else
			l.DisplayStatistics( "Unregister error. ID:"+ id + " not found.");
	} // Register

	/***************************************************************************
	* Remote METHOD:: SendEvent
	* Purpose: This method allows participants to send events to the event
	*		   manager.
	*
	* Arguments: Event
	*
	* Returns: None
	*
	* Exceptions: None
	*
	****************************************************************************/

	synchronized public void SendEvent(Event m ) throws RemoteException
	{
		EventQueue mq;

		// For every queue on the list, add the event.

		for ( int i = 0; i < EventQueueList.size(); i++ )
		{
			mq = EventQueueList.get(i);
			mq.AddEvent(m);
			EventQueueList.set(i, mq);

		} // for

		l.DisplayStatistics( "Incoming event posted from ID: " + m.GetSenderId() );

	} // SendEvent

	/***************************************************************************
	* Remote METHOD:: GetEvent
	* Purpose: Get the event queue for a participant (id).
	*
	* Arguments: long id - participants id
	*
	* Returns: EventQueue
	*
	* Exceptions: None
	*
	****************************************************************************/

	synchronized public EventQueue GetEventQueue( long id ) throws RemoteException
	{
		EventQueue mq, temp =  null;
		boolean found = false;

		// Find the queue for id.

		for ( int i = 0; i < EventQueueList.size(); i++ )
		{
			mq =  EventQueueList.get(i);

			// Get each queue off of the list and see if it is id's queue
			// Once the queue is found, then get a copy of the queue, clear the
			// queue, and return the queue back to the participant

			if (mq.GetId() == id)
			{
				mq = EventQueueList.get(i);
				temp = mq.GetCopy();
				mq.ClearEventQueue();
				found = true;

			} // if

		} // for

		if (found)
				l.DisplayStatistics( "Get event queue request from ID: " + id + ". Event queue returned.");
		else
				l.DisplayStatistics( "Get event queue request from ID: " + id + ". ID not found.");

		return temp;

	} // GetEventList

	/***************************************************************************
	* INNER CLASS:: Logger
	* Purpose: This class longs requests by displaying them on the server with
	* 		   general statics after each remote call for services. This method
	*		   increments the number of service request from participants,
	*		   counts the number of active queues (registered participants), and
	*		   displays this information on the terminal.
	*
	* Arguments: None.
	*
	* Returns: None
	*
	* Exceptions: None
	*
	****************************************************************************/

	private class RequestLogger
	{
		int RequestsServiced = 0;	// This is the number of requests seviced

		void DisplayStatistics( String message )
		{
			RequestsServiced++;

			if ( message.length() == 0 )
			{
				System.out.println( "-------------------------------------------------------------------------------");
				System.out.println( "Number of requests: " + RequestsServiced );
				System.out.println( "Number of registered participants: " + EventQueueList.size() );
				System.out.println( "-------------------------------------------------------------------------------");

			} else {

				System.out.println( "-------------------------------------------------------------------------------");
				System.out.println( "Message:: " + message );
				System.out.println( "Number of requests: " + RequestsServiced );
				System.out.println( "Number of registered participants: " + EventQueueList.size() );
				System.out.println( "-------------------------------------------------------------------------------");

			} // if

		} // Register

	} // logger

} // EventManger class