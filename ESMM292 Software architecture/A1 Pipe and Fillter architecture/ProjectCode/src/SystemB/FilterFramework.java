
import java.io.*;
import java.nio.ByteBuffer;

public class FilterFramework extends Thread {
    // Define filter input and output ports

    private PipedInputStream[] InputReadPort;
    private PipedOutputStream[] OutputWritePort;
    private FilterFramework[] InputFilter;
    public static final byte outPipe1 = 0;
    public static final byte outPipe2 = 1;
    public static final byte inPipe1 = 0;
    public static final byte inPipe2 = 1;
    private int inNumPorts = 1;
    private int outNumPorts = 1;
    public static final int ID_LENGTH = 4;
    public static final int MEASUREMENT_LENGTH = 8;

    public int getInNumPorts() {
        return inNumPorts;
    }

    public int getOutNumPorts() {
        return outNumPorts;
    }

    public FilterFramework(int outNumPipes) {
        outNumPorts = outNumPipes;

        OutputWritePort = new PipedOutputStream[outNumPipes];
        for (int i = 0; i < outNumPipes; i++) {
            OutputWritePort[i] = new PipedOutputStream();
        }

        InputReadPort = new PipedInputStream[inNumPorts];
        InputReadPort[0] = new PipedInputStream();

        InputFilter = new FilterFramework[1];
    }

    // The following reference to a filter is used because java pipes are able to reliably
    // detect broken pipes on the input port of the filter. This variable will point to
    // the previous filter in the network and when it dies, we know that it has closed its
    // output pipe and will send no more data.
    public FilterFramework(int inNumPipes, int outNumPipes) {
        outNumPorts = outNumPipes;
        inNumPorts = inNumPipes;

        OutputWritePort = new PipedOutputStream[outNumPipes];
        for (int i = 0; i < outNumPipes; i++) {
            OutputWritePort[i] = new PipedOutputStream();
        }

        InputReadPort = new PipedInputStream[inNumPipes];
        for (int i = 0; i < inNumPipes; i++) {
            InputReadPort[i] = new PipedInputStream();
        }

        InputFilter = new FilterFramework[inNumPipes];
    }

    /**
     * *************************************************************************
     * InnerClass:: EndOfStreamExeception Purpose: This
     *
     *
     *
     * Arguments: none
     *
     * Returns: none
     *
     * Exceptions: none
     *
     ***************************************************************************
     */
    @SuppressWarnings("serial")
    public class EndOfStreamException extends Exception {

        EndOfStreamException() {
            super();
        }

        EndOfStreamException(String s) {
            super(s);
        }
    } // class

    /**
     * *************************************************************************
     * CONCRETE METHOD:: Connect Purpose: This method connects filters to each
     * other. All connections are through the inputport of each filter. That is
     * each filter's inputport is connected to another filter's output port
     * through this method.
     *
     * Arguments: FilterFramework - this is the filter that this filter will
     * connect to.
     *
     * Returns: void
     *
     * Exceptions: IOException
     *
     ***************************************************************************
     */
    public void Connect(FilterFramework Filter) {
        try {
            // Connect this filter's input to the upstream pipe's output stream

            InputReadPort[0].connect(Filter.OutputWritePort[0]);
            InputFilter[0] = Filter;

        } // try
        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + Error);

        } // catch

    } // Connect

    public void Connect(FilterFramework[] filters) {
        // TODO Auto-generated method stub
        try {
            // Connect this filter's input to the upstream pipe's output stream
            for (int i = 0; i < filters.length; i++) {
                InputFilter[i] = filters[i];
                InputReadPort[i].connect(filters[i].OutputWritePort[0]);
            }
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + Error);

        } // catch

    }

    public void Connect(FilterFramework Filter, byte outNumPipe) {
        try {
            // Connect this filter's input to the upstream pipe's output stream
            InputReadPort[0].connect(Filter.OutputWritePort[outNumPipe]);
            InputFilter[0] = Filter;

        } // try
        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + Error);

        } // catch

    } // Connect

    /**
     * *************************************************************************
     * CONCRETE METHOD:: ReadFilterInputPort Purpose: This method reads data
     * from the input port one byte at a time.
     *
     * Arguments: void
     *
     * Returns: byte of data read from the input port of the filter.
     *
     * Exceptions: IOExecption, EndOfStreamException (rethrown)
     *
     ***************************************************************************
     */
    protected byte ReadFilterInputPort(byte inNumPipe) throws EndOfStreamException {
        byte datum = 0;

        try {
            while (InputReadPort[inNumPipe].available() == 0) {
                if (EndOfInputStream(inNumPipe)) {
                    //TODO: check if all inPipes are closed before closing
                    throw new EndOfStreamException("End of input stream reached");

                } //if

                sleep(250);

            } // while			
        } // try
        catch (EndOfStreamException Error) {
            throw Error;

        } // catch
        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Error in read port wait loop::" + Error);

        } // catch

        /**
         * *********************************************************************
         * If at least one byte of data is available on the input pipe we can
         * read it. We read and write one byte to and from ports.
         * *********************************************************************
         */
        try {
            datum = (byte) InputReadPort[inNumPipe].read();
            return datum;

        } // try
        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe read error::" + Error);
            return datum;

        } // catch

    } // ReadFilterPort

    /**
     * *************************************************************************
     * CONCRETE METHOD:: WriteFilterOutputPort Purpose: This method writes data
     * to the output port one byte at a time.
     *
     * Arguments: byte datum - This is the byte that will be written on the
     * output port.of the filter.
     *
     * Returns: void
     *
     * Exceptions: IOException
     *
     ***************************************************************************
     */
    protected void WriteFilterOutputPort(byte datum) {
        try {
            for (int i = 0; i < OutputWritePort.length; i++) {
                OutputWritePort[i].write((int) datum);
                OutputWritePort[i].flush();
            }


        } // try
        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe write error::" + Error);

        } // catch

        return;

    } // WriteFilterPort

    protected void WriteFilterOutputPort(byte datum, int port) {
        try {
            OutputWritePort[port].write((int) datum);
            OutputWritePort[port].flush();


        } // try
        catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe write error::" + Error);

        } // catch

        return;

    } // WriteFilterPort

    /**
     * *************************************************************************
     * CONCRETE METHOD:: EndOfInputStream Purpose: This method is used within
     * this framework which is why it is private It returns a true when there is
     * no more data to read on the input port of the instance filter. What it
     * really does is to check if the upstream filter is still alive. This is
     * done because Java does not reliably handle broken input pipes and will
     * often continue to read (junk) from a broken input pipe.
     *
     * Arguments: void
     *
     * Returns: A value of true if the previous filter has stopped sending data,
     * false if it is still alive and sending data.
     *
     * Exceptions: none
     *
     ***************************************************************************
     */
    private boolean EndOfInputStream(byte inNumPipe) {
        if (InputFilter[inNumPipe].isAlive()) {
            return false;

        } else {

            return true;

        } // if

    } // EndOfInputStream

    /**
     * *************************************************************************
     * CONCRETE METHOD:: ClosePorts Purpose: This method is used to close the
     * input and output ports of the filter. It is important that filters close
     * their ports before the filter thread exits.
     *
     * Arguments: void
     *
     * Returns: void
     *
     * Exceptions: IOExecption
     *
     ***************************************************************************
     */
    protected void ClosePorts() {
        try {
            for (int i = 0; i < InputReadPort.length; i++) {
                InputReadPort[i].close();
            }

            for (int i = 0; i < OutputWritePort.length; i++) {
                OutputWritePort[i].close();
            }

        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " ClosePorts error::" + Error);

        } // catch

    } // ClosePorts

    //Helper method for writing
    private byte[] LongToByteArray(long value) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        return bb.putLong(value).array();
    }

    private byte[] IntToByteArray(int value) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        return bb.putInt(value).array();
    }

    public void WriteLongToFilterOutputPort(long value) {
        byte[] bytesArray = LongToByteArray(value);
        for (int i = 0; i < bytesArray.length; i++) {
            WriteFilterOutputPort(bytesArray[i]);
        }
    }

    public void WriteLongToFilterOutputPort(long value, byte outNumPipe) {
        byte[] bytesArray = LongToByteArray(value);
        for (int i = 0; i < bytesArray.length; i++) {
            WriteFilterOutputPort(bytesArray[i], outNumPipe);
        }
    }

    public void WriteIntegerToFilterOutputPort(int value) {
        byte[] bytesArray = IntToByteArray(value);
        for (int i = 0; i < bytesArray.length; i++) {
            WriteFilterOutputPort(bytesArray[i]);
        }
    }
    
    public void WriteIntegerToFilterOutputPort(int value, byte outNumPipe) {
        byte[] bytesArray = IntToByteArray(value);
        for (int i = 0; i < bytesArray.length; i++) {
            WriteFilterOutputPort(bytesArray[i], outNumPipe);
        }
    }

    public int ReadIdFromFilterInputPort(byte inNumPipe) throws EndOfStreamException {

        int IdLength = 4;				// This is the length of IDs in the byte stream
        int id = 0;

        byte databyte = 0;				// This is the data byte read from the stream
        // This is the measurement id
        int i;
        for (i = 0; i < IdLength; i++) {
            databyte = ReadFilterInputPort(inNumPipe);	// This is where we read the byte from the stream...

            id = id | (databyte & 0xFF);		// We append the byte on to ID...
            //id |= databyte;

            if (i != IdLength - 1) // If this is not the last byte, then slide the
            {									// previously appended byte to the left by one byte
                id = id << 8;					// to make room for the next byte we append to the ID

            } // if

        } // for
        return id;
    }

    public long ReadMeasureFromFilterInputPort(byte inNumPipe) throws EndOfStreamException {
        int MeasurementLength = 8;
        byte databyte = 0;				// This is the data byte read from the stream
        int i;
        long measurement = 0;

        for (i = 0; i < MeasurementLength; i++) {
            databyte = ReadFilterInputPort(inNumPipe);
            measurement = measurement | (databyte & 0xFF);	// We append the byte on to measurement...

            if (i != MeasurementLength - 1) // If this is not the last byte, then slide the
            {												// previously appended byte to the left by one byte
                measurement = measurement << 8;				// to make room for the next byte we append to the
                // measurement
            } // if
        } // for
        return measurement;
    }

    /**
     * *************************************************************************
     * CONCRETE METHOD:: run Purpose: This is actually an abstract method
     * defined by Thread. It is called when the thread is started by calling the
     * Thread.start() method. In this case, the run() method should be
     * overridden by the filter programmer using this framework superclass
     *
     * Arguments: void
     *
     * Returns: void
     *
     * Exceptions: IOExecption
     *
     ***************************************************************************
     */
    public void run() {
        // The run method should be overridden by the subordinate class. Please
        // see the example applications provided for more details.
    } // run
} // FilterFramework class