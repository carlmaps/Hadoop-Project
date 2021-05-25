import java.util.stream.Stream;

public class NBARecordSender {

    private static String KafkaBrokerEndpoint = null;
    private static String KafkaTopic = null;
    private static String csvFile = null;

    public static void main(String[] args) throws Exception{
        if (args != null){
            KafkaBrokerEndpoint = args[0];
            KafkaTopic = args[1];
            csvFile = args[2];
        }

            Stream.generate(
                    new NBARecordCsvFileReader(csvFile))
                    .sequential()
                    .forEachOrdered(new KafkaNBAProducer(KafkaTopic, KafkaBrokerEndpoint));
    }
}
