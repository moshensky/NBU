
public class Plumber {

    public static void main(String argv[]) {
        String inputFileName1 = "SubsetA.dat";
        String inputFileName2 = "SubsetB.dat";
        
        String outputFileName = "OutputC.dat";
        String outputRejectedWildPointsFileName = "WildPointsPressure.dat";
        String outputRejectedAttitidePressureWildPointsFileName = "WildPointsAttitudePressure.dat";
        //Override default input/output file names
        if (argv.length >= 1) {
            inputFileName1 = argv[0];
        }
        if (argv.length >= 2) {
            inputFileName2 = argv[0];
        }
        if (argv.length >= 3) {
            outputFileName = argv[2];
        }
        if (argv.length >= 4) {
            outputRejectedWildPointsFileName = argv[3];
        }
        
        if (argv.length >= 5) {
            outputRejectedAttitidePressureWildPointsFileName = argv[4];
        }
        /**
         * **************************************************************************
         * Here we instantiate three filters.
	 ***************************************************************************
         */
        
        SourceFilter[] sourceFilters = new SourceFilter[2];
	sourceFilters[0] = new SourceFilter(1, inputFileName1);
	sourceFilters[1] = new SourceFilter(1, inputFileName2);

        MergeFilter mergeFilter = new MergeFilter(2, 1);
        SortFilter sortFilter = new SortFilter(1);
        AltitudeFilter altitudeFilter = new AltitudeFilter(1);
        TemperatureFilter temperatureFilter = new TemperatureFilter(1);
        WildPointsPressureFilter wildpointPressureFilter = new WildPointsPressureFilter(2);
        WildPointsAttitudePressureFilter wildpointPressureAttitudeFilter = new WildPointsAttitudePressureFilter(2);
        SinkFilterData sinkFilter = new SinkFilterData(1, outputFileName);
        SinkFilterDataRejected sinkFilterRejected = new SinkFilterDataRejected(1, outputRejectedWildPointsFileName);
        SinkFilterAttitudePressureDataRejected sinkAttitudePressureFilterRejected = new SinkFilterAttitudePressureDataRejected(1, outputRejectedAttitidePressureWildPointsFileName);

        sinkAttitudePressureFilterRejected.Connect(wildpointPressureAttitudeFilter, FilterFramework.outPipe2);
        sinkFilter.Connect(wildpointPressureAttitudeFilter, FilterFramework.outPipe1);
        wildpointPressureAttitudeFilter.Connect(temperatureFilter);
        temperatureFilter.Connect(altitudeFilter);
        altitudeFilter.Connect(wildpointPressureFilter, FilterFramework.outPipe1);
        sinkFilterRejected.Connect(wildpointPressureFilter, FilterFramework.outPipe2);
        wildpointPressureFilter.Connect(sortFilter);
        sortFilter.Connect(mergeFilter);
        mergeFilter.Connect(sourceFilters);
		
       
        sourceFilters[0].start();
	sourceFilters[0].setName("Source SubSetA");
	sourceFilters[1].start();
	sourceFilters[1].setName("Source SubSetB");
        mergeFilter.start();
        sortFilter.start();
        wildpointPressureFilter.start();
        sinkFilterRejected.start();
        temperatureFilter.start();
        altitudeFilter.start();
        wildpointPressureAttitudeFilter.start();
        sinkFilter.start();
        sinkAttitudePressureFilterRejected.start();
        
        
        
    } // main
} // Plumber