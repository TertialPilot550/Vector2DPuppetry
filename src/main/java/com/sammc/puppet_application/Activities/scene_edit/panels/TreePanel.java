package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.ScrollPane;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;

/**
 * Container JPanel that displays the project assets and files to the user in the control panel.
 * @author sammc
 */
public class TreePanel extends JPanel {

    // Owning instance of the scene editor
    private SceneEditFrame parent;

    // Components of Tree Display
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private JTree tree;

    // Constructor
    public TreePanel(SceneEditFrame parent) {
        this.parent = parent;
        setVisible(true);
        ScrollPane scroll = new ScrollPane();
        scroll.setBounds(0, 0, 200, 1600);
        add(scroll);

        root = new DefaultMutableTreeNode(new File(parent.getProjectRootPath()));
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        tree.setShowsRootHandles(true);
        refresh();
        scroll.add(tree);
        
    }
    
    /**
     * Returns the path of the selected file in the project file overview.
     * @return
     */
    public String getSelectedPath() {
        if (tree.getSelectionPath() == null) return "";
        return tree.getSelectionPath().getLastPathComponent().toString();
        // String selected_file_path = tree.getSelectionPath().toString();
        // System.out.println(selected_file_path);
        // selected_file_path = selected_file_path.substring(1, selected_file_path.length() - 1); // take the brackets off the end
        // selected_file_path = selected_file_path.replace(", ", "/");                    // replace the commas with 
        // selected_file_path = selected_file_path.replace(" ", "");
        // return selected_file_path;
    }

    /**
     * Refresh the display based on the owning instance's current 'project path' to show
     * the files for that particular project
     */
    public void refresh() {
        root.removeAllChildren();
        File file_root = new File(parent.getProjectRootPath());
        createChildren(file_root, root);
        model.reload();
    }
    

    /**
     * Internal Recursive method that adds a file and all of it's children to the tree display.
     * @param fileRoot
     * @param node
     */
    private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
        File[] files = fileRoot.listFiles();
        if (files == null) return;

        for (File file : files) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file);
            node.add(childNode);
            if (file.isDirectory()) {
                createChildren(file, childNode);
            }
        }
    }
}



    // private class FileNode {

    //     private File file;

    //     public FileNode(File file) {
    //         this.file = file;
    //     }

    //     @Override
    //     public String toString() {
    //         String name = file.getName();
    //         if (name.equals("")) {
    //             return file.getAbsolutePath();
    //         } else {
    //             return name;
    //         }
    //     }
    // }
