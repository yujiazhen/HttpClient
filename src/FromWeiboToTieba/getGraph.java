package FromWeiboToTieba;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class getGraph {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String url = "http://ww2.sinaimg.cn/square/7f97d70fjw1em96sqa6ilj20bj0f1abo.jpg";
		
		String fileName = url.substring(url.lastIndexOf('/')+1);
		httpClientDownload.DownloadGraph(url,fileName);
		
		File file = new File(fileName);
	    BufferedImage bi = ImageIO.read(file);    
	    int width = bi.getWidth();    
	    int height = bi.getHeight();
	    System.out.println(width + "/" + height);
	}

}
