import java.awt.geom.*;

class MathLib{
	
	public static void main(String[] args){
		Line2D l1 = new Line2D.Double(5.0,6.0,7.0,9.0);
		Line2D l2 = new Line2D.Double(6.0,10.0,6.0,5.0);

			Point2D ret = MathLib.intersectPoint(l1,l2);
			System.out.println(ret.getX() + " " + ret.getY());
			ret = MathLib.intersectPoint(l2,l1);
			System.out.println(ret.getX() + " " + ret.getY());
	}

	public MathLib(){
		super();
	}
	
	/**
	**/
	public static double slope(Point2D p1, Point2D p2) throws ArithmeticException{
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		
		return (y2 - y1)/(x2 - x1);
	}
	
	public static double yIntercept(Point2D p1, Point2D p2) throws ArithmeticException{
	
		double a = slope(p1, p2);
		
		return p1.getY() - (a*p1.getX());
		
	}
	
	public static Point2D intersectPoint(Line2D l1, Line2D l2){
		double a1 = 0.0;
		double a2 = 0.0;
		double b1 = 0.0;
		double b2 = 0.0;
		
		double x = 0.0;
		double y = 0.0;
		
		boolean slopeExists1 = true;
		boolean slopeExists2 = true;;
		
		try{
			a1 = slope(l1.getP1(), l1.getP2());
			if(a1 == Double.POSITIVE_INFINITY || a1 == Double.NEGATIVE_INFINITY){
				x = l1.getP1().getX();
				slopeExists1 = false;
			}else{
				b1 = yIntercept(l1.getP1(), l1.getP2());
			}
		}catch(ArithmeticException ex){
			x = l1.getP1().getX();
			slopeExists1 = false;
		}
		
		try{
			a2 = slope(l2.getP1(), l2.getP2());
			if(a2 == Double.POSITIVE_INFINITY || a2 == Double.NEGATIVE_INFINITY){
				x = l2.getP1().getX();
				slopeExists2 = false;
			}else{
				b2 = yIntercept(l2.getP1(), l2.getP2());
			}
		}catch(ArithmeticException ex){
			x = l2.getP1().getX();
			slopeExists2 = false;
		}
		
		if(slopeExists1){
			if(slopeExists2){
				try{
					x = (b1 - b2)/(a2 - a1);
				}catch(ArithmeticException ex){
					System.out.println("Both lines have zero slopes, they can't intersect");
					return null;
				}
			}
			y = a1*x + b1;	
		}else{
			if(!slopeExists2){
				System.out.println("Both lines have not yIntercepts, they can't intersect");
				return null;
			}
			y = a2*x + b2;
		}
		
		
		return new Point2D.Double(x, y);
	}
	
	public static double intersectPointX(Line2D l1, Line2D l2){
		return intersectPoint(l1, l2).getX();
	}
	
	public static double intersectPointY(Line2D l1, Line2D l2){
		return intersectPoint(l1, l2).getY();
	}
}