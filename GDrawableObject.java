import java.awt.*;
import java.awt.image.*;

interface GDrawableObject{
   public abstract void draw(BufferedImage image, GPhysicalPoint p, int zoom);
   public abstract Rectangle getRectangle(GPhysicalPoint p, int zoom);
}

