package android.shts.jp.nogifeed.utils;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.shts.jp.nogifeed.models.Entries;
import android.shts.jp.nogifeed.models.Entry;
import android.util.Log;
import android.util.Xml;

public class AtomRssParser {

	private static final String TAG = "RssParser";

	private static final String TAG_ENTRY = "entry";
	private static final String TAG_TITLE = "title";
	private static final String TAG_LINK = "link";
	private static final String TAG_ID = "id";
	private static final String TAG_PUBLISHED = "published";
	private static final String TAG_UPDATED = "updated";
	private static final String TAG_SUMMARY = "summary";
	private static final String TAG_NAME = "name";
	private static final String TAG_CONTENT = "content";

	private static final String CHARSET_NAME = "UTF-8";

	public static Entries parse(InputStream data) {
		Entries entries = new Entries();
		Entry entry = null;
		XmlPullParser parser = Xml.newPullParser();

		try {
			parser.setInput(data, CHARSET_NAME);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					LogUtils.log(TAG, "parse start");
					break;

				case XmlPullParser.START_TAG:
					tag = parser.getName();
                    LogUtils.log(TAG, "TAG: " + tag);

					if (tag.equals(TAG_ENTRY)) {
						entry = new Entry();
                        LogUtils.log(TAG, "entry");

					} else if (tag.equals(TAG_TITLE)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.title = text;
                            LogUtils.log(TAG, "title " + text);
						}

					} else if (tag.equals(TAG_LINK)) {
						if (entry != null) {
							int attrCount = parser.getAttributeCount();
							for (int i = 0; i < attrCount; i++) {
								String text = parser.getAttributeValue(i);
                                entry.link = text;
                                LogUtils.log(TAG, "link " + text);
							}
						}

					} else if (tag.equals(TAG_ID)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.id = text;
                            LogUtils.log(TAG, "id " + text);
						}

					} else if (tag.equals(TAG_PUBLISHED)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.published = text;
                            LogUtils.log(TAG, "published " + text);
						}

					} else if (tag.equals(TAG_UPDATED)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.updated = text;
                            LogUtils.log(TAG, "updated " + text);
						}

					} else if (tag.equals(TAG_SUMMARY)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.summary = text;
                            LogUtils.log(TAG, "summary " + text);
						}

					} else if (tag.equals(TAG_NAME)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.name = text;
                            LogUtils.log(TAG, "name " + text);
						}

					} else if (tag.equals(TAG_CONTENT)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.content = ignoreCdataTag(text); // without CDATA tag
                            LogUtils.log(TAG, "content " + text);
						}

					} else {
                        LogUtils.log(TAG, "cannot find tag: " + tag);
					}
					break;

				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals(TAG_ENTRY)) {
						entries.add(entry);
						entry = null;
					}
                    LogUtils.log(TAG, "END_TAG : " + tag);
					break;
				}
				eventType = parser.next();
			}
            LogUtils.log(TAG, "parse finish");

		} catch (XmlPullParserException e) {
			Log.e(TAG, "parse error ! : " + e);
			return null;
		} catch (IOException e) {
			Log.e(TAG, "parse error ! : " + e);
			return null;
		}
		return entries;
	}

    private static String ignoreCdataTag(String target) {
        // delete cdata tag
        String ignoreCdataStartTag = target.replace("<![CDATA[", "");
        String ignoreCdataEndTag = ignoreCdataStartTag.replace("]]>", "");
        String ignoreCrLf = ignoreCdataEndTag.replace("\n", "");
        return ignoreCrLf;
    }

}
