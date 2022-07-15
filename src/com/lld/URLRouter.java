package com.lld;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class URLRouter {
    private ConcurrentMap<HTTPRequest, Integer> reqMap;
    // private int maxSize;
    URLRouter(){
        //this.maxSize=maxSize;
        this.reqMap=new ConcurrentHashMap<HTTPRequest,Integer>();
    }

    public void consumeReq(HTTPRequest req) throws IOException, InterruptedException {
        synchronized(reqMap)
        {
            Integer responseCode=req.sendRequest();
            reqMap.put(req,responseCode);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        URLRouter urlRouter=new URLRouter();
        HTTPRequest req1=new HTTPGetRequest("google.com",RequestType.GET,"getParam");
        urlRouter.consumeReq(req1);
        HTTPRequest req2 = new HTTPPostRequest("google1.com",RequestType.POST,"postBody");
        urlRouter.consumeReq(req2);
    }
}

class HTTPRequest{
    private String url;
    private RequestType requestType;
    static final String USER_AGENT = "Mozilla/5.0";
    HTTPRequest(String url, RequestType reqType)
    {
        this.url=url;
        this.requestType=reqType;
    }

    public String getURL(){ return this.url;}
    public RequestType getRequestType(){return this.requestType;}

    public Integer sendRequest() throws IOException, InterruptedException {
        System.out.println("Sending Request");
        return 0;
    }
}

class HTTPGetRequest extends HTTPRequest{
    private String param;
    HTTPGetRequest(String url, RequestType reqType,String param)
    {
        super(url,reqType);
        this.param=param;
    }

    public Integer sendRequest() throws IOException, InterruptedException {
        System.out.println("Sending GET Request to url "+super.getURL());

        /*URL obj = new URL(super.getURL());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
        return responseCode;*/
        Thread.sleep(2000);
        return 200;
    }
}

class HTTPPostRequest extends HTTPRequest{
    private String postBody;
    HTTPPostRequest(String url, RequestType reqType, String postBody)
    {
        super(url,reqType);
        this.postBody=postBody;
    }
    public Integer sendRequest()
    {
        System.out.println("Sending POST Request to url: "+super.getURL());
        return 200;
    }
}

enum RequestType{
    GET,
    POST
}