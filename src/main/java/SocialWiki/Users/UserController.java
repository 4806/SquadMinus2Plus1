package SocialWiki.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
     * Authenticate a User's login information and return a version of the User to be used in the session
     * @param request - an HTTP request that contains the login information
     * @return an HTTP response that contains the User for the session, or an error response
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(HttpServletRequest request) {
        // get the login parameters from the post request
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");

        // send an HTTP 422 response if either parameter is missing or empty
        if (login == null || login.isEmpty() || pass == null || pass.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        // get the user by userName and password
        List<User> users = userRepo.findByUserNameAndPassword(login, pass);

        // if it could not find by using a username, try using an email instead
        if (users.isEmpty()) {
            users = userRepo.findByEmailAndPassword(login, pass);

            // send an HTTP 401 response, if no user exists for provided login info
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }

        // send an HTTP 200 response with the session user
        return ResponseEntity.ok(users.get(0).asSessionUser());
    }
}
