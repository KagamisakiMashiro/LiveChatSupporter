package chatsupporter.gui.general;

import chatsupporter.ApplicationUtil;
import chatsupporter.gui.CommonPanel;
import chatsupporter.gui.ControlWindowView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 全体設定用のパネルビュー。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class GeneralPanelView extends CommonPanel {
  private GeneralPanelController controller;
  private GeneralPanelModel model;
  private JTextField liveUrlTextField;
  private JTextField liveStatusText;
  private JButton confirmUrlButton;

  /** 全体設定用のパネルを作成します。. */
  public GeneralPanelView() {
    super();
    this.model = new GeneralPanelModel(this);
    this.controller = new GeneralPanelController(this.model, this);

    setLayout(new BorderLayout(0, 0));

    JPanel liveSettingPanel = new JPanel();
    add(liveSettingPanel, BorderLayout.NORTH);
    liveSettingPanel.setLayout(new BoxLayout(liveSettingPanel, BoxLayout.Y_AXIS));

    liveSettingPanel.add(createLiveUrlPanel());
    liveSettingPanel.add(createLiveStatuPanel());
    update();
  }

  /** メインウィンドウを設定します。. */
  public void setControlWindow(ControlWindowView window) {
    controller.setWindowView(window);
  }

  /** 入力したURLを取得します。. */
  public String getUrlText() {
    return liveUrlTextField.getText();
  }

  /** モデルを取得します。. */
  public GeneralPanelModel getModel() {
    return model;
  }

  @Override
  protected void updateByModel() {
    super.updateByModel();
    switch (this.model.getCurrentConnectionStatus()) {
      case CONNECTED:
        liveUrlTextField.setEnabled(false);
        liveStatusText.setText(ApplicationUtil.getText("CONNECTED"));
        confirmUrlButton.setText(ApplicationUtil.getText("END"));
        confirmUrlButton.setEnabled(true);
        break;
      case CONNECTING:
        liveUrlTextField.setEnabled(false);
        liveStatusText.setText(ApplicationUtil.getText("CONNECTING"));
        confirmUrlButton.setText(ApplicationUtil.getText("CONFIRM"));
        confirmUrlButton.setEnabled(false);
        break;
      default:
      case NOT_YET_CONNECTED:
        liveUrlTextField.setEnabled(true);
        liveStatusText.setText(ApplicationUtil.getText("NOT_YET_CONNECTED"));
        confirmUrlButton.setText(ApplicationUtil.getText("CONFIRM"));
        confirmUrlButton.setEnabled(true);
        break;
      case FAILED:
        liveUrlTextField.setEnabled(true);
        liveStatusText.setText(ApplicationUtil.getText("FAILED"));
        confirmUrlButton.setText(ApplicationUtil.getText("CONFIRM"));
        confirmUrlButton.setEnabled(true);
        break;
      case NOT_RECOGNIZED_URL:
        liveUrlTextField.setEnabled(true);
        liveStatusText.setText(ApplicationUtil.getText("NOT_RECOGNIZED_URL"));
        confirmUrlButton.setText(ApplicationUtil.getText("CONFIRM"));
        confirmUrlButton.setEnabled(true);
        break;
    }
  }

  @Override
  public void updateByTick() {
    controller.onUpdate();
  }

  /** URL入力パネルを作成します。. */
  private JPanel createLiveUrlPanel() {
    JPanel liveUrlPanel = new JPanel();
    liveUrlPanel.setLayout(new BoxLayout(liveUrlPanel, BoxLayout.X_AXIS));

    JLabel liveUrlTextLabel = new JLabel(ApplicationUtil.getText("LIVE_URL"));
    liveUrlPanel.add(liveUrlTextLabel);

    liveUrlTextField = new JTextField();
    liveUrlPanel.add(liveUrlTextField);

    confirmUrlButton = new JButton(ApplicationUtil.getText("CONFIRM"));
    confirmUrlButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.onConfirmButtonClick();
      }
    });
    liveUrlPanel.add(confirmUrlButton);

    return liveUrlPanel;
  }

  /** 接続状態表示のパネルを作成します。. */
  private JPanel createLiveStatuPanel() {
    JPanel liveStatusPanel = new JPanel();
    liveStatusPanel.setLayout(new BoxLayout(liveStatusPanel, BoxLayout.X_AXIS));

    liveStatusText = new JTextField();
    liveStatusText.setText(ApplicationUtil.getText("NOT_YET_CONNECTED"));
    liveStatusText.setEnabled(false);
    liveStatusText.setEditable(false);
    liveStatusPanel.add(liveStatusText);

    return liveStatusPanel;
  }
}
