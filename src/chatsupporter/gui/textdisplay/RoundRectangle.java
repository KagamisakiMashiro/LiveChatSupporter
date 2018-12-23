package chatsupporter.gui.textdisplay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;

/**
 * 角の丸い四角形。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class RoundRectangle extends JLabel {

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2 = (Graphics2D)this.getGraphics();
    g2.setColor(Color.GREEN);
    RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, 100, 100, 30, 10);
    g2.fill(rect);
    g2.setColor(Color.RED);
    g2.draw(rect);
  }
}
