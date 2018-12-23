package chatsupporter.gui.questionnaire;

import chatsupporter.gui.CommonPanel;
import chatsupporter.gui.questionnaire.QuestionnairePanelModel.QuestionnaireStatus;
import chatsupporter.gui.textdisplay.TextDisplayPanel;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * アンケート画面のパネルビュー。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class QuestionnaireWindowPanelView extends CommonPanel {
  private static final int NICO_OPTION_NUM_FOR_COLUMN_2 = 4;
  private static final int BORDER_WIDTH = 5;

  private QuestionnairePanelModel model;
  private JPanel leftPanel;
  private JPanel rightPanel;
  private TextDisplayPanel[] displayTextPanels;
  private int currentDisplayTextPanelSize = 0;
  private int currentFontSize;
  private int lastRowNumOfPanel = 0;
  private Boolean isSpacingPanelExist = false;
  private QuestionnairePanelModel.DisplayMethod currentDisplayMethod;
  private QuestionnairePanelModel.QuestionnaireStatus currentStatus =
        QuestionnaireStatus.BEFORE_START;

  /** アンケート画面のパネルを作成します。. */
  public QuestionnaireWindowPanelView() {
    setOpaque(false);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    leftPanel = new JPanel();
    leftPanel.setOpaque(false);
    rightPanel = new JPanel();
    rightPanel.setOpaque(false);
  }

  /** アンケートモデルを設定します。. */
  public void setModel(QuestionnairePanelModel model) {
    this.model = model;
  }

  /** 選択肢を表示します。. */
  private void displayOptions(
      String[] optionTexts, QuestionnairePanelModel.DisplayMethod displayMethod, int fontSize) {
    displayTextPanels = new TextDisplayPanel[optionTexts.length];
    currentDisplayMethod = displayMethod;
    currentFontSize = fontSize;
    for (int i = 0; i < optionTexts.length; ++i) {
      displayTextPanels[i] = new TextDisplayPanel(getOptionAlphabet(i) + ". " + optionTexts[i]);
      displayTextPanels[i].setFontSize(currentFontSize);
    }
    updateDisplayMethod();
  }

  /** 得票率を設定します。. */
  private void setDisplayOptionRates(double[] optionRates) {
    for (int i = 0; i < displayTextPanels.length; ++i) {
      displayTextPanels[i].setAdditionalText(String.format("%.1f%%", optionRates[i] * 100));
    }
  }

  /** 表示をクリアします。. */
  private void clear() {
    displayTextPanels = null;
    updateDisplayMethod();
  }

  @Override
  protected void updateByModel() {
    super.updateByModel();
    setFontSize(model.getFontSize());
    setDisplayMethod(model.getDisplayMethod());
    setCurrentStatus(model.getCurrentStatus());
  }

  @Override
  protected void updateByTick() {
    super.updateByTick();
    if (displayTextPanels == null) {
      return;
    }
    if (isSpacingPanelExist
        && displayTextPanels.length > 0 && lastRowNumOfPanel > 0
        && displayTextPanels[0] != null) {
      if (currentDisplayTextPanelSize != displayTextPanels[0].getSize().width) {
        int sizeDivider = getLastRowSidePanelSizeDivider();
        currentDisplayTextPanelSize = displayTextPanels[0].getSize().width;
        leftPanel.setPreferredSize(
            new Dimension(currentDisplayTextPanelSize / sizeDivider, 0));
        rightPanel.setPreferredSize(
            new Dimension(currentDisplayTextPanelSize / sizeDivider, 0));
      }
    }
    for (int i = 0; i < displayTextPanels.length; ++i) {
      displayTextPanels[i].updateSize();
    }
  }

  /** フォントサイズを設定します。. */
  private void setFontSize(int fontSize) {
    if (fontSize == currentFontSize) {
      return;
    }
    currentFontSize = fontSize;
    if (displayTextPanels == null) {
      return;
    }
    for (TextDisplayPanel panel : displayTextPanels) {
      panel.setFontSize(currentFontSize);
    }
  }

  /** 表示方式を設定します。. */
  private void setDisplayMethod(QuestionnairePanelModel.DisplayMethod displayMethod) {
    if (currentDisplayMethod == displayMethod) {
      return;
    }
    currentDisplayMethod = displayMethod;
    updateDisplayMethod();
  }

  /** アンケート状態を設定して、画面を更新します。. */
  private void setCurrentStatus(QuestionnairePanelModel.QuestionnaireStatus status) {
    if (currentStatus == status) {
      return;
    }
    currentStatus = status;
    switch (currentStatus) {
      default:
      case BEFORE_START:
        clear();
        break;
      case STARTED:
        displayOptions(model.getOptionTexts(), model.getDisplayMethod(), model.getFontSize());
        break;
      case END:
        setDisplayOptionRates(model.getVoteRates());
        break;
    }
  }

  /** 表示方式を更新します。. */
  private void updateDisplayMethod() {
    isSpacingPanelExist = false;
    removeAll();
    add(createBorderComponent());
    if (displayTextPanels != null && displayTextPanels.length != 0) {
      currentDisplayTextPanelSize = 0;
      switch (currentDisplayMethod) {
        case NICO_DISPLAY:
          setNicoDisplay();
          break;
        default:
        case TWITTER_DISPLAY:
          setTwitterDisplay();
          break;
      }
    }
    revalidate();
    repaint();
  }

  /** ニコニコ式表示に設定します。. */
  private void setNicoDisplay() {
    int numOfCol = displayTextPanels.length <= NICO_OPTION_NUM_FOR_COLUMN_2 ? 2 : 3;
    int numOfRow = (int) Math.ceil((double)displayTextPanels.length / numOfCol);
    int currentIndex = 0;
    lastRowNumOfPanel = 0;
    for (int i = 0; i < numOfRow; ++i) {
      JPanel panel = new JPanel();
      panel.setOpaque(false);
      add(panel);
      panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
      // 最後の行ではない場合、普通に追加します。最後の行でも、残り数が一行の数と一致している場合も普通に追加します。
      if (i != numOfRow - 1 || displayTextPanels.length - currentIndex == numOfCol) {
        currentIndex = addItemToRowPanel(panel, numOfCol, currentIndex);
      } else {
        // 一致していない場合、サイズ調整用のパネルが必要です。
        isSpacingPanelExist = true;
        currentIndex = addItemWithSpacingToRowPanel(panel, numOfCol, numOfRow, currentIndex);
      }
      add(createBorderComponent());
    }
  }

  /** 行のパネルにアイテムを追加します。. */
  private int addItemToRowPanel(JPanel panel, int numOfCol, int currentIndex) {
    panel.add(createBorderComponent());
    for (int i = 0; i < numOfCol; ++i) {
      displayTextPanels[currentIndex].setAlignment(SwingConstants.CENTER);
      displayTextPanels[currentIndex].setAdditionalTextAlignment(SwingConstants.CENTER);
      panel.add(displayTextPanels[currentIndex]);
      panel.add(createBorderComponent());
      ++currentIndex;
    }
    return currentIndex;
  }

  /** 行のパネルパネルにアイテムと間隔調整を追加します。. */
  private int addItemWithSpacingToRowPanel(
      JPanel panel, int numOfCol, int numOfRow, int currentIndex) {
    panel.add(leftPanel);
    panel.add(createBorderComponent());
    for (int j = currentIndex; j < displayTextPanels.length; ++j) {
      displayTextPanels[currentIndex].setAlignment(SwingConstants.CENTER);
      displayTextPanels[currentIndex].setAdditionalTextAlignment(SwingConstants.CENTER);
      panel.add(displayTextPanels[currentIndex]);
      if (j != displayTextPanels.length - 1) {
        panel.add(createBorderComponent());
      }
      ++currentIndex;
      ++lastRowNumOfPanel;
    }
    panel.add(createBorderComponent());
    panel.add(rightPanel);
    return currentIndex;
  }

  /** ツイッター式表示に設定します。. */
  private void setTwitterDisplay() {
    for (int i = 0; i < displayTextPanels.length; ++i) {
      displayTextPanels[i].setAlignment(SwingConstants.LEFT);
      displayTextPanels[i].setAdditionalTextAlignment(SwingConstants.RIGHT);
      JPanel panel = new JPanel();
      panel.setOpaque(false);
      panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
      panel.add(createBorderComponent());
      panel.add(displayTextPanels[i]);
      panel.add(createBorderComponent());
      add(panel);
      add(createBorderComponent());
    }
  }

  /** 最後の行の間隔調整パネルのサイズ比率を取得します。. */
  private int getLastRowSidePanelSizeDivider() {
    // ニコニコ式表示の表示できる数が決まっています。増えたら修正する必要があります。
    if (displayTextPanels.length < 4) {
      return 2;
    }
    return lastRowNumOfPanel;
  }

  /** 選択肢のアルファベットを取得します。. */
  private String getOptionAlphabet(int index) {
    char alphabet = 'A';
    alphabet += index;
    return String.valueOf(alphabet);
  }

  /** ボーダーを作成します。. */
  private Component createBorderComponent() {
    return Box.createRigidArea(new Dimension(BORDER_WIDTH, BORDER_WIDTH));
  }
}
