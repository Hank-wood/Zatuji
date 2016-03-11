package com.joe.huaban.spider;

public class URLUtil  
{  
	public static String getUrl(String url,int currentPage){
		if(currentPage==1){
			return url;
		}else{
			String add="&pager_offset="+currentPage;
			url+=add;
			return url;
		}
	}
}  
