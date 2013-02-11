
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TemperatureFilter extends FilterFramework {

    public static final int TEMPERATURE_MEASURE_ID = 4;

    Double ConvertFahrenheitToCelsius(double aTempF) {
        return ((aTempF - 32) / 9) * 5;
    }

    public TemperatureFilter(int outPorts) {
        super(outPorts);
    }

    public void run() {

        int bytesread = 0;
        int byteswritten = 0;
        long measurement;				// This is the word used to store all measurements - conversions are illustrated.
        int id;							// This is the measurement id

        /**
         * ***********************************************************
         * First we announce to the world that we are alive...
         * ************************************************************
         */
        System.out.print("\n" + this.getName() + "::Temperature Fillter Reading ");

        while (true) {
            try {

                id = this.ReadIdFromFilterInputPort((byte) 0);
                bytesread += ID_LENGTH;
                measurement = this.ReadMeasureFromFilterInputPort((byte) 0);
                bytesread += MEASUREMENT_LENGTH;

                if (id == TEMPERATURE_MEASURE_ID) {
                    //Get long bits, convert to double.
                    //Convert it to Celsius and write it back it's longbits
                    double temperatureF = Double.longBitsToDouble(measurement);
                    double temperatureC = ConvertFahrenheitToCelsius(temperatureF);
                    measurement = Double.doubleToLongBits(temperatureC);//modify the measure
                } // if

                this.WriteIntegerToFilterOutputPort(id);
                byteswritten += ID_LENGTH;
                this.WriteLongToFilterOutputPort(measurement);
                byteswritten += MEASUREMENT_LENGTH;


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
                System.out.print("\n" + this.getName() + "::Temperature Filter Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten);
                break;

            } // catch

        } // while

    } // run
}
