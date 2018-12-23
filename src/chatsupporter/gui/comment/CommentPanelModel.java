package chatsupporter.gui.comment;

import chatsupporter.ApplicationUtil;

import java.util.LinkedList;

import org.ini4j.Ini;

/**
 * コメントの設定、表示のモデル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommentPanelModel {
  private static final String COMMENT_SECTION = "COMMENT";
  private static final String COMMENT_NUM_KEY = "COMMENT_NUM";
  private static final String DISPLAY_TIME_KEY = "DISPLAY_TIME";
  private static final String COL_NUM_KEY = "COL_NUM";
  private static final String FONT_SIZE_KEY = "FONT_SIZE";
  private static final String SPECIFIC_CHANNEL_KEY = "SPECIFIC_CHANNEL";
  private static final String SPECIFIC_NAME_KEY = "SPECIFIC_NAME";
  private static final String SHOW_MODERATOR_KEY = "SHOW_MODERATOR";
  private static final String CHANNEL_IDS_KEY = "CHANNEL_IDS";
  private static final String USER_NAMES_KEY = "USER_NAMES";

  private static final int DEFAULT_MAX_COMMENT = 5;
  private static final int DEFAULT_DISPLAY_TIME = 30;
  private static final int DEFAULT_COL = 0;
  private static final CommentDisplayModel PREVIEW_MODEL = new CommentDisplayModel(
      ApplicationUtil.getText("PREVIEW_COMMENT_NAME"), "", "",
      ApplicationUtil.getText("PREVIEW_COMMENT_MESSAGE"), null);
  private static final int DEFAULT_FONT_SIZE = 16;

  private CommentPanelView view;
  private CommentWindowPanelView windowView;
  private Boolean isPreviewMode = false;
  private int maxComment = DEFAULT_MAX_COMMENT;
  private int displayTime = DEFAULT_DISPLAY_TIME;
  private int colNum = DEFAULT_COL;
  private int fontSize = DEFAULT_FONT_SIZE;
  private Boolean isSpecificChannel = false;
  private Boolean isSpecificUserName = false;
  private Boolean isShowModerator = true;
  private String[] channelIds = null;
  private String[] userNames = null;
  private Boolean isSettingMode = false;
  private LinkedList<CommentDisplayModel> comments = new LinkedList<CommentDisplayModel>();

  /**
   * コメントの設定、表示のモデルを作成します。.
   *
   * @param view コメント設定のパネルのビュー
   * @param ini iniファイル（null可）
   */
  public CommentPanelModel(CommentPanelView view, Ini ini) {
    this.view = view;
    if (ini != null) {
      load(ini);
    }
  }

  /** iniファイルから設定を読み込みます。. */
  public void load(Ini ini) {
    Ini.Section section = ini.get(COMMENT_SECTION);
    loadDisplaySetting(section);
    loadCheckboxSetting(section);
    loadTextAreaSetting(section);
  }

  /** iniファイルに設定を保存します。. */
  public void save(Ini ini) {
    Ini.Section section = ini.get(COMMENT_SECTION);
    if (section == null) {
      section = ini.add(COMMENT_SECTION);
    }
    section.put(COMMENT_NUM_KEY, maxComment);
    section.put(DISPLAY_TIME_KEY, displayTime);
    section.put(COL_NUM_KEY, colNum);
    section.put(FONT_SIZE_KEY, fontSize);
    section.put(SPECIFIC_CHANNEL_KEY, isSpecificChannel);
    section.put(SPECIFIC_NAME_KEY, isSpecificUserName);
    section.put(SHOW_MODERATOR_KEY, isShowModerator);
    section.putAll(CHANNEL_IDS_KEY, channelIds);
    section.putAll(USER_NAMES_KEY, userNames);
  }

  /** コメント画面のパネルビューを設定します。. */
  public void setWindowView(CommentWindowPanelView windowView) {
    this.windowView = windowView;
  }

  /** プレビューモードかを取得します。. */
  public Boolean getIsPreviewMode() {
    return isPreviewMode;
  }

  /** プレビューモードを設定します。. */
  public void setIsPreviewMode(Boolean isPreviewMode) {
    this.isPreviewMode = isPreviewMode;
    view.update();
    if (this.windowView != null) {
      this.windowView.update();
    }
  }

  /** 表示コメント数を取得します。. */
  public int getMaxComment() {
    return maxComment;
  }

  /** 表示コメント数を設定します。. */
  public void setMaxComment(int maxComment) {
    this.maxComment = maxComment;
    view.update();
  }

  /**
   * 表示時間を取得します。.
   * return 表示時間
   *     0は無制限
  */
  public int getDisplayTime() {
    return displayTime;
  }

  /**
   * 表示時間を設定します。.
   *
   * @param displayTime 表示時間
   *     0は無制限
  */
  public void setDisplayTime(int displayTime) {
    this.displayTime = displayTime;
    view.update();
  }

  /**
   * 配置の列数を取得します。.
   * @return 列数
   *     0は無限
   */
  public int getColNum() {
    return colNum;
  }

  /**
   * 配置の列数を設定します。.
   * @param colNum 列数
   *     0は無限
   */
  public void setColNum(int colNum) {
    this.colNum = colNum;
    view.update();
    if (windowView != null) {
      windowView.update();
    }
  }

  /** フォントサイズを取得します。. */
  public int getFontSize() {
    return fontSize;
  }

  /** フォントサイズを設定します。. */
  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
    view.update();
    if (windowView != null) {
      windowView.update();
    }
  }

  /** チャンネル指定を取得します。. */
  public Boolean getIsSpecificChannel() {
    return isSpecificChannel;
  }

  /** チャンネル指定を設定します。. */
  public void setIsSpecificChannel(Boolean isSpecificChannel) {
    this.isSpecificChannel = isSpecificChannel;
    view.update();
  }

  /** ユーザー名指定を取得します。. */
  public Boolean getIsSpecificUserName() {
    return isSpecificUserName;
  }

  /** ユーザー名指定を設定します。. */
  public void setIsSpecificUserName(Boolean isSpecificUserName) {
    this.isSpecificUserName = isSpecificUserName;
    view.update();
  }

  /** モデレーター指定を取得します。. */
  public Boolean getIsShowModerator() {
    return isShowModerator;
  }

  /** モデレーター指定を設定します。. */
  public void setIsShowModerator(Boolean isShowModerator) {
    this.isShowModerator = isShowModerator;
    view.update();
  }

  /**
   * 指定のチャンネルIDを取得します。.
   * return チャンネルID
   *     nullの可能性があります。
   */
  public String[] getChannelIds() {
    return channelIds;
  }

  /**
   * 指定のチャンネルIDを設定します。.
   * @param channelIds チャンネルID（null可）
   */
  public void setChannelIds(String[] channelIds) {
    this.channelIds = channelIds;
    view.update();
  }

  /**
   * 指定のユーザー名を取得します。.
   * return ユーザー名
   *     nullの可能性があります。
   */
  public String[] getUserNames() {
    return userNames;
  }

  /**
   * 指定のユーザー名を設定します。.
   * @param userNames ユーザー名（null可）
   */
  public void setUserNames(String[] userNames) {
    this.userNames = userNames;
    view.update();
  }

  /** 編集モードかを取得します。. */
  public Boolean getIsSettingMode() {
    return isSettingMode;
  }

  /** 編集モードかを設定します。. */
  public void setIsSettingMode(Boolean isSettingMode) {
    this.isSettingMode = isSettingMode;
    view.update();
  }

  /**
   * コメントを追加します。.
   *
   * <p>コメント表示数を超えたら、一番古いコメントを削除します。
   *
   * @param comment コメントデータ
   */
  public void addComment(CommentDisplayModel comment) {
    comments.add(comment);
    if (comments.size() > maxComment) {
      comments.removeFirst();
    }
    if (windowView != null) {
      windowView.update();
    }
  }

  /**
   * 表示用のコメントモデルを取得します。.
   *
   * <p>プレビュー時はプレビュー用のコメントを返します。
   *
   * @return 表示用のコメントモデル
   */
  public LinkedList<CommentDisplayModel> getDisplayComments() {
    if (isPreviewMode) {
      LinkedList<CommentDisplayModel> previewComments = new LinkedList<CommentDisplayModel>();
      for (int i = 0; i < maxComment; ++i) {
        previewComments.add(PREVIEW_MODEL);
      }
      return previewComments;
    }
    return comments;
  }

  /**
   * 時間経過を更新します。.
   *
   * <p>時間経過が表示時間を超えたコメントデータもついでに削除します。
   *
   * @param timeDelta 時間経過
   */
  public void addTimePassed(long timeDelta) {
    for (CommentDisplayModel comment : comments) {
      comment.addTimeDisplayed(timeDelta);
    }
    if (displayTime == 0) {
      return;
    }
    if (comments.removeIf(comment ->
        comment.getTimeDisplayed() >= ApplicationUtil.secToCommonScale(displayTime))) {
      if (windowView != null) {
        windowView.update();
      }
    }
  }

  /** 表示設定を読み込みます。. */
  private void loadDisplaySetting(Ini.Section section) {
    try {
      String commentNumText = section.get(COMMENT_NUM_KEY);
      maxComment = Integer.parseInt(commentNumText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String displayTimeText = section.get(DISPLAY_TIME_KEY);
      displayTime = Integer.parseInt(displayTimeText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String colNumText = section.get(COL_NUM_KEY);
      colNum = Integer.parseInt(colNumText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String fontSizeText = section.get(FONT_SIZE_KEY);
      fontSize = Integer.parseInt(fontSizeText);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 指定の設定を読み込みます。. */
  private void loadCheckboxSetting(Ini.Section section) {
    try {
      String specificChannelText = section.get(SPECIFIC_CHANNEL_KEY);
      isSpecificChannel = Boolean.parseBoolean(specificChannelText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String specificNameText = section.get(SPECIFIC_NAME_KEY);
      isSpecificUserName = Boolean.parseBoolean(specificNameText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String showModeratorText = section.get(SHOW_MODERATOR_KEY);
      isShowModerator = Boolean.parseBoolean(showModeratorText);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** チャンネルIDとユーザー名の設定を読み込みます。. */
  private void loadTextAreaSetting(Ini.Section section) {
    try {
      channelIds = section.getAll(CHANNEL_IDS_KEY, String[].class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      userNames = section.getAll(USER_NAMES_KEY, String[].class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
