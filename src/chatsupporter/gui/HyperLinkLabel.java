package chatsupporter.gui;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JLabel;

/**
 * ハイパーリンクに移動できるJLabel。.
 *
 * <p>linkを設定しない場合、普通のJLabelと同じです。<br/>
 * textを設定しない場合、ハイパーリンクに移動することはできません。
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class HyperLinkLabel extends JLabel {
  private static final String HTML_TAG_START = "<html><body><a href = \"";
  private static final String HTML_TAG_END = "\">";
  private static final String HTML_TAG_CLOSE = "</a></body></html>";

  private String text;
  private String link;

  /** ハイパーリンクに移動できるJLabelを作成します。. */
  public HyperLinkLabel() {
    this(null, null);
  }

  /** ハイパーリンクに移動できるJLabelを作成します。. */
  public HyperLinkLabel(String text) {
    this(text, null);
  }

  /** ハイパーリンクに移動できるJLabelを作成します。. */
  public HyperLinkLabel(String text, String hyperLink) {
    super(text);
    this.text = text;
    this.link = hyperLink;
    setText(text);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (link != null && !link.isEmpty()) {
          try {
            Desktop.getDesktop().browse(new URI(link));
          } catch (Exception exception) {
            exception.printStackTrace();
          }
        }
      }
    });
  }

  @Override
  public void setText(String text) {
    if (text == null || text.isEmpty() || link == null || link.isEmpty()) {
      super.setText(text);
      setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    } else {
      super.setText(HTML_TAG_START + link + HTML_TAG_END + text + HTML_TAG_CLOSE);
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
  }

  /**
   * 移動するハイパーリンクを設定します。.
   *
   * <p>ハイパーリンクはnullと空の場合、普通のJLabelと同じです。
   * @param link ハイパーリンク（null可）
   */
  public void setLink(String link) {
    this.link = link;
    setText(text);
  }
}
