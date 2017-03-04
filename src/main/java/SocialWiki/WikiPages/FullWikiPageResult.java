package SocialWiki.WikiPages;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Chris on 3/4/2017.
 */
public class FullWikiPageResult extends WikiPageResult {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FullWikiPageResult that = (FullWikiPageResult) o;

        if (!this.getId().equals(that.getId())) return false;
        if (!this.getTitle().equals(that.getTitle())) return false;
        if (!this.getParentID().equals(that.getParentID())) return false;
        if (!this.getAuthor().equals(that.getAuthor())) return false;
        if (!this.getContent().equals(that.getContent())) return false;
        return this.getCreationDate().equals(that.getCreationDate());
    }

    @Override
    public String toString() {
        return "FullWikiPageResult{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", parentID=" + this.getParentID() +
                ", author='" + this.getAuthor() + '\'' +
                ", creationDate=" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(this.getCreationDate()) +
                ", content=" + content +
                '}';
    }

    /**
     * The contents of the WikiPage in Markdown
     */
    private String content;

    /**
     * Get the contents of the WikiPage
     * @return the contents of the WikiPage
     */
    public String getContent() {
        return content;
    }

    /**
     * Creates a WikiPageResult from all required fields
     * @param id           - The ID of the WikiPage
     * @param title        - The title of the WikiPage
     * @param parentID     - The parent ID of the WikiPage
     * @param author       - The username of the author of the WikiPage
     * @param creationDate - the creation data of the WikiPage
     */
    public FullWikiPageResult(Long id, String title, Long parentID, String author, Calendar creationDate, String content) {
        super(id, title, parentID, author, creationDate);
        this.content = content;
    }

    /**
     * Creates a FullWikiPageResult object from te given WikiPage
     * @param page - The WikiPage to create a FullWikiPageResult from
     * @return The associated FullWikiPageResult from the WikiPage
     */
    public static FullWikiPageResult getFullResult(WikiPage page) {
        return new FullWikiPageResult(page.getId(), page.getTitle(), page.getParentID(), page.getAuthor().getUserName(), page.getCreationDate(), page.getContent());
    }

}
