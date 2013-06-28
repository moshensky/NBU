/******************************************************************************************************************
* File:EventQueue.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class defines event queues which are stored by the EventManger. Each registered participant
*			   has an event queue assigned to them. As events are sent by registered participants to the EventManger
*			   they are posted in each queue. Queues are removed when participants unregister.
*
* Parameters:
*			   EventList - This is the list of event objects
*			   id - this is the participant's registration id
*			   ListSize - this variable indicates how many events are in the event queue.
*
* Internal Methods: None
*
******************************************************************************************************************/
package EventPackage;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class EventQueue implements Serializable
{
	private Vector<Event> EventList;// This is the list of events associated with a participant
	private long QueueId;			// This is the participants id
	private	int ListSize;			// This is the size of the list

	public EventQueue()
	{
		EventList = new Vector<Event> (15, 1);
		Calendar TimeStamp = Calendar.getInstance();
		QueueId = TimeStamp.getTimeInMillis();
		ListSize = 0;

	} // constructor

	/***************************************************************************
	* CONCRETE METHOD:: GetId
	* Purpose: This method returns the event queue id (which is the participants id).
	*
	* Arguments: None
	*
	* Returns: long integet
	*
	* Exceptions: None
	*
	****************************************************************************/

	public long GetId()
	{
		return QueueId;

	} // AddEvent

	/***************************************************************************
	* CONCRETE METHOD:: GetSize
	* Purpose: This method returns the size of the queue.
	*
	* Arguments: None
	*
	* Returns: int
	*
	* Exceptions: None
	*
	****************************************************************************/

	public int GetSize()
	{
		return EventList.size();

	} // AddEvent

	/***************************************************************************
	* CONCRETE METHOD:: AddEvent
	* Purpose: This method adds an event to the list arriving events are
	*		   appended to the end of the list.
	*
	* Arguments: Event from a participant
	*
	* Returns: None
	*
	* Exceptions: None
	*
	****************************************************************************/

	public void AddEvent( Event m )
	{
		EventList.add( m );

	} // AddEvent

	/***************************************************************************
	* CONCRETE METHOD:: GetEvent
	* Purpose: This method gets the event off of the front of the list. This is
	*		   the oldest event in the list (arriving events are appended to the
	*		   list, hence the newest event is at the end of the list). This
	*		   method removes events from the list.
	*
	* Arguments: None
	*
	* Returns: Event
	*
	* Exceptions: None
	*
	****************************************************************************/

	public Event GetEvent()
	{
		Event m = null;

		if (EventList.size() > 0)
		{
			m = EventList.get(0);
			EventList.removeElementAt(0);

		} // if

		return m;

	} // GetEvent

	/***************************************************************************
	* CONCRETE METHOD:: ClearEventQueue
	* Purpose: This method will clears all the events the event queue.
	*
	* Arguments: None
	*
	* Returns: None
	*
	* Exceptions: None
	*
	****************************************************************************/

	public void ClearEventQueue()
	{
		EventList.removeAllElements();

	} // ClearEventQueue

	/***************************************************************************
	* CONCRETE METHOD:: GetCopy
	* Purpose: This method is used to obtain a copy of the event queue. This
	*		   method returns a second copy (separate memory) of the queue, not
	*		   a pointer to the queue.
	*
	* Arguments: None
	*
	* Returns: EventQueue. This method returns a second copy (separate memory)
	*		   of the queue, not a pointer to the queue.
	*
	* Exceptions: None
	*
	****************************************************************************/
   	@SuppressWarnings("unchecked")

	public EventQueue GetCopy()
	{
		EventQueue mq = new EventQueue();
		mq.QueueId = QueueId;
		mq.EventList = (Vector<Event>) EventList.clone();

		return mq ;

	} // GetCopy

} // EventQueue class