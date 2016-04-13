package com.joe.huaban.spider;


import com.joe.huaban.global.utils.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Spider {
	public List<Picture> getPictureFromDouBan(String url,int currentPage){
		List<Picture> listOfPic=new ArrayList<Picture>();
		try {	
				url= URLUtil.getUrl(url, currentPage);
				String htmlStr = DataUtil.doGet(url);
				Document doc = Jsoup.parse(htmlStr);
		       /* Element eleMain=doc.getElementById("main")*/;
		        //1��divs
		        Elements divs=doc.getElementsByClass("panel");
		        //1��div
		        Element divFirst=divs.get(2);
		      //2��divs
		        Elements divsS=divFirst.getElementsByClass("panel-body");
		        //2��div
		        Element divSecond=divsS.get(0);
		        //�б�
		        Elements uls=divSecond.getElementsByTag("ul");
		        Elements list=uls.get(0).getElementsByTag("li");
		        for (Element element : list) {
		        	Picture pic=new Picture();
					Elements as=element.getElementsByClass("link");
					Element link=as.get(0);
					Element desc=as.get(1);
					Elements imgs=link.getElementsByTag("img");
					Element img=imgs.get(0);
					String links=link.attr("href");
					String image=img.attr("src");
					String descs=desc.text();
					pic.setLink(links);
					pic.setDesc(descs);
					pic.setSrc(image);
					listOfPic.add(pic);
				}
		        return listOfPic;
		} catch (Exception e) {
			e.printStackTrace();
			return listOfPic;
		} 
	}
	
	public List<Picture> getPictureFromHuaBan(String url,int currentPage){
		List<Picture> listOfPic=new ArrayList<Picture>();
		try {	
			   
				url=URLUtil.getUrl(url, currentPage);
				String htmlStr = DataUtil.doGet(url);
				Document doc = Jsoup.parse(htmlStr);
		        //1��divs
		        Element waterfall=doc.getElementById("waterfall");
				LogUtils.e("waterfall为空"+(waterfall==null));
		        //1��div
		        Elements items=waterfall.getElementsByClass("pin");
		        for (Element element : items) {
		        	Picture pic=new Picture();
		        	Element A0=null;
		        	if(element.getElementsByAttributeValueContaining("href", "pins").size()>0){
		        		A0=element.getElementsByAttributeValueContaining("href", "pins").get(0);
		        	}
		        	Elements P=element.getElementsByClass("description");
		        	Element P0=null;
		        	
		        	if(P.size()>0){
		        		P0=P.get(0);
		        		pic.setDesc(P0.text());
		        	}
		        	if(A0!=null&&A0.getElementsByTag("img").size()>0){
		        		Element img=A0.getElementsByTag("img").get(0);
			        	pic.setSrc(img.attr("src"));
		        	}
					listOfPic.add(pic);
				}
		        return listOfPic;
		} catch (Exception e) {
			e.printStackTrace();
			return listOfPic;
		} 
	}
	public String getHtml(String url){
		try {
			return DataUtil.doGet(url);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}
}
