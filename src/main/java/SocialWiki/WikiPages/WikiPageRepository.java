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
            "FROM ConcreteWikiPage page  " +
            "WHERE UPPER(page.title) = UPPER(:title)")
    List<WikiPageWithAuthorProxy> findByTitleIgnoreCase(@Param("title") String title);

    /**
     * Finds the WikiPage in repository with matching id
     * @param id - id to look for
     * @return The WikiPages with matching id
     */
    @Query("SELECT NEW SocialWiki.WikiPages.WikiPageWithAuthorAndContentProxy(page) " +
            "FROM ConcreteWikiPage page  " +
            "WHERE page.id = :id")
    WikiPageWithAuthorAndContentProxy findById(@Param("id") Long id);

    /**
     * Find all WikiPages that match the query string. Cannot accept NULL parameters
     * @param title - The title of the ConcreteWikiPage (Can be a substring of full title or ConcreteWikiPage content, or null)
     * @param username - The author username of the ConcreteWikiPage (Can be a substring of full username, or null)
     * @param content - The content of the ConcreteWikiPage (Can be a substring of full content, or null)
     * @return all WikiPages matching query
     */
    @Query("SELECT NEW SocialWiki.WikiPages.WikiPageWithAuthorProxy(page) " +
            "FROM ConcreteWikiPage page LEFT JOIN page.author author " +
            "WHERE (" +
            "(UPPER(page.title) LIKE ('%' || UPPER(:title) || '%') OR  UPPER(page.content) LIKE ('%' || UPPER(:title) || '%') ) AND " +
            "(UPPER(author.userName) LIKE ('%' || UPPER(:username) || '%') ) AND " +
            "(UPPER(page.content) LIKE ('%' || UPPER(:pageContent) || '%') )" +
            ")")
    List<WikiPageWithAuthorProxy> findByTitleAndAuthorAndContent(@Param("title") String title, @Param("username") String username, @Param("pageContent") String content);

}
