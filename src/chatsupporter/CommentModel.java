package chatsupporter;

/**
 * コメントデータのモデル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommentModel {
  /** コメント作者の種類。. */
  public enum AuthorType {
    MODERATOR,
    OWNER
  }

  private AuthorType[] authorTypes;
  private String authorName;
  private String authorPhotoUrl;
  private String authorChannelId;
  private String message;

  /** 空のコメントデータのモデルを作成します。. */
  public CommentModel() {
    this("", "", "", "", null);
  }

  /**
   * コメントデータのモデルを作成します。.
   * @param authorName 作者名
   * @param authorPhotoUrl 作者のアイコンのURL
   * @param authorChannelId 作者のチャンネルID
   * @param message コメント内容
   * @param authorTypes 作者の種類（null可）
   */
  public CommentModel(String authorName, String authorPhotoUrl,
      String authorChannelId, String message, AuthorType[] authorTypes) {
    this.authorName = authorName;
    this.authorPhotoUrl = authorPhotoUrl;
    this.authorChannelId = authorChannelId;
    this.message = message;
    this.authorTypes = authorTypes;
  }

  @Override
  public CommentModel clone() {
    CommentModel model = new CommentModel();
    if (this.authorTypes != null) {
      model.authorTypes = this.authorTypes.clone();
    }
    model.authorName = this.authorName;
    model.authorPhotoUrl = this.authorPhotoUrl;
    model.authorChannelId = this.authorChannelId;
    model.message = this.message;
    return model;
  }

  /**
   * 作者の種類を取得します。.
   * @return 種類の配列（特にない場合はnullが返します。）
   */
  public AuthorType[] getAuthorTypes() {
    return authorTypes;
  }

  /**
   * 作者の種類を設定します。.
   * @param authorTypes 作者の種類の配列（null可）
   */
  public void setAuthorTypes(AuthorType[] authorTypes) {
    this.authorTypes = authorTypes;
  }

  /** 作者の名前を取得します。. */
  public String getAuthorName() {
    return authorName;
  }

  /** 作者の名前を設定します。. */
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  /** 作者のアイコンのURLを取得します。. */
  public String getAuthorPhotoUrl() {
    return authorPhotoUrl;
  }

  /** 作者のアイコンのURLを設定します。. */
  public void setAuthorPhotoUrl(String authorPhotoUrl) {
    this.authorPhotoUrl = authorPhotoUrl;
  }

  /** 作者のチャンネルIDを取得します。. */
  public String getAuthorChannelId() {
    return authorChannelId;
  }

  /** 作者のチャンネルIDを設定します。. */
  public void setAuthorChannelId(String authorChannelId) {
    this.authorChannelId = authorChannelId;
  }

  /** コメント内容を取得します。. */
  public String getMessage() {
    return message;
  }

  /** コメント内容を設定します。. */
  public void setMessage(String message) {
    this.message = message;
  }

}
