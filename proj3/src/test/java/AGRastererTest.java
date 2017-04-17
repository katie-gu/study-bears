import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AGRastererTest extends AGMapTest {
    /**
     * Test the rastering functionality of the student code, by calling their getMapRaster method
     * and ensuring that the resulting map is correct. All of the test data is stored in a
     * TestParameters object that is loaded by the AGMapTest constructor. Note that this test file
     * extends AGMapTest, where most of the interesting stuff happens.
     * @throws Exception
     */
    @Test
    public void testGetMapRaster() throws Exception {
        for (TestParameters p : params) {
            Map<String, Object> studentRasterResult = rasterer.getMapRaster(p.rasterParams);
            System.out.println("Raster result: " + p.rasterResult);
            System.out.println("Student result: " + studentRasterResult);


            System.out.println("Result images: " + Arrays.deepToString( (String[][]) p.rasterResult.get("render_grid")));

            System.out.println();
            checkParamsMap("Returned result differed for input: " + p.rasterParams + ".\n",
                    p.rasterResult, studentRasterResult);
            //System.out.println()

            //Rastered images: [[img/122.png, img/211.png, img/212.png, img/221.png, img/222.png], [img/124.png, img/213.png, img/214.png, img/223.png, img/224.png], [img/142.png, img/231.png, img/232.png, img/241.png, img/242.png], [img/144.png, img/233.png, img/234.png, img/243.png, img/244.png], [img/322.png, img/411.png, img/412.png, img/421.png, img/422.png], [img/324.png, img/413.png, img/414.png, img/423.png, img/424.png], [img/342.png, img/431.png, img/432.png, img/441.png, img/442.png]]
            //Real output:     [[img/122.png, img/211.png, img/212.png, img/221.png, img/222.png], [img/124.png, img/213.png, img/214.png, img/223.png, img/224.png], [img/142.png, img/231.png, img/232.png, img/241.png, img/242.png], [img/144.png, img/233.png, img/234.png, img/243.png, img/244.png], [img/322.png, img/411.png, img/412.png, img/421.png, img/422.png], [img/324.png, img/413.png, img/414.png, img/423.png, img/424.png], [img/342.png, img/431.png, img/432.png, img/441.png, img/442.png]]

        }
    }
}
