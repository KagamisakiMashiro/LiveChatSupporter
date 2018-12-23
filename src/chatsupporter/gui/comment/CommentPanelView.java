package chatsupporter.gui.comment;

import chatsupporter.ApplicationUtil;
import chatsupporter.CommentModel;
import chatsupporter.gui.CommonPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.ini4j.Ini;

/**
 * 特定のコメントの表示を設定するパネル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommentPanelView extends CommonPanel {
  private CommentPanelController controller;
  private CommentPanelModel model;
  private JTextField displayNumText;
  private JTextField displayTimeText;
  private JTextField displayLayoutColText;
  private JComboBox<Integer> fontSizeComboBox;
  private JButton previewButton;
  private JCheckBox channelIdCheckBox;
  private JCheckBox userNameCheckBox;
  private JCheckBox moderatorCheckBox;
  private JTextArea channelIdTextArea;
  private JTextArea userNameTextArea;
  private JButton editButton;
  private JButton cancelButton;

  /**
   * 特定のコメントの表示を設定するパネルを生成します。.
   *
   * @param ini iniファイル（null可）
   */
  public CommentPanelView(Ini ini) {
    this.model = new CommentPanelModel(this, ini);
    this.controller = new CommentPanelController(this.model, this);
    setLayout(new BorderLayout(0, 0));
    add(createWindowButtonPanel(), BorderLayout.NORTH);
    add(createSettingPanel(), BorderLayout.CENTER);
    add(createEditButtonPanel(), BorderLayout.SOUTH);
    update();
  }

  /** iniファイルに設定を保存します。. */
  public void save(Ini ini) {
    model.save(ini);
  }

  /** 読み込んだコメントを処理します。. */
  public void handleComments(LinkedList<CommentModel> comments) {
    controller.handleComments(comments);
  }

  /** コメント表示数の設定を取得します。. */
  public String getDisplayNumText() {
    return displayNumText.getText();
  }

  /** コメント表示時間の設定を取得します。. */
  public String getDisplayTimeText() {
    return displayTimeText.getText();
  }

  /** コメント表示配置の列数の設定を取得します。. */
  public String getDisplayLayoutColText() {
    return displayLayoutColText.getText();
  }

  /** チャンネルIDを指定するかの設定を取得します。. */
  public Boolean getIsSpecificChannelId() {
    return channelIdCheckBox.isSelected();
  }

  /** ユーザー名を指定するかの設定を取得します。. */
  public Boolean getIsSpecificUserName() {
    return userNameCheckBox.isSelected();
  }

  /** モデレーターを指定するかの設定を取得します。. */
  public Boolean getIsShowModerator() {
    return moderatorCheckBox.isSelected();
  }

  /** 設定したチャンネルIDを取得します。. */
  public String[] getChannelIdTexts() {
    return channelIdTextArea.getText().split("\n");
  }

  /** 設定したユーザー名を取得します。. */
  public String[] getUserNameTexts() {
    return userNameTextArea.getText().split("\n");
  }

  /** コメントウィンドウのビューの参照を設定します。. */
  public void setWindowFramePanel(JFrame windowFrame, CommentWindowPanelView windowView) {
    controller.setWindowFrame(windowFrame);
    model.setWindowView(windowView);
    windowView.setModel(model);
  }

  @Override
  protected void updateByModel() {
    super.updateByModel();
    if (this.model.getIsPreviewMode()) {
      previewButton.setText(ApplicationUtil.getText("END_PREVIEW"));
    } else {
      previewButton.setText(ApplicationUtil.getText("PREVIEW"));
    }
    displayNumText.setText(String.valueOf(this.model.getMaxComment()));
    displayTimeText.setText(String.valueOf(this.model.getDisplayTime()));
    displayLayoutColText.setText(String.valueOf(this.model.getColNum()));
    fontSizeComboBox.setSelectedItem(this.model.getFontSize());
    channelIdCheckBox.setSelected(this.model.getIsSpecificChannel());;
    userNameCheckBox.setSelected(this.model.getIsSpecificUserName());;
    moderatorCheckBox.setSelected(this.model.getIsShowModerator());;
    if (this.model.getChannelIds() != null) {
      channelIdTextArea.setText(String.join("\n", this.model.getChannelIds()));
    } else {
      channelIdTextArea.setText("");
    }
    if (this.model.getUserNames() != null) {
      userNameTextArea.setText(String.join("\n", this.model.getUserNames()));
    } else {
      userNameTextArea.setText("");
    }
    updateSettingMode();
  }

  @Override
  protected void updateByTick() {
    super.updateByTick();
    controller.timePassed(getTimeDelta());
  }

  /** モデルの設定モードによっての表示更新します。. */
  private void updateSettingMode() {
    previewButton.setEnabled(!this.model.getIsSettingMode());
    displayNumText.setEnabled(this.model.getIsSettingMode());
    displayTimeText.setEnabled(this.model.getIsSettingMode());
    displayLayoutColText.setEnabled(this.model.getIsSettingMode());
    channelIdCheckBox.setEnabled(this.model.getIsSettingMode());
    userNameCheckBox.setEnabled(this.model.getIsSettingMode());
    moderatorCheckBox.setEnabled(this.model.getIsSettingMode());
    channelIdTextArea.setEditable(this.model.getIsSettingMode());
    userNameTextArea.setEditable(this.model.getIsSettingMode());
    if (this.model.getIsSettingMode()) {
      editButton.setText(ApplicationUtil.getText("CONFIRM"));
    } else {
      editButton.setText(ApplicationUtil.getText("EDIT"));
    }
    cancelButton.setEnabled(this.model.getIsSettingMode());
  }

  /** ウィンドウのボタンパネルを作成します。. */
  private JPanel createWindowButtonPanel() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(0, 2));

    JButton commentWindowButton = new JButton(ApplicationUtil.getText("DISPLAY_COMMENT_WINDOW"));
    commentWindowButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onCommentWindowButtonClick();
      }
    });
    buttonPanel.add(commentWindowButton);

    previewButton = new JButton(ApplicationUtil.getText("PREVIEW"));
    previewButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onPreviewButtonClick();
      }
    });
    buttonPanel.add(previewButton);

    return buttonPanel;
  }

  /** 設定パネルを作成します。. */
  private JPanel createSettingPanel() {
    JPanel settingPanel = new JPanel();
    settingPanel.setLayout(new BorderLayout(0, 0));

    settingPanel.add(createDisplayDetailPanel(), BorderLayout.NORTH);
    JTabbedPane listTabbedPane = new JTabbedPane(JTabbedPane.TOP);
    settingPanel.add(listTabbedPane, BorderLayout.CENTER);

    channelIdTextArea = new JTextArea();

    JScrollPane channelIdScrollPane = new JScrollPane(channelIdTextArea);
    listTabbedPane.addTab(ApplicationUtil.getText("CHANNEL_ID"), null, channelIdScrollPane, null);

    userNameTextArea = new JTextArea();

    JScrollPane userNameScrollPane = new JScrollPane(userNameTextArea);
    listTabbedPane.addTab(ApplicationUtil.getText("USER_NAME"), null, userNameScrollPane, null);
    return settingPanel;
  }

  /** 表示の詳細設定のパネルを作成します。. */
  private JPanel createDisplayDetailPanel() {
    JPanel displayDetailPanel = new JPanel();
    displayDetailPanel.setLayout(new GridLayout(0, 3));

    addDisplayNumSettingToPanel(displayDetailPanel);
    addDisplayTimeSettingToPanel(displayDetailPanel);
    addDisplayLayoutSettingToPanel(displayDetailPanel);
    addDisplayFontSizeSettingToPanel(displayDetailPanel);
    addDisplayTypeSettingToPanel(displayDetailPanel);
    return displayDetailPanel;
  }

  /** コメント表示数設定をパネルへ追加します。. */
  private void addDisplayNumSettingToPanel(JPanel panel) {
    JLabel displayNumLabel = new JLabel(ApplicationUtil.getText("COMMENT_DISPLAY_NUM"));
    displayNumLabel.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(displayNumLabel);

    panel.add(new JLabel());

    displayNumText = new JTextField();
    displayNumText.setHorizontalAlignment(SwingConstants.TRAILING);
    panel.add(displayNumText);
  }

  /** コメント表示時間設定をパネルへ追加します。. */
  private void addDisplayTimeSettingToPanel(JPanel panel) {
    JLabel displayTimeLabel = new JLabel(ApplicationUtil.getText("COMMENT_DISPLAY_TIME"));
    displayTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(displayTimeLabel);

    panel.add(new JLabel());

    displayTimeText = new JTextField();
    displayTimeText.setHorizontalAlignment(SwingConstants.TRAILING);
    panel.add(displayTimeText);
  }

  /** コメント配置設定をパネルへ追加します。. */
  private void addDisplayLayoutSettingToPanel(JPanel panel) {
    JLabel displayLayoutLabel = new JLabel(ApplicationUtil.getText("COMMENT_DISPLAY_LAYOUT"));
    displayLayoutLabel.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(displayLayoutLabel);

    panel.add(new JLabel());

    displayLayoutColText = new JTextField();
    displayLayoutColText.setHorizontalAlignment(SwingConstants.TRAILING);
    panel.add(displayLayoutColText);
  }

  /** フォントサイズ設定をパネルへ追加します。. */
  private void addDisplayFontSizeSettingToPanel(JPanel panel) {
    JLabel fontSizeLabel = new JLabel(ApplicationUtil.getText("FONT_SIZE"));
    fontSizeLabel.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(fontSizeLabel);

    panel.add(new JLabel());

    initFontTextComboBox();
    panel.add(fontSizeComboBox);
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

  /** 表示の指定設定をパネルへ追加します。. */
  private void addDisplayTypeSettingToPanel(JPanel panel) {
    channelIdCheckBox = new JCheckBox(ApplicationUtil.getText("CHANNEL_ID"));
    panel.add(channelIdCheckBox);

    userNameCheckBox = new JCheckBox(ApplicationUtil.getText("USER_NAME"));
    panel.add(userNameCheckBox);

    moderatorCheckBox = new JCheckBox(ApplicationUtil.getText("MODERATOR"));
    panel.add(moderatorCheckBox);
  }

  /** 編集ボタンのパネルを作成します。. */
  private JPanel createEditButtonPanel() {
    JPanel editButtonPanel = new JPanel();
    editButtonPanel.setLayout(new GridLayout(0, 3));

    editButton = new JButton(ApplicationUtil.getText("EDIT"));
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onEditButtonClick();
      }
    });
    editButtonPanel.add(editButton);

    editButtonPanel.add(new JLabel());

    cancelButton = new JButton(ApplicationUtil.getText("CANCEL"));
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onCancelButtonClick();
      }
    });
    editButtonPanel.add(cancelButton);

    return editButtonPanel;
  }

}
