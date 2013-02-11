
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.*;						
import java.text.SimpleDateFormat;	

//SinkFilter format and write the inputstream in text format in output file
public class SinkFilter extends FilterFramework {

    public static final int TIMESTAMP_ID = 0;
    public static final int ALTITUDE_MEASURE_ID = 2;
    public static final int TEMPERATURE_MEASURE_ID = 4;
    private String mFileName;	// Output data file.

    public SinkFilter(int outPorts, String fileName) {
        super(outPorts);
        this.mFileName = fileName;
    }

    String composeHeader() {
        String headerTxt = String.format("%-24s%-24s%-24s", "Time:", "Temperature (C):", "Altitude (m):");
        headerTxt += String.format("\n%66s", "").replace(' ', '-');
        return headerTxt;
    }

    String composeFrameRow(Date aDate, double aTemperature, double aAltitude) {
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
        String timeStamp = TimeStampFormat.format(aDate);
        DecimalFormat formatter = new DecimalFormat("000.00000");
        String temperature = formatter.format(aTemperature);
        formatter = new DecimalFormat("000000.00000");
        String altitude = formatter.format(aAltitude);
        return String.format("\n%-24s%-24s%-24s", timeStamp, temperature, altitude);
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


        double alttudeM = 0;
        double temperatureC = 0;

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

                if (id == ALTITUDE_MEASURE_ID) {
                    alttudeM = Double.longBitsToDouble(measurement);
                } // if

                if (id == TEMPERATURE_MEASURE_ID) {
                    temperatureC = Double.longBitsToDouble(measurement);

                    //temperature has highest ID. 
                    //After reading temperature, we have all other needed measures
                    frameRow = composeFrameRow(TimeStamp.getTime(), temperatureC, alttudeM);
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
} // SingFilter