package HelloWorld;

import org.springframework.stereotype.Component;

/**
 * Created by connor on 2/21/17.
 */
@Component
public class Message {

    private String message;

    public Message() {
        this.message = "Hello, World!";
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}
