import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Desktop;
import java.io.File;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;

public class eyeDropper {

	public Robot r;
	public String path;
	public String format = "JPG";
	public String fileName = "screenshot";
	public int x = 0;
	public int y = 0;
	public int width = 1;
	public int height = 1;

	public eyeDropper() {
		try {
			r = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

    public BufferedImage screenShot() throws Exception{
    	Robot r = new Robot();
    	BufferedImage screenShot = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		return screenShot;
    }

	public BufferedImage regionShot() throws Exception{
		Rectangle region = new Rectangle(this.x, this.y, this.width, this.height);
		BufferedImage regionShot = r.createScreenCapture(region);
		return regionShot;
	}

	public void saveScreenshot(BufferedImage screenShot, String filename) throws Exception{
		ImageIO.write(screenShot, this.format, new File(filename));
	}

	public int[][] getRGBFromImage(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;
  
		int[][] result = new int[height][width];
		if (hasAlphaChannel) {
		   final int pixelLength = 4;
		   for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
			  int argb = 0;
			  argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
			  argb += ((int) pixels[pixel + 1] & 0xff); // blue
			  argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
			  argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
			  result[row][col] = argb;
			  col++;
			  if (col == width) {
				 col = 0;
				 row++;
			  }
		   }
		} else {
		   final int pixelLength = 3;
		   for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
			  int argb = 0;
			  argb += -16777216; // 255 alpha
			  argb += ((int) pixels[pixel] & 0xff); // blue
			  argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
			  argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
			  result[row][col] = argb;
			  col++;
			  if (col == width) {
				 col = 0;
				 row++;
			  }
		   }
		}
  
		return result;
	 }  

	public int[] getPixelColor(BufferedImage screenShot, int x, int y) throws Exception{
		int[] pixel = new int[3];
		int color = screenShot.getRGB(x, y);
		pixel[0] = (color >> 16) & 0xff;
		pixel[1] = (color >> 8) & 0xff;
		pixel[2] = color & 0xff;
		return pixel;
	}

	public int[] getPixelColor(BufferedImage screenShot) throws Exception{
		int[] pixel = new int[3];
		int color = screenShot.getRGB(0,0);
		pixel[0] = (color >> 16) & 0xff;
		pixel[1] = (color >> 8) & 0xff;
		pixel[2] = color & 0xff;
		return pixel;
	}

	public static void main(String[] args) throws Exception {
		eyeDropper ed = new eyeDropper();
		BufferedImage screenShot = ed.regionShot();
		ImageIO.write(screenShot, "JPG", new File("screenshot.jpg"));
	}
}
