package Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    public User login(HttpServletRequest request) {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        List<User> users = userRepo.findByUserNameAndPassword(login, pass);
        if (users.isEmpty()) {
            users = userRepo.findByEmailAndPassword(login, pass);
            if (users.isEmpty()) {
                return null;
            }
        }

        return users.get(0).asSessionUser();
    }
}
