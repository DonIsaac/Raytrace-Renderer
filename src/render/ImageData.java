package render;

public class ImageData {
	private int width,height,imageType;
	public boolean antiAliasing;
	
	public ImageData(int width, int height, int imageType, boolean antiAliasing){
		this.width=width;
		this.height=height;
		this.imageType=imageType;
		this.antiAliasing=antiAliasing;
	}
	public double aspectRatio(){
		return (double)width/(double)height;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public int getType(){
		return imageType;
	}
}
