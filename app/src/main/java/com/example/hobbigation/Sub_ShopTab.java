package com.example.hobbigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * 네이버 쇼핑 검색 결과를 xml로 받아 파싱하는 클래스
 * 파싱한 결과를 Listview에 담아 보여준다.
 */
public class Sub_ShopTab extends Fragment {

    String keyword = ""; //검색할 단어
    String[] shoparr;
    String strshop = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_sub_shop, container, false);

        keyword = PreferenceUtil.getInstance(getContext()).getStringExtra("keyword");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //검색결과를 스트링으로 받음
                    strshop = getNaverShoppingSearch(keyword);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView listview2;
                            ShopListviewAdapter adapter2;
                            int i = 0;

                            //리스트뷰 어답터 설정
                            adapter2 = new ShopListviewAdapter();
                            listview2 = (ListView) rootview.findViewById(R.id.shoplist);
                            listview2.setAdapter(adapter2);

                            //특수문자 변환
                            strshop = strshop.replace("&quot;", "\"");
                            strshop = strshop.replace("&gt;", ">");
                            strshop = strshop.replace("&lt;", "<");
                            strshop = strshop.replace("&amp;", "&");
                            strshop = strshop.replace("&nbsp;", " ");
                            int idx = strshop.indexOf("Result");
                            String str2 = strshop.substring(idx + 10);
                            //제목, 가격, 상호명, 링크, 이미지 스트링을 배열에 담기
                            shoparr = str2.split("%%%@");

                            for (i = 0; i < shoparr.length; i = i + 5) {
                                adapter2.addItem(shoparr[i], shoparr[i + 3], shoparr[i + 4], shoparr[i + 2]);
                            }

                            //포스트 링크로 이동하는 intent 생성
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

                                //아이템 클릭시 해당 포스트 링크로 이동
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
        });
        thread.start();
        return rootview;
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

                        if (tag.equals("item"));
                        else if (tag.equals("title")) {
                            //제목 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("image")) {
                            //이미지 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("link")) {
                            //링크 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("lprice")) {
                            //최저가 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("원%%%@");

                        } else if (tag.equals("mallName")) {
                            //상호명 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("description")) {

                            //내용 :
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
        //결과를 스트링으로 반환
        return sb.toString();
    }
}
