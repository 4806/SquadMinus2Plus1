package SocialWiki.WikiPages;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Chris on 2/24/2017.
 */
public interface WikiPageRepository extends CrudRepository<WikiPage, Long> {

    List<WikiPage> findByTitle(String title);

    List<WikiPage> findByAuthorID(Long authorID);

}
