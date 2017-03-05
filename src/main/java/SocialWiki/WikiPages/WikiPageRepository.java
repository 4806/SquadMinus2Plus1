package SocialWiki.WikiPages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Chris on 2/24/2017.
 * Repository to persist all WikiPages. Uses the ConcreteWikiPage class.
 */
public interface WikiPageRepository extends JpaRepository<ConcreteWikiPage, Long> {

    /**
     * Finds all WikiPages in repository with matching title
     * @param title - title to look for
     * @return all WikiPages with matching title
     */
    @Query("SELECT NEW SocialWiki.WikiPages.WikiPageWithAuthorProxy(page) " +
            "FROM ConcreteWikiPage page LEFT JOIN page.author author " +
            "WHERE UPPER(page.title) = UPPER(:title)")
    List<WikiPageWithAuthorProxy> findByTitleIgnoreCase(@Param("title") String title);

    /**
     * Find all WikiPages that match the query string
     * @param title - The title of the ConcreteWikiPage (Can be a substring of full title or ConcreteWikiPage content, or null)
     * @param username - The author username of the ConcreteWikiPage (Can be a substring of full username, or null)
     * @param content - The content of the ConcreteWikiPage (Can be a substring of full content, or null)
     * @return all WikiPages matching query
     */
    @Query("SELECT NEW SocialWiki.WikiPages.WikiPageWithAuthorProxy(page) " +
            "FROM ConcreteWikiPage page LEFT JOIN page.author author " +
                "WHERE (:title IS NULL OR " +
                    "UPPER(page.title) LIKE UPPER(CONCAT('%',:title,'%')) OR " +
                    "UPPER(page.content) LIKE UPPER(CONCAT('%',:title,'%')) ) AND " +
                "(:username IS NULL OR " +
                    "UPPER(author.userName) LIKE UPPER(CONCAT('%',:username,'%'))) AND " +
                "(:content IS NULL OR " +
                    "UPPER(page.content) LIKE UPPER(CONCAT('%',:content,'%')))")
    List<WikiPageWithAuthorProxy> findByTitleAndAuthorAndContent(@Param("title") String title, @Param("username") String username, @Param("content") String content);

}
