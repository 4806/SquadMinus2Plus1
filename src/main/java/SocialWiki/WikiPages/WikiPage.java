package SocialWiki.WikiPages;

import java.util.Calendar;

/**
 * Created by Chris on 3/4/2017.
 * Abstraction of a WikiPage
 */
public interface WikiPage {

    /**
     * Get the ID of the WikiPage
     * @return the ID of the WikiPage
     */
    Long getId();

    /**
     * Get the title of the WikiPage
     * @return the title of the WikiPage
     */
    String getTitle();

    /**
     * Get the ID of the proceeding WikiPage
     * @return the ID of the proceeding WikiPage
     */
    Long getParentID();

    /**
     * Get the date the WikiPage was created
     * @return the date the WikiPage was created
     */
    Calendar getCreationDate();

}
