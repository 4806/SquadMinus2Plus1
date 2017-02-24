package WikiPages;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by Chris on 2/24/2017.
 */
@Component
@Entity
public class WikiPage {


    /**
     * ID used when creating a WikiPage to represent that it is an original and has no parent
     */
    public static final Long isOriginalID = -1L;

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
     * The ID of the WikiPage that this version was altered from. If it is the first version of a WikiPage, the parent is itself
     */
    private Long parentID;

    /**
     * The ID of the authoring User of the WikiPage
     */
    private Long authorID;

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
        this.parentID = this.id;
        this.authorID = authorID;
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
}
