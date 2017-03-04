package SocialWiki.WikiPages;


import java.text.SimpleDateFormat;
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

    /**
     * Creates a WikiPageResult object from te given WikiPage
     * @param page - The WikiPage to create a WikiPageResult from
     * @return The associated WikiPageResult from the WikiPage
     */
    public static  WikiPageResult getResult(WikiPage page) {
        return new WikiPageResult(page.getId(),page.getTitle(),page.getParentID(),page.getAuthor().getUserName(),page.getCreationDate());
    }

    @Override
    public String toString() {
        return "WikiPageResult{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", parentID=" + parentID +
                ", author='" + author + '\'' +
                ", creationDate=" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(creationDate) +
                '}';
    }

    /**
     * Creates a WikiPageResult from all required fields
     * @param id - The ID of the WikiPage
     * @param title - The title of the WikiPage
     * @param parentID - The parent ID of the WikiPage
     * @param author - The username of the author of the WikiPage
     * @param creationDate - the creation data of the WikiPage
     */
    public WikiPageResult(Long id, String title, Long parentID, String author, Calendar creationDate) {
        this.id = id;
        this.title = title;
        this.parentID = parentID;
        this.author = author;
        this.creationDate = creationDate;
    }

    /**
     * Get the ID of the WikiPage
     * @return the ID of the WikiPage
     */
    public Long getId() {
        return id;
    }

    /**
     * Get the title of the WikiPage
     * @return the title of the WikiPage
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the ID of the proceeding WikiPage
     * @return the ID of the proceeding WikiPage
     */
    public Long getParentID() {
        return parentID;
    }

    /**
     * Get the Authoring Users uername
     * @return the Authoring Users username
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Get the date the WikiPage was created
     * @return the date the WikiPage was created
     */
    public Calendar getCreationDate() {
        return creationDate;
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
     * The username of the authoring User of the WikiPage
     */
    private String author;

    /**
     * Date the WikiPage was created. WikiPage is considered created when object is instantiated
     */
    private Calendar creationDate;

}
