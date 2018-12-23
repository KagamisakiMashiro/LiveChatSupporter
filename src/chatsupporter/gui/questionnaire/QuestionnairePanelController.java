package chatsupporter.gui.questionnaire;

import java.util.LinkedList;

import javax.swing.JFrame;

import chatsupporter.CommentModel;

/**
 * アンケート設定パネルのコントローラー。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class QuestionnairePanelController {
  private QuestionnairePanelModel model;
  private QuestionnairePanelView view;
  private JFrame windowFrame;

  /** アンケート設定のパネルを作成します。. */
  public QuestionnairePanelController(QuestionnairePanelModel model, QuestionnairePanelView view) {
    this.model = model;
    this.view = view;
  }

  /** コメントを処理します。. */
  public void handleComments(LinkedList<CommentModel> comments) {
    if (model.getCurrentStatus() != QuestionnairePanelModel.QuestionnaireStatus.STARTED) {
      return;
    }
    for (CommentModel comment : comments) {
      LinkedList<Integer> votedOptions = new LinkedList<Integer>();
      for (int i = 0; i < model.getOptionNum(); ++i) {
        if (comment.getMessage().indexOf('a' + i) != -1
            || comment.getMessage().indexOf('A' + i) != -1
            || comment.getMessage().indexOf('ａ' + i) != -1
            || comment.getMessage().indexOf('Ａ' + i) != -1) {
          votedOptions.add(i);
        }
      }
      if (!votedOptions.isEmpty()) {
        int[] votedOptionsArray = new int[votedOptions.size()];
        int index = 0;
        for (Integer votedOption : votedOptions) {
          votedOptionsArray[index] = votedOption;
          ++index;
        }
        model.addVote(comment.getAuthorChannelId(), votedOptionsArray);
      }
    }
  }

  /** アンケートウィンドウを設定します。. */
  public void setWindowFrame(JFrame windowFrame) {
    this.windowFrame = windowFrame;
  }

  /** アンケートウィンドウ表示ボタンを押したイベントです。. */
  public void onQuestionnaireWindowButtonClick() {
    windowFrame.setVisible(!windowFrame.isVisible());
  }

  /** クリアボタンを押したイベントです。. */
  public void onClearButtonClick() {
    model.setCurrentStatus(QuestionnairePanelModel.QuestionnaireStatus.BEFORE_START);
    model.resetVote();
  }

  /** 最大の選択肢数の設定。. */
  public void onOptionNumberComboBoxSelect(int optionNum) {
    if (optionNum == -1) {
      model.setOptionNum(model.getOptionNum());
    } else {
      model.setOptionNum(optionNum);
    }
    if (optionNum >= QuestionnairePanelModel.FORCE_TWITTER_OPTION_NUM) {
      model.setDisplayMethod(QuestionnairePanelModel.DisplayMethod.TWITTER_DISPLAY);
    }
  }

  /** ニコニコ式表示を選択したイベントです。. */
  public void onNicoTypeRadioButtonSelect() {
    model.setDisplayMethod(QuestionnairePanelModel.DisplayMethod.NICO_DISPLAY);
  }

  /** ツイッター式表示を選択したイベントです。. */
  public void onTwitterTypeRadioButtonSelect() {
    model.setDisplayMethod(QuestionnairePanelModel.DisplayMethod.TWITTER_DISPLAY);
  }

  /** アンケート開始ボタンを押したイベントです。. */
  public void onStartQuestionnaireButtonClick() {
    model.setOptionTexts(view.getOptionTexts());
    model.resetVote();
    model.setCurrentStatus(QuestionnairePanelModel.QuestionnaireStatus.STARTED);
  }

  /** アンケート終了ボタンを押したイベントです。. */
  public void onEndQuestionnaireButtonClick() {
    model.setCurrentStatus(QuestionnairePanelModel.QuestionnaireStatus.END);
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

}
