package com.example.hobbigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Sub_CafeTab extends Fragment {

    String keyword = "";
    String strcafe = "";
    String[] cafearr;
    public RecyclerView cafelist_recyclerview;
    public Button more_link;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_sub_cafe, container, false);

        keyword = PreferenceUtil.getInstance(getContext()).getStringExtra("keyword");

        cafelist_recyclerview = (RecyclerView) rootview.findViewById(R.id.cafelist);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        cafelist_recyclerview.setHasFixedSize(true);
        cafelist_recyclerview.setLayoutManager(layoutManager);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    strcafe = getNaverCafeSearch(keyword);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            strcafe = strcafe.replace("&quot;", "\"");
                            strcafe = strcafe.replace("&gt;", ">");
                            strcafe = strcafe.replace("&lt;", "<");
                            strcafe = strcafe.replace("&amp;", "&");
                            strcafe = strcafe.replace("&nbsp;", " ");
                            int idx = strcafe.indexOf("Result");
                            String str3 = strcafe.substring(idx + 10);
                            cafearr = str3.split("%%%@");

                            List<CafeItemInfo> cafe_item =new ArrayList<>();
                            CafeItemInfo[] item = new CafeItemInfo[cafearr.length];

                            int a=0;
                            for (int i = 0; i < cafearr.length; i = i + 5) {
                                item[a] = new CafeItemInfo(cafearr[i],cafearr[i+2],cafearr[i+1],cafearr[i+3],cafearr[i+4]);
                                cafe_item.add(item[a]);
                                a++;
                            }
                            CafeItemAdapter cafeItemAdapter = new CafeItemAdapter(getContext(),cafe_item,R.layout.fragment_sub_cafe);
                            cafelist_recyclerview.setAdapter(cafeItemAdapter);
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
                            sb.append("%%%@");

                        } else if (tag.equals("description")) {

                            //sb.append("내용 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("link")) {

                            //sb.append("링크 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("cafename")) {

                            //sb.append("카페명 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("cafeurl")) {

                            //sb.append("카페링크 : ");
                            xpp.next();

                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");
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
