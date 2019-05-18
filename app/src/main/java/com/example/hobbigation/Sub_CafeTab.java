package com.example.hobbigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Sub_CafeTab extends Fragment {

    String keyword = "";
    String strcafe = "";
    String[] cafearr;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_sub_cafe, container, false);

        keyword = PreferenceUtil.getInstance(getContext()).getStringExtra("keyword");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    strcafe = getNaverCafeSearch(keyword);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView listview3;
                            blogListviewAdapter adapter3;
                            int i = 0;

                            adapter3 = new blogListviewAdapter();
                            listview3 = (ListView) rootview.findViewById(R.id.cafelist);
                            listview3.setAdapter(adapter3);

                            strcafe = strcafe.replace("&quot;", "\"");
                            strcafe = strcafe.replace("&gt;", ">");
                            strcafe = strcafe.replace("&lt;", "<");
                            strcafe = strcafe.replace("&amp;", "&");
                            strcafe = strcafe.replace("&nbsp;", " ");
                            int idx = strcafe.indexOf("Result");
                            String str3 = strcafe.substring(idx + 7);
                            cafearr = str3.split("%");

                            for (i = 0; i < cafearr.length; i = i + 4) {
                                adapter3.addItem(ContextCompat.getDrawable(getContext(), R.drawable.cafe)
                                        , cafearr[i], cafearr[i + 2], cafearr[i + 3],"");
                            }

                            listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                final Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[1]));
                                final Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[5]));
                                final Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[9]));
                                final Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[13]));
                                final Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[17]));
                                final Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[21]));
                                final Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[25]));
                                final Intent intent8 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[29]));
                                final Intent intent9 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[33]));
                                final Intent intent10 = new Intent(Intent.ACTION_VIEW, Uri.parse(cafearr[37]));

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
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        return rootview;
    }

    public String getNaverCafeSearch(String keyword) {

        String clientID = "93_ML5hY9t_Ufmedkbz9";
        String clientSecret = "Dfp9hCV9Tn";
        StringBuffer sb = new StringBuffer();

        try {

            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/cafearticle.xml?query=" + text + "&display=10" + "&start=1";

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

                        } else if (tag.equals("cafename")) {

                            //sb.append("카페명 : ");
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
