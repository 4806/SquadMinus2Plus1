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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        //Check for successful creation
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-1");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("testTitle")))
                .andExpect(jsonPath("$.content", is("testContent")))
                .andExpect(jsonPath("$.parentID", is(-1)));
        params.clear();

        //Check for unsuccessful creation due to empty title
        params.add("title", "");
        params.add("content", "testContent");
        params.add("parentID", "-1");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to parent ID = 0
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "0");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to parent ID = -2
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-2");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to empty username
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-2");
        params.add("username", "");
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for successful creation with empty content
        params.add("title", "testTitle");
        params.add("content", "");
        params.add("parentID", "-1");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("testTitle")))
                .andExpect(jsonPath("$.content", is("")))
                .andExpect(jsonPath("$.parentID", is(-1)));
        params.clear();

        //Check for unsuccessful creation due to missing title parameter
        params.add("content", "testContent");
        params.add("parentID", "-1");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to missing content parameter
        params.add("title", "testTitle");
        params.add("parentID", "-1");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to missing parentID parameter
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("username", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to missing username parameter
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-1");
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

    }

    @Test
    public void searchWikiPage() throws Exception {

        WikiPage testWikiPage1 = new WikiPage("testTitle1", "testContent1",testUser1);
        WikiPage testWikiPage2 = new WikiPage("testTitlePair", "testContent2",testUser2);

        testWikiPage1 = wikiPageRepository.save(testWikiPage1);
        testWikiPage2 = wikiPageRepository.save(testWikiPage2);

        WikiPage testWikiPage3 = new WikiPage("testTitlePair", "testContent3", testWikiPage2.getId(),testUser2);

        testWikiPage2 = wikiPageRepository.save(testWikiPage2);
        testWikiPage3 = wikiPageRepository.save(testWikiPage3);

        MultiValueMap<String, String> params = new HttpHeaders();

        //Check for unsuccessful search due to no parameters
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Check for successful search using title parameter
        params.add("title", "testTitle1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using author parameter
        params.add("author", testUser1.getUserName());
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using content parameter
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using title and username parameters
        params.add("title", "testTitle1");
        params.add("author", testUser1.getUserName());
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using title and content parameters
        params.add("title", "testTitle1");
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using username and content parameters
        params.add("author", testUser1.getUserName());
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using all parameters
        params.add("title", "testTitle1");
        params.add("author", testUser1.getUserName());
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search resulting in multiple WikiPages found
        params.add("title", "testTitlePair");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testWikiPage2.getId().intValue())))
                .andExpect(jsonPath("$.[1].id", is(testWikiPage3.getId().intValue())));
        params.clear();

    }

}