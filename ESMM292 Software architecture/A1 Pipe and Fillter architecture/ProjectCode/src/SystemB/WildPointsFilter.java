
import java.util.LinkedList;
import java.util.List;

public class WildPointsFilter extends FilterFramework {

    public static final int TIMESTAMP_ID = 0;
    public static final int ALTITUDE_MEASURE_ID = 2;
    public static final int PRESSURE_MEASURE_ID = 3;
    public static final int PRESSURE_EXTRAPOLATED_MEASURE_ID = -3;
    public static final int TEMPERATURE_MEASURE_ID = 4;
    public static final int MIN_VALID_PRESSURE = 50;
    public static final int MAX_VALID_PRESSURE = 80;

    public WildPointsFilter(int outPorts) {
        super(outPorts);
    }

    //Tries to extrapolate and fill extrapolated pressure measures to 
    //frames with invalid pressure
    private void ExtrapolateFramesInList(List<Frame> aFrameQueue, boolean aIsOnFlush) {
        if (aFrameQueue.isEmpty()) {
            return;
        }

        Frame firstFrame = aFrameQueue.get(0);
        if (firstFrame.getHasInvalidPressureMeasure() == true && firstFrame.getExtrapolatedPressureMeasure() == 0) {
            //the stream has started with invalid pressure measure.
            //Find last valid and use it to fill invalid measurements
            long validMeasure = 0;
            Frame fr = null;
            for (int i = 0; i < aFrameQueue.size(); i++) {
                fr = aFrameQueue.get(i);
                if ((fr.getHasInvalidPressureMeasure() == false)
                        || (fr.getHasInvalidPressureMeasure() == true && fr.getExtrapolatedPressureMeasure() > 0)) {
                    validMeasure = fr.getPressureMeasure();
                    break;
                }
            }

            for (int i = 0; i < aFrameQueue.size(); i++) {
                fr = aFrameQueue.get(i);
                fr.setExtrapolatedPressureMeasure(validMeasure);
                aFrameQueue.set(i, fr);
                if (fr.getHasInvalidPressureMeasure() == false) {
                    break;
                }
            }
        } else {
            if (aFrameQueue.size() < 3) {
                if (aIsOnFlush && aFrameQueue.size() > 1) {
                    // in that case there are 2 elements.
                    //First inserted is valid
                    //Second is invalid
                    //In that no third element is expeced. Use first one to fill the pressure of second one
                    Frame fr = aFrameQueue.get(0);
                    long validPressure = fr.getHasInvalidPressureMeasure() ? fr.getExtrapolatedPressureMeasure() : fr.getPressureMeasure();
                    fr = aFrameQueue.get(1);
                    fr.setExtrapolatedPressureMeasure(validPressure);
                    aFrameQueue.set(1, fr);
                } else {
                    // in that case there are 2 elements.
                    //First inserted is valid
                    //Second is invalid
                    //In that case do nothing. Thirt element required for decition how to fix invalid pressure
                    return;
                }
            } else {
                //Got 3 ot more elements
                //and the first one is valid
                Frame fr = aFrameQueue.get(1);
                boolean isValidSecond = (fr.getHasInvalidPressureMeasure() == false
                        || (fr.getHasInvalidPressureMeasure() == true && fr.getExtrapolatedPressureMeasure() > 0));

                fr = aFrameQueue.get(2);
                boolean isValidThird = (fr.getHasInvalidPressureMeasure() == false
                        || (fr.getHasInvalidPressureMeasure() == true && fr.getExtrapolatedPressureMeasure() > 0));


                if (!isValidSecond && !isValidThird) {
                    //Extrapolate the second with the first value
                    fr = aFrameQueue.get(0);
                    long validPressure = fr.getHasInvalidPressureMeasure() ? fr.getExtrapolatedPressureMeasure() : fr.getPressureMeasure();
                    //fix second with right pressure
                    fr = aFrameQueue.get(1);
                    fr.setExtrapolatedPressureMeasure(validPressure);
                    aFrameQueue.set(1, fr);
                }
                if (!isValidSecond && isValidThird) {
                    //Interpolate second value
                    fr = aFrameQueue.get(0);
                    long firstValidPressure = fr.getHasInvalidPressureMeasure() ? fr.getExtrapolatedPressureMeasure() : fr.getPressureMeasure();
                    fr = aFrameQueue.get(2);
                    long secondValidPressure = fr.getHasInvalidPressureMeasure() ? fr.getExtrapolatedPressureMeasure() : fr.getPressureMeasure();
                    double interpolVal = (firstValidPressure + secondValidPressure) / 2.0;
                    fr = aFrameQueue.get(1);
                    fr.setExtrapolatedPressureMeasure(Double.doubleToLongBits(interpolVal));
                }

            }
        }
    }

    private void WriteFrameToOutputPorts(Frame aFrame) {
        //Write frame with valid data
        WriteIntegerToFilterOutputPort(TIMESTAMP_ID, FilterFramework.outPipe1);
        WriteLongToFilterOutputPort(aFrame.getTimeStamp(), FilterFramework.outPipe1);

        WriteIntegerToFilterOutputPort(ALTITUDE_MEASURE_ID, FilterFramework.outPipe1);
        WriteLongToFilterOutputPort(aFrame.getAltitudeMeasure(), FilterFramework.outPipe1);

        if (aFrame.getHasInvalidPressureMeasure() == true) {
            WriteIntegerToFilterOutputPort(PRESSURE_EXTRAPOLATED_MEASURE_ID, FilterFramework.outPipe1);
            WriteLongToFilterOutputPort(aFrame.getExtrapolatedPressureMeasure(), FilterFramework.outPipe1);
        } else {
            WriteIntegerToFilterOutputPort(PRESSURE_MEASURE_ID, FilterFramework.outPipe1);
            WriteLongToFilterOutputPort(aFrame.getPressureMeasure(), FilterFramework.outPipe1);
        }
        WriteIntegerToFilterOutputPort(TEMPERATURE_MEASURE_ID, FilterFramework.outPipe1);
        WriteLongToFilterOutputPort(aFrame.getTemperatureMeasure(), FilterFramework.outPipe1);
        
        //Write wildpoints
        if (aFrame.getHasInvalidPressureMeasure() == true)
        {
            WriteIntegerToFilterOutputPort(TIMESTAMP_ID, FilterFramework.outPipe2);
            WriteLongToFilterOutputPort(aFrame.getTimeStamp(), FilterFramework.outPipe2);
         WriteIntegerToFilterOutputPort(PRESSURE_MEASURE_ID, FilterFramework.outPipe2);
         WriteLongToFilterOutputPort(aFrame.getPressureMeasure(), FilterFramework.outPipe2);
        }
    }

    public void run() {


        int bytesread = 0;					// Number of bytes read from the input file.
        int byteswritten = 0;				// Number of bytes written to the stream.
        byte databyte = 0;					// The byte of data read from the file
        int id;
        long measurement;				// This is the word used to store all measurements - conversions are illustrated.

        Frame mFrame = new Frame();

        List<Frame> frameQueue = new LinkedList<Frame>();

        // Next we write a message to the terminal to let the world know we are alive...

        System.out.print("\n" + this.getName() + "::WildPointsFilter Reading ");

        while (true) {
            /**
             * ***********************************************************
             * Here we read a byte and write a byte
             * ***********************************************************
             */
            try {

                id = this.ReadIdFromFilterInputPort((byte) 0);
                bytesread += ID_LENGTH;
                measurement = this.ReadMeasureFromFilterInputPort((byte) 0);
                bytesread += MEASUREMENT_LENGTH;

                if (id == TIMESTAMP_ID) {
                    //TimeStamp.setTimeInMillis(measurement);
                    //buffer time meassure
                    mFrame.setTimeStamp(measurement);
                } // if

                if (id == ALTITUDE_MEASURE_ID) {
                    mFrame.setAltitudeMeasure(measurement);
                } // if

                if (id == PRESSURE_MEASURE_ID) {
                    mFrame.setPressureMeasure(measurement);
                } // if

                if (id == TEMPERATURE_MEASURE_ID) {
                    mFrame.setTemperatureMeasure(measurement);
                    //temperature has highest ID. 
                    //After reading temperature, we have all other needed measures

                    //add it last
                    frameQueue.add(new Frame(mFrame));
                    ExtrapolateFramesInList(frameQueue, false);
                    //get first
                    Frame lastEnqueued = frameQueue.get(0);
                    //If the frame is with valid pressure measure
                    //or it is extrapolated - write it down    
                    if ((lastEnqueued.getHasInvalidPressureMeasure() == false)
                            || (lastEnqueued.getHasInvalidPressureMeasure() == true
                            && lastEnqueued.getExtrapolatedPressureMeasure() > 0)) {
                        //Writedown measure with valid pressure
                        lastEnqueued = frameQueue.remove(0);
                        WriteFrameToOutputPorts(lastEnqueued);
                    }
                } // if

            } // try
            catch (EndOfStreamException e) {

                //FLUSH QUEUE BEFORE CLOSING THE PORTS 
                while (!frameQueue.isEmpty()) {
                    ExtrapolateFramesInList(frameQueue, true);
                    Frame lastEnqueued = frameQueue.remove(0);
                    WriteFrameToOutputPorts(lastEnqueued);
                }
                ClosePorts();
                System.out.print("\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten);
                break;

            } // catch

        } // while

    } // run

    private class Frame {

        private long mTimeStamp;
        private long mAltitudeMeasure;
        private long mPressureMeasure;
        private long mTemperatureMeasure;
        private long mExtrapolatedPressureMeasure;

        Frame() {
            mTimeStamp = 0;
            mAltitudeMeasure = 0;
            mPressureMeasure = 0;
            mTemperatureMeasure = 0;
            mExtrapolatedPressureMeasure = 0;
        }

        Frame(long aTimeStamp, long aAltitudeMeasure, long aPressureMeasure,
                long aTemperatureMeasure, long aExtrapolatedPressureMeasure) {
            mTimeStamp = aTimeStamp;
            mAltitudeMeasure = aAltitudeMeasure;
            mPressureMeasure = aPressureMeasure;
            mTemperatureMeasure = aTemperatureMeasure;
            mExtrapolatedPressureMeasure = aExtrapolatedPressureMeasure;
        }

        Frame(Frame aFrame) {
            this(aFrame.getTimeStamp(), aFrame.getAltitudeMeasure(),
                    aFrame.getPressureMeasure(), aFrame.getTemperatureMeasure(),
                    aFrame.getExtrapolatedPressureMeasure());
        }

        long getTimeStamp() {
            return mTimeStamp;
        }

        void setTimeStamp(long aTimeStampLongBits) {
            mTimeStamp = aTimeStampLongBits;
        }

        long getAltitudeMeasure() {
            return mAltitudeMeasure;
        }

        void setAltitudeMeasure(long aAltitudeMeasure) {
            mAltitudeMeasure = aAltitudeMeasure;
        }

        long getPressureMeasure() {
            return mPressureMeasure;
        }

        void setPressureMeasure(long aPressureMeasure) {
            mPressureMeasure = aPressureMeasure;
        }

        long getTemperatureMeasure() {
            return mTemperatureMeasure;
        }

        void setTemperatureMeasure(long aTemperatureMeasure) {
            mTemperatureMeasure = aTemperatureMeasure;
        }

        boolean getHasInvalidPressureMeasure() {
            double pressure = Double.longBitsToDouble(mPressureMeasure);
            if (pressure >= MIN_VALID_PRESSURE && pressure <= MAX_VALID_PRESSURE) {
                return false;
            } else {
                return true;
            }
        }

        long getExtrapolatedPressureMeasure() {
            return mExtrapolatedPressureMeasure;
        }

        void setExtrapolatedPressureMeasure(long aExtrapolatedPressureMeasure) {
            mExtrapolatedPressureMeasure = aExtrapolatedPressureMeasure;
        }
    }
} // WildPointsFilter