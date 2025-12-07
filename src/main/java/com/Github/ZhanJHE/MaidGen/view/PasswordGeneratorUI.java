package com.Github.ZhanJHE.MaidGen.view;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.model.PasswordSegmentOptions;
import com.Github.ZhanJHE.MaidGen.model.enums.DataSource;
import com.Github.ZhanJHE.MaidGen.model.enums.WordCase;
import com.Github.ZhanJHE.MaidGen.service.PasswordGeneratorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordGeneratorUI extends JFrame {

    private final PasswordGeneratorService passwordGeneratorService;

    private JTextField numberOfPasswordsField;
    private JCheckBox saveToFileCheckBox;
    private JTextField savePathField;
    private JButton savePathButton;
    private JPanel segmentsContainerPanel;
    private JTextArea resultArea;

    private final List<PasswordSegmentPanel> segmentPanels = new ArrayList<>();

    public PasswordGeneratorUI(PasswordGeneratorService passwordGeneratorService) {
        this.passwordGeneratorService = passwordGeneratorService;
        setTitle("MaidGen - 密码生成器");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Main options panel
        JPanel mainOptionsPanel = new JPanel(new GridBagLayout());
        mainOptionsPanel.setBorder(BorderFactory.createTitledBorder("主要选项"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainOptionsPanel.add(new JLabel("生成密码数量:"), gbc);

        gbc.gridx = 1;
        numberOfPasswordsField = new JTextField("10", 5);
        mainOptionsPanel.add(numberOfPasswordsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        saveToFileCheckBox = new JCheckBox("保存到文件");
        mainOptionsPanel.add(saveToFileCheckBox, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        savePathField = new JTextField(30);
        savePathField.setEnabled(false);
        mainOptionsPanel.add(savePathField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        savePathButton = new JButton("选择路径");
        savePathButton.setEnabled(false);
        mainOptionsPanel.add(savePathButton, gbc);

        saveToFileCheckBox.addActionListener(e -> {
            boolean selected = saveToFileCheckBox.isSelected();
            savePathField.setEnabled(selected);
            savePathButton.setEnabled(selected);
        });

        savePathButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择保存路径");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                savePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Segments panel
        segmentsContainerPanel = new JPanel();
        segmentsContainerPanel.setLayout(new BoxLayout(segmentsContainerPanel, BoxLayout.Y_AXIS));

        JScrollPane segmentsScrollPane = new JScrollPane(segmentsContainerPanel);
        segmentsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        segmentsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        segmentsScrollPane.setBorder(BorderFactory.createTitledBorder("密码段配置"));

        JButton addSegmentButton = new JButton("添加密码段");
        addSegmentButton.addActionListener(e -> addSegmentPanel());

        JPanel segmentsControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        segmentsControlPanel.add(addSegmentButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(mainOptionsPanel, BorderLayout.NORTH);
        topPanel.add(segmentsControlPanel, BorderLayout.CENTER);

        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("生成的密码"));

        // Generate button
        JButton generateButton = new JButton("生成密码");
        generateButton.setFont(generateButton.getFont().deriveFont(Font.BOLD, 16f));
        generateButton.addActionListener(e -> generatePasswords());

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.add(resultScrollPane, BorderLayout.CENTER);
        bottomPanel.add(generateButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(segmentsScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add one segment by default
        addSegmentPanel();
    }

    private void addSegmentPanel() {
        PasswordSegmentPanel newSegment = new PasswordSegmentPanel(this::removeSegmentPanel);
        segmentPanels.add(newSegment);
        segmentsContainerPanel.add(newSegment);
        segmentsContainerPanel.revalidate();
        segmentsContainerPanel.repaint();
    }

    private void removeSegmentPanel(PasswordSegmentPanel segmentPanel) {
        segmentPanels.remove(segmentPanel);
        segmentsContainerPanel.remove(segmentPanel);
        segmentsContainerPanel.revalidate();
        segmentsContainerPanel.repaint();
    }

    private void generatePasswords() {
        try {
            PasswordOptions options = new PasswordOptions();
            options.setNumberOfPasswords(Integer.parseInt(numberOfPasswordsField.getText()));
            options.setSaveToFile(saveToFileCheckBox.isSelected());
            if (saveToFileCheckBox.isSelected()) {
                options.setSavePath(savePathField.getText());
            }

            List<PasswordSegmentOptions> segmentOptionsList = new ArrayList<>();
            for (PasswordSegmentPanel panel : segmentPanels) {
                segmentOptionsList.add(panel.getSegmentOptions());
            }
            options.setPasswordSegments(segmentOptionsList);

            List<String> generatedPasswords = passwordGeneratorService.generatePasswordsToList(options);

            StringBuilder sb = new StringBuilder();
            for (String pw : generatedPasswords) {
                sb.append(pw).append("\n");
            }
            resultArea.setText(sb.toString());

            if (options.isSaveToFile() && options.getSavePath() != null && !options.getSavePath().isEmpty()) {
                passwordGeneratorService.getPasswordRepository().savePasswords(generatedPasswords, options.getSavePath());
                JOptionPane.showMessageDialog(this, "密码已成功保存到: " + options.getSavePath(), "保存成功", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "生成密码时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    interface SegmentRemoveListener {
        void remove(PasswordSegmentPanel panel);
    }

    private static class PasswordSegmentPanel extends JPanel {
        private final JComboBox<DataSource> dataSourceComboBox;
        private final JTextField filePathField;
        private final JButton filePathButton;
        private final JTextField customFieldField;
        private final JComboBox<WordCase> wordCaseComboBox;
        private final JSpinner lengthSpinner;

        private final JLabel filePathLabel;
        private final JLabel customFieldLabel;
        private final JLabel wordCaseLabel;
        private final JLabel lengthLabel;

        public PasswordSegmentPanel(SegmentRemoveListener removeListener) {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEtchedBorder(),
                    new EmptyBorder(5, 5, 5, 5))
            );
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 5, 2, 5);
            gbc.anchor = GridBagConstraints.WEST;

            dataSourceComboBox = new JComboBox<>(DataSource.values());
            filePathField = new JTextField(20);
            filePathButton = new JButton("...");
            customFieldField = new JTextField(20);
            wordCaseComboBox = new JComboBox<>(WordCase.values());
            lengthSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 50, 1));

            gbc.gridx = 0;
            gbc.gridy = 0;
            add(new JLabel("类型:"), gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(dataSourceComboBox, gbc);

            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;

            gbc.gridy = 1;
            gbc.gridx = 0;
            filePathLabel = new JLabel("文件路径:");
            add(filePathLabel, gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            add(filePathField, gbc);
            gbc.gridx = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            add(filePathButton, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            customFieldLabel = new JLabel("指定字符串:");
            add(customFieldLabel, gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(customFieldField, gbc);

            gbc.gridy = 3;
            gbc.gridx = 0;
            lengthLabel = new JLabel("长度:");
            add(lengthLabel, gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            add(lengthSpinner, gbc);

            gbc.gridy = 4;
            gbc.gridx = 0;
            wordCaseLabel = new JLabel("单词形式:");
            add(wordCaseLabel, gbc);
            gbc.gridx = 1;
            add(wordCaseComboBox, gbc);

            gbc.gridy = 0;
            gbc.gridx = 3;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            JButton removeButton = new JButton("移除");
            removeButton.addActionListener(e -> removeListener.remove(this));
            add(removeButton, gbc);

            dataSourceComboBox.addActionListener(e -> updateComponentVisibility());
            filePathButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            });

            updateComponentVisibility();
        }

        private void updateComponentVisibility() {
            DataSource selected = (DataSource) dataSourceComboBox.getSelectedItem();

            boolean isFile = selected == DataSource.EXTERNAL_FILE;
            boolean isWord = selected == DataSource.EXTERNAL_FILE || selected == DataSource.WORD_LIST_JSON;
            boolean isCustom = selected == DataSource.CUSTOM_FIELD;
            boolean needsLength = selected == DataSource.RANDOM_NUMBERS || selected == DataSource.RANDOM_PUNCTUATION || selected == DataSource.WORD_LIST_JSON || selected == DataSource.EXTERNAL_FILE;

            filePathLabel.setVisible(isFile);
            filePathField.setVisible(isFile);
            filePathButton.setVisible(isFile);

            customFieldLabel.setVisible(isCustom);
            customFieldField.setVisible(isCustom);

            wordCaseLabel.setVisible(isWord);
            wordCaseComboBox.setVisible(isWord);

            lengthLabel.setVisible(needsLength);
            lengthSpinner.setVisible(needsLength);

            if (selected == DataSource.WORD_LIST_JSON) {
                ((SpinnerNumberModel) lengthSpinner.getModel()).setMinimum(3);
                ((SpinnerNumberModel) lengthSpinner.getModel()).setMaximum(9);
                if ((Integer) lengthSpinner.getValue() > 9 || (Integer) lengthSpinner.getValue() < 3) {
                    lengthSpinner.setValue(3);
                }
            } else {
                ((SpinnerNumberModel) lengthSpinner.getModel()).setMinimum(1);
                ((SpinnerNumberModel) lengthSpinner.getModel()).setMaximum(50);
            }
        }

        public PasswordSegmentOptions getSegmentOptions() {
            PasswordSegmentOptions options = new PasswordSegmentOptions();
            DataSource selectedDataSource = (DataSource) dataSourceComboBox.getSelectedItem();
            options.setDataSource(selectedDataSource);

            switch (selectedDataSource) {
                case EXTERNAL_FILE:
                    options.setFilePath(filePathField.getText());
                    options.setWordCase((WordCase) wordCaseComboBox.getSelectedItem());
                    options.setLength((Integer) lengthSpinner.getValue());
                    break;
                case WORD_LIST_JSON:
                    options.setLength((Integer) lengthSpinner.getValue());
                    options.setWordCase((WordCase) wordCaseComboBox.getSelectedItem());
                    break;
                case RANDOM_PUNCTUATION:
                case RANDOM_NUMBERS:
                    options.setLength((Integer) lengthSpinner.getValue());
                    break;
                case CUSTOM_FIELD:
                    options.setCustomField(customFieldField.getText());
                    break;
            }
            return options;
        }
    }
}