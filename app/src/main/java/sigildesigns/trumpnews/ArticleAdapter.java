package sigildesigns.trumpnews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Modified version of ArrayAdapter to list multiple things in a list view
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,
                    false);
        }

        // Get the {@link Article} object located at this position in the list
        Article currentArticle = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID article_name
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.article_name);
        // Set the title to that TextView
        titleTextView.setText(currentArticle.getmTitle());

        // Find the TextView in the list_item.xml layout with the ID section_name
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_name);
        // Set the section to that TextView
        sectionTextView.setText(currentArticle.getmSection());

        // Find the TextView in the list_item.xml layout with the ID date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        // Set the date to that TextView
        dateTextView.setText(currentArticle.getmTime());

        return listItemView;
    }
}
