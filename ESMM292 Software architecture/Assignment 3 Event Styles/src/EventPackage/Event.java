/******************************************************************************************************************
* File:MessageQueue.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:	This class defines events. Events include the sender's ID, the eventID, and
*				a message field for sending small messages between entities participating in
*				the event system.
*
* Parameters:
*				EventId - Integer that indicates an id correlating to an event type. The event framework does not
*						  enforce an event ID semantic on the system.
*
*				SenderId - Long integer id that indicates who posted the event to the event manager. The sender
*						   id is issued when an entity registers with the event manager. No events can be posted
*						   by an entity until it is registered with the event manager. The participant's id is
*						   determined by the event manager at registration time and the id for every event is set
*						   to the participant's id before the event is posted.
*
*				MessageText - This is a string of text that is passed along with the event. Again, there is no
*							  particular semantic associated with the text.
*
* Internal Methods: None
*
******************************************************************************************************************/
package EventPackage;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Event implements Serializable
{

	private String MessageText;	// Any string message.
	private int EventId;		// Event Id is defined by the participant.
	private long SenderId;		// Id assigned at registration time by the event manager. The ID for every event is
								// set by the EventManagerInterface before the message is sent to the event manager.

	public Event(int EvtId, String Text )
	{
		MessageText = Text;
		EventId = EvtId;

	} // constructor

	public Event(int EvtId )
	{
		MessageText = null;
		EventId = EvtId;

	} // constructor

	/***************************************************************************
	* CONCRETE METHOD:: GetSenderID
	* Purpose: This method returns the ID of the participant that posted this
	*		   Event.
	*
	* Arguments: None
	*
	* Returns: long integer
	*
	* Exceptions: None
	*
	****************************************************************************/

	public long GetSenderId()
	{
		return SenderId;

	} // GetSenderId

	/***************************************************************************
	* CONCRETE METHOD:: SetSenderID
	* Purpose: This method sets the ID of Event to the long value.
	*
	* Arguments: long integer
	*
	* Returns: None
	*
	* Exceptions: None
	*
	****************************************************************************/

	public void SetSenderId( long id )
	{
		SenderId = id;

	} // GetSenderId

	/***************************************************************************
	* CONCRETE METHOD:: SetEventID
	* Purpose: This method returns the event ID of the posted event. There is not
	*		   semantic imposed on event IDs.
	*
	* Arguments: None
	*
	* Returns: int
	*
	* Exceptions: None
	*
	****************************************************************************/

	public int GetEventId()
	{
		return EventId;

	} // GetEventIO

	/***************************************************************************
	* CONCRETE METHOD:: GetMessage
	* Purpose: This method returns the message (if there is one) of the posted event. There is not
	*		   semantic imposed on event IDs.
	*
	* Arguments: None
	*
	* Returns: int
	*
	* Exceptions: None
	*
	****************************************************************************/

	public String GetMessage()
	{
		return MessageText;

	} // GetMessage

} // Event class