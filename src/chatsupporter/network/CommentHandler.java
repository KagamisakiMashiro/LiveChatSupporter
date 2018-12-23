package chatsupporter.network;

import chatsupporter.CallbackInterface;
import chatsupporter.CommentModel;

import java.util.LinkedList;

/**
 * コメント処理器。.
 *
 * <p>全部の処理が非同期処理となっております。<br/>
 * 使用する側はconnectTo()とupdateComment()を呼んだ後、startAsyncAction()を呼ぶ必要があります。
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommentHandler extends CurlHandler {

  /** 行動の列挙型。. */
  protected enum Action {
    INIT,
    UPDATE_COMMENT
  }

  /** 現在の行動。. */
  protected Action currentAction;

  /** 取得したコメント。. */
  protected LinkedList<CommentModel> commentResults = new LinkedList<CommentModel>();

  /** 取得したコメントを取得します。. */
  public LinkedList<CommentModel> getCommentResults() {
    return commentResults;
  }

  /**
   * 配信のURLに接続します。.
   *
   * <p>呼び出した後、startAsyncAction()を呼ばないと、処理はしません。<br/>
   * falseが返したら、初期処理が失敗していたので、startAsyncAction()を呼ばないでください。<br/>
   * 初期処理の部分が継承先で実装する必要があります。<br/>
   * 継承したらsuper.connectTo()を呼び出す必要があります。
   *
   * @param url 配信のURL
   * @param callback コールバック関数
   * @return 成功したか
   */
  public Boolean connectTo(String url, CallbackInterface callback) {
    this.currentAction = Action.INIT;
    setCallback(callback);
    return false;
  }

  /**
   * コメントを更新します。.
   *
   * <p>呼び出した後、startAsyncAction()を呼ばないと、処理はしません。
   *
   * @param callback コールバック関数
   */
  public void updateComment(CallbackInterface callback) {
    setCallback(callback);
    this.currentAction = Action.UPDATE_COMMENT;
  }

  /** 非同期処理をハジメます。. */
  public void startAsyncAction() {
    Thread thread = new Thread(this);
    thread.start();
  }

  @Override
  protected void runAction() {
    switch (currentAction) {
      case INIT:
        init();
        break;
      case UPDATE_COMMENT:
        getLiveChat();
        break;
      default:
        break;
    }
  }

  /**
   * connectTo()が呼ばれたら、これが呼ばれて初期化します。.
   *
   * <p>継承先で実装してください。
   */
  protected void init() {
  }

  /**
   * updateComment()が呼ばれたら、これが呼ばれて初期化します。.
   *
   * <p>継承先で実装してください。
   */
  protected void getLiveChat() {
  }

}
