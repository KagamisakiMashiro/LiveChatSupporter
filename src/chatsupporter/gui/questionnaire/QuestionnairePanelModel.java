package chatsupporter.gui.questionnaire;

import java.util.Arrays;
import java.util.HashMap;

import org.ini4j.Ini;

/**
 * アンケートの設定、結果、表示のモデル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class QuestionnairePanelModel {
  private static String QUESTIONNAIRE_SECTION = "QUESTIONNAIRE";
  private static String FONT_SIZE_KEY = "FONT_SIZE";
  private static String DISPLAY_METHOD_KEY = "DISPLAY_METHOD";

  private static final int DEFAULT_FONT_SIZE = 16;

  /** アンケート状態。. */
  public enum QuestionnaireStatus {
    BEFORE_START,
    STARTED,
    END
  }

  /** アンケートの表示方式。. */
  public enum DisplayMethod {
    NICO_DISPLAY,
    TWITTER_DISPLAY
  }

  /** 強制ツイッター式表示を使う選択肢数。. */
  public static final int FORCE_TWITTER_OPTION_NUM = 10;

  private QuestionnairePanelView view;
  private QuestionnaireWindowPanelView windowView;
  private Boolean isMultipleChoice = false;
  private int optionNum = 2;
  private DisplayMethod displayMethod = DisplayMethod.NICO_DISPLAY;
  private QuestionnaireStatus currentStatus = QuestionnaireStatus.BEFORE_START;
  private String[] optionTexts;
  private HashMap<String, Boolean[]> voteMap;
  private int[] optionVotes;
  private int fontSize = DEFAULT_FONT_SIZE;

  /**
   * アンケートの設定、結果、表示のモデルを作成します。.
   *
   * @param view アンケート設定パネルのビュー
   * @param ini iniファイル（null可）
   */
  public QuestionnairePanelModel(QuestionnairePanelView view, Ini ini) {
    this.view = view;
    if (ini != null) {
      load(ini);
    }
  }

  /** iniファイルから設定を読み込みます。. */
  public void load(Ini ini) {
    Ini.Section section = ini.get(QUESTIONNAIRE_SECTION);
    try {
      String fontSizeText = section.get(FONT_SIZE_KEY);
      fontSize = Integer.parseInt(fontSizeText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String displayMethodText = section.get(DISPLAY_METHOD_KEY);
      displayMethod = DisplayMethod.valueOf(displayMethodText);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** iniファイルに設定を保存します。. */
  public void save(Ini ini) {
    Ini.Section section = ini.get(QUESTIONNAIRE_SECTION);
    if (section == null) {
      section = ini.add(QUESTIONNAIRE_SECTION);
    }
    section.put(FONT_SIZE_KEY, fontSize);
    section.put(DISPLAY_METHOD_KEY, displayMethod.name());
  }

  /** アンケートウィンドウを設定します。. */
  public void setWindowView(QuestionnaireWindowPanelView windowView) {
    this.windowView = windowView;
  }

  /** 複数選択できるかを取得します。. */
  public Boolean getIsMultipleChoice() {
    return isMultipleChoice;
  }

  /** 複数選択できるかを設定します。. */
  public void setIsMultipleChoice(Boolean isMultipleChoice) {
    this.isMultipleChoice = isMultipleChoice;
    view.update();
  }

  /** 選択肢数を取得します。. */
  public int getOptionNum() {
    return optionNum;
  }

  /** 選択肢数を設定します。. */
  public void setOptionNum(int optionNum) {
    this.optionNum = optionNum;
    view.update();
  }

  /** 表示方式を取得します。. */
  public DisplayMethod getDisplayMethod() {
    return displayMethod;
  }

  /** 表示方式を設定します。. */
  public void setDisplayMethod(DisplayMethod displayMethod) {
    this.displayMethod = displayMethod;
    view.update();
    if (windowView != null) {
      windowView.update();
    }
  }

  /** アンケート状態を取得します。. */
  public QuestionnaireStatus getCurrentStatus() {
    return currentStatus;
  }

  /** アンケート状態を設定します。. */
  public void setCurrentStatus(QuestionnaireStatus currentStatus) {
    this.currentStatus = currentStatus;
    view.update();
    if (windowView != null) {
      windowView.update();
    }
  }

  /** 選択肢のテキストを取得します。. */
  public String[] getOptionTexts() {
    return optionTexts;
  }

  /** 選択肢のテキストを設定します。. */
  public void setOptionTexts(String[] optionTexts) {
    this.optionTexts = optionTexts;
    view.update();
  }

  /**
   * 投票します。.
   * @param channelId 投票者のチャンネルID
   * @param votedOptions 投票先
   */
  public void addVote(String channelId, int[] votedOptions) {
    if (!voteMap.containsKey(channelId)) {
      Boolean[] votes = new Boolean[optionNum];
      Arrays.fill(votes, false);
      voteMap.put(channelId, votes);
    } else if (!isMultipleChoice) {
      return;
    }
    for (int votedOption : votedOptions) {
      voteMap.get(channelId)[votedOption] = true;
      ++optionVotes[votedOption];
      if (!isMultipleChoice) {
        return;
      }
    }
    view.update();
  }

  /** 総投票数を取得します。. */
  public int getTotalVote() {
    return voteMap == null ? 0 : voteMap.size();
  }

  /**
   * 全部の選択肢の投票率を取得します。.
   * @return 全部の選択肢の投票率（総投票数が0の場合、全部0の配列が返します。）
   */
  public double[] getVoteRates() {
    double[] voteRates = new double[optionNum];
    int totalVote = getTotalVote();
    for (int i = 0; i < optionNum; ++i) {
      if (totalVote != 0) {
        voteRates[i] = (double) optionVotes[i] / totalVote;
      } else {
        voteRates[i] = 0;
      }
    }
    return voteRates;
  }

  /**
   * アンケートの結果をリセットします。.
   *
   * <p>投票開始前、必ずリセットしてください。
   */
  public void resetVote() {
    optionVotes = new int[optionNum];
    Arrays.fill(optionVotes, 0);
    if (voteMap != null) {
      voteMap.clear();
    } else {
      voteMap = new HashMap<String, Boolean[]>();
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

}
