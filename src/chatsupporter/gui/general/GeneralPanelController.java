package chatsupporter.gui.general;

import chatsupporter.CallbackInterface;
import chatsupporter.gui.ControlWindowView;
import chatsupporter.network.CurlHandler;
import chatsupporter.network.youtube.YouTubeCommentHandler;

/**
 * 全体設定のパネルのコントローラー。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class GeneralPanelController {
  /** コメント処理の接続状態。. */
  public enum CommentHandlerStatus {
    WAITING_RESULT,
    DONE
  }

  private static final int MAX_FAIL_COUNT = 5;
  private static final int UPDATE_FRAME = 5;

  private GeneralPanelModel model;
  private GeneralPanelView view;
  private ControlWindowView windowView;
  private CommentHandlerStatus commentHandlerStatus = CommentHandlerStatus.DONE;
  private int failCount = 0;
  private int frameCount = 0;

  private YouTubeCommentHandler youtubeCommentHandler = new YouTubeCommentHandler();

  /** 全体設定のパネルのコントローラーを生成します。. */
  public GeneralPanelController(GeneralPanelModel model, GeneralPanelView view) {
    this.model = model;
    this.view = view;
  }

  /** メインウィンドウを設定します。. */
  public void setWindowView(ControlWindowView windowView) {
    this.windowView = windowView;
  }

  /** 決定ボタンを押したイベントです。. */
  public void onConfirmButtonClick() {
    if (model.getCurrentConnectionStatus() != GeneralPanelModel.ConnectionStatus.CONNECTED) {
      Boolean result = youtubeCommentHandler.connectTo(view.getUrlText(), new CallbackInterface() {
        @Override
        public void callback() {
          commentHandlerStatus = CommentHandlerStatus.DONE;
          if (youtubeCommentHandler.getResult() == CurlHandler.Result.SUCCESS) {
            frameCount = 0;
            model.setCurrentConnectionStatus(GeneralPanelModel.ConnectionStatus.CONNECTED);
          } else {
            model.setCurrentConnectionStatus(GeneralPanelModel.ConnectionStatus.FAILED);
          }
        }
      });
      if (!result) {
        model.setCurrentConnectionStatus(GeneralPanelModel.ConnectionStatus.NOT_RECOGNIZED_URL);
      } else {
        commentHandlerStatus = CommentHandlerStatus.WAITING_RESULT;
        model.setCurrentConnectionStatus(GeneralPanelModel.ConnectionStatus.CONNECTING);
        youtubeCommentHandler.startAsyncAction();
      }
    } else {
      model.setCurrentConnectionStatus(GeneralPanelModel.ConnectionStatus.NOT_YET_CONNECTED);
    }
  }

  /** 毎フレームの更新での行動です。. */
  public void onUpdate() {
    if (model.getCurrentConnectionStatus() != GeneralPanelModel.ConnectionStatus.CONNECTED) {
      return;
    }
    if (frameCount != 0) {
      --frameCount;
      return;
    }
    frameCount = UPDATE_FRAME;
    if (commentHandlerStatus != CommentHandlerStatus.DONE) {
      return;
    }
    if (youtubeCommentHandler.getResult() == CurlHandler.Result.SUCCESS) {
      failCount = 0;
      windowView.receiveComments(youtubeCommentHandler.getCommentResults());
    } else {
      ++failCount;
    }
    if (failCount < MAX_FAIL_COUNT) {
      youtubeCommentHandler.updateComment(new CallbackInterface() {
        @Override
        public void callback() {
          commentHandlerStatus = CommentHandlerStatus.DONE;
        }
      });
      commentHandlerStatus = CommentHandlerStatus.WAITING_RESULT;
      youtubeCommentHandler.startAsyncAction();
    } else {
      model.setCurrentConnectionStatus(GeneralPanelModel.ConnectionStatus.FAILED);
    }
  }

}
