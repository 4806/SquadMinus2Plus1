package SocialWiki.WikiPages;

import SocialWiki.Users.User;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by Chris on 2/24/2017.
 * Concrete representation of a WikiPage. Holds all information about a single WikiPage.
 */
@Component
@Entity
// These queries are created here as opposed to the Repository Class as they are native Postgres queries and having them defined here allows for the query result to be a ConcreteWikiPage object,
// as opposed to a list of object arrays representing the returned columns. This allows for the result set to be much easier to work with.
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "ConcreteWikiPage.findAncestorsById",
                query = "WITH RECURSIVE pages AS (SELECT p1.* FROM concrete_wiki_page p1 WHERE p1.id = :source UNION ALL SELECT p2.* FROM pages p1 JOIN concrete_wiki_page p2 ON p2.id = p1.parentid) SELECT * FROM pages ORDER BY pages.parentid",
                resultClass = ConcreteWikiPage.class
        ),
        @NamedNativeQuery(
                name = "ConcreteWikiPage.findDescendantsById",
                query = "WITH RECURSIVE pages AS (SELECT p1.* FROM concrete_wiki_page p1 WHERE p1.id = :source UNION ALL SELECT p2.* FROM concrete_wiki_page p2 INNER JOIN pages p1 ON p2.parentid = p1.id) SELECT * FROM pages ORDER BY pages.id",
                resultClass = ConcreteWikiPage.class
        ),
        @NamedNativeQuery(
                name = "ConcreteWikiPage.findRootById",
                query = "WITH RECURSIVE pages AS (SELECT p1.* FROM concrete_wiki_page p1 WHERE p1.id = :source UNION ALL SELECT p2.* FROM pages p1 JOIN concrete_wiki_page p2 ON p2.id = p1.parentid) SELECT * FROM pages ORDER BY pages.parentid ASC LIMIT 1",
                resultClass = ConcreteWikiPage.class
        )
})
public class ConcreteWikiPage implements WikiPage {

    /**
     * ID used when creating a WikiPage to represent that it is an original and has no parent
     */
    //Alternative is method is making parentId = Id. This would be more difficult as the Id is not set until the page has been saved...
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
     * The ID of the WikiPage that this version was altered from
     */
    private Long parentID;

    /**
     * Date the WikiPage was created. WikiPage is considered created when object is instantiated
     */
    @Temporal(TemporalType.TIMESTAMP)    //This tells JPA to save this as a SQL Timestamp, so it will include year, month, day, hour, minute, second
    private Calendar creationDate;

    /**
     * The contents of the WikiPage in Markdown
     */
    private String content;

    /**
     * The authoring User of the WikiPage
     */
    @ManyToOne
    private User author;

    /**
     * Default constructor
     */
    public ConcreteWikiPage() {
        super();
    }

    /**
     * Constructor that takes all attributes
     * @param title - Title of WikiPage
     * @param content - Contents of WikiPage
     * @param parentID - ID of proceeding WikiPage
     * @param author - Authoring User
     */
    public ConcreteWikiPage(String title, String content, Long parentID, User author) {
        this.title = title;
        this.parentID = parentID;
        this.content = content;
        this.author = author;
        this.creationDate = Calendar.getInstance();
    }

    /**
     * Constructor for original WikiPage, sets the parentID equal to itself
     * @param title - Title of WikiPage
     * @param content - Contents of WikiPage
     * @param author - Authoring User
     */
    public ConcreteWikiPage(String title, String content, User author) {
        this.title = title;
        this.parentID = IS_ORIGINAL_ID;
        this.content = content;
        this.author = author;
        this.creationDate = Calendar.getInstance();
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Long getParentID() {
        return parentID;
    }

    @Override
    public Calendar getCreationDate() {
        return creationDate;
    }

    /**
     * Get the contents of the WikiPage
     * @return the contents of the WikiPage
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the Authoring User
     * @return the Authoring User
     */
    public User getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcreteWikiPage that = (ConcreteWikiPage) o;

        if (!id.equals(that.id)) return false;
        if (!title.equals(that.title)) return false;
        if (!parentID.equals(that.parentID)) return false;
        if (!creationDate.equals(that.creationDate)) return false;
        if (!content.equals(that.content)) return false;
        return author.equals(that.author);
    }

    @Override
    public String toString() {
        return "ConcreteWikiPage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", parentID=" + parentID +
                ", creationDate=" + creationDate +
                ", content='" + content + '\'' +
                ", author=" + author +
                '}';
    }

}
