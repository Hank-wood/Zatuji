package com.joe.huaban.beauty.model;

/**
 * Created by Joe on 2016/4/9.
 */
import java.io.Serializable;
import java.net.URL;
import java.net.HttpURLConnection;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.io.DataOutputStream;


public class Session implements Serializable {
    private Map<String, String> cookies = null;

    public Session() {
        this.cookies = new HashMap<String, String>();
    }

    public HttpURLConnection get(String url, Map<String, String> headers) throws Exception {
        URL getUrl = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection)getUrl.openConnection();

        for (Object key : headers.keySet()) {
            httpConn.setRequestProperty(key.toString(), headers.get(key).toString());
        }

        if (!cookies.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Object cookie : cookies.keySet()) {
                sb.append(cookie.toString()).append("=").append(cookies.get(cookie).toString())
                        .append(";");
            }
            httpConn.addRequestProperty("Cookie", sb.toString());
        }

        httpConn.connect();


        return httpConn;
    }


    public HttpURLConnection post(String url, Map<String, String> data, Map<String, String> headers) throws Exception {
        URL getUrl = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection)getUrl.openConnection();

        httpConn.setUseCaches(false);
        httpConn.setRequestMethod("POST");

        for (Object key : headers.keySet()) {
            httpConn.setRequestProperty(key.toString(), headers.get(key).toString());
        }

        if (!cookies.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Object cookie : cookies.keySet()) {
                sb.append(cookie.toString()).append("=").append(cookies.get(cookie).toString())
                        .append(";");
            }
            httpConn.addRequestProperty("Cookie", sb.toString());
        }

        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);

        DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream());
        StringBuilder sbData = new StringBuilder();
        int count = 0;
//        for (Object str : data.keySet()) {
//            count += 1;
//            if (count != data.size()) {
//                sbData.append(str.toString()).append("=").append(data.get(str).toString()).append("&");
//            } else {
//                sbData.append(str.toString()).append("=").append(data.get(str).toString());
//            }
//        }
        wr.writeBytes(
                "__EVENTTARGET=winLogin%24sfLogin%24ContentPanel1%24btnLogin&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwULL"+
                        "TE4ODU0MTIxMDdkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYJBQh3aW5Mb2dpbgUQd2luTG9naW4kc2ZMb2dpb"+
                        "gUWd2luTG9naW4kc2ZMb2dpbiRjdGwwMAUfd2luTG9naW4kc2ZMb2dpbiR0eHRVc2VyTG9naW5JRAUcd2luTG9naW4kc2ZMb2dpb"+
                        "iR0eHRQYXNzd29yZAUed2luTG9naW4kc2ZMb2dpbiRjYnhTYXZlTXlJbmZvBR53aW5Mb2dpbiRzZkxvZ2luJENvbnRlbnRQYW5lbDEFJ3dpbkxvZ2luJHNmTG"+
                        "9naW4kQ29udGVudFBhbmVsMSRidG5Mb2dpbgUIV25kTW9kYWy"+
                        "%2Bbc6QsNOdH7ZtF07lnmmRBXbUK%2F7AWPgOr2Q5ybbyRQ%3D%3D&X_CHANGED=true&winLogin%24sfLogin%24txtUserLoginID"+
                        "=这里是用户名&winLogin%24sfLogin%24txtPassword= 这里是密码&winLogin_Hidden=false&WndModal_Hidden=true&X_TARGET"+
                        "=winLogin_sfLogin_ContentPanel1_btnLogin&winLogin_sfLogin_ctl00_Collapsed=false&winLogin_sfLogin_ContentPanel1_Collapsed"+
                        "=false&winLogin_sfLogin_Collapsed=false&winLogin_Collapsed=false&WndModal_Collapsed=false&X_STATE=e30="+
                        "&X_AJAX=true");
        wr.flush();
        wr.close();


        return httpConn;
    }

}
