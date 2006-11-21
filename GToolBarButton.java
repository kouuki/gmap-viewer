import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Icon ;
import javax.swing.ImageIcon;

public class GToolBarButton extends JButton {
    private static final Insets margins =
        new Insets(0, 0, 0, 0);
   
    public GToolBarButton(Icon icon) {
        super(icon);
        setMargin(margins);
        setVerticalTextPosition(BOTTOM);
        setHorizontalTextPosition(CENTER);
    }
   
    public GToolBarButton(String imageFile) {
        this(new ImageIcon(imageFile));
    }
   
    public GToolBarButton(String imageFile, String text) {
        this(new ImageIcon(imageFile));
        setText(text);
    }
}