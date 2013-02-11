
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.*;						// This class is used to interpret time words
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.

public class SinkFilterData extends FilterFramework {

    public static final int TIMESTAMP_ID = 0;
    public static final int ALTITUDE_MEASURE_ID = 2;
    public static final int PRESSURE_MEASURE_ID = 3;
    public static final int PRESSURE_EXTRAPOLATED_MEASURE_ID = -3;
    public static final int TEMPERATURE_MEASURE_ID = 4;
    private String mFileName;	// Output data file.

    public SinkFilterData(int outPorts, String fileName) {
        super(outPorts);
        this.mFileName = fileName;
    }

    String composeHeader() {
        String headerTxt = String.format("%-24s%-24s%-24s%-24s", "Time:", "Temperature (C):", "Altitude (m):", "Pressure (psi)");
        headerTxt += String.format("\n%88s", "").replace(' ', '-');
        return headerTxt;
    }

    String composeFrameRow(Date aDate, double aTemperature, double aAltitude, double aPressure, boolean aHasExtrapolPressure) {
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy:dd:hh:mm:ss");
        String timeStamp = TimeStampFormat.format(aDate);
        DecimalFormat formatter = new DecimalFormat("000.00000");
        String temperature = formatter.format(aTemperature);
        formatter = new DecimalFormat("000000.00000");
        String altitude = formatter.format(aAltitude);
        formatter = new DecimalFormat("00.00000");
        String pressure = formatter.format(aPressure);
        if (aHasExtrapolPressure)
        {
            pressure += "*";//mark extrapolated value
        }
        return String.format("\n%-24s%-24s%-24s%-24s", timeStamp, temperature, altitude, pressure);
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
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        int bytesread = 0;				// This is the number of bytes read from the stream
        int id;
        long measurement;				// This is the word used to store all measurements - conversions are illustrated.


        double alttudeM = 0;
        double pressure = 0;
        double temperatureC = 0;
        boolean hasExtrapolatedPressure = false;
        
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

                if (id == PRESSURE_MEASURE_ID || id == PRESSURE_EXTRAPOLATED_MEASURE_ID) {
                    pressure = Double.longBitsToDouble(measurement);
                    hasExtrapolatedPressure = (id == PRESSURE_EXTRAPOLATED_MEASURE_ID) ? true : false;
                }
                
                //Temperature's id is the biggest one. After reading temperature that will be read
                if (id == TEMPERATURE_MEASURE_ID) {
                    temperatureC = Double.longBitsToDouble(measurement);

                    //temperature has highest ID. 
                    //After reading temperature, we have all other needed measures
                    frameRow = composeFrameRow(TimeStamp.getTime(), temperatureC, alttudeM, pressure, hasExtrapolatedPressure);
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