package com.example.hobbigation;

/**
 * 취미 쇼핑 검색 결과를 담는 item 클래스
 */
public class ShopListviewItem {

    private String titlestr;  //제품명
    private String pricestr;  //제품 가격
    private String mallstr;  //상호명
    private String imgurl;  //썸네일 이미지

    public void setTitle(String title) {
        titlestr = title;
    }

    public void setPrice(String price) {
        pricestr = price;
    }

    public void setMall(String mall) {
        mallstr = mall;
    }

    public void setUrl(String url) {
        imgurl = url;
    }

    public String getTitle() {
        return this.titlestr;
    }

    public String getPrice() {
        return this.pricestr;
    }

    public String getMall() {
        return this.mallstr;
    }

    public String getImgurl() {
        return this.imgurl;
    }
}



