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
 * Unit tests for WikiPageController class
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
        params.add("user", testUser1.getUserName());
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
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to parent ID = 0
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "0");
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to parent ID = -2
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-2");
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to empty user
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-2");
        params.add("user", "");
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for successful creation with empty content
        params.add("title", "testTitle");
        params.add("content", "");
        params.add("parentID", "-1");
        params.add("user", testUser1.getUserName());
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
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to missing content parameter
        params.add("title", "testTitle");
        params.add("parentID", "-1");
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to missing parentID parameter
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(post("/createWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for unsuccessful creation due to missing user parameter
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

        ConcreteWikiPage testConcreteWikiPage1 = new ConcreteWikiPage("testTitle1", "testContent1",testUser1);
        ConcreteWikiPage testConcreteWikiPage2 = new ConcreteWikiPage("testTitlePair", "testContent2",testUser2);

        testConcreteWikiPage1 = wikiPageRepository.save(testConcreteWikiPage1);
        testConcreteWikiPage2 = wikiPageRepository.save(testConcreteWikiPage2);

        ConcreteWikiPage testConcreteWikiPage3 = new ConcreteWikiPage("testTitlePair", "testContent3", testConcreteWikiPage2.getId(),testUser2);

        testConcreteWikiPage2 = wikiPageRepository.save(testConcreteWikiPage2);
        testConcreteWikiPage3 = wikiPageRepository.save(testConcreteWikiPage3);

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
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using user parameter
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using content parameter
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using title and user parameters
        params.add("title", "testTitle1");
        params.add("user", testUser1.getUserName());
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using title and content parameters
        params.add("title", "testTitle1");
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using user and content parameters
        params.add("user", testUser1.getUserName());
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search using all parameters
        params.add("title", "testTitle1");
        params.add("user", testUser1.getUserName());
        params.add("content", "testContent1");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

        //Check for successful search resulting in multiple WikiPages found
        params.add("title", "testTitlePair");
        this.mockMvc.perform(get("/searchWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(testConcreteWikiPage2.getId().intValue())))
                .andExpect(jsonPath("$.[1].id", is(testConcreteWikiPage3.getId().intValue())));
        params.clear();

    }

    @Test
    public void retrieveWikiPage() throws Exception {

        ConcreteWikiPage testConcreteWikiPage1 = new ConcreteWikiPage("testTitle1", "testContent1",testUser1);

        testConcreteWikiPage1 = wikiPageRepository.save(testConcreteWikiPage1);

        MultiValueMap<String, String> params = new HttpHeaders();

        //Check for unsuccessful search due to no parameters
        this.mockMvc.perform(get("/retrieveWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        //Check for unsuccessful search using bad id
        params.add("id", "-1");
        this.mockMvc.perform(get("/retrieveWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        params.clear();

        //Check for successful search using proper id
        params.add("id", testConcreteWikiPage1.getId().toString());
        this.mockMvc.perform(get("/retrieveWikiPage").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testConcreteWikiPage1.getId().intValue())));
        params.clear();

    }



}