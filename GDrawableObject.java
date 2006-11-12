public interface GDrawableObject {

	public void draw(BufferedImage image, GPhysicalPoint p, int zoom);
	
	public Rectangle getRectangle(GPhysicalPoint p, int zoom);
	
}