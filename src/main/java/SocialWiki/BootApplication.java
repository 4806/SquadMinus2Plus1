package SocialWiki;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by connor on 2/24/17.
 */

@SpringBootApplication
@EnableAspectJAutoProxy
public class BootApplication {
    @Bean
    public CommandLineRunner tester(UserRepository userrepo) {
        return (args) -> {
            User u = new User("unknown", "First", "Last", "myemail", "pass");
            if (userrepo.findByEmail("myemail") == null){
                userrepo.save(u);
            }
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
