package SocialWiki.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by connor on 2/24/17.
 */

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(HttpServletRequest request) {
        // get the login parameters from the post request
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");

        // send an HTTP 400 response if either parameter is missing
        if (login == null || pass == null) {
            return ResponseEntity.badRequest().body(null);
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
