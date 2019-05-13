package com.example.hobbigation;

public class ShopListviewItem {

    private String titlestr;
    private String pricestr;
    private String mallstr;
    private String imgurl;

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



