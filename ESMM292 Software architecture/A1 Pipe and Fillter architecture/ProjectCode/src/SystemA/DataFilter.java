/***********
 * Data filter remove from the stream measure that are not required int the output
 ***********/
public class DataFilter extends FilterFramework {

    public static final int TIMESTAMP_ID = 0;
    public static final int ALTITUDE_MEASURE_ID = 2;
    public static final int TEMPERATURE_MEASURE_ID = 4;

    public DataFilter(int outPorts) {
        super(outPorts);
    }

    public void run() {

        int bytesread = 0;
        int byteswritten = 0;
        long measurement;
        int id;

        System.out.print("\n" + this.getName() + "::DataFilter Reading ");

        while (true) {
            /**
             * ***********************************************************
             * Here we read a ID and Measure
             * ***********************************************************
             */
            try {
                id = ReadIdFromFilterInputPort((byte) 0);
                bytesread += ID_LENGTH;
                measurement = ReadMeasureFromFilterInputPort((byte) 0);
                bytesread += MEASUREMENT_LENGTH;

                //Write to output stream only required measures and their ids
                if (id == TIMESTAMP_ID
                        || id == ALTITUDE_MEASURE_ID
                        || id == TEMPERATURE_MEASURE_ID) {
                    this.WriteIntegerToFilterOutputPort(id);
                    byteswritten += ID_LENGTH;
                    this.WriteLongToFilterOutputPort(measurement);
                    byteswritten += MEASUREMENT_LENGTH;
                } // if
            } // try
            catch (EndOfStreamException e) {
                ClosePorts();
                System.out.print("\n" + this.getName() + "::Data Filter Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten);
                break;

            } // catch

        } // while

    } // run
} // DataFilter