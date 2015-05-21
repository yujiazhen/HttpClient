package FromWeiboToTieba;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.message.BasicHeaderElementIterator;

import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;

public class httpClientDownload {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String DownloadFromWeb(String webUrl,String cookie) throws Exception {
		// TODO Auto-generated method stub
	//	CloseableHttpClient httpclient = HttpClients.createDefault();
	/*	BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(cm)
				.build();*/
		ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy(){
			@Override
			public long getKeepAliveDuration(HttpResponse response,HttpContext context){
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (keepAlive == -1) {
					// Keep connections alive 5 seconds if a keep-alive value
					// has not be explicitly set by the server
					keepAlive = 5000;
					}
				return keepAlive;
			}
		};
		CloseableHttpClient httpclient = HttpClients.custom()
		.setKeepAliveStrategy(keepAliveStrat)
		.build();
			
		String result="";
		try{
			String weiboLoginUrl = webUrl;  
	        HttpGet getReq = new HttpGet(weiboLoginUrl);
	        
	  //      getReq.setHeaders(getDefaultHeaders());
	        getReq.setHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	        getReq.setHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
	        getReq.setHeader(HttpHeaders.CONNECTION,"keep-alive");
	        
	        getReq.setHeader("Cookie", cookie);
	        CloseableHttpResponse response = httpclient.execute(getReq);
	        try{
	        	HttpEntity respEnt = response.getEntity();
	        	
	        	InputStream instream = respEnt.getContent();
	            BufferedReader br=new BufferedReader(new InputStreamReader(instream,"UTF-8"));
	            
	            String line;
	            while ((line = br.readLine()) != null) 
	    		{   
	            	result+=line+"\n";
	    		}
	            
	        }finally{
	        	response.close();
	        }
		}finally{
			httpclient.close();
		}
		return result;
	}
	
	public static String DownloadFromWebPost(String webUrl,String cookie,Map<String,String> params) throws Exception {
		// TODO Auto-generated method stub
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
/*		ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy(){
			@Override
			public long getKeepAliveDuration(HttpResponse response,HttpContext context){
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (keepAlive == -1) {
					// Keep connections alive 5 seconds if a keep-alive value
					// has not be explicitly set by the server
					keepAlive = 5000;
					}
				return keepAlive;
			}
		};
		CloseableHttpClient httpclient = HttpClients.custom()
		.setKeepAliveStrategy(keepAliveStrat)
		.build();*/
/*		BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(cm)
				.build();*/
		
		String result="";
		try{
			String baiduMainLoginUrl = webUrl;  
	        HttpPost postReq = new HttpPost(baiduMainLoginUrl);
	        
	        List<NameValuePair> list = new ArrayList<NameValuePair>();
	        for (String temp : params.keySet()) {
	            list.add(new BasicNameValuePair(temp, params.get(temp)));
	        }
	        postReq.setEntity(new UrlEncodedFormEntity(list));
	        
	//        postReq.setHeaders(getDefaultHeaders());
	        postReq.setHeader("Host", "tieba.baidu.com");
	        postReq.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
	        postReq.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	        postReq.setHeader("Accept-Language", "en-US,en;q=0.5");
	        postReq.setHeader("Connection", "keep-alive");
	        postReq.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	        
	        postReq.setHeader("Cookie", cookie);
	        
	        
	        CloseableHttpResponse response = httpclient.execute(postReq);
	        
	        try{
	        	HttpEntity respEnt = response.getEntity();
	        	
	        	InputStream instream = respEnt.getContent();
	            BufferedReader br=new BufferedReader(new InputStreamReader(instream,"UTF-8"));
	            
	            String line;
	            while ((line = br.readLine()) != null) 
	    		{   
	            	result+=line+"\n";
	    		}
	            
	        }finally{
	        	response.close();
	        }
		}finally{
			httpclient.close();
		}
		return result;
	}
	
	public static String getTbs() throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result="";
		
		try{
			/*URI uri = new URIBuilder()
			.setScheme("http")
			.setHost("tieba.baidu.com")
			.setPath("/f")
			.setParameter("kw", "%E9%94%99%E8%AF%AF")
			.setParameter("fr", "wwwt")
			.build();*/
			String uri = "http://tieba.baidu.com/dc/common/tbs";
			HttpGet httpget = new HttpGet(uri);
			
			httpget.setHeaders(getDefaultHeaders());
	        CloseableHttpResponse response = httpclient.execute(httpget);
	        
	        try{
	        	HttpEntity respEnt = response.getEntity();
	        	
	        	InputStream instream = respEnt.getContent();
	            BufferedReader br=new BufferedReader(new InputStreamReader(instream,"UTF-8"));
	            
	            String line;
	            while ((line = br.readLine()) != null) 
	    		{   
	            	result+=line+"\n";
	    		}
	            
	        }finally{
	        	response.close();
	        }
		}finally{
			httpclient.close();
		}
		Pattern contentP = Pattern.compile("\"tbs\":\"([\\s\\S]*?)\"");
		Matcher contentMatcher = contentP.matcher(result);
		//boolean foundTokenValue = tokenValMatcher.matches(); // will not match, but can search to find it
		boolean foundContentValue = contentMatcher.find();
		if(foundContentValue){
			return contentMatcher.group(1);
		}else{
			return null;
		}
	}
	
	public static void DownloadGraph(String graphUrl,String saveUrl) throws Exception {
		// TODO Auto-generated method stub
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try{
			String baiduMainLoginUrl = graphUrl;  
	        HttpGet getReq = new HttpGet(baiduMainLoginUrl);
	        
	        getReq.setHeaders(getDefaultHeaders());
	        CloseableHttpResponse response = httpclient.execute(getReq);
	        
	        try{
	        	HttpEntity respEnt = response.getEntity();
	        	InputStream instream = respEnt.getContent();
	        	byte[] data = readInputStream(instream);  
	            //new一个文件对象用来保存图片，默认保存当前工程根目录  
	            File imageFile = new File(saveUrl);  
	            //创建输出流  
	            FileOutputStream outStream = new FileOutputStream(imageFile);  
	            //写入数据  
	            outStream.write(data);  
	            //关闭输出流  
	            outStream.close();  
	            
	        }finally{
	        	response.close();
	        }
		}finally{
			httpclient.close();
		}
	}
	
	
	/**
     * //TODO 默认header
     * 
     * @return
     */
    private static Header[] getDefaultHeaders() {
        Header[] allHeader = new BasicHeader[3];
        allHeader[0] = new BasicHeader("Connection", "keep-alive");
        allHeader[1] = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        allHeader[2] = new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        return allHeader;
    }
    
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    }  
    

}
