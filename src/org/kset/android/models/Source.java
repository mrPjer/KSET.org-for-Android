package org.kset.android.models;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Build;
import android.text.Html;

/**
 * A class representing a source for the Feed categories and Feeds themselves.
 * 
 * @author Petar �egina <psegina@ymail.com>
 * 
 */
public class Source {

	private static final String TAG_CATEGORY = "category", TAG_FEED = "feed",
			ATTR_CAT_NAME = "name", ATTR_CAT_DESCRIPTION = "description",
			ATTR_FEED_NAME = "name", ATTR_FEED_DESCRIPTION = "description",
			ATTR_FEED_LOCATION = "location";

	private Context mContext;
	private Vector<Category> mCategories = new Vector<Category>();
	private NodeList categories, feeds;
	private int i, j, k, l;
	private Element category, feed;
	private Category c_category;

	@SuppressWarnings("unused")
	private Source() {
	};

	/**
	 * Creates a new Source from the supplied Context
	 * 
	 * @param context
	 */
	public Source(Context context) {
		mContext = context;

		categories = readSources().getElementsByTagName(TAG_CATEGORY);
		// Release the Context reference
		mContext = null;
		k = categories.getLength();
		for (i = 0; i < k; i++) {
			category = (Element) categories.item(i);
			feeds = category.getElementsByTagName(TAG_FEED);
			l = feeds.getLength();
			c_category = new Category(category.getAttribute(ATTR_CAT_NAME),
					category.getAttribute(ATTR_CAT_DESCRIPTION));
			for (j = 0; j < l; j++) {
				feed = (Element) feeds.item(j);
				c_category.addFeed(new Category.Feed(feed
						.getAttribute(ATTR_FEED_NAME), feed
						.getAttribute(ATTR_FEED_DESCRIPTION), feed
						.getAttribute(ATTR_FEED_LOCATION)));
			}
			mCategories.add(c_category);
		}

	}

	/**
	 * @return An array of categories associated with this Source
	 */
	public Category[] getCategories() {
		return mCategories.toArray(new Category[0]);
	}

	/**
	 * Reads the res/raw/sources.xml file and returns a handle to it's XML
	 * components
	 * 
	 * @return
	 */
	private Document readSources() {
		// try {
		// return DocumentBuilderFactory
		// .newInstance()
		// .newDocumentBuilder()
		// .parse(mContext.getResources().openRawResource(
		// R.raw.sources));
		// } catch (ParserConfigurationException e) {
		// e.printStackTrace();
		// return null;
		// } catch (NotFoundException e) {
		// e.printStackTrace();
		// return null;
		// } catch (SAXException e) {
		// e.printStackTrace();
		// return null;
		// } catch (IOException e) {
		// e.printStackTrace();
		// return null;
		// }
		return null;
	}

	/**
	 * An interface defining an object which can supply it's name and it's
	 * Description
	 * 
	 * @author Petar �egina <psegina@ymail.com>
	 * 
	 */
	public interface Describable {

		public String getName();

		public String getDescription();

	}

	/**
	 * A class representing a Category containing various RSS feeds sharing a
	 * common characteristic
	 * 
	 * @author Petar �egina <psegina@ymail.com>
	 * 
	 */
	public static class Category implements Describable {
		private Vector<Feed> mFeeds = new Vector<Feed>();
		private String mName = "";
		private String mDescription = "";
		int i, j, k, l;

		@SuppressWarnings("unused")
		private Category() {
		};

		/**
		 * Create a new Category
		 * 
		 * @param name
		 *            The name of the Category
		 * @param description
		 *            A brief description
		 */
		public Category(String name, String description) {
			mName = new String(name);
			mDescription = new String(description);
		}

		/**
		 * Add a Feed to this Category
		 * 
		 * @param feed
		 *            The Feed object which will be added
		 */
		public void addFeed(Feed feed) {
			mFeeds.add(feed);
		}

		/**
		 * Removes the supplied Feed if it exists in this category
		 * 
		 * @param feed
		 *            The Feed to remove
		 */
		public void removeFeed(Feed feed) {
			while (mFeeds.remove(feed))
				;
		}

		/**
		 * @return An Array of Feed objects associated with this Category
		 */
		public Feed[] getFeeds() {
			return mFeeds.toArray(new Feed[0]);
		}

		@Override
		public String getName() {
			return new String(mName);
		}

		@Override
		public String getDescription() {
			return new String(mDescription);
		}

		/**
		 * A class representing an RSS feed
		 * 
		 * @author Petar �egina <psegina@ymail.com>
		 * 
		 */
		public static class Feed implements Describable {
			private static final String TAG_ITEM = "item", TAG_IMG = "image",
					TAG_LINK = "link", TAG_TITLE = "title",
					TAG_CONTENT = "description", TAG_DATE = "pubDate",
					DELIMITER = "\\|\\|";

			private Vector<Article> mArticles = new Vector<Article>();
			private String mName;
			private String mDescription;
			private Vector<URL> mLink = new Vector<URL>();
			private Document d;
			private NodeList items, itemChildren, m;
			private String rk;
			private Node node;
			private int itemsLength, children, i, k;
			private String t_val;
			private Article t_article;

			@SuppressWarnings("unused")
			private Feed() {
			};

			public Feed(String name, String description, String location) {
				mName = new String(name);
				mDescription = new String(description);
				try {
					for (String s : location.split(DELIMITER))
						mLink.add(new URL(s.toString().replace("&amp;", "&")));
				} catch (MalformedURLException e) {
					e.printStackTrace();
					throw new IllegalStateException(
							"Provided URL is not valid!");
				}
			}

			/**
			 * Removes any Articles associated with this Feed
			 */
			public void clearArticles() {
				this.mArticles.clear();
			}

			public void addArticle(Article article) {
				mArticles.add(article);
			}

			public void removeArticle(Article article) {
				while (mArticles.remove(article))
					;
			}

			public Article[] getArticles() {
				return mArticles.toArray(new Article[0]);
			}

			/**
			 * Reverses the order of the associated Articles
			 */
			public void reverseArticleOrder() {
				Collections.reverse(mArticles);
			}

			/**
			 * Fetches the articles from this Feed and stores them locally so
			 * that they can be accessed with Article[] getArticles()
			 * 
			 * Note that this is a potentially blocking operation and as such
			 * should be separated from the UI thread.
			 * 
			 * @return Returns self for chaining purposes
			 */
			public Feed fetchArticles() {
				for (URL u : mLink)
					try {
						d = DocumentBuilderFactory.newInstance()
								.newDocumentBuilder().parse(u.openStream());
						items = d.getElementsByTagName(TAG_ITEM);
						itemsLength = items.getLength();
						for (i = 0; i < itemsLength; i++) {
							itemChildren = items.item(i).getChildNodes();
							children = itemChildren.getLength();
							t_article = new Article();
							for (k = 0; k < children; k++) {
								node = itemChildren.item(k);
								if (node == null
										|| node.getFirstChild() == null)
									continue;
								t_val = node.getNodeName();
								if (t_val.equalsIgnoreCase(TAG_TITLE))
									t_article.setTitle(node.getFirstChild()
											.getNodeValue());
								else if (t_val.equalsIgnoreCase(TAG_CONTENT)) {
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
										t_article.setContent(node
												.getTextContent());
									} else {
										m = node.getChildNodes();
										rk = "";
										for (int i = 0; i < m.getLength(); i++)
											rk += m.item(i).getNodeValue();
										t_article.setContent(rk);
									}
								} else if (t_val.equalsIgnoreCase(TAG_LINK)) {
									m = node.getChildNodes();
									rk = "";
									for (int i = 0; i < m.getLength(); i++)
										rk += m.item(i).getNodeValue();
									t_article.setLocation(new URL(rk));
								} else if (t_val.equalsIgnoreCase(TAG_IMG)) {
									t_article.setLocation(new URL(node
											.getFirstChild().getNodeValue()));
								} else if (t_val.equals(TAG_DATE)) {
									t_article.setDate(Date.parse(node
											.getFirstChild().getNodeValue()));
								}
							}
							mArticles.add(t_article);
						}
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				sortArticlesByDateDesc();
				return this;
			}

			public void sortArticlesByDateDesc() {
				// Consider implementing a faster sorting algorithm
				int i, j, size = mArticles.size();
				for (i = 0; i < size - 1; i++) {
					for (j = i + 1; j < size; j++) {
						if (mArticles.elementAt(i).mDate < mArticles
								.elementAt(j).mDate) {
							Collections.swap(mArticles, i, j);
						}
					}
				}
			}

			@Override
			public String getName() {
				return mName;
				// return new String(mName);
			}

			@Override
			public String getDescription() {
				return mDescription;
				// return new String(mDescription);
			}

			public URL getLink() {
				try {
					return new URL(mLink.toString());
				} catch (MalformedURLException e) {
					e.printStackTrace();
					return null;
				}
			}

			/**
			 * A class representing an RSS article
			 * 
			 * @author Petar �egina <psegina@ymail.com>
			 * 
			 */
			public static class Article implements Describable, Serializable {
				private static final long serialVersionUID = -9219480082482195154L;
				private String mTitle;
				private String mContent;
				private String mContentPlain;
				private URL mImageLocation;
				private URL mLocation;
				private long mDate = -1;

				private Article() {
				};

				public Article(String title, String content) {
					this(title, content, null, null);
				}

				public Article(String title, String content, URL location) {
					this(title, content, location, null);
				}

				public Article(String title, String content, URL location,
						URL imageLocation) {
					mTitle = new String(title);
					mContent = new String(content);
					mContentPlain = Html.fromHtml(content).toString()
							.replace("\n", "").replace("￼", "");
					try {
						mLocation = new URL(location.toString());
						mImageLocation = new URL(imageLocation.toString());
					} catch (NullPointerException n) {
						mLocation = null;
						mImageLocation = null;
					} catch (MalformedURLException e) {
						e.printStackTrace();
						new IllegalStateException(
								"Passed in URL is not valid\n" + mLocation);
					}
				}

				public void setDate(long date) {
					mDate = new Long(date);
				}

				public long getDate() {
					return new Long(mDate);
				}

				public void setTitle(String title) {
					mTitle = new String(title);
				}

				public String getName() {
					return getTitle();
				}

				public String getTitle() {
					return mTitle == null ? "" : new String(mTitle);
				}

				public void setContent(String content) {
					mContent = new String(content);
					mContentPlain = Html.fromHtml(content).toString()
							.replace("\n", "").replace("￼", "");
				}

				public String getDescription() {
					return new String(mContentPlain);
				}

				public String getContent() {
					return mContent == null ? "" : new String(mContent);
				}

				public void setLocation(URL location)
						throws MalformedURLException {
					mLocation = new URL(location.toString());
				}

				public URL getLocation() {
					try {
						return new URL(mLocation.toString());
					} catch (MalformedURLException e) {
						return null;
					}
				}

				public void setImageLocation(URL imageLocation)
						throws MalformedURLException {
					mImageLocation = new URL(imageLocation.toString());
				}

				public URL getImageLocation() {
					try {
						return new URL(mImageLocation.toString());
					} catch (Exception e) {
						return null;
					}
				}

			}

		}

	}

}