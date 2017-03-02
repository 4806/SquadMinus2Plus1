package SocialWiki.WikiPages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
    List<WikiPage> findByTitle(String title);

    /**
     * Finds all WikiPages in repository with matching Author ID
     * @param authorID - ID of author to look for
     * @return all WikiPages with matching Author ID
     */
    List<WikiPage> findByAuthorID(Long authorID);

    @Query("SELECT page FROM WikiPage page WHERE (:title IS NULL OR UPPER(page.title) LIKE UPPER(CONCAT('%',:title,'%')) ) AND " +
                                                "(:authorID IS NULL OR UPPER(page.authorID) LIKE UPPER(CONCAT('%',:authorID,'%'))) AND " +
                                                "(:content IS NULL OR UPPER(page.content) LIKE UPPER(CONCAT('%',:content,'%')))")
    List<WikiPage> findByTitleAndAuthorIDAndContent(@Param("title") String title, @Param("authorID") Long authorID, @Param("content") String content);

}
