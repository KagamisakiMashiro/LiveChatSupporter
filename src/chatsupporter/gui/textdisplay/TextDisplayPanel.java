package chatsupporter.gui.textdisplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 角の丸い四角形のテキスト表示パネル。.
 *
 * <p>text以外にもadditionalText（二行目）があります。.<br/>
 * 違う水平の整列方法を設定できます。
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class TextDisplayPanel extends JPanel {
  private static final Color BORDER_COLOR = new Color(217, 225, 246);
  private static final Color RECTANGLE_COLOR = new Color(166, 178, 226);
  private static final int ARC_VALUE = 20;
  private static final String HTML_START_TAG = "<html><body width ='";
  private static final String HTML_START_TAG_END = "px'>";
  private static final String HTML_ALIGN_TAG = "<div align=\"";
  private static final String HTML_ALIGN_TAG_END = "\">";
  private static final String HTML_ALIGN_TAG_CLOSE = "</div>";
  private static final String HTML_END_TAG = "</body></html>";
  private static final String HTML_ALIGN_CENTER_PARAM = "center";
  private static final String HTML_ALIGN_LEFT_PARAM = "left";
  private static final String HTML_ALIGN_RIGHT_PARAM = "right";
  private static final double WIDTH_RATIO = 0.7;

  private String text;
  private String additionalText;
  private JLabel textLabel;
  private GridBagConstraints gbc = new GridBagConstraints();
  private int currentWidth = 0;
  private int alignment = SwingConstants.CENTER;
  private int verticalAlignment = SwingConstants.CENTER;
  private int additionalTextAlignment = SwingConstants.CENTER;

  /** 角の丸い四角形のテキスト表示パネルを作成します。. */
  public TextDisplayPanel() {
    this("");
  }

  /**
 * 角の丸い四角形のテキスト表示パネルを作成します。.
   * @param text 表示するテキスト
   */
  public TextDisplayPanel(String text) {
    setOpaque(false);
    setBorder(new RoundBorder(BORDER_COLOR, RECTANGLE_COLOR, ARC_VALUE));
    setLayout(new GridBagLayout());
    this.text = text;

    textLabel = new JLabel(text);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.weighty = 1;
    add(textLabel, gbc);
  }

  /** 表示するテキストを取得します。. */
  public String getText() {
    return text;
  }

  /** 表示するテキストを設定します。. */
  public void setText(String text) {
    this.text = text;
    textLabel.setText(getHtmlString());
    revalidate();
    repaint();
  }

  /** フォントサイズを設定します。. */
  public void setFontSize(int size) {
    Font font = textLabel.getFont();
    textLabel.setFont(new Font(font.getFontName(), Font.PLAIN, size));
  }

  /** テキストの表示サイズ更新します。. */
  public void updateSize() {
    if (getSize().width != currentWidth) {
      currentWidth = getSize().width;
      textLabel.setText(getHtmlString());
      revalidate();
      repaint();
    }
  }

  /** 水平の整列方法を取得します。. */
  public int getAlignment() {
    return alignment;
  }

  /**
   * 水平の整列方法を設定します。.
   * @param alignment 整列方法
   *     SwingConstants.CENTER、SwingConstants.LEFT、SwingConstants.RIGHTのみ対応します。
   */
  public void setAlignment(int alignment) {
    if (this.alignment != alignment) {
      this.alignment = alignment;
      textLabel.setText(getHtmlString());
    }
  }

  /**
   * 垂直の整列方法を設定します。.
   * @param verticalAlignment 整列方法
   *     SwingConstants.CENTER、SwingConstants.NORTH、SwingConstants.SOUTHのみ対応します。
   */
  public void setVerticalAlignment(int verticalAlignment) {
    if (this.verticalAlignment != verticalAlignment) {
      this.verticalAlignment = verticalAlignment;
      switch (verticalAlignment) {
        case SwingConstants.NORTH:
          gbc.anchor = GridBagConstraints.NORTH;
          break;
        default:
        case SwingConstants.CENTER:
          gbc.anchor = GridBagConstraints.CENTER;
          break;
        case SwingConstants.SOUTH:
          gbc.anchor = GridBagConstraints.SOUTH;
          break;
      }
      add(textLabel, gbc);
    }
  }

  /** 追加テキスト（二行目）を取得します。. */
  public String getAdditionalText() {
    return additionalText;
  }

  /**
   * 追加テキスト（二行目）を設定します。.
   * @param additionalText 追加テキスト
   */
  public void setAdditionalText(String additionalText) {
    if (this.additionalText == null || !this.additionalText.equals(additionalText)) {
      this.additionalText = additionalText;
      textLabel.setText(getHtmlString());
    }
  }

  /** 追加テキスト（二行目）の水平整列方法を取得します。. */
  public int getAdditionalTextAlignment() {
    return additionalTextAlignment;
  }

  /**
   * 追加テキストの整水平列方法を設定します。.
   * @param additionalTextAlignment 追加テキストの整列方法
   *     SwingConstants.CENTER、SwingConstants.LEFT、SwingConstants.RIGHTのみ対応します。
   */
  public void setAdditionalTextAlignment(int additionalTextAlignment) {
    if (this.additionalTextAlignment != additionalTextAlignment) {
      this.additionalTextAlignment = additionalTextAlignment;
      textLabel.setText(getHtmlString());
    }
    this.additionalTextAlignment = additionalTextAlignment;
  }

  /** HTML化したテキストを取得します。. */
  private String getHtmlString() {
    String htmlText = HTML_START_TAG + (int)(getSize().width * WIDTH_RATIO) + HTML_START_TAG_END;
    if (text != null && !text.isEmpty()) {
      htmlText += getAlignmentTag(alignment) + text + HTML_ALIGN_TAG_CLOSE;
    }
    if (additionalText != null && !additionalText.isEmpty()) {
      htmlText += getAlignmentTag(additionalTextAlignment) + additionalText + HTML_ALIGN_TAG_CLOSE;
    }
    htmlText += HTML_END_TAG;
    return htmlText;
  }

  /** 水平整列方法のHTMLタグを取得します。. */
  private String getAlignmentTag(int alignment) {
    String tag = HTML_ALIGN_TAG;
    switch (alignment) {
      default:
      case SwingConstants.CENTER:
        tag += HTML_ALIGN_CENTER_PARAM;
        break;
      case SwingConstants.LEFT:
        tag += HTML_ALIGN_LEFT_PARAM;
        break;
      case SwingConstants.RIGHT:
        tag += HTML_ALIGN_RIGHT_PARAM;
        break;
    }
    tag += HTML_ALIGN_TAG_END;
    return tag;
  }
}
