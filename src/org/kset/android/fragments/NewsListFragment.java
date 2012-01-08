package org.kset.android.fragments;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.kset.android.R;
import org.kset.android.models.Source.Category.Feed;
import org.kset.android.models.Source.Category.Feed.Article;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This fragment contains a list of all the news available from the following
 * RSS feed: http://www.kset.org/feeds/rss/
 * 
 * @author Petar Šegina <psegina@kset.org>
 * 
 */
public class NewsListFragment extends ListFragment {
	private static final String FEED_LOCATION = "http://www.kset.org/feeds/rss/";
	private static Feed mFeed = new Feed("KSET", "KSET", FEED_LOCATION);

	private List<NewsListListener> mListeners = new Vector<NewsListListener>();

	/**
	 * A simple Adapter which binds data from an array of Article objects to a
	 * list.
	 * 
	 * @author Petar Šegina <psegina@kset.org>
	 * 
	 */
	private class FeedListAdapter extends BaseAdapter {

		private Context mContext;
		private Calendar mCalendar = Calendar.getInstance();
		private int mLayout, size, cPosition = 0;
		private ViewHolder holder;

		/*
		 * Cache fields
		 */
		private String[] cTitle, cContent, cDay, cDayName, cMonth;

		/**
		 * ViewHolder class used to avoid expensive calls to findViewById()
		 * during getView()
		 * 
		 * @author Petar Šegina <psegina@kset.org>
		 * 
		 */
		private class ViewHolder {
			TextView title, content, day, dayName, month;
		}

		public FeedListAdapter(Context context, int textViewResourceId,
				Article[] objects) {
			/*
			 * Save the references to the Context and the Layout in order to use
			 * for layout inflation at a later time
			 */
			mContext = context;
			mLayout = textViewResourceId;

			/*
			 * Since the size of the content will be relatively compact, we can
			 * afford to cache all the values locally in order to save on
			 * processing time during the getView() method and make the
			 * scrolling process as smooth as possible
			 */
			size = objects.length;
			cTitle = new String[size];
			cContent = new String[size];
			cDay = new String[size];
			cDayName = new String[size];
			cMonth = new String[size];

			/*
			 * Generate and cache all the fields that will be displayed
			 */
			for (Article a : objects) {
				mCalendar.setTimeInMillis(a.getDate());
				cTitle[cPosition] = a.getTitle();
				cContent[cPosition] = a.getDescription();
				cDay[cPosition] = Integer.toString(mCalendar
						.get(Calendar.DAY_OF_MONTH));
				cDayName[cPosition] = DateFormat.format("E", mCalendar)
						.toString();
				cMonth[cPosition] = DateFormat.format("MMM", mCalendar)
						.toString();

				cPosition++;
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				/*
				 * If a recycled View is not available, inflate a new one and
				 * cache the references to the Views that contain data in a new
				 * ViewHolder object
				 */
				convertView = LayoutInflater.from(mContext).inflate(mLayout,
						parent, false);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(android.R.id.text1);
				holder.content = (TextView) convertView
						.findViewById(android.R.id.text2);
				holder.day = (TextView) convertView
						.findViewById(R.id.news_list_day_of_month);
				holder.dayName = (TextView) convertView
						.findViewById(R.id.news_list_day_name);
				holder.month = (TextView) convertView
						.findViewById(R.id.news_list_month);
				convertView.setTag(holder);
			} else {
				/*
				 * If the View is a recycled one, reuse it to avoid inflation
				 * and cache the references to the fields of interest
				 */
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(cTitle[position]);
			holder.content.setText(cContent[position]);
			holder.day.setText(cDay[position]);
			holder.dayName.setText(cDayName[position]);
			holder.month.setText(cMonth[position]);
			return convertView;
		}

		@Override
		public int getCount() {
			return cPosition;
		}

		@Override
		public Object getItem(int position) {
			return mFeed.getArticles()[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	/**
	 * An asynchronous task which will load the Feed in a background thread to
	 * avoid blocking the UI and push the results to the List once they are
	 * available
	 * 
	 * @author Petar Šegina <psegina@kset.org>
	 * 
	 */
	private class LoadFeedTask extends AsyncTask<Feed, Void, Feed> {

		@Override
		protected void onPreExecute() {
			// Display a progress circle
			setListShown(false);
		}

		@Override
		protected Feed doInBackground(Feed... arg0) {
			// Clear any present Articles to avoid duplication
			arg0[0].clearArticles();
			// Do the actual loading and pass the result to onPostExecute()
			return arg0[0].fetchArticles();
		}

		@Override
		protected void onPostExecute(Feed result) {
			if (result.getArticles().length > 0) {
				// Reverse the Articles so they are ordered by date ascending
				result.reverseArticleOrder();
				// Bind the latest data
				if (isVisible()) {
					setListAdapter(new FeedListAdapter(getActivity(),
							R.layout.news_list_item, mFeed.getArticles()));
					// Remove the progress circle
					setListShown(true);
				}
			} else {
				Toast.makeText(getActivity(), R.string.news_unavailable,
						Toast.LENGTH_LONG).show();
				setListShown(true);
			}
		}

	}

	/**
	 * An interface for objects interested in being notified when the relevant
	 * actions are performed in this Fragment
	 * 
	 * @author Petar Šegina <psegina@kset.org>
	 * 
	 */
	public interface NewsListListener {

		/**
		 * Called when an Article is selected from the list
		 * 
		 * @param which
		 *            The Fragment in which this action happened
		 * @param a
		 *            The article that was selected
		 */
		public void onArticleSelected(ListFragment which, Article a);

	}

	/**
	 * Registers a listener which will be notified of actions performend in this
	 * Fragment
	 * 
	 * @param l
	 *            The listener to register
	 */
	public void registerListener(NewsListListener l) {
		mListeners.add(l);
	}

	/**
	 * Removes a listener from the listener stack - it will not be notified of
	 * any new actions
	 * 
	 * @param l
	 *            The listener to remove
	 */
	public void unregisterListener(NewsListListener l) {
		mListeners.remove(l);
	}

	/*
	 * Called when an item in the list is clicked
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		/*
		 * Notify all the listeners that an Article has been selected in the
		 * list
		 */
		for (NewsListListener listener : mListeners) {
			listener.onArticleSelected(this, mFeed.getArticles()[position]);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mFeed.getArticles().length == 0) {
			new LoadFeedTask().execute(mFeed);
		} else {
			// Bind the latest data
			setListAdapter(new FeedListAdapter(getActivity(),
					R.layout.news_list_item, mFeed.getArticles()));
			// Remove the progress circle
			setListShown(true);
		}
	}

}
