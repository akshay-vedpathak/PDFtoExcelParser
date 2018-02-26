package com.benefix.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.benefix.daoImpl.InsuranceDetails;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Utility class that provides the core functionality for the application
 *
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    private static final String TAG = Utils.class.getName()+": ";
    public static Map<String, String> ageLabelsMap = null;
    public static List<String> excelHeadersList = null;
    public static int lastProcessedRow = 1;

    /**
     * Takes in the path to directory that contains the input files and returns an array of File objects
     * @param pathToInputDirectory
     * @return array of file objects for all the PDF files present in the directory
     */
    public File[] getInputFiles(String pathToInputDirectory){
        File directory = new File(pathToInputDirectory);
        return directory.listFiles((dir, filename) -> filename.endsWith(".pdf"));
    }

    /**
     * Reads the file src/main/resources/config/agegroups.txt that contains information about the possible age groups
     * that are in the PDF files and their corresponding headers in excel template.
     * It returns a HashMap with key as the age group entry and value as the header from the excel template
     * @param ageGroupsPath
     */
    private void initializeAgeLabels(String ageGroupsPath) {
        ageLabelsMap = new HashMap<>();
        logger.info(TAG+"Reading age groups data from "+ageGroupsPath);
        File file = new File(ageGroupsPath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String split[] = line.split(",");
                ageLabelsMap.put(split[0], split[1]);
            }
            br.close();
            logger.info(TAG+"Successfully loaded "+ ageLabelsMap.size()+" age group records.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes initializeAgeLabels() in case the ageLabelsMap HashMap is not created
     * @return ageLabelsMap HashMap
     * @param ageGroupsPath
     */
    public  Map<String, String> getAgeLabels(String ageGroupsPath) {
        if (ageLabelsMap == null) {
            initializeAgeLabels(ageGroupsPath);
        }
        return ageLabelsMap;
    }

    /**
     * Invokes initializeExcelHeaders() in case the excelHeadersList List is not created
     * @param excelHeadersPath
     * @return excelHeadersList List
     */
    public List<String> getExcelHeaders(String excelHeadersPath) {
        if (excelHeadersList == null) {
            initializeExcelHeaders(excelHeadersPath);
        }
        return excelHeadersList;
    }

    /**
     * Reads from the file "src/main/resources/config/" that contains all the possible excel headers for different
     * age groups and stores them in the excelHeadersList List
     * @param excelHeadersPath
     */
    private void initializeExcelHeaders(String excelHeadersPath) {
        excelHeadersList = new ArrayList<>();
        logger.info(TAG+"Reading excel headers from "+excelHeadersPath);
        File file = new File(excelHeadersPath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                excelHeadersList.add(line);
            }
            br.close();
            logger.info(TAG+"Successfully loaded "+ excelHeadersList.size()+" excel headers");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a Blank Excel Template from the excelHeadersList List
     */
    public void createBlankExcelTemplate(String excelHeadersPath, String outputFilePath){
        List<String> excelHeaders = getExcelHeaders(excelHeadersPath);
        Workbook workbook = new XSSFWorkbook();
        logger.info(TAG+"Creating blank excel template");
        Sheet sheet = workbook.createSheet("Blank Template");
        Row row = sheet.createRow(0);
        for(int i=0;i<excelHeaders.size();i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeaders.get(i));
        }
        try{
            FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath));
            workbook.write(outputStream);
            outputStream.close();
            logger.info(TAG+"Successfully created BeneFix_Small_Group_Plans_upload_template.xlsx under "+outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in a list of InsuranceDetails objects and writes the data for each object to the Excel file
     * @param insuranceDetailsList
     */
    public void writeObjectstoExcel(List<InsuranceDetails> insuranceDetailsList, String excelHeadersPath, String outputFilePath){
        List<String> excelHeadersList = getExcelHeaders(excelHeadersPath);
        logger.info(TAG+"Writing "+insuranceDetailsList.size()+" records to excel template");
        try {
            FileInputStream inputStream = new FileInputStream(outputFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Blank Template");
            int lastCell = sheet.getRow(0).getLastCellNum();
            int i=lastProcessedRow;
            for(InsuranceDetails insuranceDetails : insuranceDetailsList){
                Row row = sheet.createRow(i++);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(insuranceDetails.getStartDate());
                Cell cell2 = row.createCell(1);
                cell2.setCellValue(insuranceDetails.getEndDate());
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(insuranceDetails.getProductName());
                Cell cell4 = row.createCell(3);;
                cell4.setCellValue(insuranceDetails.getStates());
                Cell cell5 = row.createCell(4);;
                cell5.setCellValue(insuranceDetails.getGroupRatingAreas());
                for(int j=5;j<lastCell;j++){
                    Cell currentCell = row.createCell(j);
                    String header = excelHeadersList.get(j);
                    currentCell.setCellValue(insuranceDetails.getAgeAndRates().get(header));
                }
            }
            inputStream.close();
            lastProcessedRow = i;
            FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath));
            workbook.write(outputStream);
            outputStream.close();
            logger.info(TAG+"Successfully written "+insuranceDetailsList.size()+" records to template file");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method takes a PDF file as input and invokes the writeObjectToExcel method which inturn writes the parsed
     * PDF data (i.e. InsuranceDetails object) to the Excel Template that's created
     * @param file
     * @param ageGroupsPath
     */
    public List<InsuranceDetails> parseDatafromPDF(File file, String ageGroupsPath, List<String> pdfTags){
        logger.info(TAG+"Parsing data for file: "+file.getName());
        Map<String, Double> ageAndRates = new HashMap<>();
        Map<String,String> ageGroupsMap = getAgeLabels(ageGroupsPath);
        List<InsuranceDetails> insuranceDetailsList = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss zzz");
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            PdfReader reader = new PdfReader(file.getAbsolutePath());
            int n = reader.getNumberOfPages();

            for (int pageNo = 1; pageNo <= n; pageNo++) {
                String str = PdfTextExtractor.getTextFromPage(reader, pageNo);
                String[] lines = str.split("\n");
                InsuranceDetails.Builder insuranceDetailsBuilder = InsuranceDetails.newBuilder();
                for (String line : lines) {
                    if (line.contains(pdfTags.get(0))) {
                        String dates[] = line.substring(line.indexOf(':') + 1).trim().split("-");
                        insuranceDetailsBuilder.setStartDate(outputFormat.format(format.parse(dates[0])));
                        insuranceDetailsBuilder.setEndDate(outputFormat.format(format.parse(dates[1])));
                    }
                    if (line.contains(pdfTags.get(1))) {
                        insuranceDetailsBuilder.setGroupRatingAreas(line.substring(line.indexOf(':') + 1)
                                .replace("*", "").trim());
                    }
                    if (line.contains(pdfTags.get(2))) {
                        String check = pdfTags.get(2);
                        insuranceDetailsBuilder.setProductName(line.substring(line.indexOf(check) +
                                check.length() + 1).trim());
                        insuranceDetailsBuilder.setStates(line.substring(line.indexOf(check) + check.length() + 1)
                                .trim().split(" ")[0]);
                    }
                    if (Character.isDigit(line.charAt(0))) {
                        String values[] = line.split(" ");
                        for (int i = 0; i < values.length - 1; i += 2) {
                            if (values[i].contains("-") || values[i].contains("+")) {
                                String[] ageSplit = ageGroupsMap.get(values[i]).split(":");
                                ageAndRates.put(ageSplit[0], Double.parseDouble(values[i + 1]));
                                ageAndRates.put(ageSplit[1], Double.parseDouble(values[i + 1]));
                            } else {
                                ageAndRates.put(ageGroupsMap.get(values[i]), Double.parseDouble(values[i + 1]));
                            }
                        }
                    }
                }
                insuranceDetailsBuilder.setAgeAndRates(ageAndRates);
                InsuranceDetails id = insuranceDetailsBuilder.build();
                insuranceDetailsList.add(id);
            }
            reader.close();

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return insuranceDetailsList;
    }

    /**
     * Takes the path to file that contains the tags to extract from the PDF input file and returns a List of Tags
     * @param pdfTagsPath
     * @return List of PDF Tags to extract data foe
     */
    public List<String> getPdfTagsToExtract(String pdfTagsPath) {
        List<String> pdfTagstoExtract = new ArrayList<>();
        logger.info(TAG+"Reading PDF tags to extract from "+pdfTagsPath);
        File file = new File(pdfTagsPath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                pdfTagstoExtract.add(line);
            }
            br.close();
            logger.info(TAG+"Successfully loaded "+ pdfTagstoExtract.size()+" tags to extract from PDF files");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfTagstoExtract;
    }
}
