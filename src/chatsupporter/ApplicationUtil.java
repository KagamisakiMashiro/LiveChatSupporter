package chatsupporter;

import java.util.ResourceBundle;

/**
 * アプリケーションの共通関数ライブラリ。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class ApplicationUtil {
  private static final ResourceBundle textBundle =
      ResourceBundle.getBundle("chatsupporter.textProperties");

  private static final int MAJOR_VERSION = 1;
  private static final int MINOR_VERSION = 0;
  private static final int BUILD_VERSION = 1;

  private static final long MILLI_TO_NANO = 1000000;
  private static final long SECOND_TO_MILLI = 1000;
  /** 一秒間の処理数. */
  public static final long FPS = 30;
  /** 一回の処理の時間. */
  public static final long ONE_FRAME_TIME = 1 * SECOND_TO_MILLI * MILLI_TO_NANO / FPS;
  /** 一回の処理の最小待ち時間. */
  public static final long MIN_FRAME_TIME = 2 * MILLI_TO_NANO;
  /** ini設定ファイルの名前. */
  public static final String INI_FILE_NAME = "setting.ini";

  /**
   * ミリ秒を共通の時間単位に変更します。.
   * @param ms ミリ秒
   * @return 共通単位の時間
   */
  public static long msToCommonScale(long ms) {
    return ms * MILLI_TO_NANO;
  }

  /**
   * 秒を共通の時間単位に変更します。.
   * @param s 秒
   * @return 共通単位の時間
   */
  public static long secToCommonScale(long s) {
    return s * SECOND_TO_MILLI * MILLI_TO_NANO;
  }

  /**
   * 共通単位の時間をThread.sleep()用の待ち時間単位に変更します。.
   * @param ns 共通単位の時間
   * @return Thread.sleep()用の時間単位の時間
   */
  public static long commonScaleToDelayScale(long ns) {
    return ns / MILLI_TO_NANO;
  }

  /** 現在の時間を取得します。. */
  public static long getTime() {
    return System.nanoTime();
  }

  /**
   * 特定のラベルのテキストを取得します。.
   * @param propertyLabel ラベル
   * @return テクスト
   *     取得できない場合、空の文字列が返します。
   */
  public static String getText(String propertyLabel) {
    try {
      return textBundle.getString(propertyLabel);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /** 現在のバージョンを取得します。. */
  public static String getVersion() {
    return String.valueOf(MAJOR_VERSION) + '.'
        + String.valueOf(MINOR_VERSION) + '.' + String.valueOf(BUILD_VERSION);
  }
}
