package sigildesigns.trumpnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    // Input URL for Guardian News API for the trump articles
    private static final String TRUMP_URL = "http://content.guardianapis" +
            ".com/search?order-by=newest&q=trump&api-key=test";

    // Create the adapter
    private ArticleAdapter mAdapter;

    // Constant value for the ArticleLoader ID.
    private static final int ARTICLE_LOADER_ID = 1;

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ArticleLoader(this, TRUMP_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        mAdapter.clear();

        /**
         * If there is a valid list of {@link Article}s, then add them to the adapter's data set.
         * This will trigger the ListView to update.
         */
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);
        // Set the empty view in case there's no results
        articleListView.setEmptyView(findViewById(R.id.empty_view));

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView} so the list can be populated.
        articleListView.setAdapter(mAdapter);

        // Check to see if the network is active, otherwise don't attempt to load data
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with the loader
            LoaderManager loaderManager = getLoaderManager();

            /**
             * Initialize the loader. Pass in the int ID constant defined above and pass in null
             * for the bundle. Pass in this activity for the LoaderCallbacks parameter (which is
             * valid because this activity implements the LoaderCallbacks interface).
             */
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Hide the ListView and replace it with the empty TextView
            articleListView.setVisibility(GONE);
            TextView emptyTextView = (TextView) findViewById(R.id.empty_view);
            emptyTextView.setVisibility(VISIBLE);
        }
        // Set an item click listener on the ListView, which sends an intent to open a browser
        // window to the page of the article.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object to pass into intent
                Uri articleUri = Uri.parse(currentArticle.getmUrl());

                // Create a new intent to view the article
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });
    }
}
