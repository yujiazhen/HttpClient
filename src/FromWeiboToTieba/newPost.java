package FromWeiboToTieba;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

public class newPost {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static String startNewPost(String content) throws Exception {
		//首先通过访问
		
		String tbs = httpClientDownload.getTbs();
		
		
		// TODO Auto-generated method stub
		String url = "http://tieba.baidu.com/f/commit/thread/add";
		String cookie = "BAIDUID=CAD3A0B3D3A534F55C2003BABC8EFE5C:FG=1; BAIDUPSID=CAD3A0B3D3A534F55C2003BABC8EFE5C; TIEBA_USERTYPE=3b2188065056e2a3fd2ae7f5; TIEBAUID=aee2d0d6cc41d59f79da914f; bdshare_firstime=1422241427649; BDUSS=EV1UWZhQXBVWE9WcjJtVkhoQzhjNHlPak1rY1NjVWl1MEtLcTBZWWdKSzZrLTFVQVFBQUFBJCQAAAAAAAAAAAEAAAAalSQkeXVqaWF6aGVuMTk5MQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALoGxlS6BsZUV";
		Map<String,String> params = new HashMap<String,String>();
		params.put("ie", "utf-8");
		params.put("fid", "67993");
		String str = "%E9%94%99%E8%AF%AF";
        String result = unescape(str);
		params.put("kw", result);
		params.put("vcode_md5", "");
		params.put("tid", "0");
		params.put("rich_text", "1");
		params.put("tbs", tbs);
		/*带有双引号等特殊字符等要特殊处理下*/
  //      String strSource = "好事//@where_are_you_from: 好诗[br]@黑童话集[br]看完这组诗真的让人诗兴大发，感觉自己白活了......[img pic_type=1 width=22 height=22]http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/70/88_org.gif[/img][br][img pic_type=1 width=80 height=80]http://ww2.sinaimg.cn/square/7f97d70fjw1em96sqa6ilj20bj0f1abo.jpg[/img]";
        String codes="";
		codes = URLEncoder.encode(content, "UTF-8");
		codes = codes.replaceAll("\\+", "%20");
		codes = unescape(codes);
		params.put("content", codes);
		
		String strSource = "微博随时更新";
		codes = URLEncoder.encode(strSource, "UTF-8");
		codes = codes.replaceAll("\\+", "%20");
		codes = unescape(codes);
		params.put("title", codes);
//		params.put("prefix", "123");
		params.put("__type__", "thread");
		params.put("mouse_pwd_isclick", "0");
		params.put("mouse_pwd", "40,43,35,54,47,42,45,46,19,43,54,42,54,43,54,42,54,43,54,42,19,40,44,34,44,40,19,43,41,44,44,54,45,44,34,14222612032260");
		params.put("mouse_pwd_t", "1422261203226");
		
		String Post = httpClientDownload.DownloadFromWebPost(url,cookie,params);
		return Post;
	}
	
	public static String unescape(String src)
    {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0,pos = 0;
        char ch;
        while(lastPos < src.length())
        {
            pos = src.indexOf("%",lastPos);
            if(pos == lastPos)
            {
                if(src.charAt(pos + 1) == 'u')
                {
                    ch = (char) Integer.parseInt(src.substring(pos + 2,pos + 6),16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                }
                else
                {
                    ch = (char) Integer.parseInt(src.substring(pos + 1,pos + 3),16);
                    tmp.
                        append(ch);
                    lastPos = pos + 3;
                }
            }
            else
            {
                if(pos == -1)
                {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                }
                else
                {
                    tmp.append(src.substring(lastPos,pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
    public static String escape(String src)
    {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for(i = 0;i < src.length();i++)
        {
            j = src.charAt(i);
            if(Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
            {
                tmp.append(j);
            }
            else if(j < 256)
            {
                tmp.append("%");
                if(j < 16)
                {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(j,16));
            }
            else
            {
                tmp.append("%u");
                tmp.append(Integer.toString(j,16));
            }
        }
        return tmp.toString();
    }

}
