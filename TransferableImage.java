import java.awt.*;
import java.awt.image.*;
import java.awt.datatransfer.*;
import java.io.*;

/** This is a wrapper class for a BufferedImage that implements the transferable interface, allowing it to be placed in the clipboard. */

public class TransferableImage implements Transferable {
    private BufferedImage image;

    public TransferableImage(BufferedImage image) {
        this.image = image;
    }

    /** Returns an array with a single element, containing the data flavor for images. */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    /** Returns true iff flavor is equal to image flavor, since images can only be exported in one flavor. */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    /** Returns the content of the wrapper, which is the image. Images can only be exported in image flavor, so if a different flavor is supplied, it throws an exception. */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!DataFlavor.imageFlavor.equals(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }
}