/******************************************************************************************************************
* File:HumiditySensor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a humidity sensor. It polls the event manager for events corresponding to changes in state
* of the humidifier or dehumidifier and reacts to them by trending the relative humidity up or down. The current
* relative humidity is posted to the event manager.
*
* Parameters: IP address of the event manager (on command line). If blank, it is assumed that the event manager is
* on the local machine.
*
* Internal Methods:
*	float GetRandomNumber()
*	boolean CoinToss()
*   void PostHumidity(EventManagerInterface ei, float humidity )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import EventPackage.*;
import java.util.*;

class HumiditySensor
{
		
	public static void main(String args[])
	{
		String EvtMgrIP;					// Event Manager IP address
		Event Evt = null;					// Event object
		EventQueue eq = null;				// Message Queue
		int EvtId = 0;						// User specified event ID
		EventManagerInterface em = null;	// Interface object to the event manager
		boolean HumidifierState = false;	// Humidifier state: false == off, true == on
		boolean DehumidifierState = false;	// Dehumidifier state: false == off, true == on
		float RelativeHumidity;				// Current simulated ambient room humidity
		float DriftValue;					// The amount of humidity gained or lost
		int	Delay = 2500;					// The loop delay (2.5 seconds)
		boolean Done = false;				// Loop termination flag


		
		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the event manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length == 0 )
 		{
			// event manager is on the local system

			System.out.println("\n\nAttempting to register on the local machine..." );

			try
			{
				// Here we create an event manager interface object. This assumes
				// that the event manager is on the local machine

				em = new EventManagerInterface();
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating event manager interface: " + e);

			} // catch

		} else {

			// event manager is not on the local system

			EvtMgrIP = args[0];

			System.out.println("\n\nAttempting to register on the machine:: " + EvtMgrIP );

			try
			{
				// Here we create an event manager interface object. This assumes
				// that the event manager is NOT on the local machine

				em = new EventManagerInterface( EvtMgrIP );
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating event manager interface: " + e);

			} // catch

		} // if

		// Here we check to see if registration worked. If ef is null then the
		// event manager interface was not properly created.

		if (em != null)
		{

			// We create a message window. Note that we place this panel about 1/2 across 
			// and 2/3s down the screen
			
			float WinPosX = 0.5f; 	//This is the X position of the message window in terms 
									//of a percentage of the screen height
			float WinPosY = 0.60f;	//This is the Y position of the message window in terms 
								 	//of a percentage of the screen height 
			
			MessageWindow mw = new MessageWindow("Humidity Sensor", WinPosX, WinPosY);

			mw.WriteMessage("Registered with the event manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				mw.WriteMessage("Error:: " + e);

			} // catch

			mw.WriteMessage("\nInitializing Humidity Simulation::" );

			RelativeHumidity = GetRandomNumber() * (float) 100.00;

			if ( CoinToss() )
			{
				DriftValue = GetRandomNumber() * (float) -1.0;

			} else {

				DriftValue = GetRandomNumber();

			} // if

			mw.WriteMessage("   Initial Humidity Set:: " + RelativeHumidity );
			mw.WriteMessage("   Drift Value Set:: " + DriftValue );

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			mw.WriteMessage("Beginning Simulation... ");


			while ( !Done )
			{
				// Post the current relative humidity

				PostHumidity( em, RelativeHumidity );

				mw.WriteMessage("Current Relative Humidity:: " + RelativeHumidity + "%");

				// Get the message queue

				try
				{
					eq = em.GetEventQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting event queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for EventIDs = -4, this means the the humidify or
				// dehumidifier has been turned on/off. Note that we get all the messages
				// from the queue at once... there is a 2.5 second delay between samples,..
				// so the assumption is that there should only be a message at most.
				// If there are more, it is the last message that will effect the
				// output of the humidity as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Evt = eq.GetEvent();

					if ( Evt.GetEventId() == -4 )
					{
						if (Evt.GetMessage().equalsIgnoreCase("H1")) // humidifier on
						{
							HumidifierState = true;

						} // if

						if (Evt.GetMessage().equalsIgnoreCase("H0")) // humidifier off
						{
							HumidifierState = false;

						} // if

						if (Evt.GetMessage().equalsIgnoreCase("D1")) // dehumidifier on
						{
							DehumidifierState = true;

						} // if

						if (Evt.GetMessage().equalsIgnoreCase("D0")) // dehumidifier off
						{
							DehumidifierState = false;

						} // if

					} // if

					// If the event ID == 99 then this is a signal that the simulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the event manager.

					if ( Evt.GetEventId() == 99 )
					{
						Done = true;

						try
						{
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
							mw.WriteMessage("Error unregistering: " + e);

				    	} // catch

				    	mw.WriteMessage("\n\nSimulation Stopped. \n");

					} // if

				} // for

				// Now we trend the relative humidity according to the status of the
				// humidifier/dehumidifier controller.

				if (HumidifierState)
				{
					RelativeHumidity += GetRandomNumber();

				} // if humidifier is on

				if (!HumidifierState && !DehumidifierState)
				{
					RelativeHumidity += DriftValue;

				} // if both the humidifier and dehumidifier are off

				if (DehumidifierState)
				{
					RelativeHumidity -= GetRandomNumber();

				} // if dehumidifier is on

				// Here we wait for a 2.5 seconds before we start the next sample

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the event manager.\n\n" );

		} // if

	} // main

	/***************************************************************************
	* CONCRETE METHOD:: GetRandomNumber
	* Purpose: This method provides the simulation with random floating point
	*		   humidity values between 0.1 and 0.9.
	*
	* Arguments: None.
	*
	* Returns: float
	*
	* Exceptions: None
	*
	***************************************************************************/

	static private float GetRandomNumber()
	{
		Random r = new Random();
		Float Val;

		Val = Float.valueOf((float)-1.0);

		while( Val < 0.1 )
		{
			Val = r.nextFloat();
	 	}

		return( Val.floatValue() );

	} // GetRandomNumber

	/***************************************************************************
	* CONCRETE METHOD:: CoinToss
	* Purpose: This method provides a random true or false value used for
	* determining the positiveness or negativeness of the drift value.
	*
	* Arguments: None.
	*
	* Returns: boolean
	*
	* Exceptions: None
	*
	***************************************************************************/

	static private boolean CoinToss()
	{
		Random r = new Random();

		return(r.nextBoolean());

	} // CoinToss

	/***************************************************************************
	* CONCRETE METHOD:: PostHumidity
	* Purpose: This method posts the specified relative humidity value to the
	* specified event manager. This method assumes an event ID of 2.
	*
	* Arguments: EventManagerInterface ei - this is the eventmanger interface
	*			 where the event will be posted.
	*
	*			 float humidity - this is the humidity value.
	*
	* Returns: none
	*
	* Exceptions: None
	*
	***************************************************************************/

	static private void PostHumidity(EventManagerInterface ei, float humidity )
	{
		// Here we create the event.

		Event evt = new Event( (int) 2, String.valueOf(humidity) );

		// Here we send the event to the event manager.

		try
		{
			ei.SendEvent( evt );
			//mw.WriteMessage( "Sent Humidity Event" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting Relative Humidity:: " + e );

		} // catch

	} // PostHumidity

} // Humidity Sensor