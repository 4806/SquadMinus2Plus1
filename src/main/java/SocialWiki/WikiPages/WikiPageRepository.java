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
            "WHERE UPPER(page.title) = UPPER(:title)" +
            "ORDER BY " +
            "CASE " +
            "    WHEN (UPPER(page.title) = UPPER(:title) ) THEN 0 " +
            "    WHEN (UPPER(page.title) LIKE (UPPER(:title) || '%') ) THEN 1 " +
            "    WHEN (UPPER(page.title) LIKE ('%' || UPPER(:title) || '%') ) THEN 2 " +
            "    WHEN (UPPER(page.title) LIKE ('%' || UPPER(:title)) ) THEN 3 " +
            "    ELSE 4 " +
            "END ASC," +
            "page.creationDate DESC," +
            "page.id DESC")
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
     * Finds the descendants of source WikiPage
     * Need to make native query to access recursive capabilities of PostgreSQL.
     * @param sourceId - The id of source WikiPage
     * @return Descendant WikiPages
     */
    @Query(name = "ConcreteWikiPage.findDescendantsById") //calls the NamedNativeQuery defined in the ConcreteWikiPage Class
    List<ConcreteWikiPage> findDescendantsById(@Param("source") Long sourceId);

    /**
     * Finds the ancestors of source WikiPage
     * Need to make native query to access recursive capabilities of PostgreSQL.
     * @param sourceId - The id of source WikiPage
     * @return Ancestor WikiPages
     */
    @Query(name = "ConcreteWikiPage.findAncestorsById") //calls the NamedNativeQuery defined in the ConcreteWikiPage Class
    List<ConcreteWikiPage> findAncestorsById(@Param("source") Long sourceId);

    /**
     * Finds the root WikiPage of source WikiPage
     * Need to make native query to access recursive capabilities of PostgreSQL.
     * @param sourceId - The id of source WikiPage
     * @return Root WikiPages
     */
    @Query(name = "ConcreteWikiPage.findRootById") //calls the NamedNativeQuery defined in the ConcreteWikiPage Class
    ConcreteWikiPage findRootById(@Param("source") Long sourceId);

    /**
     * Find all WikiPages that match the query string. Cannot accept NULL parameters
     * @param title - The title of the ConcreteWikiPage (Can be a substring of full title)
     * @param username - The author username of the ConcreteWikiPage (Can be a substring of full username)
     * @param content - The content of the ConcreteWikiPage (Can be a substring of full content)
     * @return all WikiPages matching query
     */
    @Query("SELECT NEW SocialWiki.WikiPages.WikiPageWithAuthorProxy(page) " +
            "FROM ConcreteWikiPage page LEFT JOIN page.author author " +
            "WHERE (" +
            "(UPPER(page.title) LIKE ('%' || UPPER(:title) || '%')  ) AND " +
            "(UPPER(author.userName) LIKE ('%' || UPPER(:username) || '%') ) AND " +
            "(UPPER(page.content) LIKE ('%' || UPPER(:pageContent) || '%') )) " +
            "ORDER BY " +
            "CASE " +
            "    WHEN (UPPER(page.title) = UPPER(:title) ) THEN 0 " +
            "    WHEN (UPPER(page.title) LIKE (UPPER(:title) || '%') ) THEN 1 " +
            "    WHEN (UPPER(page.title) LIKE ('%' || UPPER(:title) || '%') ) THEN 2 " +
            "    WHEN (UPPER(page.title) LIKE ('%' || UPPER(:title)) ) THEN 3 " +
            "    ELSE 4 " +
            "END ASC," +
            "CASE " +
            "    WHEN (UPPER(author.userName) = UPPER(:username) ) THEN 0 " +
            "    WHEN (UPPER(author.userName) LIKE (UPPER(:username) || '%') ) THEN 1 " +
            "    WHEN (UPPER(author.userName) LIKE ('%' || UPPER(:username) || '%') ) THEN 2 " +
            "    WHEN (UPPER(author.userName) LIKE ('%' || UPPER(:username)) ) THEN 3 " +
            "    ELSE 4 " +
            "END ASC," +
            "page.creationDate DESC," +
            "page.id DESC")
    List<WikiPageWithAuthorProxy> findByTitleAndAuthorAndContent(@Param("title") String title, @Param("username") String username, @Param("pageContent") String content);

    /**
     * Find all WikiPages that match the query string. Cannot accept NULL parameters
     * @param titleOrContent - The title of the ConcreteWikiPage (Can be a substring of full title or ConcreteWikiPage content)
     * @return all WikiPages matching query
     */
    @Query("SELECT NEW SocialWiki.WikiPages.WikiPageWithAuthorProxy(page) " +
            "FROM ConcreteWikiPage page LEFT JOIN page.author author " +
            "WHERE " +
            "(UPPER(page.title) LIKE ('%' || UPPER(:title) || '%') OR  UPPER(page.content) LIKE ('%' || UPPER(:title) || '%') ) " +
            "ORDER BY " +
            "CASE " +
            "    WHEN (UPPER(page.title) = UPPER(:title) ) THEN 0 " +
            "    WHEN (UPPER(page.title) LIKE (UPPER(:title) || '%') ) THEN 1 " +
            "    WHEN (UPPER(page.title) LIKE ('%' || UPPER(:title) || '%') ) THEN 2 " +
            "    WHEN (UPPER(page.title) LIKE ('%' || UPPER(:title)) ) THEN 3 " +
            "    ELSE 4 " +
            "END ASC," +
            "page.creationDate DESC," +
            "page.id DESC")
    List<WikiPageWithAuthorProxy> findByTitleAndContent(@Param("title") String titleOrContent);


}
