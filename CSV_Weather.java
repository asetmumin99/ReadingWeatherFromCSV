import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

/**
 * Write a description of CSV_Weather here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSV_Weather {
    
    public CSVRecord coldestHourInFile(CSVParser parser){
        CSVRecord coldest = null;
        for(CSVRecord record : parser){
            if(coldest == null)
                coldest = record;
            else{
                double currentRow = Double.parseDouble(record.get("TemperatureF"));
                if(currentRow == -9999)
                    continue;
                double currentColdest = Double.parseDouble(coldest.get("TemperatureF"));
                coldest = (currentRow < currentColdest) ? record : coldest;
            }
        }
        return coldest;
    }
    
    public void printAllTemps(CSVParser parser){
        System.out.println("All the Temperatures on the coldest day were:");
        for(CSVRecord record : parser){
            System.out.println(record.get("DateUTC") + ": " + record.get("TemperatureF"));
            
        }
    }
    
    public void printAllHumidities(CSVParser parser){
        System.out.println("All the Humidities on the lowest day were:");
        for(CSVRecord record : parser){
            System.out.println(record.get("DateUTC") + ": " + record.get("Humidity"));
            
        }
    }
    
    public void testColdestHourInFile(){
        /*FileResource fr = new FileResource("nc_weather2/2014/weather-2014-05-01.csv");
        CSVParser parser = fr.getCSVParser();
        
        CSVRecord coldest = coldestHourInFile(parser);
        System.out.println("Coldest temp is " + coldest.get("TemperatureF") + " at " + 
        coldest.get("DateUTC"));*/
        
        String coldestFile = fileWithColdestTemperature();
        FileResource fr = new FileResource("nc_weather2/2013/" + coldestFile);
        CSVRecord coldest = coldestHourInFile(fr.getCSVParser());
        System.out.println("Coldest day was in file " + coldestFile);
        System.out.println("Coldest temperature on that day was " + coldest.get("TemperatureF"));
        printAllTemps(fr.getCSVParser());
    }
    
    public String fileWithColdestTemperature(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord coldestFile = null;
        File coldestFileName = null;
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVRecord currentFileRecord = coldestHourInFile(fr.getCSVParser());
            if(coldestFile == null){
                coldestFile = currentFileRecord;
                coldestFileName = f;
            }
            else{
                double currentRow = Double.parseDouble(currentFileRecord.get("TemperatureF"));
                double currentColdest = Double.parseDouble(coldestFile.get("TemperatureF"));
                if(currentRow < currentColdest) {
                    coldestFile = currentFileRecord;
                    coldestFileName = f;
                }
            }
        }
        /*System.out.println("Coldest temp is " + coldestFile.get("TemperatureF") + " at " + 
        coldestFile.get("DateUTC") + " in file " + coldestFileName.getName());*/
        return coldestFileName.getName();
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowest = null;
        for(CSVRecord record : parser){
            if(lowest == null)
                lowest = record;
            else{
                String humidity = record.get("Humidity");
                if(humidity.equals("N/A"))
                    continue;
                double currentRow = Double.parseDouble(humidity);
                double currentColdest = Double.parseDouble(lowest.get("Humidity"));
                lowest = (currentRow < currentColdest) ? record : lowest;
            }
        }
        return lowest;
    }
    
    public void testLowestHumidityInFile(){
        /*FileResource fr = new FileResource("nc_weather2/2014/weather-2014-07-22.csv");
        CSVParser parser = fr.getCSVParser();
        
        CSVRecord coldest = lowestHumidityInFile(parser);
        System.out.println("Lowest Humidity is " + coldest.get("Humidity") + " at " + 
        coldest.get("DateUTC"));*/
        
        String lowestFile = lowestHumidityInManyFiles();
        FileResource fr = new FileResource("nc_weather2/2013/" + lowestFile);
        CSVRecord lowest = lowestHumidityInFile(fr.getCSVParser());
        System.out.println("Lowest Humidity was in file " + lowestFile);
        System.out.println("Lowest Humidity on that day was " + lowest.get("Humidity") + " at " + lowest.get("DateUTC"));
        //printAllHumidities(fr.getCSVParser());
    }
    
    public String lowestHumidityInManyFiles(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord lowestFile = null;
        File lowestFileName = null;
        int day = 1;
        for(File f : dr.selectedFiles()){
            System.out.println("Day " + day++ + " " + f.getName());
            FileResource fr = new FileResource(f);
            CSVRecord currentFileRecord = lowestHumidityInFile(fr.getCSVParser());
            if(lowestFile == null){
                lowestFile = currentFileRecord;
                lowestFileName = f;
            }
            else{
                String humidity = currentFileRecord.get("Humidity");
                if(humidity.equals("N/A"))
                    continue;
                double currentRow = Double.parseDouble(currentFileRecord.get("Humidity"));
                double currentLowest = Double.parseDouble(lowestFile.get("Humidity"));
                if(currentRow < currentLowest) {
                    lowestFile = currentFileRecord;
                    lowestFileName = f;
                }
            }
        }
        /*System.out.println("Coldest temp is " + coldestFile.get("TemperatureF") + " at " + 
        coldestFile.get("DateUTC") + " in file " + coldestFileName.getName());*/
        return lowestFileName.getName();
    }
    
    public double averageTemperatureInFile (CSVParser parser){
        double sum = 0.0;
        int count = 0;
        for(CSVRecord record : parser){
            sum += Double.parseDouble(record.get("TemperatureF"));
            count++;
        }
        return sum / count;
    }
    
    public void testAverageTemperatureInFile(){
        FileResource fr = new FileResource("nc_weather2/2013/weather-2013-08-10.csv");
        CSVParser parser = fr.getCSVParser();
        
        //CSVRecord avgTemp = averageTemperatureInFile(parser);
        System.out.println("AvgTemp is " + averageTemperatureInFile(parser));
    }
    
    public double averageTemperatureWithHighHumidityInFile (CSVParser parser, int value){
        double sum = 0.0;
        int count = 0;
        for(CSVRecord record : parser){
            if(Integer.parseInt(record.get("Humidity")) > value){
                sum += Double.parseDouble(record.get("TemperatureF"));
                count++;
            }
        }
        if(count == 0)
            return 0.0;
        else
            return sum / count;
    }
    
    public void testAverageTemperatureWithHighHumidityInFile(){
        FileResource fr = new FileResource("nc_weather2/2013/weather-2013-09-02.csv");
        CSVParser parser = fr.getCSVParser();
        
        //CSVRecord avgTemp = averageTemperatureInFile(parser);
        double avgTempWithHumid = averageTemperatureWithHighHumidityInFile(parser, 80);
        if(avgTempWithHumid == 0.0)
            System.out.println("No temperatures with that humidity");
        else
            System.out.println("AvgTemp when high Humidity is " + avgTempWithHumid);
    }
}
