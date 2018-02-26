import com.benefix.daoImpl.InsuranceDetails;
import com.benefix.utils.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    Utils utils = null;
    Map<String,Double> ageAndRates=null;
    Map<String,String> ageLabels=null;

    @Before
    public void setUp(){
        utils = new Utils();

        ageAndRates = new HashMap<>();
        ageAndRates.put("forty_one",393.89);ageAndRates.put("twenty_nine",338.53);ageAndRates.put("fifty_nine",787.48);
        ageAndRates.put("zero_eighteen",192.1);ageAndRates.put("sixty_one",850.1);ageAndRates.put("forty_six",453.79);
        ageAndRates.put("thirty_eight",376.95);ageAndRates.put("fifty",540.31);ageAndRates.put("fifty_six",705.79);
        ageAndRates.put("twenty_four",302.53);ageAndRates.put("thirty_seven",374.53);ageAndRates.put("thirty_three",362.43);
        ageAndRates.put("fifty_one",564.21);ageAndRates.put("fifty_five",674.63);ageAndRates.put("thirty_two",357.89);
        ageAndRates.put("twenty_seven",317.05);ageAndRates.put("forty_three",410.53);ageAndRates.put("nineteen_twenty",192.1);
        ageAndRates.put("forty_four",422.63);ageAndRates.put("twenty_two",302.53);ageAndRates.put("forty",386.63);
        ageAndRates.put("sixty",821.06);ageAndRates.put("fifty_eight",770.84);ageAndRates.put("thirty_nine",381.79);
        ageAndRates.put("thirty_four",367.27);ageAndRates.put("twenty_eight",328.85);ageAndRates.put("thirty_five",369.69);
        ageAndRates.put("twenty_three",302.53);ageAndRates.put("fifty_three",617.15);ageAndRates.put("sixty_four",907.28);
        ageAndRates.put("fifty_seven",737.26);ageAndRates.put("forty_two",400.85);ageAndRates.put("forty_five",436.85);
        ageAndRates.put("forty_nine",516.11);ageAndRates.put("fifty_two",590.53);ageAndRates.put("twenty_six",309.79);
        ageAndRates.put("thirty_one",350.63);ageAndRates.put("forty_seven",472.85);ageAndRates.put("fifty_four",645.89);
        ageAndRates.put("sixty_two",869.16);ageAndRates.put("forty_eight",494.63);ageAndRates.put("twenty_one",302.53);
        ageAndRates.put("thirty_six",372.11);ageAndRates.put("sixty_three",893.06);ageAndRates.put("sixty_five_plus",907.28);
        ageAndRates.put("twenty_five",303.74);ageAndRates.put("thirty",343.37);

        ageLabels = new HashMap<>();
        ageLabels.put("0-20","zero_eighteen:nineteen_twenty");ageLabels.put("21","twenty_one");ageLabels.put("22","twenty_two");
        ageLabels.put("23","twenty_three");ageLabels.put("24","twenty_four");ageLabels.put("25","twenty_five");
        ageLabels.put("26","twenty_six");ageLabels.put("27","twenty_seven");ageLabels.put("28","twenty_eight");
        ageLabels.put("29","twenty_nine");ageLabels.put("30","thirty");ageLabels.put("31","thirty_one");
        ageLabels.put("32","thirty_two");ageLabels.put("33","thirty_three");ageLabels.put("34","thirty_four");
        ageLabels.put("35","thirty_five");ageLabels.put("36","thirty_six");ageLabels.put("37","thirty_seven");
        ageLabels.put("38","thirty_eight");ageLabels.put("39","thirty_nine");ageLabels.put("40","forty");
        ageLabels.put("41","forty_one");ageLabels.put("42","forty_two");ageLabels.put("43","forty_three");
        ageLabels.put("44","forty_four");ageLabels.put("45","forty_five");ageLabels.put("46","forty_six");
        ageLabels.put("47","forty_seven");ageLabels.put("48","forty_eight");ageLabels.put("49","forty_nine");
        ageLabels.put("50","fifty");ageLabels.put("51","fifty_one");ageLabels.put("52","fifty_two");
        ageLabels.put("53","fifty_three");ageLabels.put("54","fifty_four");ageLabels.put("55","fifty_five");
        ageLabels.put("56","fifty_six");ageLabels.put("57","fifty_seven");ageLabels.put("58","fifty_eight");
        ageLabels.put("59","fifty_nine");ageLabels.put("60","sixty");ageLabels.put("61","sixty_one");
        ageLabels.put("62","sixty_two");ageLabels.put("63","sixty_three");ageLabels.put("64+","sixty_four:sixty_five_plus");


    }

    @Test
    public void getInputFilesTest(){
        File[] files = new File[]{new File("src/test/resources/inputFiles/para01.pdf"),
                new File("src/test/resources/inputFiles/para02.pdf")};

        File[] result = utils.getInputFiles("src/test/resources/inputFiles/");

        assertArrayEquals(files,result);
    }

    @Test
    public void getExcelHeaders(){
        List<String> excelHeaders = new ArrayList<>();
        excelHeaders.add("start_date");
        excelHeaders.add("end_date");
        excelHeaders.add("product_name");
        excelHeaders.add("states");
        excelHeaders.add("group_rating_areas");
        excelHeaders.add("zero_eighteen");
        excelHeaders.add("nineteen_twenty");

        List<String> result = utils.getExcelHeaders("src/test/resources/excelHeaderstoTest.txt");

        assertTrue(result.containsAll(excelHeaders));
    }

    @Test
    public void parseDatafromPDFTest(){
        List<String> pdfTags = new ArrayList<>();
        pdfTags.add("Effective Dates:");
        pdfTags.add("Rating Area:");
        pdfTags.add("Plan Name:");
        List<InsuranceDetails> insuranceDetailsList = utils.parseDatafromPDF(new File("src/test/resources/inputFiles/para01.pdf"),
                "src/test/resources/config/agegroups.txt",pdfTags);
        /**
         * We check for the first Insurance Details object from the list against the expected data
         */
        assertEquals("2017-04-01 05:04:00 UTC",insuranceDetailsList.get(0).getStartDate());
        assertEquals("2017-06-30 05:06:00 UTC",insuranceDetailsList.get(0).getEndDate());
        assertEquals("PA Gold QPOS 2000 100/50 HSA T",insuranceDetailsList.get(0).getProductName());
        assertEquals("PA",insuranceDetailsList.get(0).getStates());
        assertEquals("PARA01",insuranceDetailsList.get(0).getGroupRatingAreas());
        assertEquals(ageAndRates,insuranceDetailsList.get(0).getAgeAndRates());
    }

    @Test
    public void getPDFTagsToExtract(){
        List<String> pdfTags = new ArrayList<>();
        pdfTags.add("Effective Dates:");
        pdfTags.add("Rating Area:");
        pdfTags.add("Plan Name:");
        List<String> result = utils.getPdfTagsToExtract("src/test/resources/config/pdfTags.txt");

        assertTrue(result.containsAll(pdfTags));
    }

    @Test
    public void getAgeLabelsTest(){
        Map<String,String> result = utils.getAgeLabels("src/test/resources/config/agegroups.txt");
        assertEquals(ageLabels,result);
    }

}
