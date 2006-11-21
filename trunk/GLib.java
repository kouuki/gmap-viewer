class GLib{

	private static final int R = 6367; //R = 6367 km = 3956 mi  
	
	public static double computeDistance(GPhysicalPoint a, GPhysicalPoint b){
		double lat1 = degToRad(a.getX());
		double lat2 = degToRad(b.getX());    
		double dlon = degToRad(b.getY()) - degToRad(a.getY()); //lon2 - lon1 in Rad
		double dlat = (lat2) - (lat1); //lat2 - lat1 in Rad
		double f = (.5 - (.5*Math.cos(dlat))) + (Math.cos(lat1) * Math.cos(lat2) * (.5 - (.5*Math.cos(dlon)))); 		//sin^2(x) = 1/2 - (1/2)cos(2x); x = long/2; 2x = long;
		double c = 2 * Math.atan2(Math.sqrt(f),(Math.sqrt(1-f)));
		double d = R * c;
		return d;
    }
	private double degToRad(double deg)
	{
		return (deg*(Math.PI / 180));
	}

}