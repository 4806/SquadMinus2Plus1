package SocialWiki.WikiPages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

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

        MultiValueMap<String, String> params = new HttpHeaders();

        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-1");
        params.add("authorID", "1");

        //Check for successful creation
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"id\":")))
                .andExpect(content().string(containsString(",\"title\":\"testTitle\",\"content\":\"testContent\",\"parentID\":-1,\"authorID\":1,\"creationDate\":")))
                .andExpect(content().string(containsString("}")));

        //Check for unsuccessful creation due to empty title
        params.set("title", "");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        params.set("title", "testTitle");

        //Check for unsuccessful creation due to parent ID = 0
        params.set("parentID", "0");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        //Check for unsuccessful creation due to parent ID = -2
        params.set("parentID", "-2");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        params.set("parentID", "-1");

        //Check for unsuccessful creation due to author ID = 0
        params.set("authorID", "0");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        //Check for unsuccessful creation due to author ID = -1
        params.set("authorID", "-1");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        params.set("authorID", "1");

        //Check for successful creation with empty content
        params.set("content", "");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"id\":")))
                .andExpect(content().string(containsString(",\"title\":\"testTitle\",\"content\":\"\",\"parentID\":-1,\"authorID\":1,\"creationDate\":")))
                .andExpect(content().string(containsString("}")));

        params.set("content", "testContent");

        //Check for unsuccessful creation due to missing title parameter
        params.remove("title");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        params.add("title", "testTitle");

        //Check for unsuccessful creation due to missing content parameter
        params.remove("content");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        params.add("content", "testContent");

        //Check for unsuccessful creation due to missing parentID parameter
        params.remove("parentID");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        params.add("parentID", "-1");

        //Check for unsuccessful creation due to missing authorID parameter
        params.remove("authorID");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        params.add("authorID", "1");
    }

}