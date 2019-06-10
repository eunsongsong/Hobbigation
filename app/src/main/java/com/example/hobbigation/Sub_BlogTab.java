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

/**
 * 네이버 블로그 검색 결과를 xml로 받아 파싱하는 클래스
 * 파싱한 결과를 리스트뷰에 담아 보여준다.
 */
public class Sub_BlogTab extends Fragment {

    String keyword = "";  //검색할 단어
    String strblog = "";
    String[] blogarr;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_sub_blog, container, false);

        keyword = PreferenceUtil.getInstance(getContext()).getStringExtra("keyword");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //검색결과를 스트링으로 받음
                    strblog = getNaverBlogSearch(keyword);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView listview1;
                            blogListviewAdapter adapter1;
                            int i = 0;

                            //리스트뷰 어답터 설정
                            adapter1 = new blogListviewAdapter();
                            listview1 = (ListView) rootview.findViewById(R.id.bloglist);
                            listview1.setAdapter(adapter1);

                            //특수문자 변환
                            strblog = strblog.replace("&quot;", "\"");
                            strblog = strblog.replace("&gt;", ">");
                            strblog = strblog.replace("&lt;", "<");
                            strblog = strblog.replace("&amp;", "&");
                            strblog = strblog.replace("&nbsp;", " ");
                            int idx = strblog.indexOf("Result");
                            String str1 = strblog.substring(idx + 10);
                            //제목, 내용, 링크, 블로거명, 작성날짜 스트링을 배열에 담기
                            blogarr = str1.split("%%%@");

                            for (i = 0; i < blogarr.length; i = i + 5) {
                                //tmp에 작성날짜 담기
                                String tmp = blogarr[i + 4];
                                //날짜형식으로 변경 (ex. 20190613 -> 2019.06.13)
                                tmp = tmp.substring(0, 4) + "." + tmp.substring(4, 6) + "." + tmp.substring(6, 8);
                                blogarr[i + 4] = tmp;
                                adapter1.addItem(ContextCompat.getDrawable(getContext(), R.drawable.blog)
                                        , blogarr[i], blogarr[i + 2], blogarr[i + 3], blogarr[i + 4]);
                            }

                            //포스트 링크로 이동하는 intent 생성
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

                                //아이템 클릭시 해당 포스트 링크로 이동
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

                        if (tag.equals("item")) ;
                        else if (tag.equals("title")) {
                            //제목 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("description")) {
                            //내용 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("link")) {
                            //링크 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("bloggername")) {
                            //블로거명 :
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("%%%@");

                        } else if (tag.equals("postdate")) {
                            //작성날짜 :
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

