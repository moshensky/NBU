
public class Plumber {

    public static void main(String argv[]) {
        String inputFileName = "FlightData.dat";
        String outputFileName = "OutputB.dat";
        String outputRejectedWildPointsFileName = "WildPoints.dat";
        //Override default input/output file names
        if (argv.length >= 1) {
            inputFileName = argv[0];
        }
        if (argv.length >= 2) {
            outputFileName = argv[1];
        }
        if (argv.length >= 3) {
            outputRejectedWildPointsFileName = argv[2];
        }
        /**
         * **************************************************************************
         * Here we instantiate three filters.
	 ***************************************************************************
         */
        SourceFilter sourceFilter = new SourceFilter(1, inputFileName);
        DataFilter dataFilter = new DataFilter(1);
        AltitudeFilter altitudeFilter = new AltitudeFilter(1);
        TemperatureFilter temperatureFilter = new TemperatureFilter(1);
        WildPointsFilter wildpointFilter = new WildPointsFilter(2);
        SinkFilterData sinkFilter = new SinkFilterData(1, outputFileName);
        SinkFilterDataRejected sinkFilterRejected = new SinkFilterDataRejected(1, outputRejectedWildPointsFileName);

        
        sinkFilter.Connect(wildpointFilter, FilterFramework.outPipe1);
        sinkFilterRejected.Connect(wildpointFilter, FilterFramework.outPipe2);
        wildpointFilter.Connect(temperatureFilter);
        temperatureFilter.Connect(altitudeFilter);
        altitudeFilter.Connect(dataFilter);
        dataFilter.Connect(sourceFilter);

        sourceFilter.start();
        dataFilter.start();
        temperatureFilter.start();
        altitudeFilter.start();
        wildpointFilter.start();
        sinkFilter.start();
        sinkFilterRejected.start();
    } // main
} // Plumber