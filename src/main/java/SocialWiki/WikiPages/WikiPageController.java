package SocialWiki.WikiPages;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 2/24/2017.
 * ConcreteWikiPage controller provides a REST API interface that gives access to functionality allowing the creation, retrieval, and searching of WikiPages
 */
@RestController
public class WikiPageController {

    /**
     * Repository for all WikiPages.
     */
    @Autowired
    private WikiPageRepository wikiPageRepo;

    /**
     * Repository for all Users.
     */
    @Autowired
    private UserRepository userRepo;

    /**
     * Method to handle the creation or editing of a ConcreteWikiPage
     * @param request - contains the title, content, parentID of the ConcreteWikiPage being created/altered
     * @return the new ConcreteWikiPage
     */
    @PostMapping("/createWikiPage")
    public ResponseEntity<WikiPageWithAuthorAndContentProxy> createWikiPage(HttpServletRequest request) {

        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the logged in user from the current session
        User user = (User) session.getAttribute("user");

        //Retrieve parameters from request
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Long parentID;

        try {
            parentID = Long.parseLong(request.getParameter("parentID"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        //Validate parameters

        if (title == null || title.isEmpty()) {    //title must be valid, non-empty string
            return ResponseEntity.unprocessableEntity().body(null);
        }
        if (content == null) {     //content must be valid string
            return ResponseEntity.unprocessableEntity().body(null);
        }
        if (parentID.compareTo(0L) == 0 || parentID.compareTo(-1L) < 0) {  //Parent ID must be > 0 or -1
            return ResponseEntity.unprocessableEntity().body(null);
        }

        ConcreteWikiPage newPage;

        //If the ConcreteWikiPage being created has no predecessor and is original then use specific constructor
        if (parentID.compareTo(ConcreteWikiPage.IS_ORIGINAL_ID) == 0) {
            newPage = new ConcreteWikiPage(title, content, user);
        }
        else {
            newPage = new ConcreteWikiPage(title, content, parentID, user);
            WikiPageWithAuthorAndContentProxy parent = wikiPageRepo.findById(parentID);

            if (parent.getTitle().equals(newPage.getTitle()) && parent.getContent().equals(newPage.getContent())) { //Ensure that a change was actually made before saving
                return ResponseEntity.unprocessableEntity().body(null);
            }

        }

        //Save the ConcreteWikiPage
        try {
            newPage = wikiPageRepo.save(newPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.ok(WikiPageWithAuthorAndContentProxy.getFullResult(newPage));
    }

    /**
     * Method to handle searching for list of WikiPages. Will return all WikiPages if all parameters are blank
     * @param request - contains the parameters of the WikiPages being searched for
     * @return the list of WikiPages found
     */
    @GetMapping("/searchWikiPage")
    public ResponseEntity<List<WikiPageWithAuthorProxy>> searchWikiPage(HttpServletRequest request) {

        //Retrieve parameters from request
        String title = request.getParameter("title");
        String username = request.getParameter("user");
        String content = request.getParameter("content");

        if (title == null && username == null && content == null ) {    //If no parameters where provided
            return ResponseEntity.unprocessableEntity().body(null);
        } else {
            //Need to sanitize input to ensure no NULL parameters are passed in query
            if (title == null) {
                title = "";
            }
            if (username == null) {
                username = "";
            }
            if (content == null) {
                content = "";
            }
        }
        //Note this still allows for parameters to be NULL if at least one is not null. In these cases, null parameters will be treated as empty string by the query.

        List<WikiPageWithAuthorProxy> pages = wikiPageRepo.findByTitleAndAuthorAndContent(title, username, content);

        return ResponseEntity.ok(pages);

    }

    /**
     * Method to handle retrieval of WikiPages
     * @param request - contains id of the WikiPages being retrieved
     * @return the WikiPages found
     */
    @GetMapping("/retrieveWikiPage")
    public ResponseEntity<WikiPageWithAuthorAndContentProxy> retrieveWikiPage(HttpServletRequest request) {

        Long id;

        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        WikiPageWithAuthorAndContentProxy page = wikiPageRepo.findById(id);

        if (page == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else {
            return ResponseEntity.ok(page);
        }

    }

    /**
     * Method to handle retrieval of WikiPage history
     * @param request - contains id of the WikiPage which is source of search
     * @return the WikiPages found
     */
    @GetMapping("/retrieveWikiPageHistory")
    public ResponseEntity<List<WikiPageWithAuthorProxy>> retrieveWikiPageHistory(HttpServletRequest request) {

        //Retrieve parameters from request
        Long id;

        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        //Get root WikiPage from given page
        ConcreteWikiPage root = wikiPageRepo.findRootById(id);

        if (root == null) {    //If no root exist than invalid page given
            return ResponseEntity.unprocessableEntity().body(null);
        }

        //Get descendants of root
        List<ConcreteWikiPage> descendants = wikiPageRepo.findDescendantsById(root.getId());

        //Create list to hold all WikiPage proxy objects
        List<WikiPageWithAuthorProxy> pages = new ArrayList<>();

        //need to parse descendants list in order to create WikiPage proxy objects to send back as response
        for (ConcreteWikiPage page: descendants) {
            pages.add(new WikiPageWithAuthorProxy(page));
        }

        return ResponseEntity.ok(pages);

    }


}
