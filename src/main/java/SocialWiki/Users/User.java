package SocialWiki.Users;

import SocialWiki.WikiPages.ConcreteWikiPage;
import SocialWiki.WikiPages.WikiPageWithAuthorProxy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by connor on 2/23/17.
 */

@Entity(name = "WikiUser")
@Component
public class User {

    /**
     * The unique ID for the User
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    /**
     * The unique name used for logging in and displaying to other SocialWiki.Users
     */
    @Column(unique = true)
    @Getter @Setter
    private String userName;

    /**
     * The User's first name
     */
    @Getter @Setter
    private String firstName;

    /**
     * The User's last name
     */
    @Getter @Setter
    private String lastName;

    /**
     * The unique email address used for logging in and contacting the User outside of the application
     */
    @Column(unique = true)
    @Getter @Setter
    private String email;

    /**
     * The password used for logging into the User's account
     */
    @Getter @Setter
    private String password;

    /**
     * The flag that marks the user's account as deleted
     */
    @Getter
    private boolean isDeleted;

    /**
     * The list of pages that the User likes
     */
    @Getter
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name="liked_page_id")
    @JsonIgnore
    private List<ConcreteWikiPage> likedPages;

    /**
     * The list of Users that the User follows
     */
    @Getter
    @OneToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<User> followedUsers;

    /**
     * Default constructor
     */
    public User() {
        this.isDeleted = false;
        this.likedPages = new ArrayList<>();
    }

    /**
     * Constructor that takes in all required attributes
     * @param userName - the user name of the User
     * @param firstName - the first name of the User
     * @param lastName - the last name of the User
     * @param email - the email address of the User
     * @param password - the password for the User
     */
    public User(String userName, String firstName, String lastName, String email, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isDeleted = false;
        this.likedPages = new ArrayList<>();
        this.followedUsers = new ArrayList<>();
    }

    /**
     * Constructor used for creating a session user
     * @param id - the unique ID of the User
     * @param userName - the user name of the User
     * @param firstName - the first name of the User
     * @param lastName - the last name of the User
     * @param email - the email address of the User
     * @param isDeleted - boolean as to whether or not the user is deleted
     * @param likedPages - list of pages the User likes
     * @param followedUsers - list of users the User follows
     */
    public User(Long id, String userName, String firstName, String lastName, String email, boolean isDeleted, List<ConcreteWikiPage> likedPages, List<User> followedUsers) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isDeleted = isDeleted;
        this.likedPages = likedPages;
        this.followedUsers = followedUsers;
    }

    /**
     * Get the string representation of the User
     * @return the User as a string
     */
    @Override
    public String toString() {
        return "ID: " + this.id +
                "\nUser Name: " + this.userName +
                "\nFirst Name: " + this.firstName +
                "\nLast Name: " + this.lastName +
                "\nEmail: " + this.email +
                "\nPassword: " + this.password;
    }

    /**
     * Determine if this User is equal to another object
     * @param obj - the other object to be compared for equality
     * @return the equality of this and obj
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        User user = (User) obj;

        if(this.id == user.getId() && this.userName.equals(user.getUserName()) && this.email.equals(user.getEmail())) {
            return true;
        }
        return false;
    }

    /**
     * Get a version of the User that does not have the password, to be used for a User's session
     * @return the User without a password
     */
    public User asSessionUser() {
        return new User(this.id,
                this.userName,
                this.firstName,
                this.lastName,
                this.email,
                this.isDeleted,
                this.likedPages,
                this.followedUsers);
    }

    /**
     * Clear the user's sensitive data and flag user account as being deleted
     */
    public void delete() {
        this.isDeleted = true;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.password = null;
        this.likedPages = null;
        this.followedUsers = null;
    }

    /**
     * Add a wiki page to the User's liked pages
     * @param page - the wiki page that the User likes
     */
    public void likePage(ConcreteWikiPage page) {
        this.likedPages.add(page);
    }

    /**
     * Remove a wiki page from the User's liked pages
     * @param page - the wiki page that the User no longer likes
     */
    public void unlikePage(ConcreteWikiPage page) {
        this.likedPages.remove(page);
    }

    /**
     * Get a list of liked pages to display in user profile
     * @return the list of liked proxy pages
     */
    public List<WikiPageWithAuthorProxy> getLikedProxyPages() {
        List<WikiPageWithAuthorProxy> list = new ArrayList<>();
        for(ConcreteWikiPage page : this.likedPages) {
            list.add(new WikiPageWithAuthorProxy(page));
        }
        return list;
    }

    /**
     * Add a user to the User's followed users
     * @param user - the user that the User followed
     */
    public void followUser(User user) {
        this.followedUsers.add(user);
    }

    /**
     * Remove a user from the User's followed users
     * @param user - the user that the User no longer follows
     */
    public void unfollowUser(User user) {
        this.followedUsers.remove(user);
    }
}
