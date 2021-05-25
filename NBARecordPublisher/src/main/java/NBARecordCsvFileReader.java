import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import com.csvreader.CsvReader;

public class NBARecordCsvFileReader implements Supplier<NBARecord> {

    private String filePath;
    private CsvReader csvReader;

    public NBARecordCsvFileReader(String filePath) throws IOException {
        this.filePath = filePath;
        try {
            csvReader = new CsvReader(filePath);
            csvReader.readHeaders();
        }
        catch (IOException e) {
            throw new IOException("Error reading TaxiRecords from file: " + filePath, e);
        }
    }

    @Override
    public NBARecord get() {
        NBARecord nbaRecord = null;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try{
            if(csvReader.readRecord()) {
                csvReader.getRawRecord();
                nbaRecord = new NBARecord(csvReader.get(0),csvReader.get(1),simpleDateFormat.parse(csvReader.get(2)),
                        csvReader.get(3), Integer.valueOf(csvReader.get(4)),
                        Integer.valueOf(csvReader.get(5)), Integer.valueOf(csvReader.get(6)),
                        Integer.valueOf(csvReader.get(7)), Integer.valueOf(csvReader.get(8)),
                        Integer.valueOf(csvReader.get(9)), Integer.valueOf(csvReader.get(10)),
                        csvReader.get(11), csvReader.get(12), Integer.valueOf(csvReader.get(13)), csvReader.get(14),
                        Integer.valueOf(csvReader.get(15)), Integer.valueOf(csvReader.get(15)));

            }
        } catch (IOException | ParseException e) {
            throw new NoSuchElementException("IOException from " + filePath);
        }

        if (null==nbaRecord) {
            throw new NoSuchElementException("All records read from " + filePath);
        }

        return nbaRecord;
    }
}
