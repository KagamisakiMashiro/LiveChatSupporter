package chatsupporter.gui;

import java.awt.Dimension;
import java.awt.Point;

import org.ini4j.Ini;

/**
 * 全体のウィンドウフレームのサイズと位置のモデル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class ControlWindowModel {
  private static final String FRAME_SECTION = "FRAME";
  private static final String COMMENT_SECTION = "COMMENT_FRAME";
  private static final String QUESTIONNAIRE_SECTION = "QUESTIONNAIRE_FRAME";
  private static final String WIDTH_KEY = "WIDTH";
  private static final String HEIGHT_KEY = "HEIGHT";
  private static final String X_KEY = "X";
  private static final String Y_KEY = "Y";

  private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(440, 400);
  private static final Point DEFAULT_FRAME_LOCATION = null;
  private static final Dimension DEFAULT_QUESTIONNAIRE_FRAME_SIZE = new Dimension(440, 400);
  private static final Point DEFAULT_QUESTIONNAIRE_FRAME_LOCATION = null;
  private static final Dimension DEFAULT_COMMENT_FRAME_SIZE = new Dimension(440, 400);
  private static final Point DEFAULT_COMMENT_FRAME_LOCATION = null;
  /** バージョン情報ウィンドウのサイズ。. */
  public static final Dimension DEFAULT_VERSION_CHECK_FRAME_SIZE = new Dimension(380, 380);
  /** バージョン情報ウィンドウの相対位置。. */
  public static final Point DEFAULT_VERSION_CHECK_FRAME_OFFSET = new Point(40, 0);

  private Dimension frameSize = DEFAULT_FRAME_SIZE;
  private Point frameLocation = DEFAULT_FRAME_LOCATION;
  private Dimension questionnaireFrameSize = DEFAULT_QUESTIONNAIRE_FRAME_SIZE;
  private Point questionnaireFrameLocation = DEFAULT_QUESTIONNAIRE_FRAME_LOCATION;
  private Dimension commentFrameSize = DEFAULT_COMMENT_FRAME_SIZE;
  private Point commentFrameLocation = DEFAULT_COMMENT_FRAME_LOCATION;

  /**
   * 全体のウィンドウフレームのサイズと位置のモデルを作成します。.
   *
   * <p>既定のサイズと位置のモデルを作成します。<br/>
   * iniファイルがある場合、設定を読み込みます。
   *
   * @param ini ini設定ファイル（null可）
   */
  public ControlWindowModel(Ini ini) {
    this.frameSize = DEFAULT_FRAME_SIZE;
    this.frameLocation = DEFAULT_FRAME_LOCATION;
    this.questionnaireFrameSize = DEFAULT_QUESTIONNAIRE_FRAME_SIZE;
    this.questionnaireFrameLocation = DEFAULT_QUESTIONNAIRE_FRAME_LOCATION;
    this.commentFrameSize = DEFAULT_COMMENT_FRAME_SIZE;
    this.commentFrameLocation = DEFAULT_COMMENT_FRAME_LOCATION;
    if (ini != null) {
      this.load(ini);
    }
  }

  /**
   * iniファイルから設定を読み込みます。.
   * @param ini iniファイル
   */
  public void load(Ini ini) {
    loadFrameData(ini);
    loadCommentData(ini);
    loadQuestionnaireData(ini);
  }

  /**
   * iniファイルに設定を保存します。.
   * @param ini iniファイル
   */
  public void save(Ini ini) {
    Ini.Section section = ini.get(FRAME_SECTION);
    if (section == null) {
      section = ini.add(FRAME_SECTION);
    }
    section.put(WIDTH_KEY, frameSize.width);
    section.put(HEIGHT_KEY, frameSize.height);
    section.put(X_KEY, frameLocation.x);
    section.put(Y_KEY, frameLocation.y);

    section = ini.get(COMMENT_SECTION);
    if (section == null) {
      section = ini.add(COMMENT_SECTION);
    }
    section.put(WIDTH_KEY, commentFrameSize.width);
    section.put(HEIGHT_KEY, commentFrameSize.height);
    section.put(X_KEY, commentFrameLocation.x);
    section.put(Y_KEY, commentFrameLocation.y);

    section = ini.get(QUESTIONNAIRE_SECTION);
    if (section == null) {
      section = ini.add(QUESTIONNAIRE_SECTION);
    }
    section.put(WIDTH_KEY, questionnaireFrameSize.width);
    section.put(HEIGHT_KEY, questionnaireFrameSize.height);
    section.put(X_KEY, questionnaireFrameLocation.x);
    section.put(Y_KEY, questionnaireFrameLocation.y);
  }

  /** ウィンドウのサイズを取得します。. */
  public Dimension getFrameSize() {
    return frameSize;
  }

  /** ウィンドウのサイズを設定します。. */
  public void setFrameSize(Dimension frameSize) {
    this.frameSize = frameSize;
  }

  /** ウィンドウ位置を取得します。. */
  public Point getFrameLocation() {
    return frameLocation;
  }

  /** ウィンドウ位置を設定します。. */
  public void setFrameLocation(Point frameLocation) {
    this.frameLocation = frameLocation;
  }

  /** アンケートウィンドウのサイズを取得します。. */
  public Dimension getQuestionnaireFrameSize() {
    return questionnaireFrameSize;
  }

  /** アンケートウィンドウのサイズを設定します。. */
  public void setQuestionnaireFrameSize(Dimension questionnaireFrameSize) {
    this.questionnaireFrameSize = questionnaireFrameSize;
  }

  /** アンケートウィンドウ位置を取得します。. */
  public Point getQuestionnaireFrameLocation() {
    return questionnaireFrameLocation;
  }

  /** アンケートウィンドウ位置を設定します。. */
  public void setQuestionnaireFrameLocation(Point questionnaireFrameLocation) {
    this.questionnaireFrameLocation = questionnaireFrameLocation;
  }

  /** コメントウィンドウのサイズを取得します。. */
  public Dimension getCommentFrameSize() {
    return commentFrameSize;
  }

  /** コメントウィンドウのサイズを設定します。. */
  public void setCommentFrameSize(Dimension commentFrameSize) {
    this.commentFrameSize = commentFrameSize;
  }

  /** コメントウィンドウ位置を取得します。. */
  public Point getCommentFrameLocation() {
    return commentFrameLocation;
  }

  /** コメントウィンドウ位置を設定します。. */
  public void setCommentFrameLocation(Point commentFrameLocation) {
    this.commentFrameLocation = commentFrameLocation;
  }

  /** iniファイルからウィンドウの設定を読み込みます。. */
  private void loadFrameData(Ini ini) {
    Ini.Section section = ini.get(FRAME_SECTION);
    try {
      String widthText = section.get(WIDTH_KEY);
      String heightText = section.get(HEIGHT_KEY);
      int width = Integer.parseInt(widthText);
      int height = Integer.parseInt(heightText);
      this.frameSize = new Dimension(width, height);;
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String xtext = section.get(X_KEY);
      String ytext = section.get(Y_KEY);
      int x = Integer.parseInt(xtext);
      int y = Integer.parseInt(ytext);
      this.frameLocation = new Point(x, y);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** iniファイルからコメントウィンドウの設定を読み込みます。. */
  private void loadCommentData(Ini ini) {
    Ini.Section section = ini.get(COMMENT_SECTION);
    try {
      String widthText = section.get(WIDTH_KEY);
      String heightText = section.get(HEIGHT_KEY);
      int width = Integer.parseInt(widthText);
      int height = Integer.parseInt(heightText);
      commentFrameSize = new Dimension(width, height);;
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String xtext = section.get(X_KEY);
      String ytext = section.get(Y_KEY);
      int x = Integer.parseInt(xtext);
      int y = Integer.parseInt(ytext);
      commentFrameLocation = new Point(x, y);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** iniファイルからアンケートウィンドウの設定を読み込みます。. */
  private void loadQuestionnaireData(Ini ini) {
    Ini.Section section = ini.get(QUESTIONNAIRE_SECTION);
    try {
      String widthText = section.get(WIDTH_KEY);
      String heightText = section.get(HEIGHT_KEY);
      int width = Integer.parseInt(widthText);
      int height = Integer.parseInt(heightText);
      questionnaireFrameSize = new Dimension(width, height);;
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String xtext = section.get(X_KEY);
      String ytext = section.get(Y_KEY);
      int x = Integer.parseInt(xtext);
      int y = Integer.parseInt(ytext);
      questionnaireFrameLocation = new Point(x, y);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
