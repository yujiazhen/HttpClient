package FromWeiboToTieba;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class FromBaiduToTieba {

	/**
	 * @param args
	 * 不要忘记写进日志，灰常之重要
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//step1:get the weibo content(include the right format)
/*		GetThread oneThread = new GetThread();
		oneThread.start();*/
		String content = getWeiboContent();
		if(!content.equals("error")){
			String result = newPost.startNewPost(content);
			System.out.println(result);
		}
		else{
			System.out.println("there is error:can't get the weibo content");
		}

		
	}
	
	/**
	 * 每隔3秒钟去检查一次 微博 有没有更新，如果内容和上次不一样了，就表示更新了;
	 * 如果更新了，那么需要马上发帖到贴吧，否则不需要，不然就等着被封吧
	 * @author yjz
	 *
	 */
	static public class GetThread extends Thread{
		String Precontent = "error";
		
		@Override
		public void run(){
			try {
				while(true){
					String content = getWeiboContent();
					System.out.println("Precontent："+Precontent);
					System.out.println("Content："+content);
					if(!content.equals(Precontent)){
						if(!content.equals("error")){
		//					String result = newPost.startNewPost(content);
							System.out.println("微博已更新。。。");
							System.out.println(content);
						}
						else{
							System.out.println("there is error:can't get the weibo content");
						}
					}else{
						System.out.println("微博没有更新。。。");
					}
					Precontent = content;
					Thread.sleep(3000);
					System.out.println("睡眠过后开始看看微博有没有更新。。。");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	static String getWeiboContent()throws Exception{
		/*文字和表情这不风经过处理后的样子*/
		String AfterHandlewordsExp = "";
		//图像经过处理后的样子
		String AfterHandleimage = "";
		//来自哪里喝时间处理后的样子
		String AfterHandlefrom = "";
		
		//转发时被转发的个人INfo
		String AfterHandleInfo = "";
		
		//被转发的人的正文
		String AfterForwardWordsExp = "";
		
		//被转发的图片
		String AfterForwardImage = "";
		//转发和被转发的时间和来自哪里
		String AfterForwardFrom = "";
		
		String url = "http://weibo.com/p/1005052965449484/home?from=page_100505_profile&wvr=6&mod=data";
		String cookie = "SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5BSwQQW1xduXa-530_g-T15JpX5K2t; SINAGLOBAL=8078589017328.969.1417141477868; ULV=1422100804136:2:1:1:6727338042972.86.1422100804109:1417141477909; YF-Page-G0=f9cf428f7c30a82f1d66fab847fbb873; SUB=_2A255x8dIDeTxGeRH7VcV9CfIwziIHXVatL-ArDV8PUNbuNBeLXbfkW-W8SxZb1dQOVoB1e6FnzD0Rt-B5g..; SUHB=0HTxZWRiOXYYwM; YF-V5-G0=5161d669e1ac749e79d0f9c1904bc7bf; _s_tentry=login.sina.com.cn; Apache=6727338042972.86.1422100804109; myuid=2614113133; YF-Ugrow-G0=8751d9166f7676afdce9885c6d31cd61; login_sid_t=4342a3a2f37aed5464a7ee6ea5cdcfdd; UOR=,,login.sina.com.cn; SUS=SID-2965449484-1422112536-GZ-dwnvk-b1448249083b84b94c68148e60af6f82";
		String result = httpClientDownload.DownloadFromWeb(url,cookie);
		Pattern contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\"([\\s\\S]*?)收藏");
		Matcher contentMatcher = contentP.matcher(result);
		//boolean foundTokenValue = tokenValMatcher.matches(); // will not match, but can search to find it
		boolean foundContentValue = contentMatcher.find();
        if(foundContentValue){
        	String AllContent = contentMatcher.group();
        	//区分是原创还是转发的？（通过是否带有lyf来判断）
        	contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\" nick-name=\\\\\"fef份儿饭反而_812\\\\\">");
        	contentMatcher = contentP.matcher(AllContent);
        	if(contentMatcher.find()){
        		//原创
        		//文字和表情和@和话题内容
        		contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\" nick-name=\\\\\"fef份儿饭反而_812\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String wordsExp = contentMatcher.group(1);
            	    AfterHandlewordsExp = judgeWordsExp(wordsExp)+"[br]";
            	}
            	//如果有图片的话，图片内容
            	contentP = Pattern.compile("<div class=\\\\\"WB_media_wrap clearfix\\\\\" node-type=\\\\\"feed_list_media_prev\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String image = contentMatcher.group(1);
            		AfterHandleimage = handleImage(image);
            	}
            	//日期和通过什么途径发的
            	contentP = Pattern.compile("<div class=\\\\\"WB_from S_txt2\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String fromwhere = contentMatcher.group(1);
            		AfterHandlefrom = handleFromWhere(fromwhere)+"[br]";
            	}
            	
            	return AfterHandlewordsExp+AfterHandleimage+AfterHandlefrom;
        	}
        	else{
        		//转发
        		contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\" >\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String wordsExp = contentMatcher.group(1);
            	    AfterHandlewordsExp = judgeWordsExp(wordsExp);
            	    AfterHandlewordsExp = AfterHandlewordsExp.replaceAll("\\\\/\\\\/", "//")+"[br]";
            	}
            	
            	//转发的正体（也会包括文字（表情，。。。），然后图片部分，最后日期和来自哪里）
            	
            	//首先博主
            	contentP = Pattern.compile("<div class=\\\\\"WB_info\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String personInfo = contentMatcher.group(1);
            		AfterHandleInfo = "　　"+findName(personInfo)+"[br]";
            	}
            	
            	//然后是正文，也是包括文字，表情，话题，@
            	contentP = Pattern.compile("<div class=\\\\\"WB_text\\\\\" node-type=\\\\\"feed_list_reason\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String forwardWordsExp = contentMatcher.group(1);
            		AfterForwardWordsExp = "　　"+judgeWordsExp(forwardWordsExp)+"[br]";
            	}
            	
            	//然后是图片
            	contentP = Pattern.compile("<div class=\\\\\"WB_media_wrap clearfix\\\\\">([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String forwardImage = contentMatcher.group(1);
            		AfterForwardImage = handleImage(forwardImage);
            	}
            	
            	//被转发的微博的时间和来自哪里
            	contentP = Pattern.compile("<div class=\\\\\"WB_from S_txt2\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	while(contentMatcher.find()){
            		String fromWhere = contentMatcher.group(1);
            		AfterForwardFrom += handleFromWhere(fromWhere)+"[br]";
            	}
            	
            	return AfterHandlewordsExp+AfterHandleInfo+AfterForwardWordsExp+AfterForwardImage+AfterForwardFrom;
        	}
        }
        else{
        	System.out.println("无法获得页面内容,无法获得微博内容，可能是cookie过期或者url有问题");
        	return "error";
        }
	}
	
	
    static //to determine if it is words or expresssion 暂时还缺少下载图片到本地来指导其长度和宽度
	String  judgeWordsExp(String wordsExp) throws Exception{
		/*正则表达式匹配*/
		Pattern contentP;
		Matcher contentMatcher;
		
		String wordsExpCopy = wordsExp.trim();
		String afterWords = "";
		int beginIndex = 0,endIndex;
		String temp;
		while(wordsExpCopy != null && !wordsExpCopy.equals("")){
			endIndex = wordsExpCopy.indexOf("<a render");
			//用来判断是否存在表情比链接在前面的情况
			int expEndIndex = wordsExpCopy.indexOf("<img render");
			if(expEndIndex != -1 && expEndIndex<endIndex){
				endIndex = -1;
			}
			
			if(endIndex == -1){
				endIndex = wordsExpCopy.indexOf("<img render");
				if(endIndex == -1){
					//后面既没有链接也没有表情了
					temp = wordsExpCopy.substring(beginIndex).trim();
					afterWords += temp;
					wordsExpCopy = "";
				}
				else{
					//第一个出现的是表情
					temp = wordsExpCopy.substring(beginIndex, endIndex).trim();
					afterWords += temp;
					if(temp.equals("")||temp == null){
						//表情前面是否没有文字了
						contentP = Pattern.compile("<img render=\\\\\"ext\\\\\" src=\\\\\"([\\s\\S]*?)\" title");
						contentMatcher = contentP.matcher(wordsExpCopy);
						if(contentMatcher.find()){
							temp = contentMatcher.group(1);
							temp = temp.replaceAll("\\\\", "");
							//对表情的连接进行处理，加上[img pic_type=1 width=xx height=xx]url[/img]
							temp = handleGraph(temp);
							afterWords += temp;
						}
						endIndex = wordsExpCopy.indexOf("type=\\\"face\\\" \\/>");
						wordsExpCopy = wordsExpCopy.substring(endIndex+17).trim();
					}
					else
						wordsExpCopy = wordsExpCopy.substring(endIndex).trim();
				}
			}
			else{
				temp = wordsExpCopy.substring(beginIndex, endIndex).trim();
				afterWords += temp;
				if(temp.equals("")||temp == null){
					contentP = Pattern.compile("<a render=([\\s\\S]*?)\">([\\s\\S]*?)<\\\\/a>");
					contentMatcher = contentP.matcher(wordsExpCopy);
					if(contentMatcher.find()){
						temp = contentMatcher.group(2);
						afterWords += temp;
					}
					endIndex = wordsExpCopy.indexOf("<\\/a>");
					wordsExpCopy = wordsExpCopy.substring(endIndex+5).trim();
				}
				else
					wordsExpCopy = wordsExpCopy.substring(endIndex).trim();
			}
		}
		
		return afterWords;
	}
	
	
	//对图像做出处理之后
	static String handleImage(String image) throws Exception{
		/*正则表达式匹配*/
		Pattern contentP;
		Matcher contentMatcher;
		
		String imageCopy = image.trim();
		String afterImage = "";
		int imageNumber=0;
		contentP = Pattern.compile("<img([\\s\\S]*?)src=\\\\\"([\\s\\S]*?)\\\\\"");
		contentMatcher = contentP.matcher(imageCopy);
		while(contentMatcher.find()){
			imageNumber = imageNumber+1;
			String temp = contentMatcher.group(2);
			temp = temp.replaceAll("\\\\", "");
			temp = handleGraph(temp);
			afterImage += temp;
			if(imageNumber%3 == 0)
				afterImage += "[br]";
		}
		return afterImage;
	}
	
	
	//对来自哪里和时间经过处理
	static String handleFromWhere(String fromwhere){
		/*正则表达式匹配*/
		Pattern contentP;
		Matcher contentMatcher;
		
		String fromCopy = fromwhere.trim();
		String afterFrom = "";
		contentP = Pattern.compile("<a([\\s\\S]*?)href=([\\s\\S]*?)title=\\\\\"([\\s\\S]*?)\\\\\"([\\s\\S]*?)rel=\\\\\"nofollow\\\\\">([\\s\\S]*?)<\\\\/a>");
		contentMatcher = contentP.matcher(fromCopy);
		if(contentMatcher.find()){
			String fromDate = contentMatcher.group(3);
			String fromWhere = " 来自"+contentMatcher.group(5);
			afterFrom = fromDate+fromWhere;
		}
		
		return afterFrom;
	}
	
	
	static String findName(String personInfo){
		/*正则表达式匹配*/
		Pattern contentP;
		Matcher contentMatcher;
		
		String input = personInfo.trim();
		String afterName = "";
		contentP = Pattern.compile("<a node-type=\'feed_list_originNick\'([\\s\\S]*?)usercard([\\s\\S]*?)\\\\\">([\\s\\S]*?)<\\\\/a>");
		contentMatcher = contentP.matcher(input);
		if(contentMatcher.find()){
			String nickName = contentMatcher.group(3);
			afterName = nickName;
		}
		return afterName;
		
	}
	
	static String handleGraph(String graphUrl) throws Exception{
		String url="";
		String fileName = graphUrl.substring(graphUrl.lastIndexOf('/')+1);
		httpClientDownload.DownloadGraph(graphUrl,fileName);
		
		File file = new File(fileName);
	    BufferedImage bi = ImageIO.read(file);    
	    int width = bi.getWidth();    
	    int height = bi.getHeight();
	    
	    url = "[img pic_type=1 width="+width+" height="+height+"]"+graphUrl+"[/img]";
	    return url;
	    
	}

}
