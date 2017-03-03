package SocialWiki.WikiPages;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import org.junit.After;
import org.junit.Before;
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

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @After
    public void tearDown() throws Exception {
        wikiPageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Before
    public void setUp() throws Exception {
        testUser1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        testUser2 = new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2");
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);

    }

    @Test
    public void createWikiPage() throws Exception {

        //Cannot use Id when checking return string as Id is randomly generated value. Cannot creation date as there is no way to know exact time of creation

        MultiValueMap<String, String> params = new HttpHeaders();

        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-1");
        params.add("username", testUser1.getUserName());

        //Check for successful creation
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"id\":")))
                .andExpect(content().string(containsString(",\"title\":\"testTitle\",\"content\":\"testContent\",\"parentID\":-1,\"")))
                .andExpect(content().string(containsString("}")));

        //Check for unsuccessful creation due to empty title
        params.set("title", "");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().is(422));

        params.set("title", "testTitle");

        //Check for unsuccessful creation due to parent ID = 0
        params.set("parentID", "0");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().is(422));

        //Check for unsuccessful creation due to parent ID = -2
        params.set("parentID", "-2");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().is(422));

        params.set("parentID", "-1");

        //Check for successful creation with empty content
        params.set("content", "");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"id\":")))
                .andExpect(content().string(containsString(",\"title\":\"testTitle\",\"content\":\"\",\"parentID\":-1,\"")))
                .andExpect(content().string(containsString("}")));

        params.set("content", "testContent");

        //Check for unsuccessful creation due to missing title parameter
        params.remove("title");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().is(422));

        params.add("title", "testTitle");

        //Check for unsuccessful creation due to missing content parameter
        params.remove("content");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().is(422));

        params.add("content", "testContent");

        //Check for unsuccessful creation due to missing parentID parameter
        params.remove("parentID");

        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().is(422));

        params.add("parentID", "-1");

    }

}