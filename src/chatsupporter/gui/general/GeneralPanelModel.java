package chatsupporter.gui.general;

import java.awt.Color;

/**
 * 全体設定のパネルのモデル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class GeneralPanelModel {
  /** 接続状態。. */
  public enum ConnectionStatus {
    NOT_YET_CONNECTED,
    CONNECTING,
    CONNECTED,
    FAILED,
    NOT_RECOGNIZED_URL
  }

  private static final Color DEFAULT_BACKGROUND_COLOR = Color.GREEN;

  private GeneralPanelView view;
  private ConnectionStatus currentConnectionStatus = ConnectionStatus.NOT_YET_CONNECTED;
  private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;

  /** 全体設定のパネルのモデルを作成します。. */
  public GeneralPanelModel(GeneralPanelView view) {
    this.view = view;
  }

  /** 現在の接続状態を取得します。. */
  public ConnectionStatus getCurrentConnectionStatus() {
    return currentConnectionStatus;
  }

  /** 現在の接続状態を取得します。. */
  public void setCurrentConnectionStatus(ConnectionStatus currentConnectionStatus) {
    this.currentConnectionStatus = currentConnectionStatus;
    view.update();
  }

  /** 背景色を取得します。. */
  public Color getBackgroundColor() {
    return backgroundColor;
  }

  /** 背景色を設定します。. */
  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
    view.update();
  }

}
