package chatsupporter.gui;

import chatsupporter.ApplicationUtil;

import java.io.File;

import org.ini4j.Ini;

/**
 * アプリケーションのメイン関数のクラス。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class ApplicationMain {
  private static ControlWindowView window;

  /** GUIアプリのメイン関数。. */
  public static void main(String[] args) {
    // ini設定ファイルが読み込みできたら、設定を読み込みます。
    try {
      File iniFile = new File(ApplicationUtil.INI_FILE_NAME);
      Ini ini = new Ini(iniFile);
      window = new ControlWindowView(ini);
    } catch (Exception e) {
      window = new ControlWindowView();
      e.printStackTrace();
    }
    Thread windowThread = new Thread(window);
    windowThread.start();
    // 終了時ini設定ファイルを保存します。
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

      @Override
      public void run() {
        File iniFile = new File(ApplicationUtil.INI_FILE_NAME);
        try {
          iniFile.createNewFile();
          Ini ini = new Ini(iniFile);
          window.save(ini);
          ini.store();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }));
  }

}
