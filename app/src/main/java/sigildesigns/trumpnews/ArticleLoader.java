package sigildesigns.trumpnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 *
 */

public class ArticleLoader extends AsyncTaskLoader {
    // Query URL
    private String mUrl;

    /**
     * Constructs a new {@link ArticleLoader}
     *
     * @param context of the activity
     * @param url     to query the guardian news API with
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract the list of articles
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }
}
