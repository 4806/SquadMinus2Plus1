package SocialWiki.Users;

import SocialWiki.Cookies.CookieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by Chris on 3/30/2017.
 */
@RestController
public class UserAccountController {

    /**
     * Repository for all of the Users
     */
    @Autowired
    private UserRepository userRepo;

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

        String pattern = "^[a-zA-Z0-9]*$";

        // send an HTTP 422 response if any parameter is missing, empty, does not match the pattern (except email)
        if (user == null || user.isEmpty() || !user.matches(pattern)) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (first == null || first.isEmpty() || !first.matches(pattern)) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (last == null || last.isEmpty() || !last.matches(pattern)) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (email == null || email.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        } else if (pass == null || pass.isEmpty() || !pass.matches(pattern)) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // check to see if the userName or email are already taken
        List<User> uniqueAttributeCheck = userRepo.findByUserNameOrEmail(user, email);

        // send an HTTP 409 response if either already exists
        if (uniqueAttributeCheck.size() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        User newUser;

        // create the new User and save it in the user repository
        try {
            newUser = userRepo.save(new User(user, first, last, email, pass));
        } catch (TransactionSystemException e) { // this exception will be thrown if the email does not validate
            return ResponseEntity.unprocessableEntity().body(null);
        }

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

        // add the clearUser cookie so that it deletes the browsers cookie
        response.addCookie(CookieManager.getClearUserCookie());

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

        //remove deleted user from all followers lists
        List<User> followers = userRepo.findUsersFollowingUserByUser(user);
        followers.forEach(user1 -> user1.unfollowUser(user));
        userRepo.save(followers);

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

}
