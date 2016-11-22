package sigildesigns.trumpnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static sigildesigns.trumpnews.MainActivity.LOG_TAG;

/**
 * Holds the helper methods related to requesting and receiving data from the Guardian News API.
 */

public class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Make an HTTP request to the Guardian News API and return a String as the response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If url is null, return an empty string early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // Then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem receiving the JSON Response", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response
     * from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
            return null;
        }
        return url;
    }

    // Return a list of {@link Article} objects that has been built up from parson a JSON response.
    public static List<Article> extractFeatureFromJson(String guardianJson) {
        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(guardianJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        List<Article> articles = new ArrayList<>();

        /**
         * Try to parse TRUMP_URL. If there's a problem with the way the JSON is formatted,
         * a JSONException exception object will be thrown. Catch the exception so the app doesn't
         * crash, and print the error message to the logs.
         */
        try {
            // Create a new JSONObject named root which pulls JSON from TRUMP_URL
            JSONObject root = new JSONObject(guardianJson);

            // Extract the "response" object from the JSON
            JSONObject response = root.getJSONObject("response");

            // Extract the "results" array from the JSON
            JSONArray results = response.getJSONArray("results");

            // Loop through each JSONObject in the results array, add it to the ArrayList
            for (int i = 0; i < results.length(); i++) {
                // Collect the JSONObject
                JSONObject article = results.getJSONObject(i);
                // Extract the "type" from the article
                String type = article.getString("type");
                // Extract the title
                String title = article.getString("webTitle");
                // Extract the URL
                String webUrl = article.getString("webUrl");
                // Extract the time
                String time = article.getString("webPublicationDate");
                // Format the time
                time = time.substring(0, 10);

                // Extract the section
                String section = article.getString("sectionName");
                // Add the article to the ArrayList
                articles.add(new Article(title, time, webUrl, section));
            }
        } catch (JSONException e) {
            /**
             * If an error is thrown when executing any of the above statements in the try block
             * catch the exception here, so the app doesn't crash. Print a log message
             * with the message from the exception.
             */
            Log.e("QueryUtils", "Problem parsing the Guardian JSON results", e);
        }
        return articles;
    }

    // Query the GuardianNews API and return a list of {@link Article} objects.
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create a URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Article}s
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Article}s
        return articles;
    }
}
