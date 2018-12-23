package chatsupporter.gui;

import javax.swing.JPanel;

/**
 * 各種パネルビューの共通クラス。.
 *
 * <p>毎フレームにupdatePerFrame(timeDelta)を呼ぶ必要があります。<br/>
 * モデル側が変更したら、update()を呼ぶことを想定しています。<br/>
 * モデルの変更の反映はupdateByModel()を継承して実装してください。<br/>
 * 毎フレームの処理はupdateByTick()を継承して実装してください。
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class CommonPanel extends JPanel {
  /** 選択可能なフォントサイズ。. */
  protected static final Integer[] FONT_SIZE_CHOICES = {
      8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72
  };

  private Boolean isUpdate = false;
  private long timeDelta = 0;

  /**
   * 毎フレームでの更新行動をします。.
   *
   * <p>スレッドから呼び出されることを想定しています。
   * @param timeDelta 前のフレームの時間
   */
  public final void updatePerFrame(long timeDelta) {
    updateByTick();
    if (isUpdate) {
      updateByModel();
      isUpdate = false;
    }
    this.timeDelta = timeDelta;
  }

  /** モデルの最新状態を反映する要求します。. */
  public final void update() {
    isUpdate = true;
  }

  /** 前のフレームの経過時間を取得します。. */
  protected long getTimeDelta() {
    return timeDelta;
  }

  /**
   * モデルの最新状態を更新する関数です。.
   *
   * <p>基本内部での呼び出しは想定されていません。<br/>
   * update()を使用してモデルを反映することを想定しています。
   */
  protected void updateByModel() {
  }

  /**
   * 毎フレームの更新処理の関数です。.
   *
   * <p>基本内部での呼び出しは想定されていません。<br/>
   * 毎フレームの処理のupdatePerFrame(timeDelta)から呼び出されることを想定しています。
   */
  protected void updateByTick() {
  }

}
