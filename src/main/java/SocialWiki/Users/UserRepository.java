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
     * @return a list of Users that match the userName (should be a max of one since userName is unique)
     */
    List<User> findByUserName(String userName);

    /**
     * Find the User with the corresponding email
     * @param email - the email for the User that is being queried
     * @return a list of Users that match the email (should be a max of one since email is unique)
     */
    List<User> findByEmail(String email);

    /**
     * Find the User with the corresponding userName and password
     * @param userName - the userName for the User that is being queried
     * @param password - the password for the User's account
     * @return a list of Users that match the userName and password (should be a max of one since userName is unique)
     */
    List<User> findByUserNameAndPassword(String userName, String password);

    /**
     * Find the User with the corresponding email and password
     * @param email - the email for the User that is being queried
     * @param password - the password for the User's account
     * @return a list of Users that match the email and password (should be a max of one since email is unique)
     */
    List<User> findByEmailAndPassword(String email, String password);
}
