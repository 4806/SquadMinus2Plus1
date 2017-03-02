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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 2/24/2017.
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
     * Method to handle the creation or editing of a WikiPage
     * @param request - contains the title, content, parentID, and authorID of the WikiPage being created/altered
     * @return the new WikiPage
     */
    @PostMapping("/createWikiPage")
    public ResponseEntity<WikiPage> createWikiPage(HttpServletRequest request) {

        //Retrieve parameters from request
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        Long parentID, authorID;

        try {
            parentID = Long.parseLong(request.getParameter("parentID"));
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }

        try {
            authorID = Long.parseLong(request.getParameter("authorID"));
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }

        //Validate parameters

        if (title == null || title.isEmpty()) {    //title must be valid, non-empty string
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }
        else if (content == null) {     //content must be valid string
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }
        else if (parentID.compareTo(0L) == 0 || parentID.compareTo(-1L) < 0) {  //Parent ID must be > 0 or -1
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }
        else if (authorID.compareTo(0L) <= 0) {   //Author ID must be > 0
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }

        WikiPage newPage;

        //If the WikiPage being created has no predecessor and is original then use specific constructor
        if (parentID.compareTo(WikiPage.IS_ORIGINAL_ID) == 0) {
            newPage = new WikiPage(title, content, authorID);
        }
        else {
            newPage = new WikiPage(title, content, parentID, authorID);
        }

        //Save the WikiPage
        try {
            newPage = wikiPageRepo.save(newPage);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(newPage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(newPage, HttpStatus.OK);
    }

    /**
     * Method to handle searching for list of WikiPages by title
     * @param request - contains the title of the WikiPages being searched for
     * @return the list of WikiPages with matching title
     */
    @GetMapping("/searchWikiPageByTitle")
    public ResponseEntity<List<WikiPage>> searchWikiPageByTitle(HttpServletRequest request) {

        //Retrieve parameters from request
        String title = request.getParameter("title");

        if (title == null || title.isEmpty()) {    //title must be valid, non-empty string
            return ResponseEntity.badRequest().body(null);
        }

        List<WikiPage> pages = wikiPageRepo.findByTitle(title);

        return new ResponseEntity<>(pages, HttpStatus.OK);

    }

    /**
     * Method to handle searching for list of WikiPages by author
     * @param request - contains the Username of the author for the WikiPages being searched for
     * @return the list of WikiPages with matching author
     */
    @GetMapping("/searchWikiPageByAuthor")
    public ResponseEntity<List<WikiPage>> searchWikiPageByAuthor(HttpServletRequest request) {

        //Retrieve parameters from request
        String authorUserName = request.getParameter("author");

        if (authorUserName == null || authorUserName.isEmpty()) {    //author must be valid, non-empty string
            return ResponseEntity.badRequest().body(null);
        }

        List<User> authorQuery = userRepo.findByUserName(authorUserName);

        if (authorQuery.size() == 1) { //If author was found, return list of pages made by author

            List<WikiPage> pages = wikiPageRepo.findByAuthorID(authorQuery.get(0).getId());

            return new ResponseEntity<>(pages, HttpStatus.OK);
        }

        //Else if author was not found, then return empty list
        return new ResponseEntity<>(new ArrayList<WikiPage>(), HttpStatus.OK);

    }

}
