package SocialWiki.Notifications;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import SocialWiki.WikiPages.ConcreteWikiPage;
import SocialWiki.WikiPages.WikiPageRepository;
import SocialWiki.WikiPages.WikiPageWithAuthorAndContentProxy;
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
public class UserNotificationsAspect {

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

    @Autowired
    private WikiPageRepository wikiRepo;

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
                (response.getStatusCode().equals(HttpStatus.OK) || response.getStatusCode().equals(HttpStatus.NO_CONTENT))&&   //If the response status is OK or NO_CONTENT
                NOTIFIABLE_REQUESTS.contains(requestURI)) {         //If the request type was something worth notifying followers of

            String username = (String) session.getAttribute("user");
            User user = userRepo.findByUserName(username);

            if (user != null) { //If username in session is valid

                List<User> followingUsers = userRepo.findUsersFollowingUserByUser(user);

                if (followingUsers.size() > 0 ) {    //If user has any followers
                    SendNotifications(request, response, requestURI, followingUsers, user);
                }
            }
        }
    }

    /**
     * Sends notification to list of following users based on approved request of User
     * @param request - The request made by the user
     * @param response - The response to the request
     * @param requestURI - the URI of the request (The type of action made)
     * @param followingUsers - The list of users following the User who made the request
     * @param user - The User that initiated the request
     */
    private void SendNotifications(HttpServletRequest request, ResponseEntity response, String requestURI, List<User> followingUsers, User user) {

        String notificationMessage = "";

        switch (requestURI) {

            case FOLLOW_USER_URI:
                String followed = request.getParameter("user");   //Get userName of user being followed
                notificationMessage = "The user " + user.getUserName() + " has just followed " + followed;
                break;

            case LIKE_PAGE_URI:
                Long id = Long.valueOf(request.getParameter("id")); //Get Id of page being liked
                ConcreteWikiPage page = wikiRepo.findById(id);
                notificationMessage = "The user " + user.getUserName() + " has just liked the page "  + page.getTitle();
                break;

            case CREATE_WIKI_PAGE_URI:
                WikiPageWithAuthorAndContentProxy createdPage = (WikiPageWithAuthorAndContentProxy) response.getBody();
                if (createdPage.getParentID().equals(ConcreteWikiPage.IS_ORIGINAL_ID)) {  //If new page created
                    notificationMessage = "The user " + user.getUserName() + " has just created the page "  + createdPage.getTitle();
                } else {    //If page was edited
                    notificationMessage = "The user " + user.getUserName() + " has just edited the page "  + createdPage.getTitle();
                }
                break;
        }

        String finalNotificationMessage = notificationMessage;    //Need an "effectively" final variable for lambda expression
        followingUsers.forEach(user1 -> user1.addNotification(finalNotificationMessage));
        userRepo.save(followingUsers);
    }


}
