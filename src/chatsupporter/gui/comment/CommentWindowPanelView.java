package chatsupporter.gui.comment;

import chatsupporter.ApplicationUtil;
import chatsupporter.gui.CommonPanel;
import chatsupporter.gui.textdisplay.TextDisplayPanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * コメント画面のパネル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommentWindowPanelView extends CommonPanel {
  private static final int DEFAULT_GAP = 5;

  private CommentPanelModel model;
  private GridLayout gridLayout = new GridLayout(1, 0, DEFAULT_GAP, DEFAULT_GAP);
  private TextDisplayPanel[] textPanels;
  private int colNum = 0;
  private int commentNum = 0;

  /** コメント画面のパネルを作成します。. */
  public CommentWindowPanelView() {
    setOpaque(false);
    setBorder(new LineBorder(new Color(0, 0, 0, 0), DEFAULT_GAP));
    setLayout(gridLayout);
  }

  /** コメントモデルを設定します。. */
  public void setModel(CommentPanelModel model) {
    this.model = model;
  }

  @Override
  protected void updateByModel() {
    super.updateByModel();
    updateLayout(model.getColNum());
    updateCommentNum(model.getMaxComment());
    updateDisplayPanel(model.getDisplayComments(), model.getFontSize());
    revalidate();
    repaint();
  }

  @Override
  protected void updateByTick() {
    super.updateByTick();
    if (textPanels == null) {
      return;
    }
    for (TextDisplayPanel textPanel : textPanels) {
      textPanel.updateSize();
    }
  }

  /** コメントの配置を更新します。. */
  private void updateLayout(int colNum) {
    if (colNum == this.colNum) {
      return;
    }
    this.colNum = colNum;
    if (colNum != 0) {
      gridLayout.setColumns(colNum);
      gridLayout.setRows(0);
    } else {
      gridLayout.setRows(1);
      gridLayout.setColumns(0);
    }
  }

  /** 表示するっコメント数を更新します。. */
  private void updateCommentNum(int commentNum) {
    if (this.commentNum == commentNum) {
      return;
    }
    this.commentNum = commentNum;
    removeAll();
    textPanels = new TextDisplayPanel[commentNum];
    for (int i = 0; i < commentNum; ++i) {
      textPanels[i] = new TextDisplayPanel();
      add(textPanels[i]);
    }
  }

  /** テキスト表示パネルを更新します。. */
  private void updateDisplayPanel(LinkedList<CommentDisplayModel> comments, int fontSize) {
    int index = 0;
    if (comments != null) {
      for (CommentDisplayModel comment : comments) {
        textPanels[index].setVisible(true);
        textPanels[index].setText(comment.getAuthorName()
            + ApplicationUtil.getText("COMMENT_NAME_MESSAGE_SEPARATE") + comment.getMessage());
        textPanels[index].setAlignment(SwingConstants.LEFT);
        textPanels[index].setVerticalAlignment(SwingConstants.NORTH);
        textPanels[index].setFontSize(fontSize);
        ++index;
      }
    }
    for (int i = index; i < textPanels.length; ++i) {
      textPanels[i].setVisible(false);
    }
  }

}
