
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.*;						// This class is used to interpret time words
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.

public class SinkFilterAttitudePressureDataRejected extends FilterFramework {

    public static final int TIMESTAMP_ID = 0;
    public static final int PRESSURE_MEASURE_ID = 3;
    public static final int ATTITUDE_MEASURE_ID = 5;
    
    private String mFileName;	// Input data file.

    public SinkFilterAttitudePressureDataRejected(int outPorts, String fileName) {
        super(outPorts);
        this.mFileName = fileName;
    }

    String composeHeader() {
        String headerTxt = String.format("%-24s%-24s%-24s", "Time:", "Pressure (psi)", "Attitude");
        headerTxt += String.format("\n%66s", "").replace(' ', '-');
        return headerTxt;
    }

    String composeFrameRow(Date aDate, double aPressure, double aAttitude) {
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
        String timeStamp = TimeStampFormat.format(aDate);
        DecimalFormat formatter = new DecimalFormat("00.00000");
        String pressure = formatter.format(aPressure);
        String attitude = formatter.format(aAttitude);
        return String.format("\n%-24s%-24s%-24s", timeStamp, pressure, aAttitude);
    }

    public void run() {
        /**
         * **********************************************************************************
         * TimeStamp is used to compute time using java.util's Calendar class.
         * TimeStampFormat is used to format the time value so that it can be
         * easily printed to the terminal.
         * ***********************************************************************************
         */
        Calendar TimeStamp = Calendar.getInstance();
       
        int bytesread = 0;				// This is the number of bytes read from the stream
        int id;
        long measurement;				// This is the word used to store all measurements - conversions are illustrated.

        double pressure = 0;
        double attitude = 0;
        
        String frameRow = null;
        PrintStream outs = null;			// File stream reference.
        try {
            outs = new PrintStream(new FileOutputStream(mFileName, false));
        } catch (FileNotFoundException e) {
            System.out.println(this.getName() + "::Cant Open file for writing!");
            return;
        }

        System.out.print("\n" + this.getName() + "::Sink Reading ");
        String headerStr = composeHeader();
        System.out.print("\n" + headerStr);
        outs.print(headerStr);
        while (true) {
            try {
                id = this.ReadIdFromFilterInputPort((byte) 0);
                bytesread += ID_LENGTH;
                measurement = this.ReadMeasureFromFilterInputPort((byte) 0);
                bytesread += MEASUREMENT_LENGTH;

                if (id == TIMESTAMP_ID) {
                    TimeStamp.setTimeInMillis(measurement);
                } // if

                if (id == PRESSURE_MEASURE_ID) {
                    pressure = Double.longBitsToDouble(measurement);
                } // if

                
                //Pressure's id is the biggest one. After reading pressure that will be read
                if (id == ATTITUDE_MEASURE_ID) {
                    attitude = Double.longBitsToDouble(measurement);
                    //attitude has highest ID. 
                    //After reading attitude, we have all other needed measures
                    frameRow = composeFrameRow(TimeStamp.getTime(), pressure, attitude);
                    System.out.print(frameRow);
                    outs.print(frameRow);
                } // if
            } // try
            /**
             * *****************************************************************************
             * The EndOfStreamExeception below is thrown when you reach end of
             * the input stream (duh). At this point, the filter ports are
             * closed and a message is written letting the user know what is
             * going on.
             * ******************************************************************************
             */
            catch (EndOfStreamException e) {
                ClosePorts();
                outs.close();
                System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread);
                break;

            } // catch

        } // while

    } // run
} 