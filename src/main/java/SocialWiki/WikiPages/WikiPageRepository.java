package SocialWiki.WikiPages;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Chris on 2/24/2017.
 */
public interface WikiPageRepository extends CrudRepository<WikiPage, Long> {

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

}
