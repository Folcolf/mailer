package com.folcolf.automailer.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVUtil {

    public static List<String[]> readCSV(String fileName) {
        List<String[]> csvData = null;
        try {
            csvData = new CSVReader(new FileReader(fileName)).readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return csvData;
    }

    public static List<String[]> getCSVData(String fileName) {
        List<String[]> csvData = readCSV(fileName);
        csvData.remove(0);
        return csvData;
    }

}
