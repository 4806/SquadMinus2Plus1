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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by connor on 2/24/17.
 */

@RestController
@Transactional
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
    public ResponseEntity<User> retrieveUser(HttpServletRequest request, HttpServletResponse response) {
        // get userName from request
        String userName = request.getParameter("user");

        // send an HTTP 422 response if user parameter is missing or empty
        if (userName == null || userName.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the user from the user repository
        User user = userRepo.findByUserNameWithoutDeletions(userName);

        // send an HTTP 422 response if there is no user with userName
        if (user == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.addCookie(CookieManager.getClearIsFollowedCookie());
        } else {
            String username = (String) session.getAttribute("user");
            User sessionUser = userRepo.findByUserName(username);
            response.addCookie(CookieManager.getIsFollowedCookie(sessionUser, userName));
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
    public ResponseEntity<String> followUser(HttpServletRequest request, HttpServletResponse response) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get userName from request
        String userName = request.getParameter("user");

        // send an HTTP 422 response if user parameter is missing or empty
        if (userName == null || userName.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the user from the user repository
        User followinguser = userRepo.findByUserName(userName);

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

        //Respond with isFollowed Cookie
        response.addCookie(CookieManager.getIsFollowedCookie(user, userName));

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
    public ResponseEntity<String> unfollowUser(HttpServletRequest request, HttpServletResponse response) {
        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get userName from request
        String userName = request.getParameter("user");

        // send an HTTP 422 response if user parameter is missing or empty
        if (userName == null || userName.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the user from the user repository
        User followinguser = userRepo.findByUserName(userName);

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

        //Respond with isFollowed Cookie
        response.addCookie(CookieManager.getIsFollowedCookie(user, userName));

        // send an HTTP 204 response to signify the user was successfully unfollowed
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * Gets a list of usernames of users that are being followed
     * @param request - an HTTP request that contains the session's cookie information
     * @return an HTTP response that contains users username's
     */
    @GetMapping("/getFollowingUsers")
    @Transactional
    public ResponseEntity<List<String>> getFollowingUsers(HttpServletRequest request) {

        // get userName from request
        String userName = request.getParameter("user");

        // send an HTTP 422 response if user parameter is missing or empty
        if (userName == null || userName.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        List<User> followingUsers = userRepo.findFollowedUsersByUsername(userName);

        List<String> userNames = new ArrayList<>();
        for (User followedUser: followingUsers) {
            userNames.add(followedUser.getUserName());
        }

        return ResponseEntity.ok(userNames);
    }

    /**
     * Gets a list of usernames of users that are following user specified
     * @param request - an HTTP request that contains the session's cookie information
     * @return an HTTP response that contains users username's
     */
    @GetMapping("/getUsersFollowing")
    @Transactional
    public ResponseEntity<List<String>> getUsersFollowing(HttpServletRequest request) {

        // get userName from request
        String userName = request.getParameter("user");

        // send an HTTP 422 response if user parameter is missing or empty
        if (userName == null || userName.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        User user = userRepo.findByUserName(userName);

        // send an HTTP 422 response if there is no user with username
        if (user == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        List<User> usersFollowing = userRepo.findUsersFollowingUserByUser(user);

        List<String> userNames = new ArrayList<>();
        for (User followedUser: usersFollowing) {
            userNames.add(followedUser.getUserName());
        }

        return ResponseEntity.ok(userNames);
    }

    /**
     * Gets a list of notifications for the current user
     * @param request - an empty HTTP request, the function retrieves the user from the current session
     * @return a list of notifications for the current User
     */
    @GetMapping("/getUserNotifications")
    @Transactional
    public ResponseEntity<List<String>> getUserNotifications(HttpServletRequest request) {

        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the logged in user from the current session
        String username = (String) session.getAttribute("user");
        User user = userRepo.findByUserName(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(user.getNotifications());

    }

    /**
     * Removes the given notification from the list of notifications of the current user
     * @param request - a HTTP request that contains the notification to be removed
     * @return an HTTP response that indicates the notification as removed
     */
    @PostMapping("/removeUserNotifications")
    @Transactional
    public ResponseEntity<List<String>> removeUserNotifications(HttpServletRequest request) {

        // send an HTTP 403 response if there is currently not a session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get the logged in user from the current session
        String username = (String) session.getAttribute("user");
        User user = userRepo.findByUserName(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // get notification from request
        String notification = request.getParameter("notification");

        // send an HTTP 422 response if notification parameter is missing or empty
        if (notification == null || notification.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // send an HTTP 422 response if notification is not is list of notifications
        if (!user.getNotifications().contains(notification)) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        user.removeNotification(notification);

        userRepo.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}

