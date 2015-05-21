/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class QuickStart {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        	formparams.add(new BasicNameValuePair("username","yujiazhen1991"));
        	formparams.add(new BasicNameValuePair("password","yujiazhen"));
        	formparams.add(new BasicNameValuePair("token","1f5511e84469c9545007351846d2ea90"));
        	formparams.add(new BasicNameValuePair("charset","utf-8"));
        	formparams.add(new BasicNameValuePair("isPhone","false"));
        	formparams.add(new BasicNameValuePair("staticpage","http://www.baidu.com/cache/user/html/v3Jump.html"));
        	formparams.add(new BasicNameValuePair("logintype","dialogLogin"));
        	formparams.add(new BasicNameValuePair("callback","parent.bd__pcbs__960c22"));
        	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        	HttpPost httppost = new HttpPost("https://passport.baidu.com/v2/api/?login");
        	httppost.setEntity(entity);
        	CloseableHttpResponse response1 = httpclient.execute(httppost);
        	
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try {
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                InputStream instream = entity1.getContent();
                BufferedReader br=new BufferedReader(new InputStreamReader(instream));
                String result="";
                String line;
                while ((line = br.readLine()) != null) 
        		{   
                	result+=line+"\n";
        		}
                System.out.print(result);
                /*File f = new File("d:" + File.separator+"test.txt");
                OutputStream outstream=new FileOutputStream(f);
                entity1.writeTo(outstream);*/
            } finally {
                response1.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
