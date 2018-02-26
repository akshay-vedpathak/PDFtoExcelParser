package com.benefix.main;

import com.benefix.daoImpl.InsuranceDetails;
import com.benefix.utils.Utils;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The Main class that takes in the path to the input files and hence starts the processing each file and eventually
 * calls a function to write the data back to the excel file template
 */
public class TransferPDFDataToExcel {

    private static final Logger logger = Logger.getLogger(TransferPDFDataToExcel.class.getName());
    private static final String TAG = TransferPDFDataToExcel.class.getName()+": ";

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(args[0]);
            properties.load(inputStream);

            Utils utils = new Utils();

            logger.info(TAG+"Fetching PDF files from the input directory: "+properties.get("inputFilesPath"));

            File[] inputFiles = utils.getInputFiles(properties.getProperty("inputFilesPath"));

            if(inputFiles.length>0) {

                logger.info(TAG+"Loaded "+inputFiles.length+" PDF files to parse");

                logger.info(TAG+"Creating Blank Excel Template");
                utils.createBlankExcelTemplate(properties.getProperty("excelHeadersPath"), properties.getProperty("outputFilePath"));

                logger.info(TAG+"Fetching tags to extract from PDF files");
                List<String> pdfTags = utils.getPdfTagsToExtract(properties.getProperty("pdfTagsPath"));

                for(File file : inputFiles){
                    List<InsuranceDetails> insuranceDetailsList = utils.parseDatafromPDF(file, properties.getProperty("ageGroupsPath"), pdfTags);
                    utils.writeObjectstoExcel(insuranceDetailsList,properties.getProperty("excelHeadersPath"),
                            properties.getProperty("outputFilePath"));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
