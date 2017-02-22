package HelloWorld;

import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;

import static org.junit.Assert.*;

/**
 * Created by connor on 2/21/17.
 */
public class MessageTest {

    private Message msg;

    @Before
    public void setUp() throws Exception {
        msg = new Message();
    }

    @Test
    public void getMessage() throws Exception {
        assertTrue(msg.getMessage().equals("Hello, World!"));
    }

    @Test
    public void setMessage() throws Exception {
        msg.setMessage("New message");
        assertTrue(msg.getMessage().equals("New message"));
    }

}