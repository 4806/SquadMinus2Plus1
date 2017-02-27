package SocialWiki.WikiPages;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Chris on 2/24/2017.
 */
@Component
@Entity
public class WikiPage {


    /**
     * ID used when creating a WikiPage to represent that it is an original and has no parent
     */
    /*
        Alternative is method is making parentId = Id. This would be more difficult as the Id is not set until the page has been saved...
    */
    public static final Long IS_ORIGINAL_ID = -1L;

    /**
     * The unique ID for a WikiPage
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The title of the WikiPage
     */
    private String title;

    /**
     * The contents of the WikiPage in Markdown
     */
    private String content;

    /**
     * The ID of the WikiPage that this version was altered from
     */
    private Long parentID;

    /**
     * The ID of the authoring User of the WikiPage
     */
    private Long authorID;

    /**
     * Date the WikiPage was created. WikiPage is considered created when object is instantiated
     */
    @Temporal(TemporalType.TIMESTAMP)    //This tells JPA to save this as a SQL Timestamp, so it will include year, month, day, hour, minute, second
    private Calendar creationDate;


    /**
     * Default constructor
     */
    public WikiPage() {}

    /**
     * Constructor that takes all attributes
     * @param title - Title of WikiPage
     * @param content - Contents of WikiPage
     * @param parentID - ID of proceeding WikiPage
     * @param authorID - ID of authoring User
     */
    public WikiPage(String title, String content, Long parentID, Long authorID) {
        this.title = title;
        this.content = content;
        this.parentID = parentID;
        this.authorID = authorID;
        this.creationDate = Calendar.getInstance();
    }

    /**
     * Constructor for original WikiPage, sets the parentID equal to itself
     * @param title - Title of WikiPage
     * @param content - Contents of WikiPage
     * @param authorID - ID of authoring User
     */
    public WikiPage(String title, String content, Long authorID) {
        this.title = title;
        this.content = content;
        this.parentID = IS_ORIGINAL_ID;
        this.authorID = authorID;
        this.creationDate = Calendar.getInstance();
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
     * Get the contents of the WikiPage
     * @return the contents of the WikiPage
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the ID of the proceeding WikiPage
     * @return the ID of the proceeding WikiPage
     */
    public Long getParentID() {
        return parentID;
    }

    /**
     * Get the ID of the authoring User
     * @return the ID of the authoring User
     */
    public Long getAuthorID() {
        return authorID;
    }

    /**
     * Get the date the WikiPage was created
     * @return the date the WikiPage was created
     */
    public Calendar getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "WikiPage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", parentID=" + parentID +
                ", authorID=" + authorID +
                ", creationDate=" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(creationDate.getTime()) +
                '}';
    }
}
