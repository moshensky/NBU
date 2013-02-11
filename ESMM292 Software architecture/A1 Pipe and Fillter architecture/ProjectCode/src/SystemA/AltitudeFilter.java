
public class AltitudeFilter extends FilterFramework {

    public static final int ALTITUDE_MEASURE_ID = 2;

    Double ConvertfeetToMeters(double aFeets) {
        return aFeets / 3.2808;
    }

    public AltitudeFilter(int outPorts) {
        super(outPorts);
    }

    public void run() {

        int bytesread = 0;
        int byteswritten = 0;
        long measurement;   // This is the word used to store all measurements.
        int id;             // This is the measurement id

        /**
         * ***********************************************************
         * First we announce to the world that we are alive...
         * ************************************************************
         */
        System.out.print("\n" + this.getName() + "::Altitude Fillter Reading ");

        while (true) {
            try {
                id = this.ReadIdFromFilterInputPort((byte) 0);
                bytesread += ID_LENGTH;
                measurement = this.ReadMeasureFromFilterInputPort((byte) 0);
                bytesread += MEASUREMENT_LENGTH;

                if (id == ALTITUDE_MEASURE_ID) {
                    //Convert the measure from long bits to double
                    //convert it to Celsius
                    //set it it's long bits back to measure
                    double altitudeF = Double.longBitsToDouble(measurement);
                    double altitudeM = ConvertfeetToMeters(altitudeF);
                    measurement = Double.doubleToLongBits(altitudeM);
                } // if

                //Write the id and the measure to the output stream
                this.WriteIntegerToFilterOutputPort(id);
                this.WriteLongToFilterOutputPort(measurement);

            } // try
            /**
             * *****************************************************************************
             * The EndOfStreamExeception below is thrown when you reach end of
             * the input stream. At this point, the filter ports are closed and
             * a message is written letting the user know what is going on.
             * ******************************************************************************
             */
            catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + this.getName() + "::Altitude Filter Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten);
                break;

            } // catch

        } // while

    } // run
}
