package SocialWiki.WikiPages;


import java.util.Calendar;

/**
 * Created by Chris on 3/3/2017.
 * Class to hold meta information of WikiPage that will be sent in response to search query
 */
public class WikiPageResult {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WikiPageResult that = (WikiPageResult) o;

        if (!id.equals(that.id)) return false;
        if (!title.equals(that.title)) return false;
        if (!parentID.equals(that.parentID)) return false;
        if (!author.equals(that.author)) return false;
        return creationDate.equals(that.creationDate);
    }

    public static  WikiPageResult getResults(WikiPage page) {
        return new WikiPageResult(page.getId(),page.getTitle(),page.getParentID(),page.getAuthor().getUserName(),page.getCreationDate());
    }

    @Override
    public String toString() {
        return "WikiPageResult{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", parentID=" + parentID +
                ", author='" + author + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    public WikiPageResult(Long id, String title, Long parentID, String author, Calendar creationDate) {
        this.id = id;
        this.title = title;
        this.parentID = parentID;
        this.author = author;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * The unique ID for a WikiPage
     */

    private Long id;

    /**
     * The title of the WikiPage
     */
    private String title;

    /**
     * The ID of the WikiPage that this version was altered from
     */
    private Long parentID;

    /**
     * The authoring User of the WikiPage
     */
    private String author;

    /**
     * Date the WikiPage was created. WikiPage is considered created when object is instantiated
     */
    private Calendar creationDate;

}
