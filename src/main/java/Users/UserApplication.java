package Users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by connor on 2/24/17.
 */

@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    //@Bean
    public CommandLineRunner demo(UserRepository repo) {
        return (args) -> {
            repo.save(new User("testUserName", "testFirstName", "testLastName", "testEmail", "testPassword"));
            repo.save(new User("CJMatx", "Connor", "Matthews", "connor.matthews@carleton.ca", "password123"));

            User user1 = new User();
            user1.setUserName("testUserName1");
            user1.setFirstName("testFirstName1");
            user1.setLastName("testLastName1");
            user1.setEmail("testEmail1");
            user1.setPassword("testPassword1");

            repo.save(user1);

            System.out.println("----- All users -----");
            for(User user : repo.findAll()) {
                System.out.println(user.toString());
            }

            System.out.println("----- Filtered by username: testUserName -----");
            for(User user : repo.findByUserName("testUserName")) {
                System.out.println(user.toString());
            }
        };
    }
}
