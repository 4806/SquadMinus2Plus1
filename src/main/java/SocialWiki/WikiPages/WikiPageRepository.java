package SocialWiki.WikiPages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Chris on 2/24/2017.
 */
public interface WikiPageRepository extends JpaRepository<WikiPage, Long> {

    /**
     * Finds all WikiPages in repository with matching title
     * @param title - title to look for
     * @return all WikiPages with matching title
     */
    List<WikiPage> findByTitleIgnoreCase(String title);

    /**
     * Find all WikiPages that match the query string
     * @param title - The title of the WikiPage (Can be a substring of full title or WikiPage content, or null)
     * @param username - The author username of the WikiPage (Can be a substring of full username, or null)
     * @param content - The content of the WikiPage (Can be a substring of full content, or null)
     * @return all WikiPages matching query
     */
    @Query("SELECT page FROM WikiPage page LEFT JOIN page.author author WHERE (:title IS NULL OR " +
            "                                           UPPER(page.title) LIKE UPPER(CONCAT('%',:title,'%')) OR " +
            "                                           UPPER(page.content) LIKE UPPER(CONCAT('%',:title,'%')) ) AND " +
                                                "(:username IS NULL OR " +
            "                                           UPPER(author.userName) LIKE UPPER(CONCAT('%',:username,'%'))) AND " +
                                                "(:content IS NULL OR " +
            "                                           UPPER(page.content) LIKE UPPER(CONCAT('%',:content,'%')))")
    List<WikiPage> findByTitleAndAuthorAndContent(@Param("title") String title, @Param("username") String username, @Param("content") String content);

}
