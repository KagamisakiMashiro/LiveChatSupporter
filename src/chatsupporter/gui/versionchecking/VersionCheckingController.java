package chatsupporter.gui.versionchecking;

import chatsupporter.ApplicationUtil;
import chatsupporter.CallbackInterface;
import chatsupporter.network.CurlHandler;

/**
 * バージョン確認のパネルのコントローラー。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class VersionCheckingController {
  private VersionCheckingPanelModel model;

  private CurlHandler curlHandler = new CurlHandler();

  /**
   * バージョン確認のパネルのコントローラーを作成します。.
   * @param model モデル
   */
  public VersionCheckingController(VersionCheckingPanelModel model) {
    this.model = model;
    checkVersion();
  }

  /**
   * バージョン確認します。.
   *
   * <p>完了した場合は何もしません。
   */
  public void checkVersion() {
    if (model.getCurrentStatus() == VersionCheckingPanelModel.Status.DONE) {
      return;
    }
    model.setCurrentStatus(VersionCheckingPanelModel.Status.CONNECTING);
    curlHandler.asyncHttpRequest(
        ApplicationUtil.getText("VERSION_CHECK_URL"), "GET", new CallbackInterface() {
          @Override
          public void callback() {
            if (curlHandler.getResult() == CurlHandler.Result.FAILED) {
              model.setCurrentStatus(VersionCheckingPanelModel.Status.FAILED);
              return;
            }
            if (curlHandler.getHttpRequestResult() == null
                || curlHandler.getHttpRequestResult().isEmpty()) {
              model.setCurrentStatus(VersionCheckingPanelModel.Status.FAILED);
              return;
            }
            model.setNewestVersion(curlHandler.getHttpRequestResult());
            model.setCurrentStatus(VersionCheckingPanelModel.Status.DONE);
          }
        }
    );
  }

}
