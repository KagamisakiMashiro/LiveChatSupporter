package chatsupporter.gui.versionchecking;

/**
 * バージョン情報のパネルのモデル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class VersionCheckingPanelModel {
  /** アップデート確認の状態。. */
  public enum Status {
    FAILED,
    CONNECTING,
    DONE
  }

  private VersionCheckingPanelView view;
  private String newestVersion;
  private Status currentStatus = Status.CONNECTING;

  /** バージョン情報のパネルのモデルを作成します。. */
  public VersionCheckingPanelModel(VersionCheckingPanelView view) {
    this.view = view;
  }

  /** 最新バージョンを取得します。. */
  public String getNewestVersion() {
    return newestVersion;
  }

  /** 最新バージョンを設定します。. */
  public void setNewestVersion(String newestVersion) {
    this.newestVersion = newestVersion;
    view.update();
  }

  /** アップデート確認の状態を取得します。. */
  public Status getCurrentStatus() {
    return currentStatus;
  }

  /** アップデート確認の状態を設定します。. */
  public void setCurrentStatus(Status currentStatus) {
    this.currentStatus = currentStatus;
    view.update();
  }

}
