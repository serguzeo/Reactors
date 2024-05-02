import Reactors.Reactor;
import Reactors.ReactorsOwner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static Importers.UniversalReactorImporter.getUniversalReactorImporter;

public class MainWindow extends JFrame {

    private JButton importButton;
    private JPanel panel;
    private JTree reactorsTree;
    private ReactorsOwner reactorsOwner = new ReactorsOwner();

    public MainWindow() {
        setContentPane(panel);
        setTitle("Reactors");
        setSize(500, 500);

        addListeners();

        DefaultTreeModel treeModel = (DefaultTreeModel) reactorsTree.getModel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Реакторы");
        treeModel.setRoot(root);

        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void addListeners() {

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Файлы .yaml / .xml / .json", "yaml", "xml", "json"
                );
                fileChooser.setFileFilter(filter);

                File defaultDirectory = new File("./");
                fileChooser.setCurrentDirectory(defaultDirectory);

                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    if (!(file.getName().toLowerCase().endsWith(".xml") ||
                            file.getName().toLowerCase().endsWith(".yaml") ||
                            file.getName().toLowerCase().endsWith(".json"))) {
                        JOptionPane.showMessageDialog(
                                null, "Выберите файл формата .yaml / .xml / .json", "Ошибка",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    getUniversalReactorImporter().importReactorsFromFile(file, reactorsOwner);

                    fillTree();

                    importButton.setText("Выбран: " + file.getName());
                }
            }
        });

        reactorsTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() != 2) { return; }

                TreePath selectionPath = reactorsTree.getSelectionPath();
                if (selectionPath == null) { return; }

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
                String nodeText = selectedNode.getUserObject().toString();
                if (nodeText.equals("Реакторы")) { return; }

                Reactor selectedReactor = (Reactor) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject();

                JOptionPane.showMessageDialog(
                        null,
                        selectedReactor.getFullDescription(),
                        "Полное описание реактора",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }

    private void fillTree() {
        DefaultTreeModel treeModel = (DefaultTreeModel) reactorsTree.getModel();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Реакторы");

        for (String type : reactorsOwner.getReactorMap().keySet()) {
            DefaultMutableTreeNode reactorNode = new DefaultMutableTreeNode(reactorsOwner.getReactorMap().get(type));
            root.add(reactorNode);
        }

        treeModel.setRoot(root);
        reactorsTree.setEnabled(true);
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
