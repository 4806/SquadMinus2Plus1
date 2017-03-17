package SocialWiki.WikiPages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;

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
     * Finds the descendants of source WikiPage
     * Need to make native query to access recursive capabilities of PostgreSQL. This makes creating objects within the query impossible and forces
     * the return to be a list of objects.
     * @param sourceId - The id of root WikiPage
     * @return Descendant WikiPages
     */
    @Query(nativeQuery = true,
            value = "WITH RECURSIVE pages AS (" +
                    " SELECT p1.*" +
                    " FROM concrete_wiki_page p1 WHERE p1.id = :source" +
                    " UNION ALL" +
                    " SELECT p2.*" +
                    " FROM concrete_wiki_page p2 INNER JOIN pages p1 ON p2.parentid = p1.id" +
                    ") SELECT * FROM pages")
    List<List<Objects>> findDescendantsById(@Param("source") Long sourceId);

    /**
     * Finds the ancestors of source WikiPage
     * Need to make native query to access recursive capabilities of PostgreSQL. This makes creating objects within the query impossible and forces
     * the return to be a list of objects.
     * @param sourceId - The id of leaf WikiPage
     * @return Ancestor WikiPages
     */
    @Query(nativeQuery = true,
            value = "WITH RECURSIVE pages AS (" +
                    " SELECT p1.*" +
                    " FROM concrete_wiki_page p1 WHERE p1.id = :source" +
                    " UNION ALL" +
                    " SELECT p2.*" +
                    " FROM pages p1 JOIN concrete_wiki_page p2 ON p2.id = p1.parentid" +
                    ") SELECT * FROM pages")
    List<List<Objects>> findAncestorsById(@Param("source") Long sourceId);

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
