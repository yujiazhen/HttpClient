package FromWeiboToTieba;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class FromBaiduToTieba {

	/**
	 * @param args
	 * ��Ҫ����д����־���ҳ�֮��Ҫ
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
	 * ÿ��3����ȥ���һ�� ΢�� ��û�и��£�������ݺ��ϴβ�һ���ˣ��ͱ�ʾ������;
	 * ��������ˣ���ô��Ҫ���Ϸ��������ɣ�������Ҫ����Ȼ�͵��ű����
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
					System.out.println("Precontent��"+Precontent);
					System.out.println("Content��"+content);
					if(!content.equals(Precontent)){
						if(!content.equals("error")){
		//					String result = newPost.startNewPost(content);
							System.out.println("΢���Ѹ��¡�����");
							System.out.println(content);
						}
						else{
							System.out.println("there is error:can't get the weibo content");
						}
					}else{
						System.out.println("΢��û�и��¡�����");
					}
					Precontent = content;
					Thread.sleep(3000);
					System.out.println("˯�߹���ʼ����΢����û�и��¡�����");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	static String getWeiboContent()throws Exception{
		/*���ֺͱ����ⲻ�羭������������*/
		String AfterHandlewordsExp = "";
		//ͼ�񾭹�����������
		String AfterHandleimage = "";
		//���������ʱ�䴦��������
		String AfterHandlefrom = "";
		
		//ת��ʱ��ת���ĸ���INfo
		String AfterHandleInfo = "";
		
		//��ת�����˵�����
		String AfterForwardWordsExp = "";
		
		//��ת����ͼƬ
		String AfterForwardImage = "";
		//ת���ͱ�ת����ʱ�����������
		String AfterForwardFrom = "";
		
		String url = "http://weibo.com/p/1005052965449484/home?from=page_100505_profile&wvr=6&mod=data";
		String cookie = "SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5BSwQQW1xduXa-530_g-T15JpX5K2t; SINAGLOBAL=8078589017328.969.1417141477868; ULV=1422100804136:2:1:1:6727338042972.86.1422100804109:1417141477909; YF-Page-G0=f9cf428f7c30a82f1d66fab847fbb873; SUB=_2A255x8dIDeTxGeRH7VcV9CfIwziIHXVatL-ArDV8PUNbuNBeLXbfkW-W8SxZb1dQOVoB1e6FnzD0Rt-B5g..; SUHB=0HTxZWRiOXYYwM; YF-V5-G0=5161d669e1ac749e79d0f9c1904bc7bf; _s_tentry=login.sina.com.cn; Apache=6727338042972.86.1422100804109; myuid=2614113133; YF-Ugrow-G0=8751d9166f7676afdce9885c6d31cd61; login_sid_t=4342a3a2f37aed5464a7ee6ea5cdcfdd; UOR=,,login.sina.com.cn; SUS=SID-2965449484-1422112536-GZ-dwnvk-b1448249083b84b94c68148e60af6f82";
		String result = httpClientDownload.DownloadFromWeb(url,cookie);
		Pattern contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\"([\\s\\S]*?)�ղ�");
		Matcher contentMatcher = contentP.matcher(result);
		//boolean foundTokenValue = tokenValMatcher.matches(); // will not match, but can search to find it
		boolean foundContentValue = contentMatcher.find();
        if(foundContentValue){
        	String AllContent = contentMatcher.group();
        	//������ԭ������ת���ģ���ͨ���Ƿ����lyf���жϣ�
        	contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\" nick-name=\\\\\"fef�ݶ�������_812\\\\\">");
        	contentMatcher = contentP.matcher(AllContent);
        	if(contentMatcher.find()){
        		//ԭ��
        		//���ֺͱ����@�ͻ�������
        		contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\" nick-name=\\\\\"fef�ݶ�������_812\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String wordsExp = contentMatcher.group(1);
            	    AfterHandlewordsExp = judgeWordsExp(wordsExp)+"[br]";
            	}
            	//�����ͼƬ�Ļ���ͼƬ����
            	contentP = Pattern.compile("<div class=\\\\\"WB_media_wrap clearfix\\\\\" node-type=\\\\\"feed_list_media_prev\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String image = contentMatcher.group(1);
            		AfterHandleimage = handleImage(image);
            	}
            	//���ں�ͨ��ʲô;������
            	contentP = Pattern.compile("<div class=\\\\\"WB_from S_txt2\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String fromwhere = contentMatcher.group(1);
            		AfterHandlefrom = handleFromWhere(fromwhere)+"[br]";
            	}
            	
            	return AfterHandlewordsExp+AfterHandleimage+AfterHandlefrom;
        	}
        	else{
        		//ת��
        		contentP = Pattern.compile("<div class=\\\\\"WB_text W_f14\\\\\" node-type=\\\\\"feed_list_content\\\\\" >\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String wordsExp = contentMatcher.group(1);
            	    AfterHandlewordsExp = judgeWordsExp(wordsExp);
            	    AfterHandlewordsExp = AfterHandlewordsExp.replaceAll("\\\\/\\\\/", "//")+"[br]";
            	}
            	
            	//ת�������壨Ҳ��������֣����飬����������Ȼ��ͼƬ���֣�������ں��������
            	
            	//���Ȳ���
            	contentP = Pattern.compile("<div class=\\\\\"WB_info\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String personInfo = contentMatcher.group(1);
            		AfterHandleInfo = "����"+findName(personInfo)+"[br]";
            	}
            	
            	//Ȼ�������ģ�Ҳ�ǰ������֣����飬���⣬@
            	contentP = Pattern.compile("<div class=\\\\\"WB_text\\\\\" node-type=\\\\\"feed_list_reason\\\\\">\\\\n([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String forwardWordsExp = contentMatcher.group(1);
            		AfterForwardWordsExp = "����"+judgeWordsExp(forwardWordsExp)+"[br]";
            	}
            	
            	//Ȼ����ͼƬ
            	contentP = Pattern.compile("<div class=\\\\\"WB_media_wrap clearfix\\\\\">([\\s\\S]*?)<\\\\/div>");
            	contentMatcher = contentP.matcher(AllContent);
            	if(contentMatcher.find()){
            		String forwardImage = contentMatcher.group(1);
            		AfterForwardImage = handleImage(forwardImage);
            	}
            	
            	//��ת����΢����ʱ�����������
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
        	System.out.println("�޷����ҳ������,�޷����΢�����ݣ�������cookie���ڻ���url������");
        	return "error";
        }
	}
	
	
    static //to determine if it is words or expresssion ��ʱ��ȱ������ͼƬ��������ָ���䳤�ȺͿ��
	String  judgeWordsExp(String wordsExp) throws Exception{
		/*������ʽƥ��*/
		Pattern contentP;
		Matcher contentMatcher;
		
		String wordsExpCopy = wordsExp.trim();
		String afterWords = "";
		int beginIndex = 0,endIndex;
		String temp;
		while(wordsExpCopy != null && !wordsExpCopy.equals("")){
			endIndex = wordsExpCopy.indexOf("<a render");
			//�����ж��Ƿ���ڱ����������ǰ������
			int expEndIndex = wordsExpCopy.indexOf("<img render");
			if(expEndIndex != -1 && expEndIndex<endIndex){
				endIndex = -1;
			}
			
			if(endIndex == -1){
				endIndex = wordsExpCopy.indexOf("<img render");
				if(endIndex == -1){
					//�����û������Ҳû�б�����
					temp = wordsExpCopy.substring(beginIndex).trim();
					afterWords += temp;
					wordsExpCopy = "";
				}
				else{
					//��һ�����ֵ��Ǳ���
					temp = wordsExpCopy.substring(beginIndex, endIndex).trim();
					afterWords += temp;
					if(temp.equals("")||temp == null){
						//����ǰ���Ƿ�û��������
						contentP = Pattern.compile("<img render=\\\\\"ext\\\\\" src=\\\\\"([\\s\\S]*?)\" title");
						contentMatcher = contentP.matcher(wordsExpCopy);
						if(contentMatcher.find()){
							temp = contentMatcher.group(1);
							temp = temp.replaceAll("\\\\", "");
							//�Ա�������ӽ��д�������[img pic_type=1 width=xx height=xx]url[/img]
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
	
	
	//��ͼ����������֮��
	static String handleImage(String image) throws Exception{
		/*������ʽƥ��*/
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
	
	
	//�����������ʱ�侭������
	static String handleFromWhere(String fromwhere){
		/*������ʽƥ��*/
		Pattern contentP;
		Matcher contentMatcher;
		
		String fromCopy = fromwhere.trim();
		String afterFrom = "";
		contentP = Pattern.compile("<a([\\s\\S]*?)href=([\\s\\S]*?)title=\\\\\"([\\s\\S]*?)\\\\\"([\\s\\S]*?)rel=\\\\\"nofollow\\\\\">([\\s\\S]*?)<\\\\/a>");
		contentMatcher = contentP.matcher(fromCopy);
		if(contentMatcher.find()){
			String fromDate = contentMatcher.group(3);
			String fromWhere = " ����"+contentMatcher.group(5);
			afterFrom = fromDate+fromWhere;
		}
		
		return afterFrom;
	}
	
	
	static String findName(String personInfo){
		/*������ʽƥ��*/
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
