package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesTest {

    ClassLoader cl = FilesTest.class.getClassLoader();

    @Test
    public void zipIsReadableAndCorrect() throws Exception {
        boolean csvChecked = false;
        boolean pdfChecked = false;
        boolean xlsChecked = false;

        ZipFile zf = new ZipFile(new File("src/test/resources/ezyzip.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("ezyzip.zip"));
        ZipEntry entry;
        while((entry = is.getNextEntry()) != null) {
            try (InputStream inputStream = zf.getInputStream(entry)){
                if (entry.getName().endsWith(".xls")) {
                    XLS xls = new XLS(inputStream);
                    System.out.println(xls.name);
                   /* assertThat(xls.excel.getSheetAt(0).getRow(2).getCell(2).getStringCellValue())
                            .isEqualTo("Hashimoto");*/
                    xlsChecked = true;
                }
                if (entry.getName().endsWith(".pdf")){
                    PDF parsed = new PDF(inputStream);
                    assertThat(parsed.author).isEqualTo("Jelena Ples");
                    assertThat(parsed.title).startsWith("Certificate");
                    assertThat(parsed.text).contains("Aleksei Vasilev");
                    pdfChecked = true;
                }
                if (entry.getName().endsWith(".csv")) {
                    try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    List<String[]> content = reader.readAll();
                    assertThat(content).containsSequence(
                        new String[] {"","First Name","Last Name","Gender","Country","Age","Date","Id"},
                        new String[] {"1","Dulce","Abril","Female","United States","32","15/10/2017","1562"});
                    }
                    csvChecked = true;
                }
            }
        }
        assertTrue(csvChecked && pdfChecked && xlsChecked);
    }

    @Test
    public void jsonIsSerializableTest() throws Exception{
        Gson gson = new Gson();
        try (InputStream stream = cl.getResourceAsStream("file_example_JSON_1kb.json")) {
            byte[] fileContent = stream.readAllBytes();
            String strContent = new String(fileContent, StandardCharsets.UTF_8);
            ArrayList<Countries> countries =
                    gson.fromJson(strContent, new TypeToken<ArrayList<Countries>>(){}.getType());
            assertEquals(countries.size(), 246);
            for (Countries country : countries) {
                assertEquals(country.getIsoCode().length(), 2);
            }
        }
    }

}
