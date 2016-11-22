package sigildesigns.trumpnews;

/**
 * {@link Article} represents a single article for the news data.
 * It contains the title, the author, and the URL for the story.
 */

public class Article {

    // Title of the article
    private String mTitle;

    // Time of the article
    private String mTime;

    // URL for more information about the article
    private String mUrl;

    // Section the article belongs to
    private String mSection;

    public Article(String title, String time, String url, String section) {
        mTitle = title;
        mTime = time;
        mUrl = url;
        mSection = section;
    }

    // Get the title of the article
    public String getmTitle() {
        return mTitle;
    }

    // Get the time of the article
    public String getmTime() {
        return mTime;
    }

    // Get the URL for more information
    public String getmUrl() {
        return mUrl;
    }

    // Get the section of the article
    public String getmSection() {
        return mSection;
    }
}
