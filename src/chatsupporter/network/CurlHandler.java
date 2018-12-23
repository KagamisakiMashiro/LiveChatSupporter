package chatsupporter.network;

import chatsupporter.CallbackInterface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP通信の処理器。.
 *
 * <p>同期と非同期のリクエストもできます。
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CurlHandler implements Runnable {
  private static final String USER_AGENT_KEY = "User-Agent";
  private static final String FIREFOX_USER_AGENT =
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0";
  private static final String ENCODING_CHARSET = "UTF-8";

  /** HTTPリクエストの結果。. */
  public enum Result {
    FAILED,
    SUCCESS
  }

  private int lastResponseCode;
  private CallbackInterface callback;
  private String httpRequestResult;
  private String urlString;
  private String requestMethod;
  protected Result result;

  /**
   * 同期でHTTPリクエストを執行します。.
   * @param urlString URL
   * @param requestMethod リクエスト方式
   * @return HTTPリクエストからのリスポンス
   *     HTTP_OKでない場合、nullが返します。
   *     getLastResponseCode()でリスポンスコードを取れます。
   */
  public String httpRequest(String urlString, String requestMethod) {
    try {
      URL url = new URL(urlString);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestProperty(USER_AGENT_KEY, FIREFOX_USER_AGENT);
      connection.setRequestMethod(requestMethod);
      connection.connect();

      lastResponseCode = connection.getResponseCode();
      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream in = connection.getInputStream();
        InputStreamReader inReader = new InputStreamReader(in, ENCODING_CHARSET);
        BufferedReader reader = new BufferedReader(inReader);
        StringBuffer result = new StringBuffer();
        for (String line; (line = reader.readLine()) != null;) {
          result.append(line);
        }
        reader.close();
        inReader.close();
        in.close();
        return result.toString();
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 非同期でHTTPリクエストします。.
   * @param urlString URL
   * @param requestMethod HTTPリクエスト方式
   * @param callback コールバック関数
   */
  public void asyncHttpRequest(String urlString, String requestMethod, CallbackInterface callback) {
    this.urlString = urlString;
    this.requestMethod = requestMethod;
    this.callback = callback;
    Thread thread = new Thread(this);
    thread.start();
  }

  /** HTTPリクエストのリスポンスコードを取得します。. */
  public int getLastResponseCode() {
    return lastResponseCode;
  }

  /** HTTPリクエストの結果を取得します。. */
  public Result getResult() {
    return result;
  }

  /** HTTPリクエストのリスポンスを取得します。. */
  public String getHttpRequestResult() {
    return httpRequestResult;
  }

  /** 登録したコールバックを取得します。. */
  public CallbackInterface getCallback() {
    return callback;
  }

  /** コールバックを登録します。. */
  public void setCallback(CallbackInterface callback) {
    this.callback = callback;
  }

  @Override
  public void run() {
    runAction();
    if (callback != null) {
      callback.callback();
    }
  }

  /**
   * 非同期で実行した関数です。.
   *
   * <p>継承先が非同期処理を対応したい場合、オーバーライドで処理を書き替えしてください。
   */
  protected void runAction() {
    if (urlString == null || urlString.isEmpty()
        || requestMethod == null || requestMethod.isEmpty()) {
      return;
    }
    httpRequestResult = httpRequest(urlString, requestMethod);
    if (httpRequestResult == null) {
      result = Result.FAILED;
    } else {
      result = Result.SUCCESS;
    }
  }

}
