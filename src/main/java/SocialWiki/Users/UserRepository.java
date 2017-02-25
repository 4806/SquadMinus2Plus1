package SocialWiki.Users;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by connor on 2/24/17.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUserName(String userName);

    List<User> findByEmail(String email);

    List<User> findByUserNameAndPassword(String userName, String password);

    List<User> findByEmailAndPassword(String email, String password);
}
