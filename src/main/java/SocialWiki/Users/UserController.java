package SocialWiki.Users;

import SocialWiki.Cookies.CookieManager;
import SocialWiki.WikiPages.ConcreteWikiPage;
import SocialWiki.WikiPages.WikiPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.*;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by connor on 2/24/17.
 */

@RestController
public class UserController {

    /**
     * Repository for all of the Users
     */
    @Autowired
    private UserRepository userRepo;

    /**
     * Repository for all of the Wiki Pages
     */
    @Autowired
    private WikiPageRepository pageRepo;

    /**
     * Authenticate a User's login information and return a version of the User to be used in the session
     * @param request - an HTTP request that contains the login information
     * @param response - an HTTP response that will be used to provide the user cookie on successful login
     * @return an HTTP response that contains the User for the session, or an error response
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response) {
        // check that another session doesn't already exist for this request, and if there is, send a 403 response
        HttpSession session = request.getSession(false);
        if (session != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the login parameters from the post request
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");

        // send an HTTP 422 response if either parameter is missing or empty
        if (login == null || login.isEmpty() || pass == null || pass.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the user by userName and password
        User user = userRepo.findByUserNameAndPassword(login, pass);

        // if it could not find by using a username, try using an email instead
        if (user == null) {
            user = userRepo.findByEmailAndPassword(login, pass);

            // send an HTTP 401 response, if no user exists for provided login info
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }

        // create a new session for the User
        session = request.getSession();
        session.setAttribute("user", user.getUserName());

        // add the user cookie to the response
        response.addCookie(CookieManager.getUserCookie(user.getUserName()));

        // send an HTTP 200 response with the session user
        return ResponseEntity.ok(user.asSessionUser());
    }

    /**
     * Create a new User in the system, add it to the user repository, and return a version of the User to be used in the session
     * @param request - an HTTP request that contains the new User's information
     * @param response - an HTTP response that will be used to provide the user cookie on successful User creation
     * @return an HTTP response that contains the new User for the session, or an error response
     */
    @PostMapping("/signup")
    public ResponseEntity<User> create(HttpServletRequest request, HttpServletResponse response) {
        // check that another session doesn't already exist for this request, and if there is, send a 403 response
        HttpSession session = request.getSession(false);
        if (session != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the sign-up parameters from the post request
        String user = request.getParameter("user");
        String first = request.getParameter("first");
        String last = request.getParameter("last");
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");

        // send an HTTP 422 response if any parameter is missing or empty
        if (user == null || user.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (first == null || first.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (last == null || last.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (email == null || email.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (pass == null || pass.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // check to see if the userName or email are already taken
        List<User> uniqueAttributeCheck = userRepo.findByUserNameOrEmail(user, email);

        // send an HTTP 409 response if either already exists
        if (uniqueAttributeCheck.size() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        // create the new User and save it in the user repository
        User newUser = userRepo.save(new User(user, first, last, email, pass));

        // create a new session for the new User
        session = request.getSession();
        session.setAttribute("user", user);

        // add the user cookie to the response
        response.addCookie(CookieManager.getUserCookie(user));

        // send an HTTP 201 response with the new session User
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser.asSessionUser());
    }

    /**
     * Log the user out of their current session
     * @param request - an HTTP request that contains the session's cookie information
     * @param response - an HTTP response that will be used to clear the user cookie on successful log out
     * @return an HTTP response that signifies whether the log out was successful
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // invalidate the old session, so that one for the new user can be created later
        session.invalidate();

        // clear the cookies for the user's session on logout
        response.addCookie(CookieManager.getClearUserCookie());
        response.addCookie(CookieManager.getClearIsLikedCookie());
        // TODO: uncomment this when it is added
        // response.addCookie(CookieManager.getClearIsFollowedCookie());

        // send an HTTP 204 response to signify successful logout
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * Remove a user account from the system
     * @param request - an HTTP request that contains the session's cookie information
     * @param response - an HTTP response that will be used to clear the user cookie on successful delete
     * @return an HTTP response that signifies whether the deletion was successful
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the logged in user from the current session
        String username = (String) session.getAttribute("user");
        User user = userRepo.findByUserName(username);

        // remove the user's sensitive info and mark as deleted
        user.delete();

        // save the deletion of the account into the repository
        userRepo.save(user);

        // invalidate the user session, since the user no longer exists
        session.invalidate();

        // add the clearUser cookie so that it deletes the browsers cookie
        response.addCookie(CookieManager.getClearUserCookie());

        // send an HTTP 204 response to signify successful deletion
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * Add a wiki page to the list of pages that a User currently likes
     * @param request - an HTTP request that contains the session's cookie information
     * @return an HTTP response that signifies whether the liking of the page was successful
     */
    @PostMapping("/likePage")
    @Transactional
    public ResponseEntity<String> likePage(HttpServletRequest request) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the pageId parameter from the post request
        Long pageId;
        try {
            pageId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null); // send an HTTP 422 response if parameter cannot be cast as a Long
        }

        // get the wiki page from the page repo
        ConcreteWikiPage page = pageRepo.findOne(pageId);

        // send an HTTP 422 response if there is no page with pageId
        if (page == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the logged in user from the current session
        String username = (String) session.getAttribute("user");
        User user = userRepo.findByUserName(username);

        // send an HTTP 403 response if the User already likes the page
        if (user.getLikedPages().contains(page)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // add the page to the User's liked pages
        user.likePage(page);

        // save the update to the user in the database and session
        userRepo.save(user);

        // send an HTTP 204 response to signify the page was successfully liked
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/unlikePage")
    @Transactional
    public ResponseEntity<String> unlikePage(HttpServletRequest request) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the pageId parameter from the post request
        Long pageId;
        try {
            pageId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null); // send an HTTP 422 response if parameter cannot be cast as a Long
        }

        // get the wiki page from the page repo
        ConcreteWikiPage page = pageRepo.findOne(pageId);

        // send an HTTP 422 response if there is no page with pageId
        if (page == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the logged in user from the current session
        String username = (String) session.getAttribute("user");
        User user = userRepo.findByUserName(username);

        // send an HTTP 403 response if the User does not like the page
        if (!user.getLikedPages().contains(page)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // remove the page from the User's liked pages
        user.unlikePage(page);

        // save the update to the user in the database and session
        user = userRepo.save(user);

        // send an HTTP 204 response to signify the page was successfully unliked
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/retrieveUser")
    public ResponseEntity<User> retrieveUser(HttpServletRequest request) {
        // get userName from request
        String userName = request.getParameter("user");

        // send an HTTP 422 response if user parameter is missing or empty
        if (userName == null || userName.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the user from the user repository
        User user = userRepo.findByUserName(userName);

        // send an HTTP 422 response if there is no user with userName
        if (user == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        return ResponseEntity.ok(user.asSessionUser());
    }

    /**
     * Add a user to the list of users that a User currently follows
     * @param request - an HTTP request that contains the session's cookie information
     * @return an HTTP response that signifies whether the following of the user was successful
     */
    @PostMapping("/followUser")
    @Transactional
    public ResponseEntity<String> followUser(HttpServletRequest request) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the userId parameter from the post request
        Long userId;
        try {
            userId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null); // send an HTTP 422 response if parameter cannot be cast as a Long
        }

        // get the user from the user repo
        User followinguser = userRepo.findOne(userId);

        // send an HTTP 422 response if there is no user with userId
        if (followinguser == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the logged in user from the current session
        String username = (String) session.getAttribute("user");
        User user = userRepo.findByUserName(username);

        // send an HTTP 403 response if the User already follows the user or the following user is the user behind the request
        if (user.getFollowedUsers().contains(followinguser) || followinguser.equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // add the user to the User's followed users
        user.followUser(followinguser);

        // save the update to the user in the database and session
        userRepo.save(user);

        // send an HTTP 204 response to signify the user was successfully followed
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * Remove a user from the list of users that a User currently follows
     * @param request - an HTTP request that contains the session's cookie information
     * @return an HTTP response that signifies whether the user was removed successfully
     */
    @PostMapping("/unfollowUser")
    @Transactional
    public ResponseEntity<String> unfollowPage(HttpServletRequest request) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the pageId parameter from the post request
        Long pageId;
        try {
            pageId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null); // send an HTTP 422 response if parameter cannot be cast as a Long
        }

        // get the userId parameter from the post request
        Long userId;
        try {
            userId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return ResponseEntity.unprocessableEntity().body(null); // send an HTTP 422 response if parameter cannot be cast as a Long
        }

        // get the user from the user repo
        User followinguser = userRepo.findOne(userId);

        // send an HTTP 422 response if there is no user with userId
        if (followinguser == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the logged in user from the current session
        String username = (String) session.getAttribute("user");
        User user = userRepo.findByUserName(username);

        // send an HTTP 403 response if the User doesn't follow the user
        if (!user.getFollowedUsers().contains(followinguser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }


        // remove the user from the User's followed users
        user.unfollowUser(followinguser);

        // save the update to the user in the database and session
        user = userRepo.save(user);

        // send an HTTP 204 response to signify the user was successfully unfollowed
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
