
public class Plumber {

    public static void main(String argv[]) {
        //Default filenames that will be used when no parameters are provided
        String inputFileName = "FlightData.dat";
        String outputFileName = "OutputA.dat";

        //Override default input/output file names
        if (argv.length >= 1) {
            inputFileName = argv[0];
        }
        if (argv.length >= 2) {
            outputFileName = argv[1];
        }

        //Instance the filters
        SourceFilter sourceFilter = new SourceFilter(1, inputFileName);
        DataFilter dataFilter = new DataFilter(1);
        AltitudeFilter altitudeFilter = new AltitudeFilter(1);
        TemperatureFilter temperatureFilter = new TemperatureFilter(1);
        SinkFilter sinkFilter = new SinkFilter(1, outputFileName);

        //Connect the filters
        sinkFilter.Connect(temperatureFilter);
        temperatureFilter.Connect(altitudeFilter);
        altitudeFilter.Connect(dataFilter);
        dataFilter.Connect(sourceFilter);

        //Start the filters
        sourceFilter.start();
        dataFilter.start();
        temperatureFilter.start();
        altitudeFilter.start();
        sinkFilter.start();

    } // main
} // Plumber