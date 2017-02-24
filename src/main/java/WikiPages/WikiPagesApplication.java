package WikiPages;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by Chris on 2/24/2017.
 */
@SpringBootApplication
public class WikiPagesApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiPagesApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(WikiPageRepository repo) {
        return (args) -> {
            WikiPage testWikiPage1  = new WikiPage("testTitle1", "testContent1",1L);
            repo.save(testWikiPage1);
            WikiPage testWikiPage2 = new WikiPage("testTitle2", "testContent2", testWikiPage1.getId(),2L);
            repo.save(testWikiPage2);

            System.out.println("----- All Wiki Pages -----");
            for(WikiPage page : repo.findAll()) {
                System.out.println(page.toString());
            }

            System.out.println("----- Filtered by title: testTitle1 -----");
            for(WikiPage page : repo.findByTitle("testTitle1")) {
                System.out.println(page.toString());
            }

            System.out.println("----- Filtered by AuthorID: 2L -----");
            for(WikiPage page : repo.findByAuthorID(2L)) {
                System.out.println(page.toString());
            }
        };
    }

}
