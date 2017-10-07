import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Created by jhinukbarman on 2/2/17.
 */
public class FlikTest {
    //The bug is in Steve's code because the Flik library test is successful!
    @Test
    public void testIsSameNumber(){
        assertTrue(Flik.isSameNumber(5,5));
    }
}
