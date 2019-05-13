package com.example.hobbigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SubActivity extends AppCompatActivity {


    String keyword = "";
    String strblog = "";
    String strshop = "";
    String[] blogarr;
    String[] shoparr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = getIntent();
                    keyword = intent.getStringExtra("keyword");
                    strblog = getNaverBlogSearch(keyword);
                    strshop = getNaverShoppingSearch(keyword);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView listview1, listview2;
                            blogListviewAdapter adapter;
                            ShopListviewAdapter adapter2;
                            int i = 0;

                            adapter = new blogListviewAdapter();
                            adapter2 = new ShopListviewAdapter();
                            listview1 = (ListView) findViewById(R.id.bloglist);
                            listview2 = (ListView) findViewById(R.id.shoplist);
                            listview1.setAdapter(adapter);
                            listview2.setAdapter(adapter2);

                            strblog = strblog.replace("&quot;","\"");
                            strblog = strblog.replace("&gt;",">");
                            strblog = strblog.replace("&lt;","<");
                            strblog = strblog.replace("&amp;","&");
                            strblog = strblog.replace("&nbsp;"," ");
                            int idx = strblog.indexOf("Result");
                            String str1 = strblog.substring(idx+7);
                            blogarr = str1.split("%");


                            strshop = strshop.replace("&quot;","\"");
                            strshop = strshop.replace("&gt;",">");
                            strshop = strshop.replace("&lt;","<");
                            strshop = strshop.replace("&amp;","&");
                            strshop = strshop.replace("&nbsp;"," ");
                            idx = strshop.indexOf("Result");
                            String str2 = strshop.substring(idx+7);
                            shoparr = str2.split("%");

                            for(i=0; i < blogarr.length; i=i+5) {
                                String tmp = blogarr[i+4];
                                tmp = tmp.substring(0,4)+"."+tmp.substring(4,6)+"."+tmp.substring(6,8);
                                blogarr[i+4] = tmp;
                                adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blog)
                                        , blogarr[i], blogarr[i + 2], blogarr[i + 3], blogarr[i + 4]);
                            }


                            for(i=0; i < shoparr.length; i=i+5) {
                                adapter2.addItem(shoparr[i], shoparr[i + 3], shoparr[i + 4], shoparr[i + 2]);
                            }

                            listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                final Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[1]));
                                final Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[6]));
                                final Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[11]));
                                final Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[16]));
                                final Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[21]));
                                final Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[26]));
                                final Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[31]));
                                final Intent intent8 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[36]));
                                final Intent intent9 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[41]));
                                final Intent intent10 = new Intent(Intent.ACTION_VIEW, Uri.parse(blogarr[46]));

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0)
                                        startActivity(intent1);
                                    else if (position == 1)
                                        startActivity(intent2);
                                    else if (position == 2)
                                        startActivity(intent3);
                                    else if (position == 3)
                                        startActivity(intent4);
                                    else if (position == 4)
                                        startActivity(intent5);
                                    else if (position == 5)
                                        startActivity(intent6);
                                    else if (position == 6)
                                        startActivity(intent7);
                                    else if (position == 7)
                                        startActivity(intent8);
                                    else if (position == 8)
                                        startActivity(intent9);
                                    else if (position == 9)
                                        startActivity(intent10);
                                }
                            });

                            listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                final Intent intent_1 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[1]));
                                final Intent intent_2 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[6]));
                                final Intent intent_3 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[11]));
                                final Intent intent_4 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[16]));
                                final Intent intent_5 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[21]));
                                final Intent intent_6 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[26]));
                                final Intent intent_7 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[31]));
                                final Intent intent_8 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[36]));
                                final Intent intent_9 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[41]));
                                final Intent intent_10 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoparr[46]));

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0)
                                        startActivity(intent_1);
                                    else if (position == 1)
                                        startActivity(intent_2);
                                    else if (position == 2)
                                        startActivity(intent_3);
                                    else if (position == 3)
                                        startActivity(intent_4);
                                    else if (position == 4)
                                        startActivity(intent_5);
                                    else if (position == 5)
                                        startActivity(intent_6);
                                    else if (position == 6)
                                        startActivity(intent_7);
                                    else if (position == 7)
                                        startActivity(intent_8);
                                    else if (position == 8)
                                        startActivity(intent_9);
                                    else if (position == 9)
                                        startActivity(intent_10);
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });thread.start();
    }
    public String getNaverBlogSearch(String keyword) {

        String clientID = "93_ML5hY9t_Ufmedkbz9";
        String clientSecret = "Dfp9hCV9Tn";
        StringBuffer sb = new StringBuffer();

        try {

            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query=" + text + "&display=10" + "&start=1";

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            String tag;
            //inputStream으로부터 xml값 받기
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("item")) ; //첫번째 검색 결과
                        else if (tag.equals("title")) {

                            //sb.append("제목 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%");

                        } else if (tag.equals("description")) {

                            //sb.append("내용 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%");

                        } else if (tag.equals("link")) {

                            //sb.append("링크 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%");

                        } else if (tag.equals("bloggername")) {

                            //sb.append("블로거명 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%");

                        } else if (tag.equals("postdate")) {

                            //sb.append("작성날짜 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%");
                        }

                        break;
                }

                eventType = xpp.next();

            }

        } catch (Exception e) {
            return e.toString();
        }

        return sb.toString();
    }



    public String getNaverShoppingSearch(String keyword) {

        String clientID = "93_ML5hY9t_Ufmedkbz9";
        String clientSecret = "Dfp9hCV9Tn";
        StringBuffer sb = new StringBuffer();

        try {

            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/shop.xml?query=" + text + "&display=10" + "&start=1";

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            String tag;
            //inputStream으로부터 xml값 받기
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("item")) ; //첫번째 검색 결과
                        else if (tag.equals("title")) {

                            //sb.append("제목 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n%");

                        } else if (tag.equals("image")) {

                            //sb.append("이미지 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%");

                        } else if (tag.equals("link")) {

                            //sb.append("링크 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n%");

                        } else if (tag.equals("lprice")) {

                            //sb.append("최저가 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("원\n%");

                        } else if (tag.equals("mallName")) {

                            //sb.append("상호명 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n%");
                        } else if (tag.equals("description")) {

                            //sb.append("내용 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%");

                        }

                        break;
                }

                eventType = xpp.next();

            }

        } catch (Exception e) {
            return e.toString();
        }

        return sb.toString();
    }
}