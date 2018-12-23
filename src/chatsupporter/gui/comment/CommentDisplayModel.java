package chatsupporter.gui.comment;

import chatsupporter.CommentModel;

/**
 * 表示用のコメントデータのモデル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommentDisplayModel extends CommentModel {
  private long timeDisplayed = 0;

  /** 空の表示用のコメントデータを作成します。. */
  public CommentDisplayModel() {
    super();
  }

  /** コメントモデルから表示用のコメントデータのモデルを作成します。. */
  public CommentDisplayModel(CommentModel comment) {
    super(comment.getAuthorName(), comment.getAuthorPhotoUrl(),
        comment.getAuthorChannelId(), comment.getMessage(), comment.getAuthorTypes());
  }

  /**
   * コメントデータのモデルを作成します。.
   * @param authorName 作者名
   * @param authorPhotoUrl 作者のアイコンのURL
   * @param authorChannelId 作者のチャンネルID
   * @param message コメント内容
   * @param authorTypes 作者の種類（nullでも可）
   */
  public CommentDisplayModel(String authorName, String authorPhotoUrl,
      String authorChannelId, String message, AuthorType[] authorTypes) {
    super(authorName, authorPhotoUrl, authorChannelId, message, authorTypes);
  }

  /** 表示した時間を取得します。. */
  public long getTimeDisplayed() {
    return timeDisplayed;
  }

  /** 表示した時間を更新します。. */
  public void addTimeDisplayed(long timeDelta) {
    this.timeDisplayed += timeDelta;
  }

}
