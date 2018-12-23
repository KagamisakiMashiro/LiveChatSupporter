package chatsupporter.gui.versionchecking;

import chatsupporter.ApplicationUtil;
import chatsupporter.gui.CommonPanel;
import chatsupporter.gui.HyperLinkLabel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * バージョン情報表示のパネル。.
 *
 * @author Kagamisaki Mashiro
 * @copyright 2018 Kagamisaki Mashiro
 * @license http://www.apache.org/licenses/LICENSE-2.0 Apache-2.0
 *
 */
public class VersionCheckingPanelView extends CommonPanel {
  private VersionCheckingPanelModel model;
  private VersionCheckingController controller;
  private JLabel newestVersionLabel = new JLabel();

  /** バージョン情報表示のパネルを作成します。. */
  public VersionCheckingPanelView() {
    model = new VersionCheckingPanelModel(this);
    controller = new VersionCheckingController(model);

    setLayout(new BorderLayout());
    add(createVersionPanel(), BorderLayout.NORTH);
    JPanel panel = new JPanel();
    add(panel, BorderLayout.CENTER);
    panel.setLayout(new BorderLayout());

    panel.add(createAuthorDetailsPanel(), BorderLayout.NORTH);
    panel.add(new JPanel(), BorderLayout.CENTER);
    updateByModel();
  }

  /** バージョンを確認します。. */
  public void checkVersion() {
    controller.checkVersion();
  }

  @Override
  public void updateByModel() {
    switch (model.getCurrentStatus()) {
      default:
      case CONNECTING:
        newestVersionLabel.setText(ApplicationUtil.getText("CONFIRMING_VERSION"));
        break;
      case DONE:
        if (model.getNewestVersion().equals(ApplicationUtil.getVersion())) {
          newestVersionLabel.setText(ApplicationUtil.getText("CONFIRMING_VERSION")
              + ApplicationUtil.getText("CURRENT_NEWEST"));
        } else {
          newestVersionLabel.setText(ApplicationUtil.getText("NEW_VERSION")
              + model.getNewestVersion()
              + ApplicationUtil.getText("VERSION_EXIST"));
        }
        break;
      case FAILED:
        newestVersionLabel.setText(ApplicationUtil.getText("CONFIRMING_VERSION")
            + ApplicationUtil.getText("FAILED"));
        break;
    }
  }

  /** バージョン表示のパネルを作成します。. */
  private JPanel createVersionPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));

    JPanel currentVersionPanel = new JPanel();
    panel.add(currentVersionPanel);
    currentVersionPanel.setLayout(new GridLayout(0, 3));

    currentVersionPanel.add(new JLabel(ApplicationUtil.getText("CURRENT_VERSION")));
    currentVersionPanel.add(new JLabel());
    JLabel versionLabel = new JLabel(ApplicationUtil.getVersion());
    versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    currentVersionPanel.add(versionLabel);

    panel.add(newestVersionLabel);

    panel.add(new HyperLinkLabel(ApplicationUtil.getText("DOWNLOAD_PAGE"),
        ApplicationUtil.getText("DOWNLOAD_URL")));

    return panel;
  }

  /** 作者情報のパネルを作成します。. */
  private JPanel createAuthorDetailsPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));

    panel.add(new JSeparator());

    JPanel authorNamePanel = new JPanel();
    panel.add(authorNamePanel);
    authorNamePanel.setLayout(new GridLayout(1, 0));

    authorNamePanel.add(new JLabel(ApplicationUtil.getText("AUTHOR")));

    authorNamePanel.add(new JLabel());

    JLabel nameLabel = new JLabel(ApplicationUtil.getText("KAGAMISAKI_MASHIRO"));
    nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    authorNamePanel.add(nameLabel);

    panel.add(new HyperLinkLabel(ApplicationUtil.getText("YOUTUBE_CHANNEL"),
        ApplicationUtil.getText("YOUTUBE_CHANNEL_URL")));

    panel.add(new HyperLinkLabel(ApplicationUtil.getText("TWITTER"),
        ApplicationUtil.getText("TWITTER_URL")));

    panel.add(new HyperLinkLabel(ApplicationUtil.getText("GITHUB"),
        ApplicationUtil.getText("GITHUB_URL")));

    return panel;
  }

}
