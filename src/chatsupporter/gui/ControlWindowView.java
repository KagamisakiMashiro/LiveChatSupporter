package chatsupporter.gui;

import chatsupporter.ApplicationUtil;
import chatsupporter.CommentModel;
import chatsupporter.gui.comment.CommentPanelView;
import chatsupporter.gui.comment.CommentWindowPanelView;
import chatsupporter.gui.general.GeneralPanelView;
import chatsupporter.gui.questionnaire.QuestionnairePanelView;
import chatsupporter.gui.questionnaire.QuestionnaireWindowPanelView;
import chatsupporter.gui.versionchecking.VersionCheckingPanelView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import org.ini4j.Ini;

/**
  * 全体操作ウィンドウのビュー。.
  *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class ControlWindowView extends JFrame implements Runnable {
  private static ImageIcon img = new ImageIcon("resource/LiChaS.png");
  private long frameTimeDelta = 0;
  private JFrame questionnaireWindowFrame;
  private QuestionnaireWindowPanelView questionnaireWindowPanel;
  private JFrame commentWindowFrame;
  private CommentWindowPanelView commentWindowPanel;
  private JFrame versionCheckingFrame;
  private VersionCheckingPanelView versionCheckingPanel;
  private ControlWindowModel model;
  private GeneralPanelView generalPanel;
  private QuestionnairePanelView questionnairePanel;
  private CommentPanelView commentPanel;

  /** 全体操作ウィンドウのビューを作成します。. */
  public ControlWindowView() {
    this(null);
  }

  /**
   * 全体操作ウィンドウのビューを作成します。.
   * @param ini iniファイル(null可)
   */
  public ControlWindowView(Ini ini) {
    this.model = new ControlWindowModel(ini);
    // Windowサイズと�?閉じる�?�タン押下時の処�?を追�?
    this.setSize(this.model.getFrameSize());
    if (this.model.getFrameLocation() == null) {
      this.setLocationRelativeTo(null);
    } else {
      this.setLocation(this.model.getFrameLocation());
    }
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle(ApplicationUtil.getText("CONTROL_WINDOW_TITLE"));
    initToolBar();
    initTabbedPane(ini);
    initQuestionnaireWindow();
    initCommentWindow();
    initVersionCheckingFrame();
    setFrameIcon(this);

    this.setVisible(true);
  }

  /** ini設定ファイルに保存します。. */
  public void save(Ini ini) {
    saveControlWindowModel(ini);
    questionnairePanel.save(ini);
    commentPanel.save(ini);
  }

  @Override
  public void run() {
    while (true) {
      long oldTime = ApplicationUtil.getTime();
      // フレーム開始
      update();
      // フレーム終了
      long newTime = ApplicationUtil.getTime();
      frameTimeDelta = ApplicationUtil.ONE_FRAME_TIME - (newTime - oldTime);
      // 最短待ち時間
      if (frameTimeDelta < ApplicationUtil.MIN_FRAME_TIME) {
        frameTimeDelta = ApplicationUtil.MIN_FRAME_TIME;
      }
      try {
        // 次のフレームへ
        Thread.sleep(ApplicationUtil.commonScaleToDelayScale(frameTimeDelta));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** コメントを読み込みします。. */
  public void receiveComments(LinkedList<CommentModel> comments) {
    questionnairePanel.handleComments(comments);
    commentPanel.handleComments(comments);
  }

  /** ツールバーを初期化をします。. */
  private void initToolBar() {
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu menu = new JMenu(ApplicationUtil.getText("OTHER"));
    menuBar.add(menu);

    JMenuItem menuItem = new JMenuItem(ApplicationUtil.getText("VERSION_INFORMATION"));
    menu.add(menuItem);
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        versionCheckingFrame.setLocation(getLocation().x
            + ControlWindowModel.DEFAULT_VERSION_CHECK_FRAME_OFFSET.x,
            getLocation().y + ControlWindowModel.DEFAULT_VERSION_CHECK_FRAME_OFFSET.y);
        versionCheckingFrame.setVisible(true);
        versionCheckingPanel.checkVersion();
      }
    });

  }

  /**
   * タブの初期化をします。.
   * @param ini iniファイル（null可）
   */
  private void initTabbedPane(Ini ini) {
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    getContentPane().add(tabbedPane, BorderLayout.CENTER);

    generalPanel = new GeneralPanelView();
    tabbedPane.addTab(ApplicationUtil.getText("GENERAL_SETTING"), null, generalPanel, null);
    generalPanel.setControlWindow(this);

    questionnairePanel = new QuestionnairePanelView(ini);
    tabbedPane.addTab(ApplicationUtil.getText("QUESTIONNAIRE_FUNCTION"),
        null, questionnairePanel, null);

    commentPanel = new CommentPanelView(ini);
    tabbedPane.addTab(ApplicationUtil.getText("COMMENT_FUNCTION"), null, commentPanel, null);
  }

  /** アンケートウィンドウを初期化します。. */
  private void initQuestionnaireWindow() {
    questionnaireWindowFrame = new JFrame(ApplicationUtil.getText("QUESTIONNAIRE_WINDOW_TITLE"));
    questionnaireWindowFrame.setSize(this.model.getQuestionnaireFrameSize());
    questionnaireWindowFrame.getContentPane().setBackground(
        generalPanel.getModel().getBackgroundColor());
    if (this.model.getQuestionnaireFrameLocation() == null) {
      questionnaireWindowFrame.setLocationRelativeTo(null);
    } else {
      questionnaireWindowFrame.setLocation(this.model.getQuestionnaireFrameLocation());
    }
    setFrameIcon(questionnaireWindowFrame);
    questionnaireWindowPanel = new QuestionnaireWindowPanelView();
    questionnaireWindowFrame.add(questionnaireWindowPanel);

    questionnairePanel.setWindowFramePanel(questionnaireWindowFrame, questionnaireWindowPanel);
  }

  /** コメントウィンドウを初期化します。. */
  private void initCommentWindow() {
    commentWindowFrame = new JFrame(ApplicationUtil.getText("COMMENT_WINDOW_TITLE"));
    commentWindowFrame.setSize(this.model.getCommentFrameSize());
    commentWindowFrame.getContentPane().setBackground(
        generalPanel.getModel().getBackgroundColor());
    if (this.model.getCommentFrameLocation() == null) {
      commentWindowFrame.setLocationRelativeTo(null);
    } else {
      commentWindowFrame.setLocation(this.model.getCommentFrameLocation());
    }
    setFrameIcon(commentWindowFrame);
    commentWindowPanel = new CommentWindowPanelView();
    commentWindowFrame.add(commentWindowPanel);
    commentPanel.setWindowFramePanel(commentWindowFrame, commentWindowPanel);
  }

  /** バージョン確認ウィンドウを初期化します。. */
  private void initVersionCheckingFrame() {
    versionCheckingFrame = new JFrame(ApplicationUtil.getText("VERSION_INFORMATION_TITLE"));
    versionCheckingFrame.setSize(ControlWindowModel.DEFAULT_VERSION_CHECK_FRAME_SIZE);
    versionCheckingPanel = new VersionCheckingPanelView();
    versionCheckingFrame.add(versionCheckingPanel);
    setFrameIcon(versionCheckingFrame);
  }

  /** 毎フレームの更新処理をします。. */
  private void update() {
    generalPanel.updatePerFrame(frameTimeDelta);
    questionnairePanel.updatePerFrame(frameTimeDelta);
    commentPanel.updatePerFrame(frameTimeDelta);
    questionnaireWindowPanel.updatePerFrame(frameTimeDelta);
    commentWindowPanel.updatePerFrame(frameTimeDelta);
    versionCheckingPanel.updatePerFrame(frameTimeDelta);
  }

  /** モデルデータをiniファイルにセーブします。. */
  private void saveControlWindowModel(Ini ini) {
    model.setFrameSize(getSize());
    model.setFrameLocation(getLocation());
    model.setCommentFrameSize(commentWindowFrame.getSize());
    model.setCommentFrameLocation(commentWindowFrame.getLocation());
    model.setQuestionnaireFrameSize(questionnaireWindowFrame.getSize());
    model.setQuestionnaireFrameLocation(questionnaireWindowFrame.getLocation());
    model.save(ini);
  }

  /** ウィンドウのアイコンを設定します。. */
  private void setFrameIcon(JFrame frame) {
    if (img != null) {
      frame.setIconImage(img.getImage());
    }
  }
}
