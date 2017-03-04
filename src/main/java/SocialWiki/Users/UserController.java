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
        User user = userRepo.findByUserNameAndPassword(login, pass);

        // if it could not find by using a username, try using an email instead
        if (user == null) {
            user = userRepo.findByEmailAndPassword(login, pass);

            // send an HTTP 401 response, if no user exists for provided login info
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }

        // send an HTTP 200 response with the session user
        return ResponseEntity.ok(user.asSessionUser());
    }

    /**
     * Create a new User in the system, add it to the user repository, and return a version of the User to be used in the session
     * @param request - an HTTP request that contains the new User's information
     * @return an HTTP response that contains the new User for the session, or an error response
     */
    @PostMapping("/signup")
    public ResponseEntity<User> create(HttpServletRequest request) {
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
        User newUser = new User(user, first, last, email, pass);
        userRepo.save(newUser);

        // send an HTTP 201 response with the new session User
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser.asSessionUser());
    }
}
