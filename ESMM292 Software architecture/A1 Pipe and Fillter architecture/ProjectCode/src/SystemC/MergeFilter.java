
public class MergeFilter extends FilterFramework
{
	public MergeFilter(int outNumPipes) {
		super(outNumPipes);
		// TODO Auto-generated constructor stub
	}

	public MergeFilter(int inNumPipes, int outNumPipes) 
	{
		super(inNumPipes, outNumPipes);
		// TODO Auto-generated constructor stub
	}

	public void run()
    {

		int frameLength = 72;	
		int bytesread = 0;					// Number of bytes read from the input file.
		int byteswritten = 0;				// Number of bytes written to the stream.
		byte databyte = 0;					// The byte of data read from the file
		
		byte pipeNum = 0;
		byte closePort = 0;

		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Middle Reading ");

		while (true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte
			*************************************************************/

			for(int i = 0; i < frameLength; i++)
			{
				try
				{
					databyte = ReadFilterInputPort(pipeNum);
					bytesread++;
					WriteFilterOutputPort(databyte);
					byteswritten++;
	
				} // try
	
				catch (EndOfStreamException e)
				{
					closePort++;
					if(closePort == getInNumPorts())
					{
						//TODO: stop reading only current Pipe. If all pipes are closed than exit
						ClosePorts();
						System.out.print( "\n" + this.getName() + ":: " + pipeNum + " ::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
						return;
					}
					break;
	
				} // catch
			}
			pipeNum++;
			if(pipeNum == 2)
			{
				pipeNum = 0;
			}
			
		} // while

   } // run
} //MergeFilter