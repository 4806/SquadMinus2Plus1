package Users;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by connor on 2/23/17.
 */

@Entity
@Component
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * The unique ID for the User
     */
    private long id;

    /**
     * The name used for logging in and displaying to other Users
     */
    private String userName;

    /**
     * The User's first name
     */
    private String firstName;

    /**
     * The User's last name
     */
    private String lastName;

    /**
     * The email address used for logging in and contacting the User outside of the application
     */
    private String email;

    /**
     * The password used for logging into the User's account
     */
    private String password;

    /**
     * Default constructor
     */
    public User() {
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
    }

    /**
     * Get the unique ID of the User
     * @return the User's ID
     */
    public long getId() {
        return id;
    }

    /**
     * Get the user name for the User
     * @return the name of the User
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the user name of the User
     * @param userName - the User's new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the first name of the User
     * @return the User's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the User
     * @param firstName - the User's new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the last name of the User
     * @return the User's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the User
     * @param lastName - the User's new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the email address of the User
     * @return the User's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address of the User
     * @param email - the User's new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password for the User
     * @return the User's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password for the User
     * @param password - the User's new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
