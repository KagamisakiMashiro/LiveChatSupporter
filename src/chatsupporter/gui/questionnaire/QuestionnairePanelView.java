package chatsupporter.gui.questionnaire;

import chatsupporter.ApplicationUtil;
import chatsupporter.CommentModel;
import chatsupporter.gui.CommonPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.ini4j.Ini;

/**
 * アンケート設定用のパネル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class QuestionnairePanelView extends CommonPanel {
  /** 最大の選択肢数。. */
  public static final int MAX_CHOICE_NUM = 26;

  private QuestionnairePanelController controller;
  private QuestionnairePanelModel model;
  private JCheckBox enableMultiOptionCheckBox;
  private JComboBox<Integer> optionNumberComboBox;
  private JComboBox<Integer> fontSizeComboBox;
  private JTextField questionnaireTimeText;
  private JTextField[] optionTextFields = new JTextField[MAX_CHOICE_NUM];
  private final ButtonGroup displayMethodButtonGroup = new ButtonGroup();
  private JRadioButton nicoTypeRadioButton;
  private JRadioButton twitterTypeRadioButton;
  private JButton startQuestionnaireButton;
  private JButton endQuestionnaireButton;
  private JButton clearButton;
  private JTextField totalVoteText;
  private Boolean isUpdateTimer = false;
  private long questionnaireStartTime;

  /**
   * アンケート設定用のパネルを作成します。.
   * @param ini iniファイル（null可）
   */
  public QuestionnairePanelView(Ini ini) {
    this.model = new QuestionnairePanelModel(this, ini);
    this.controller = new QuestionnairePanelController(this.model, this);
    setLayout(new BorderLayout(0, 0));

    add(createWindowButtonPanel(), BorderLayout.NORTH);
    add(createOptionPanel(), BorderLayout.CENTER);
    add(createStartButtonPanel(), BorderLayout.SOUTH);
    update();
  }

  /** iniファイルに設定を保存します。. */
  public void save(Ini ini) {
    model.save(ini);
  }

  /** コメントを処理します。. */
  public void handleComments(LinkedList<CommentModel> comments) {
    controller.handleComments(comments);
  }

  /** 選択肢のテキストを取得します。. */
  public String[] getOptionTexts() {
    String[] optionTexts = new String[model.getOptionNum()];
    for (int i = 0; i < optionTexts.length; ++i) {
      optionTexts[i] = optionTextFields[i].getText();
    }
    return optionTexts;
  }

  /** アンケートウィンドウのビューの参照を設定します。. */
  public void setWindowFramePanel(JFrame windowFrame, QuestionnaireWindowPanelView windowView) {
    controller.setWindowFrame(windowFrame);
    model.setWindowView(windowView);
    windowView.setModel(model);
  }

  @Override
  protected void updateByModel() {
    super.updateByModel();
    enableMultiOptionCheckBox.setSelected(this.model.getIsMultipleChoice());
    optionNumberComboBox.setSelectedItem(this.model.getOptionNum());
    switch (this.model.getDisplayMethod()) {
      default:
      case NICO_DISPLAY:
        displayMethodButtonGroup.setSelected(nicoTypeRadioButton.getModel(), true);
        break;
      case TWITTER_DISPLAY:
        displayMethodButtonGroup.setSelected(twitterTypeRadioButton.getModel(), true);
        break;
    }
    updateEnableStatus();
    for (int i = 0; i < MAX_CHOICE_NUM; ++i) {
      optionTextFields[i].setEditable(
          this.model.getCurrentStatus() == QuestionnairePanelModel.QuestionnaireStatus.BEFORE_START
          && i < this.model.getOptionNum());
    }
    fontSizeComboBox.setSelectedItem(this.model.getFontSize());
    totalVoteText.setText(String.valueOf(this.model.getTotalVote()));
  }

  @Override
  protected void updateByTick() {
    super.updateByTick();
    if (isUpdateTimer) {
      long timePassed = System.nanoTime() - questionnaireStartTime;
      double timePassedInSecond = (double)timePassed / 1000000000;
      questionnaireTimeText.setText(String.format("%.2f", timePassedInSecond));
    }
    totalVoteText.setText(String.valueOf(this.model.getTotalVote()));
  }

  /** ウィンドウのボタンパネルを作成します。. */
  private JPanel createWindowButtonPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(0, 2));

    JButton questionnaireWindowButton =
        new JButton(ApplicationUtil.getText("DISPLAY_QUESTIONNARIE_WINDOW"));
    questionnaireWindowButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onQuestionnaireWindowButtonClick();
      }
    });
    buttonPanel.add(questionnaireWindowButton);

    clearButton = new JButton(ApplicationUtil.getText("CLEAR"));
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        questionnaireTimeText.setText("0.00");
        controller.onClearButtonClick();
      }
    });
    buttonPanel.add(clearButton);

    return buttonPanel;
  }

  /** 設定のパネルを作成します。. */
  private JPanel createOptionPanel() {
    JPanel optionPanel = new JPanel();
    optionPanel.setLayout(new BorderLayout(0, 0));
    optionPanel.add(createSettingPanel(), BorderLayout.NORTH);
    optionPanel.add(createOptionTextPane(), BorderLayout.CENTER);
    return optionPanel;
  }

  /** 設定パネルを作成します。. */
  private JPanel createSettingPanel() {
    JPanel settingPanel = new JPanel();
    settingPanel.setLayout(new GridLayout(0, 3));

    enableMultiOptionCheckBox = new JCheckBox(ApplicationUtil.getText("MULTIPLE_CHOICE"));
    settingPanel.add(enableMultiOptionCheckBox);

    JLabel optionNumberLabel = new JLabel(ApplicationUtil.getText("CHOICE_NUM"));
    optionNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    optionNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    settingPanel.add(optionNumberLabel);

    initOptionNumberComboBox();
    settingPanel.add(optionNumberComboBox);

    JLabel fontSizeLabel = new JLabel(ApplicationUtil.getText("FONT_SIZE"));
    fontSizeLabel.setHorizontalAlignment(SwingConstants.LEFT);
    settingPanel.add(fontSizeLabel);

    settingPanel.add(new JLabel());

    initFontTextComboBox();
    settingPanel.add(fontSizeComboBox);

    JLabel displayMethodLabel = new JLabel(ApplicationUtil.getText("DISPLAY_METHOD"));
    displayMethodLabel.setHorizontalAlignment(SwingConstants.LEFT);
    settingPanel.add(displayMethodLabel);

    initRadioButton();
    settingPanel.add(nicoTypeRadioButton);
    settingPanel.add(twitterTypeRadioButton);

    return settingPanel;
  }

  /** 選択肢数のコンボボックスを初期化します。. */
  private void initOptionNumberComboBox() {
    optionNumberComboBox = new JComboBox<Integer>();
    optionNumberComboBox.setEditable(true);
    optionNumberComboBox.setModel(createNumOptionComboBoxModel(MAX_CHOICE_NUM));
    optionNumberComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (optionNumberComboBox.getSelectedIndex() == -1) {
          controller.onOptionNumberComboBoxSelect(-1);
        } else {
          controller.onOptionNumberComboBoxSelect(
              optionNumberComboBox.getItemAt(optionNumberComboBox.getSelectedIndex()));
        }
      }
    });
  }

  /** フォントサイズのコンボボックスを初期化します。. */
  private void initFontTextComboBox() {
    fontSizeComboBox = new JComboBox<Integer>();
    fontSizeComboBox.setEditable(true);
    fontSizeComboBox.setModel(new DefaultComboBoxModel<Integer>(FONT_SIZE_CHOICES));
    fontSizeComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          controller.onFontSizeComboBoxChange((Integer)fontSizeComboBox.getSelectedItem());
        } catch (Exception exception) {
          controller.onFontSizeComboBoxChange(null);
          exception.printStackTrace();
        }
      }
    });
  }

  /** 表示方式のラジオボタンを初期化します。. */
  private void initRadioButton() {
    nicoTypeRadioButton = new JRadioButton(ApplicationUtil.getText("NICO_METHOD"));
    displayMethodButtonGroup.add(nicoTypeRadioButton);
    nicoTypeRadioButton.setSelected(true);
    nicoTypeRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onNicoTypeRadioButtonSelect();
      }
    });

    twitterTypeRadioButton = new JRadioButton(ApplicationUtil.getText("TWITTER_METHOD"));
    displayMethodButtonGroup.add(twitterTypeRadioButton);
    twitterTypeRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onTwitterTypeRadioButtonSelect();
      }
    });
  }

  /** アンケート開始終了ボタンのパネルを生成します。. */
  private JPanel createStartButtonPanel() {
    JPanel startButtonPannel = new JPanel();
    startButtonPannel.setLayout(new GridLayout(0, 3));

    startButtonPannel.add(new JLabel(ApplicationUtil.getText("TOTAL_VOTE")));
    startButtonPannel.add(new JLabel());

    totalVoteText = new JTextField("0");
    totalVoteText.setHorizontalAlignment(SwingConstants.TRAILING);
    totalVoteText.setEditable(false);
    startButtonPannel.add(totalVoteText);

    startQuestionnaireButton = new JButton(ApplicationUtil.getText("START_QUESTIONNAIRE"));
    startQuestionnaireButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isUpdateTimer = true;
        questionnaireStartTime = System.nanoTime();
        controller.onStartQuestionnaireButtonClick();
      }
    });
    startButtonPannel.add(startQuestionnaireButton);

    questionnaireTimeText = new JTextField("0.00");
    questionnaireTimeText.setHorizontalAlignment(SwingConstants.CENTER);
    questionnaireTimeText.setEditable(false);
    startButtonPannel.add(questionnaireTimeText);

    endQuestionnaireButton = new JButton(ApplicationUtil.getText("END_QUESTIONNAIRE"));
    endQuestionnaireButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isUpdateTimer = false;
        controller.onEndQuestionnaireButtonClick();
      }
    });
    startButtonPannel.add(endQuestionnaireButton);

    return startButtonPannel;
  }

  /** 選択肢数のコンボボックスモデルを生成します。. */
  private DefaultComboBoxModel<Integer> createNumOptionComboBoxModel(int num) {
    Integer[] intChoices = new Integer[num];
    for (int i = 1; i <= num; ++i) {
      intChoices[i - 1] = i;
    }
    return new DefaultComboBoxModel<Integer>(intChoices);
  }

  /** 選択肢のテキストのパネルを生成します。. */
  private JScrollPane createOptionTextPane() {
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    JPanel panel = new JPanel();
    scrollPane.setViewportView(panel);
    panel.setLayout(new GridLayout(0, 1));

    for (int i = 0; i < MAX_CHOICE_NUM; ++i) {
      optionTextFields[i] = new JTextField();
      panel.add(optionTextFields[i]);
    }

    return scrollPane;
  }

  /** 状態によってボタンなどの状態を更新します。. */
  private void updateEnableStatus() {
    switch (this.model.getCurrentStatus()) {
      default:
      case BEFORE_START:
        startQuestionnaireButton.setEnabled(true);
        endQuestionnaireButton.setEnabled(false);
        clearButton.setEnabled(false);
        enableMultiOptionCheckBox.setEnabled(true);
        optionNumberComboBox.setEnabled(true);
        break;
      case END:
        startQuestionnaireButton.setEnabled(false);
        endQuestionnaireButton.setEnabled(false);
        clearButton.setEnabled(true);
        enableMultiOptionCheckBox.setEnabled(false);
        optionNumberComboBox.setEnabled(false);
        break;
      case STARTED:
        startQuestionnaireButton.setEnabled(false);
        endQuestionnaireButton.setEnabled(true);
        clearButton.setEnabled(false);
        enableMultiOptionCheckBox.setEnabled(false);
        optionNumberComboBox.setEnabled(false);
        break;
    }
    if (this.model.getOptionNum() >= QuestionnairePanelModel.FORCE_TWITTER_OPTION_NUM) {
      nicoTypeRadioButton.setEnabled(false);
    } else {
      nicoTypeRadioButton.setEnabled(true);
    }
  }

}
