/******************************************************************************************************************
* File:EventUtility.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class illustrates how to utilize the event manager and provides a few
* basic utilities that can help developers test and debug their systems.
*
* Parameters: None
*
* Internal Methods: None
*
******************************************************************************************************************/
import TermioPackage.*;
import EventPackage.*;

public class EventUtility
{
	public static void main(String args[])
	{
    	Termio UserInput = new Termio();// Termio IO Object
		String EvtMgrIP;				// Event Manager IP address
		String EvtMgrPort;				// Event Manager port
		boolean Done = false;			// Main loop flag
		String Option;					// Menu choice from user
		Event Evt = null;				// Event object
		boolean Error;					// Error flag
		EventQueue eq = null;			// Message Queue
		int EvtId = 0;					// User specified event ID
		EventManagerInterface ef = null;// Interface object to the event manager

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the event manager
		/////////////////////////////////////////////////////////////////////////////////

 		System.out.println( "\n\n\n\n" );

  		System.out.println( "Enter IP address of event manager or..." );
  		System.out.print( "press enter if on local machine: " );
		EvtMgrIP = UserInput.KeyboardReadString();

		System.out.println( "\n\n\n\n" );

		System.out.print("\n\nAttempting to register..." );

		if (EvtMgrIP.length() == 0)
		{
				try
				{
					// Here we create an event manager interface object. This assumes
					// that the event manager is on the local machine

					ef = new EventManagerInterface();
				}

				catch (Exception e)
				{
					System.out.println("Error instantiating event manager interface: " + e);

				} // catch

		} else {

			if (EvtMgrIP.length() != 0)
			{
				try
				{
					// Here we create an event manager interface object. This assumes
					// that the event manager is NOT on the local machine

					ef = new EventManagerInterface( EvtMgrIP );
				}

				catch (Exception e)
				{
					System.out.println("Error instantiating event manager interface: " + e);

				} // catch

			}// if

		}// if

		// Here we check to see if registration worked. If ef is null then the
		// event manager interface was not properly created.

		if (ef != null )
		{
			System.out.println("Registered with the event manager.\n\n" );

			while (!Done)
			{
				// Here is the main menu

				System.out.println( "Select an Option: \n" );
				System.out.println( "1: What is my ID?" );
				System.out.println( "2: What was my registration time? " );
				System.out.println( "3: Send Event Message." );
				System.out.println( "4: Get Event List." );
				System.out.println( "X: Exit \n" );
				System.out.print( "\n>>>> " );
				Option = UserInput.KeyboardReadString();

				//////////// option 1 ////////////

				// Here we print out the participants ID number. This is established when
				// the event interface is instantiated. If the participant is not
				// registered an exception is thrown.

				if ( Option.equals( "1" ) )
				{
			    	try
			    	{
						long MyParticipantID = ef.GetMyId();
						System.out.println( "\nMy participant id = " + MyParticipantID );

			    	} // try

			    	catch (Exception e)
			    	{
						System.out.println("Error:: " + e);

			    	} // catch

				}

				//////////// option 2 ////////////

				// Here we print out the participants registrations time. This is actual time
				// that the event interface was instantiated. If the participant is not
				// registered an exception is thrown.

				if ( Option.equals( "2" ) )
				{
			    	try
			    	{
						String MyRegistrationTime = ef.GetRegistrationTime();
						System.out.println( "\nMy participant id = " + MyRegistrationTime );

			    	} // try

			    	catch (Exception e)
			    	{
						System.out.println("Error:: " + e);

			    	} // catch

				}

				//////////// option 3 ////////////

				if ( Option.equals( "3" ) )
				{
					// Here we get an event ID from the user,... it has to be an integer,
					// so the input is verified. If the input is not an integer, we chastise
					// the user and ask them to try again...

					Error = true;

					while (Error)
					{
						System.out.println( "\nEnter an integer event ID: " );
						Option = UserInput.KeyboardReadString();

						if (UserInput.IsNumber(Option))
						{
							Error = false;
							EvtId = Integer.valueOf(Option).intValue();

						} else {

						System.out.println( "Please enter an integer value... try again..." );

						} // if

					} // while

					// Here ask the user to provide a string message to post with the
					// if event.

					System.out.println( "\nEnter a string to post with the event, or enter to continue: " );
					Option = UserInput.KeyboardReadString();

					// Here we create the event.

					Evt = new Event( EvtId, Option );

					// Here we send the event to the event manager.

			    	try
			    	{
						ef.SendEvent( Evt );
						System.out.println( "Message posted." );

			    	} // try

			    	catch (Exception e)
			    	{
						System.out.println("Error:: " + e);

			    	} // catch

				} // if

				//////////// option 4 ////////////

				if ( Option.equals( "4" ) )
				{
					// Here we get the event queue for this event interface from the event manager.

					Error = true;

			    	try
			    	{
						eq = ef.GetEventQueue();

						System.out.println( eq.GetSize() + " messages received..." );
						System.out.println( "=========================" );

						int qlen = eq.GetSize();

						for ( int i = 0; i < qlen; i++ )
						{
							Evt = eq.GetEvent();
							System.out.print( (i+1) + "::Sender ID: " + Evt.GetSenderId());
							System.out.print( ":: Event ID:: " + Evt.GetEventId());
							System.out.println("::" + Evt.GetMessage());

						} // for

						System.out.println("\n");

			    	} // try

			    	catch (Exception e)
			    	{
						System.out.println("Error getting event list: " + e);

			    	} // catch

				} // if

				//////////// option X ////////////

				if ( Option.equalsIgnoreCase( "X" ) )
				{
					// Here the user is done, so we set the Done flag and unregister
					// the event interface from the event manager. If you fail to
					// unregister, the event manager doesn't know to remove queues.
					// These become dead queues and they collect events and will eventually
					// cause problems for the event manager.

					Done = true;

					try
					{
						ef.UnRegister();

			    	} // try

			    	catch (Exception e)
			    	{
						System.out.println("Error unregistering: " + e);

			    	} // catch

				} // if

			} // while

		} else {

			System.out.println("Unable to register with the event manager.\n\n" );

		} // if

  	} // main

} // EventTest
