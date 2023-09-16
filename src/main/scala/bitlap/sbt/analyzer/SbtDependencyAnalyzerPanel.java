package bitlap.sbt.analyzer;

import bitlap.sbt.analyzer.util.Notifications$;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

/**
 * @author 梦境迷离
 * @version 1.0, 2023/9/8
 */
@SuppressWarnings("unchecked")
public class SbtDependencyAnalyzerPanel {
    public JPanel mainPanel;

    public JTextField organization;
    public JCheckBox compileCheckBox;
    public JCheckBox providedCheckBox;
    public JCheckBox testCheckBox;
    public JTextField fileCache;
    private final SettingsState settings;
    private final Project project;

    public SbtDependencyAnalyzerPanel(Project project) {
        this.project = project;
        this.settings = SettingsState.getSettings(project);
    }

    boolean isModified() {
        boolean disableAnalyzeCompile = settings.getDisableAnalyzeCompile() == compileCheckBox.isSelected();
        boolean disableAnalyzeTest = settings.getDisableAnalyzeTest() == testCheckBox.isSelected();
        boolean disableAnalyzeProvided = settings.getDisableAnalyzeProvided() == providedCheckBox.isSelected();
        boolean fileCacheTimeout = String.valueOf(settings.fileCacheTimeout()).equals(fileCache.getText()); 
        boolean org = settings.getOrganization() == null && organization.getText() == null ||
                (organization.getText() != null && organization.getText().equals(settings.getOrganization()));

        return !(org && fileCacheTimeout && disableAnalyzeCompile && disableAnalyzeTest && disableAnalyzeProvided);

    }

    void apply() {
        // if data change, we publish a notification
        boolean changed = false;
        if (isModified()) {
            changed = true;
        }

        settings.setOrganization(organization.getText());
        settings.setDisableAnalyzeCompile(compileCheckBox.isSelected());
        settings.setDisableAnalyzeTest(testCheckBox.isSelected());
        settings.setDisableAnalyzeProvided(providedCheckBox.isSelected());
        try {
            int t = Integer.parseInt(fileCache.getText());
            settings.setFileCacheTimeout(t);
        } catch (Exception ignore) {
            settings.setFileCacheTimeout(3600);
        }
        if (changed) {
            Notifications$.MODULE$.notifySettingsChanged(project);
            SettingsState.SettingsChangePublisher().onAnalyzerConfigurationChanged(this.project, settings);
        }
    }

    void from() {
        compileCheckBox.setSelected(settings.disableAnalyzeCompile());
        providedCheckBox.setSelected(settings.disableAnalyzeProvided());
        testCheckBox.setSelected(settings.disableAnalyzeTest());
        organization.setText(settings.getOrganization());
        fileCache.setText(String.valueOf(settings.fileCacheTimeout()));
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), 0, 0, true, false));
        mainPanel.setAlignmentX(0.0f);
        mainPanel.setAlignmentY(0.0f);
        mainPanel.setAutoscrolls(false);
        mainPanel.setMaximumSize(new Dimension(-1, -1));
        mainPanel.setMinimumSize(new Dimension(-1, -1));
        mainPanel.setPreferredSize(new Dimension(600, 120));
        final JLabel label1 = new JLabel();
        label1.setText("Disable Scope:");
        mainPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Organization:");
        mainPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        testCheckBox = new JCheckBox();
        testCheckBox.setText("Test");
        mainPanel.add(testCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        compileCheckBox = new JCheckBox();
        compileCheckBox.setText("Compile");
        mainPanel.add(compileCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        providedCheckBox = new JCheckBox();
        providedCheckBox.setText("Provided");
        mainPanel.add(providedCheckBox, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("File Cache Timeout:");
        mainPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileCache = new JTextField();
        fileCache.setColumns(4);
        fileCache.setHorizontalAlignment(2);
        fileCache.setText("3600");
        mainPanel.add(fileCache, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        organization = new JTextField();
        organization.setColumns(12);
        organization.setHorizontalAlignment(2);
        organization.setText("");
        mainPanel.add(organization, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("seconds");
        mainPanel.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}