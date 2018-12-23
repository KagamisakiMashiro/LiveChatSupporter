package chatsupporter.gui.comment;

import chatsupporter.CommentModel;

import java.util.LinkedList;

import javax.swing.JFrame;

/**
 * コメント機能のコントローラー。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommentPanelController {
  private CommentPanelModel model;
  private CommentPanelView view;
  private JFrame windowFrame;

  /** コメント機能のコントローラーを作成します。。. */
  public CommentPanelController(CommentPanelModel model, CommentPanelView view) {
    this.model = model;
    this.view = view;
  }

  /** コメントを処理します。. */
  public void handleComments(LinkedList<CommentModel> comments) {
    for (CommentModel comment : comments) {
      Boolean addComment = false;
      if (model.getIsSpecificUserName() && model.getUserNames() != null) {
        for (String name : model.getUserNames()) {
          if (name.equals(comment.getAuthorName())) {
            addComment = true;
            break;
          }
        }
      }
      if (!addComment && model.getIsSpecificChannel() && model.getChannelIds() != null) {
        for (String channelId : model.getChannelIds()) {
          if (channelId.equals(comment.getAuthorChannelId())) {
            addComment = true;
            break;
          }
        }
      }
      if (!addComment && model.getIsShowModerator() && comment.getAuthorTypes() != null) {
        for (CommentModel.AuthorType authorType : comment.getAuthorTypes()) {
          if (authorType == CommentModel.AuthorType.MODERATOR
              || authorType == CommentModel.AuthorType.OWNER) {
            addComment = true;
            break;
          }
        }
      }
      if (addComment) {
        CommentDisplayModel commentDisplay = new CommentDisplayModel(comment.clone());
        model.addComment(commentDisplay);
      }
    }
  }

  /** コメントウィンドウを設定します。. */
  public void setWindowFrame(JFrame windowFrame) {
    this.windowFrame = windowFrame;
  }

  /** コメントウィンドウ表示のボタン押したイベントです。. */
  public void onCommentWindowButtonClick() {
    windowFrame.setVisible(!windowFrame.isVisible());
  }

  /** プレビュー表示のボタン押したイベントです。. */
  public void onPreviewButtonClick() {
    model.setIsPreviewMode(!model.getIsPreviewMode());
  }

  /** 設定ボタン押したイベントです。. */
  public void onEditButtonClick() {
    // 設定と保存ボタンは共通です。
    if (!model.getIsSettingMode()) {
      startEdit();
    } else {
      finishEditing();
    }
  }

  /** キャンセルボタンを押したイベントです。. */
  public void onCancelButtonClick() {
    model.setIsSettingMode(false);
  }

  /**
   * フォントサイズのコンボボックスが変更したイベントです。.
   * @param value 変更した値（null可）
   */
  public void onFontSizeComboBoxChange(Integer value) {
    if (value == null || value == 0) {
      model.setFontSize(model.getFontSize());
    } else {
      model.setFontSize(value);
    }
  }

  /** 時間経過を設定します。. */
  public void timePassed(long timeDelta) {
    model.addTimePassed(timeDelta);
  }

  /** 編集開始します。. */
  private void startEdit() {
    model.setIsSettingMode(true);
  }

  /** 編集終了します。. */
  private void finishEditing() {
    final String displayNumText = view.getDisplayNumText();
    final String displayTimeText = view.getDisplayTimeText();
    final String displayLayoutColText = view.getDisplayLayoutColText();
    final Boolean isSpecificChannel = view.getIsSpecificChannelId();
    final Boolean isSpecificUserName = view.getIsSpecificUserName();
    final Boolean isShowModerator = view.getIsShowModerator();
    final String[] channelIdTexts = view.getChannelIdTexts();
    final String[] userNameTexts = view.getUserNameTexts();
    model.setMaxComment(getNumericValue(displayNumText, model.getMaxComment()));
    model.setDisplayTime(getNumericValue(displayTimeText, model.getDisplayTime()));
    model.setColNum(getNumericValue(displayLayoutColText, model.getColNum()));
    model.setIsSpecificChannel(isSpecificChannel);
    model.setIsSpecificUserName(isSpecificUserName);
    model.setIsShowModerator(isShowModerator);
    model.setChannelIds(channelIdTexts);
    model.setUserNames(userNameTexts);
    model.setIsSettingMode(false);
  }

  /**
   * 文字列を数字に変更します。.
   *
   * <p>変更できない場合、origValueを返します。
   */
  private int getNumericValue(String text, int origValue) {
    try {
      return Integer.parseInt(text);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return origValue;
    }
  }

}
