package APItest1;

import org.testng.annotations.Test;

public class APItest extends APICRUDTests {

    @Test
    public void testGet() throws Exception {
        Get();
    }

    @Test
    public void testPost() throws Exception {
        Post();
    }

    @Test
    public void testPut() throws Exception {
        Put();
    }

    @Test
    public void testDelete() throws Exception {
        Delete();
    }

    @Test
    public void testPostTime() throws Exception {
        Posttime();
    }

    @Test
    public void testSchema() throws Exception {
        schema();
    }

    @Test
    public void testPagination() throws Exception {
        pagination();
    }

    @Test
    public void testFileUploadDownload() throws Exception {
        fileUploaddownload();
    }


    @Test
    public void testDataDrivenCSV() throws Exception {
        datadrivencsv();
    }
}

