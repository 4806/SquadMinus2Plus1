package SocialWiki.WikiPages;


import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

/**
 * Created by Chris on 3/3/2017.
 * Proxy for ConcreteWikiPage that exposes the username of the authoring user
 */
public class WikiPageWithAuthorProxy implements WikiPage {

    /**
     * The real WikiPage
     */
    protected ConcreteWikiPage realWikiPage;


    @Getter @Setter
    private Integer likes;

    /**
     * Constructs WikiPageWithAuthorProxy for the provided WikiPage
     * @param realWikiPage - WikiPage that requires proxy
     */
    public WikiPageWithAuthorProxy(ConcreteWikiPage realWikiPage) {
        this.realWikiPage = realWikiPage;
    }

    /**
     * Get the Authoring Users username
     * @return the Authoring Users username
     */
    public String getAuthor() {
        return realWikiPage.getAuthor().getUserName();
    }

    /**
     * Creates a WikiPageWithAuthorProxy object from te given WikiPage
     * @param page - The WikiPage to create a WikiPageWithAuthorProxy from
     * @return The associated WikiPageWithAuthorProxy from the WikiPage
     */
    public static WikiPageWithAuthorProxy getResult(ConcreteWikiPage page) {
        return new WikiPageWithAuthorProxy(page);
    }

    @Override
    public Long getId() {
        return realWikiPage.getId();
    }

    @Override
    public String getTitle() {
        return realWikiPage.getTitle();
    }

    @Override
    public Long getParentID() {
        return realWikiPage.getParentID();
    }

    @Override
    public Calendar getCreationDate() {
        return realWikiPage.getCreationDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WikiPageWithAuthorProxy that = (WikiPageWithAuthorProxy) o;

        return realWikiPage.equals(that.realWikiPage);
    }

}
