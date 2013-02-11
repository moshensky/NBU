
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SortFilter extends FilterFramework {

    public static final int TIMESTAMP_ID = 0;
    public static final int VELOCITY_ID = 1;
    public static final int ALTITUDE_MEASURE_ID = 2;
    public static final int PRESSURE_MEASURE_ID = 3;
    public static final int TEMPERATURE_MEASURE_ID = 4;
    public static final int ATTITUDE_MEASURE_ID = 5;
    public static final int FILTER_SIZE = 6;

    public SortFilter(int outPorts) {
        super(outPorts);
    }

    private void WriteFrameToOutputPorts(Frame aFrame) {
        //Write frame with valid data
        WriteIntegerToFilterOutputPort(TIMESTAMP_ID);
        WriteLongToFilterOutputPort(aFrame.getTimeStamp());

        WriteIntegerToFilterOutputPort(VELOCITY_ID);
        WriteLongToFilterOutputPort(aFrame.getVelocity());

        WriteIntegerToFilterOutputPort(ALTITUDE_MEASURE_ID);
        WriteLongToFilterOutputPort(aFrame.getAltitudeMeasure());

        WriteIntegerToFilterOutputPort(PRESSURE_MEASURE_ID);
        WriteLongToFilterOutputPort(aFrame.getPressureMeasure());
       
                    
        WriteIntegerToFilterOutputPort(TEMPERATURE_MEASURE_ID);
        WriteLongToFilterOutputPort(aFrame.getTemperatureMeasure());
        
        WriteIntegerToFilterOutputPort(ATTITUDE_MEASURE_ID);
        WriteLongToFilterOutputPort(aFrame.getAttitudeMeasure());
    }

    private void SortList(List<Frame> aFrameQueue) {
        Collections.sort(aFrameQueue, new FrameComparator());
    }
    
    private class FrameComparator implements Comparator<Frame>
    {
        public int compare(Frame f1, Frame f2)
        {
            return (f1.getTimeStamp()>f2.getTimeStamp() ? 1 : (f1.getTimeStamp()==f2.getTimeStamp() ? 0 : -1));
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

                if (id == VELOCITY_ID) {
                    mFrame.setVelocity(measurement);
                } // if

                if (id == ALTITUDE_MEASURE_ID) {
                    mFrame.setAltitudeMeasure(measurement);
                } // if

                if (id == PRESSURE_MEASURE_ID) {
                    mFrame.setPressureMeasure(measurement);
                } // if

                if (id == TEMPERATURE_MEASURE_ID) {
                    mFrame.setTemperatureMeasure(measurement);
                } // if

                
                if (id == ATTITUDE_MEASURE_ID) {
                    mFrame.setAttitude(measurement);
                    
                    //Attitude has highest ID. 
                    //After reading attitude, we have all other needed measures

                    //add it last
                    Frame newFrame = new Frame(mFrame);
                    frameQueue.add(newFrame);
                    if (frameQueue.size() > FILTER_SIZE)
                    {
                        SortList(frameQueue);
                        //get first
                        Frame lastEnqueued = frameQueue.remove(0);
                        WriteFrameToOutputPorts(lastEnqueued);
                    }
                } // if

            } // try
            catch (EndOfStreamException e) {

                //FLUSH QUEUE BEFORE CLOSING THE PORTS 
                while (!frameQueue.isEmpty()) {
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
        private long mVelocity;
        private long mAltitudeMeasure;
        private long mPressureMeasure;
        private long mTemperatureMeasure;
        private long mAttitudeMeasure;
       
        Frame() {
            mTimeStamp = 0;
            mVelocity = 0;
            mAltitudeMeasure = 0;
            mPressureMeasure = 0;
            mTemperatureMeasure = 0;
            mAttitudeMeasure = 0;
        }

        Frame(long aTimeStamp, long aVelocity, long aAltitudeMeasure, long aPressureMeasure,
                long aTemperatureMeasure, long aAttitudeMeasure) {
            mTimeStamp = aTimeStamp;
            mVelocity = aVelocity;
            mAltitudeMeasure = aAltitudeMeasure;
            mPressureMeasure = aPressureMeasure;
            mTemperatureMeasure = aTemperatureMeasure;
            mAttitudeMeasure = aAttitudeMeasure;
        }

        Frame(Frame aFrame) {
            this(aFrame.getTimeStamp(),aFrame.getVelocity(), aFrame.getAltitudeMeasure(),
                    aFrame.getPressureMeasure(), aFrame.getTemperatureMeasure(),
                    aFrame.getAttitudeMeasure());
        }

        long getTimeStamp() {
            return mTimeStamp;
        }

        void setTimeStamp(long aTimeStampLongBits) {
            mTimeStamp = aTimeStampLongBits;
        }

        long getVelocity()
        {
            return mVelocity;
        }
        
        void setVelocity(long aVelocity)
        {
            mVelocity = aVelocity;
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
        
        long getAttitudeMeasure()
        {
            return mAttitudeMeasure;
        }

        void setAttitude(long aAttitudeMeasure)
        {
            mAttitudeMeasure = aAttitudeMeasure;
        }
        
    }
}