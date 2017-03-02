package SocialWiki.WikiPages;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import org.junit.After;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @After
    public void tearDown() throws Exception {
        wikiPageRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @Test
    public void searchWikiPageByTitle() throws Exception {

        MultiValueMap<String, String> params = new HttpHeaders();

        params.add("title", "testTitle1");

        //Set up Database for tests
        WikiPage testWikiPage1 = new WikiPage("testTitle1", "testContent1",1L);
        WikiPage testWikiPage2 = new WikiPage("testTitlePair", "testContent1",2L);
        WikiPage testWikiPage3 = new WikiPage("testTitlePair", "testContent2", testWikiPage2.getId(),2L);

        testWikiPage1 = wikiPageRepository.save(testWikiPage1);
        testWikiPage2 = wikiPageRepository.save(testWikiPage2);
        testWikiPage3 = wikiPageRepository.save(testWikiPage3);

        //Check for successful search for single WikiPage
        this.mockMvc.perform(get("/searchWikiPageByTitle").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testWikiPage1.toJSON())));


        params.set("title", "testTitlePair");

        //Check for successful search for multiple WikiPages
        this.mockMvc.perform(get("/searchWikiPageByTitle").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testWikiPage2.toJSON())))
                .andExpect(content().string(containsString(testWikiPage3.toJSON())));

        params.remove("title");

        //Test bad search with no parameter title
        this.mockMvc.perform(get("/searchWikiPageByTitle").params(params))
                .andDo(print())
                .andExpect(status().isBadRequest());

        params.add("title", "");

        //Test bad search with empty title parameter
        this.mockMvc.perform(get("/searchWikiPageByTitle").params(params))
                .andDo(print())
                .andExpect(status().isBadRequest());

        params.set("title", "non-existing title");

        //Check for successful search for WikiPage, but with non-existing title
        this.mockMvc.perform(get("/searchWikiPageByTitle").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void searchWikiPageByAuthor() throws Exception {

        MultiValueMap<String, String> params = new HttpHeaders();

        params.add("author", "testUserName1");

        User testUser1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        User testUser2 = new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2");
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);

        //Set up Database for tests
        WikiPage testWikiPage1 = new WikiPage("testTitle1", "testContent1", testUser1.getId());
        WikiPage testWikiPage2 = new WikiPage("testTitlePair", "testContent1", testUser2.getId());
        WikiPage testWikiPage3 = new WikiPage("testTitlePair", "testContent2", testWikiPage2.getId(), testUser2.getId());

        testWikiPage1 = wikiPageRepository.save(testWikiPage1);
        testWikiPage2 = wikiPageRepository.save(testWikiPage2);
        testWikiPage3 = wikiPageRepository.save(testWikiPage3);

        //Check for successful search for single WikiPage
        this.mockMvc.perform(get("/searchWikiPageByAuthor").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testWikiPage1.toJSON())));


        params.set("author", "testUserName2");

        //Check for successful search for multiple WikiPages
        this.mockMvc.perform(get("/searchWikiPageByAuthor").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testWikiPage2.toJSON())))
                .andExpect(content().string(containsString(testWikiPage3.toJSON())));

        params.remove("author");

        //Test bad search with no parameter author
        this.mockMvc.perform(get("/searchWikiPageByAuthor").params(params))
                .andDo(print())
                .andExpect(status().isBadRequest());

        params.add("author", "");

        //Test bad search with empty author parameter
        this.mockMvc.perform(get("/searchWikiPageByAuthor").params(params))
                .andDo(print())
                .andExpect(status().isBadRequest());

        params.set("author", "non-existing author");

        //Check for successful search for WikiPage, but with non-existing author
        this.mockMvc.perform(get("/searchWikiPageByAuthor").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));

    }

}