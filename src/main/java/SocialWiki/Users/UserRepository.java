package SocialWiki.Users;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by connor on 2/24/17.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find the User with the corresponding userName
     * @param userName - the userName for the User that is being queried
     * @return the User that matches the userName
     */
    User findByUserName(String userName);

    /**
     * Find the User with the corresponding email
     * @param email - the email for the User that is being queried
     * @return the User that matches the email
     */
    User findByEmail(String email);

    /**
     * Find the User with the corresponding userName and password
     * @param userName - the userName for the User that is being queried
     * @param password - the password for the User's account
     * @return the User that matches the userName and password
     */
    User findByUserNameAndPassword(String userName, String password);

    /**
     * Find the User with the corresponding email and password
     * @param email - the email for the User that is being queried
     * @param password - the password for the User's account
     * @return the User that matches the email and password
     */
    User findByEmailAndPassword(String email, String password);

    /**
     * Find the User with the corresponding userName or email
     * @param userName - the userName for the User that is being queried
     * @param email - the email for the User that is being queried
     * @return a list of Users that match the userName or email (should be a max of two since userName and email is unique)
     */
    List<User> findByUserNameOrEmail(String userName, String email);
}
