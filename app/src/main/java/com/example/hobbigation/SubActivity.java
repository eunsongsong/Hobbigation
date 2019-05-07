package com.example.hobbigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SubActivity extends AppCompatActivity {


    String keyword = "";
    String str = "";
    String[] strarr;

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
                    str = getNaverSearch(keyword);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView searchResult1 = (TextView) findViewById(R.id.searchResult1);
                            TextView searchResult2 = (TextView) findViewById(R.id.searchResult2);
                            TextView searchResult3 = (TextView) findViewById(R.id.searchResult3);
                            TextView searchResult4 = (TextView) findViewById(R.id.searchResult4);
                            TextView searchResult5 = (TextView) findViewById(R.id.searchResult5);
                            TextView searchResult6 = (TextView) findViewById(R.id.searchResult6);
                            TextView searchResult7 = (TextView) findViewById(R.id.searchResult7);
                            TextView searchResult8 = (TextView) findViewById(R.id.searchResult8);
                            TextView searchResult9 = (TextView) findViewById(R.id.searchResult9);
                            TextView searchResult10 = (TextView) findViewById(R.id.searchResult10);

                            str = str.replace("&quot;","\"");
                            str = str.replace("&gt;",">");
                            str = str.replace("&lt;","<");
                            str = str.replace("&amp;","&");
                            str = str.replace("&nbsp;"," ");
                            int idx = str.indexOf("Result");
                            String str2 = str.substring(idx+7);

                            strarr = str2.split("%");
                            searchResult1.setText(strarr[0]);
                            searchResult2.setText(strarr[1]);
                            searchResult3.setText(strarr[2]);
                            searchResult4.setText(strarr[3]);
                            searchResult5.setText(strarr[4]);
                            searchResult6.setText(strarr[5]);
                            searchResult7.setText(strarr[6]);
                            searchResult8.setText(strarr[7]);
                            searchResult9.setText(strarr[8]);
                            searchResult10.setText(strarr[9]);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });thread.start();
    }
    public String getNaverSearch(String keyword) {

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

                            sb.append("제목 : ");
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");

                        } else if (tag.equals("description")) {

                            sb.append("내용 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");

                        } else if (tag.equals("link")) {

                            sb.append("링크 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");

                        } else if (tag.equals("bloggername")) {

                            sb.append("블로거명 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");

                        } else if (tag.equals("postdate")) {

                            sb.append("작성날짜 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n%");
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
