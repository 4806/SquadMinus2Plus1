package WikiPages;

import HelloWorld.BootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Chris on 2/24/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class WikiPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createWikiPage() throws Exception {

        //Cannot use Id when checking return string as Id is randomly generated value. Cannot creation date as there is no way to know exact time of creation
        this.mockMvc.perform(post("/createWikiPage?title=testTitle&content=testContent&parentID=-1&authorID=1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"id\":")))
                .andExpect(content().string(containsString(",\"title\":\"testTitle\",\"content\":\"testContent\",\"parentID\":-1,\"authorID\":1,\"creationDate\":")))
                .andExpect(content().string(containsString("}")));
    }

}