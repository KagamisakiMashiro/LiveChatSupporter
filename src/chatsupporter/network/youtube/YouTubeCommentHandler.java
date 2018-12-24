package chatsupporter.network.youtube;

import chatsupporter.CallbackInterface;
import chatsupporter.CommentModel;
import chatsupporter.network.CommentHandler;
import chatsupporter.network.CurlHandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * YouTubeの配信コメントの処理器。.
 *
 * <p>CommentHandlerを継承しているため、全部の処理が非同期処理となっております。<br/>
 * 使用する側はconnectTo()とupdateComment()を呼んだ後、startAsyncAction()を呼ぶ必要があります。
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class YouTubeCommentHandler extends CommentHandler {
  private static final String LIVE_CHAT_URL = "https://www.youtube.com/live_chat?v=";
  private static final String GET_LIVE_CHAT_URL = "https://www.youtube.com/live_chat/get_live_chat?continuation=";
  private static final String GET_LIVE_CHAT_URL_EXTRA_PARAM = "&pbj=1";

  private String continuation;
  private String videoId;

  @Override
  public Boolean connectTo(String url, CallbackInterface callback) {
    super.connectTo(url, callback);
    String videoId = extractYouTubeId(url);
    if (videoId == null) {
      return false;
    }
    this.videoId = videoId;
    return true;
  }

  public CurlHandler.Result getResult() {
    return result;
  }

  @Override
  protected void init() {
    String result = httpRequest(LIVE_CHAT_URL + videoId, "GET");
    if (result == null) {
      this.result = Result.FAILED;
      return;
    }
    String initialData = extractInitialData(result);
    if (initialData == null) {
      this.result = Result.FAILED;
      return;
    }
    this.continuation = getContinuationFromInitialData(initialData);
    if (this.continuation != null && !this.continuation.isEmpty()) {
      this.result = Result.SUCCESS;
    } else {
      this.result = Result.FAILED;
    }
  }

  @Override
  protected void getLiveChat() {
    String result = httpRequest(
        GET_LIVE_CHAT_URL + continuation + GET_LIVE_CHAT_URL_EXTRA_PARAM, "GET");
    if (result == null) {
      this.result = Result.FAILED;
      return;
    }
    try {
      JSONParser parser = new JSONParser();
      JSONObject object = (JSONObject)parser.parse(result);
      String continuation = getContinuationFromLiveChat(object);
      if (continuation == null || continuation.isEmpty()) {
        this.result = Result.FAILED;
        return;
      }
      this.continuation = continuation;
      parseComments(object);
      this.result = Result.SUCCESS;
    } catch (Exception e) {
      this.result = Result.FAILED;
      return;
    }
  }

  /**
   * YouTubeのURLからIDを取り出します。.
   *
   * @return ID（失敗したらnull）
   */
  private String extractYouTubeId(String url) {
    String regex = ".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(url);
    if (m.find() && m.group(1) != null && !m.group(1).isEmpty()) {
      return m.group(1);
    } else {
      return null;
    }
  }

  /**
   * YouTube配信チャットの初期データをを取り出します。.
   * @param htmlResult live_chatから返したHTTPリスポンス
   * @return 初期データ（失敗したらnull）
   */
  private String extractInitialData(String htmlResult) {
    String regex = ".window\\[\"ytInitialData\"\\] = (\\{.+\\});\\s*<\\/script>.*";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(htmlResult);
    if (m.find() && m.group(1) != null && !m.group(1).isEmpty()) {
      return m.group(1);
    } else {
      return null;
    }
  }

  /**
   * YouTube配信初期データからチャット用のcontinuationを（トークン）取り出します。.
   * @param initialData 初期データ
   * @return チャット用のcontinuation（トークン）（失敗したらnull）
   */
  private String getContinuationFromInitialData(String initialData) {
    JSONParser parser = new JSONParser();
    try {
      JSONObject object = (JSONObject)parser.parse(initialData);
      JSONObject contents = (JSONObject)object.get("contents");
      JSONObject liveChatRenderer = (JSONObject)contents.get("liveChatRenderer");
      JSONArray continuations = (JSONArray)liveChatRenderer.get("continuations");
      @SuppressWarnings("unchecked")
      Iterator<JSONObject> itr = continuations.iterator();
      while (itr.hasNext()) {
        JSONObject eachContinuation = itr.next();
        JSONObject continuationData =
            (JSONObject)eachContinuation.get("invalidationContinuationData");
        if (continuationData == null) {
          continuationData =
              (JSONObject)eachContinuation.get("timedContinuationData");
        }
        String continuation = (String)continuationData.get("continuation");
        if (continuation != null) {
          return continuation;
        }
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  /**
   * get_live_chatでのリスポンスから新しいcontinuation（トークン）を取り出します。.
   * @param object get_live_chatでのリスポンスのJSONObject
   * @return 新しいcontinuation（トークン）（失敗したらnull）
   */
  private String getContinuationFromLiveChat(JSONObject object) {
    try {
      JSONObject response = (JSONObject)object.get("response");
      JSONObject continuationContents = (JSONObject)response.get("continuationContents");
      JSONObject liveChatContinuation =
          (JSONObject)continuationContents.get("liveChatContinuation");
      JSONArray continuations = (JSONArray)liveChatContinuation.get("continuations");
      @SuppressWarnings("unchecked")
      Iterator<JSONObject> itr = continuations.iterator();
      while (itr.hasNext()) {
        JSONObject eachContinuation = itr.next();
        JSONObject continuationData =
            (JSONObject)eachContinuation.get("invalidationContinuationData");
        if (continuationData == null) {
          continuationData =
              (JSONObject)eachContinuation.get("timedContinuationData");
        }
        String continuation = (String)continuationData.get("continuation");
        if (continuation != null) {
          return continuation;
        }
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  /**
   * get_live_chatでのリスポンスから新しいコメントを取り出します。.
   * @param object get_live_chatでのリスポンスのJSONObject
   */
  private void parseComments(JSONObject object) {
    commentResults.clear();
    try {
      JSONObject response = (JSONObject)object.get("response");
      JSONObject continuationContents = (JSONObject)response.get("continuationContents");
      JSONObject liveChatContinuation =
          (JSONObject)continuationContents.get("liveChatContinuation");
      JSONArray actions = (JSONArray)liveChatContinuation.get("actions");
      @SuppressWarnings("unchecked")
      Iterator<JSONObject> itr = actions.iterator();
      while (itr.hasNext()) {
        JSONObject action = itr.next();
        JSONObject addChatItemAction = (JSONObject)action.get("addChatItemAction");
        JSONObject item = (JSONObject)addChatItemAction.get("item");
        JSONObject liveChatText = (JSONObject)item.get("liveChatTextMessageRenderer");
        if (item != null) {
          CommentModel comment = parseCommentsItem(liveChatText);
          if (comment != null) {
            commentResults.add(comment);
          }
        }
      }
    } catch (Exception e) {
      return;
    }
  }

  /**
   * コメントの内容を取り出します。.
   * @param liveChatText ライブチャットのオブジェクト
   * @return 取り出したコメントモデル（失敗したらnull）
   */
  private CommentModel parseCommentsItem(JSONObject liveChatText) {
    try {
      CommentModel comment = new CommentModel();
      JSONObject authorName = (JSONObject)liveChatText.get("authorName");
      comment.setAuthorName(parseAuthorName(authorName));
      String url = parseUrlString(liveChatText);
      if (url != null) {
        comment.setAuthorPhotoUrl(url);
      }
      comment.setAuthorChannelId((String)liveChatText.get("authorExternalChannelId"));
      JSONObject message = (JSONObject)liveChatText.get("message");
      comment.setMessage(parseMessage(message));
      CommentModel.AuthorType[] authorTypes = parseAuthorTypes(liveChatText);
      if (authorTypes != null && authorTypes.length > 0) {
        comment.setAuthorTypes(authorTypes);
      }
      return comment;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 作者名を取り出します。.
   * @param authorName 作者名のオブジェクト
   * @return 取り出した名前（失敗したらnull）
   */
  private String parseAuthorName(JSONObject authorName) {
    String text = (String)authorName.get("simpleText");
    if (text != null) {
      return text;
    }
    JSONArray runs = (JSONArray)authorName.get("runs");
    @SuppressWarnings("unchecked")
    Iterator<JSONObject> itr = runs.iterator();
    while (itr.hasNext()) {
      JSONObject run = itr.next();
      text = (String)run.get("text");
      if (text != null) {
        return text;
      }
    }
    return null;
  }

  /**
   * コメントを取り出します。.
   * @param message コメントのオブジェクト
   * @return 取り出したコメント失敗したらnull）
   */
  private String parseMessage(JSONObject message) {
    String text = (String)message.get("simpleText");
    if (text != null) {
      return text;
    }
    JSONArray runs = (JSONArray)message.get("runs");
    @SuppressWarnings("unchecked")
    Iterator<JSONObject> itr = runs.iterator();
    while (itr.hasNext()) {
      JSONObject run = itr.next();
      text = (String)run.get("text");
      if (text != null) {
        return text;
      }
    }
    return null;
  }

  /**
   * コメント内容からアイコンのURLを取り出します。.
   * @param liveChatText ライブチャットのオブジェクト
   * @return アイコンのURL
   */
  private String parseUrlString(JSONObject liveChatText) {
    try {
      JSONObject authorPhoto = (JSONObject)liveChatText.get("authorPhoto");
      JSONArray thumbnails = (JSONArray)authorPhoto.get("thumbnails");
      @SuppressWarnings("unchecked")
      Iterator<JSONObject> itr = thumbnails.iterator();
      while (itr.hasNext()) {
        JSONObject thumbnail = itr.next();
        String url = (String)thumbnail.get("url");
        if (url != null) {
          return url;
        }
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * コメント内容から作者の種類を取り出します。.
   * @param liveChatText ライブチャットのオブジェクト
   * @return 作者の種類（なかったらnull）
   */
  private CommentModel.AuthorType[] parseAuthorTypes(JSONObject liveChatText) {
    try {
      JSONArray authorBadges = (JSONArray)liveChatText.get("authorBadges");
      LinkedList<CommentModel.AuthorType> authorTypes = new LinkedList<CommentModel.AuthorType>();
      @SuppressWarnings("unchecked")
      Iterator<JSONObject> itr = authorBadges.iterator();
      while (itr.hasNext()) {
        JSONObject authorBadge = itr.next();
        JSONObject liveChatAuthorBadge = (JSONObject)authorBadge.get("liveChatAuthorBadgeRenderer");
        JSONObject icon = (JSONObject)liveChatAuthorBadge.get("icon");
        String iconType = (String)icon.get("iconType");
        if (iconType == null) {
          continue;
        }
        for (CommentModel.AuthorType type : CommentModel.AuthorType.values()) {
          if (iconType.equals(type.name())) {
            authorTypes.add(type);
            break;
          }
        }
      }
      CommentModel.AuthorType[] authorTypesArray = new CommentModel.AuthorType[authorTypes.size()];
      int index = 0;
      for (CommentModel.AuthorType authorType : authorTypes) {
        authorTypesArray[index] = authorType;
        ++index;
      }
      return authorTypesArray;
    } catch (Exception e) {
      return null;
    }
  }
}
