package SocialWiki.Notifications;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import SocialWiki.WikiPages.ConcreteWikiPage;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chris on 3/30/2017.
 */
@Aspect
@Component
public class UserNotifications {

    private final String FOLLOW_USER_URI = "/followUser";
    private final String LIKE_PAGE_URI = "/likePage";
    private final String CREATE_WIKI_PAGE_URI = "/createWikiPage";

    private final List<String> NOTIFIABLE_REQUESTS = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add(FOLLOW_USER_URI);
                add(LIKE_PAGE_URI);
                add(CREATE_WIKI_PAGE_URI);
            }});

    @Autowired
    private UserRepository userRepo;

    /**
     * Pointcut for any method that has the PostMapping annotation
     */
    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    private void PostMapping() {}

    /**
     * Pointcut for any method within UserController Class
     */
    @Pointcut(value = "within(SocialWiki.Users.UserController)")
    private void userController() {}

    /**
     * Pointcut for any method within WikiPageController Class
     */
    @Pointcut(value = "within(SocialWiki.WikiPages.WikiPageController)")
    private void wikiPageController() {}

    /**
     * Pointcut for any public method that returns a ResponseEntity and has a HttpServletRequest as a parameter
     * @param request - The request from the client
     */
    @Pointcut(value = "execution(public org.springframework.http.ResponseEntity *(javax.servlet.http.HttpServletRequest,..)) && args(request,..)")
    private void restMethod(HttpServletRequest request) {}

    /**
     * Advice that executes whenever a post-mapped REST method is executed within the UserController or WikiPageController Classes
     * @param request - The request from the client
     */
    @AfterReturning(value = "PostMapping() && restMethod(request) && (userController() || wikiPageController())", argNames = "request,response", returning = "response")
    public void userNotificationAdvice(HttpServletRequest request, ResponseEntity response) {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);
        if (session != null &&                                      //If request was made by a logged in user with valid session
                response.getStatusCode().equals(HttpStatus.OK)&&    //If the response status is OK
                NOTIFIABLE_REQUESTS.contains(requestURI)) {         //If the request type was something worth notifying followers of

            String username = (String) session.getAttribute("user");
            User user = userRepo.findByUserName(username);

            if (user != null) { //If username in session is valid

                List<User> followingUsers = userRepo.findUsersFollowingUserByUser(user);

                if (followingUsers.size() > 0 ) {    //If user has any followers
                    SendNotifications(request, requestURI, followingUsers);
                }
            }
        }
    }

    /**
     * Sends notification to list of following users based on approved request of User
     * @param request - The request made by the user
     * @param requestURI - the URI of the request (The type of action made)
     * @param followingUsers - The list of users following the User who made the request
     */
    private void SendNotifications(HttpServletRequest request, String requestURI, List<User> followingUsers) {
        String param;
        switch (requestURI) {

            case FOLLOW_USER_URI:
                param = request.getParameter("user");   //Get userName of user being followed
                //TODO:Notify following users of event
                break;

            case LIKE_PAGE_URI:

                param = request.getParameter("id"); //Get Id of page being liked
                //TODO:Notify following users of event
                break;

            case CREATE_WIKI_PAGE_URI:

                param = request.getParameter("title"); //Get Title of page being created/edited
                String parentId = request.getParameter("parentID");
                if (parentId.equals(ConcreteWikiPage.IS_ORIGINAL_ID.toString())) {  //If new page created
                    //TODO:Notify following users of event
                } else {    //If page was edited
                    //TODO:Notify following users of event
                }
                break;
        }
    }


}
