package chatsupporter.gui.textdisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.AbstractBorder;

/**
 * 角の丸い四角形のボーダー.
 *
 * <p>パネル用のボーダーです。太い輪郭線と指定の色で塗りつぶしの角の丸い四角形のボーダーです。
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class RoundBorder extends AbstractBorder {
  private static final float STROKE_WIDTH = 5.0f;

  private final Color borderColor;
  private final Color rectangleColor;
  private final int arc;

  /**
   * 角の丸い資格聖のボーダーを作ります。.
   *
   * @param borderColor 辺の色
   * @param rectangleColor 四角形の色
   * @param arc 角を丸くするための引数。数字が大きいほど丸くなります。
   */
  public RoundBorder(Color borderColor, Color rectangleColor, int arc) {
    this.borderColor = borderColor;
    this.rectangleColor = rectangleColor;
    this.arc = arc;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2d = (Graphics2D) g.create();
    RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width - 1, height - 1, arc, arc);
    g2d.setColor(rectangleColor);
    g2d.fill(rect);
    g2d.setColor(borderColor);
    g2d.setStroke(new BasicStroke(STROKE_WIDTH));
    g2d.draw(rect);
    g2d.dispose();
  }

  @Override
  public Insets getBorderInsets(Component c) {
    return (getBorderInsets(c, new Insets(arc, arc, arc, arc)));
  }

  @Override
  public Insets getBorderInsets(Component c, Insets insets) {
    insets.left = insets.top = insets.right = insets.bottom = arc / 2;
    return insets;
  }

  @Override
  public boolean isBorderOpaque() {
    return true;
  }

}
