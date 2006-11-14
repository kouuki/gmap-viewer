import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class GLine{
	
	Line2D line;
	int zoom;
	
	public GLine(Point2D p1, Point2D p2, int zoom){
		line = new Line2D.Double(p1, p2);
		this.zoom = zoom;
	}
	
	public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
		Point2D pCorner = new Point2D.Double(p.getPixelX(zoom), p.getPixelY(zoom));
		Dimension imageDim = new Dimension(image.getWidth(), image.getHeight());
		Rectangle imageRect = new Rectangle((int)p.getPoint().getX() - 1, (int)p.getPoint().getY() - 1, imageDim.width - 2, imageDim.height - 2);
		Graphics g = image.getGraphics();
		
		int power = this.zoom - zoom;
		Line2D cLine = new Line2D.Double(line.getX1()*Math.pow(2, power), line.getY1()*Math.pow(2,power), line.getX2()*Math.pow(2,power), line.getY2()*Math.pow(2,power));
		
		//DEBUG
		System.out.println("Method Running");
		System.out.println(p.getPoint().getX() + " "  + p.getPoint().getY() + " " + imageDim.width + " " + imageDim.height);
		System.out.println("(" + cLine.getX1() + "," + cLine.getY1() + ") (" + cLine.getX2() + "," + cLine.getY2() + ")");
		
		//If line does not intersect the image, return image w/o modification
		if(cLine.intersects(imageRect)){				
		
			//will contain drawing coordinates for image (will take p as point (0,0)
			int x1 = -1; 
			int y1 = -1;
			int x2 = -1;
			int y2 = -1;
			
			double slope;
			
			try{
				slope = (-(int)cLine.getY2() + (int)cLine.getY1())/((int)cLine.getX2() - (int)cLine.getX1());
			}catch(java.lang.ArithmeticException e){
				if((-(int)cLine.getY2() + (int)cLine.getY1()) > 0){
					slope = 999999999.0;
				}else{
					slope = -999999999.0;
				}
			}
			double yIntercept = -(int)cLine.getY2() - slope*(int)cLine.getX2();
			
			System.out.println("In intersect if statement");
			if(imageRect.contains(cLine.getX1(), cLine.getY1())){
				x1 = (int)cLine.getX1();
				y1 = (int)cLine.getY1();
				if(imageRect.contains(cLine.getX2(), line.getY2())){
					x2 = (int)cLine.getX2();
					y2 = (int)cLine.getY2();
				}
				else{
					if((new Line2D.Double(imageRect.x, imageRect.y, imageRect.x + imageRect.width, imageRect.y)).intersectsLine(cLine)){
						y2 = imageRect.y;
						x2 = (int)((-y2 - yIntercept)/slope);
					}else if((new Line2D.Double(imageRect.x + imageRect.width, imageRect.y, imageRect.x + imageRect.width, imageRect.y + imageRect.height)).intersectsLine(cLine)){
						x2 = imageRect.x + imageRect.width;
						y2 = -(int)(slope*x2 + yIntercept);
					}else if((new Line2D.Double(imageRect.x + imageRect.width, imageRect.y + imageRect.height, imageRect.x, imageRect.y + imageRect.height)).intersectsLine(cLine)){
						y2 = imageRect.y + imageRect.height;
						x2 = (int)((-y2 - yIntercept)/slope);
					}else if((new Line2D.Double(imageRect.x, imageRect.y + imageRect.height, imageRect.x, imageRect.y)).intersectsLine(cLine)){
						x2 = imageRect.x;
						y2 = -(int)(slope*x2 + yIntercept);
					}
				}
			}
			else{
				if(imageRect.contains(line.getX2(), line.getY2())){
					x2 = (int)cLine.getX2();
					y2 = (int)cLine.getY2();
					if((new Line2D.Double(imageRect.x, imageRect.y, imageRect.x + imageRect.width, imageRect.y)).intersectsLine(cLine)){
						y1 = imageRect.y;
						x1 = (int)((-y2 - yIntercept)/slope);
					}else if((new Line2D.Double(imageRect.x + imageRect.width, imageRect.y, imageRect.x + imageRect.width, imageRect.y + imageRect.height)).intersectsLine(cLine)){
						x1 = imageRect.x + imageRect.width;
						y1 = -(int)(slope*x2 + yIntercept);
					}else if((new Line2D.Double(imageRect.x + imageRect.width, imageRect.y + imageRect.height, imageRect.x, imageRect.y + imageRect.height)).intersectsLine(cLine)){
						y1 = imageRect.y + imageRect.height;
						x1 = (int)((-y2 - yIntercept)/slope);
					}else if((new Line2D.Double(imageRect.x, imageRect.y + imageRect.height, imageRect.x, imageRect.y)).intersectsLine(cLine)){
						x1 = imageRect.x;
						y1 = -(int)(slope*x2 + yIntercept);
					}
				}
				else{
					if((new Line2D.Double(imageRect.x, imageRect.y, imageRect.x + imageRect.width, imageRect.y)).intersectsLine(cLine)){
						if(x1 < 0 && y1 < 0){
							y1 = imageRect.y;
							x1 = (int)((-y2 - yIntercept)/slope);
						}
						else{
							y2 = imageRect.y;
							x2 = (int)((-y2 - yIntercept)/slope);
						}
					}
					if((new Line2D.Double(imageRect.x + imageRect.width, imageRect.y, imageRect.x + imageRect.width, imageRect.y + imageRect.height)).intersectsLine(cLine)){
						if(x1 < 0 && y1 < 0){
							x1 = imageRect.x + imageRect.width;	
							y1 = -(int)(slope*x2 + yIntercept);
						}
						else{
							x2 = imageRect.x + imageRect.width;	
							y2 = -(int)(slope*x2 + yIntercept);
						}
					}
					if((new Line2D.Double(imageRect.x + imageRect.width, imageRect.y + imageRect.height, imageRect.x, imageRect.y + imageRect.height)).intersectsLine(cLine)){
						if(x1 < 0 && y1 < 0){
							y1 = imageRect.y + imageRect.height;
							x1 = (int)((-y2 - yIntercept)/slope);
						}
						else{
							y2 = imageRect.y + imageRect.height;
							x2 = (int)((-y2 - yIntercept)/slope);
						}
					}
					if((new Line2D.Double(imageRect.x, imageRect.y + imageRect.height, imageRect.x, imageRect.y)).intersectsLine(cLine)){
						if(x1 < 0 && y1 < 0){
							x1 = imageRect.x;
							y1 = -(int)(slope*x2 + yIntercept);
						}
						else{
							x2 = imageRect.x;
							y2 = -(int)(slope*x2 + yIntercept);
						}
					}
				}
			}
			
			x1 -= (int)p.getPoint().getX();
			y1 -= (int)p.getPoint().getY();
			
			x2 -= (int)p.getPoint().getX();
			y2 -= (int)p.getPoint().getY();
			
			//DEBUG
			//System.out.println(x1 + " " + y1);
			//System.out.println(x2 + " " + y2);
			
			int [] xPoints = {x1 - 1, x1 + 1, x2 + 1, x2 + 1, x2 - 1, x1 - 1};
			int [] yPoints = {y1 - 1, y1 - 1, y2 - 1, y2 + 1, y2 + 1, y1 + 1};
			int nPoints = 6;
			Polygon polyLine = new Polygon(xPoints, yPoints, nPoints);
			
			g.fillPolygon(polyLine);
		}
	}
	
	public Rectangle getRectangle(GPhysicalPoint p, int zoom){
		//Implementation goes here
	}
	
}