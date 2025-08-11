package com.sammc.puppet;

import java.io.File;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.scene_edit.SceneEditFrame;

/**
 * Run the application.
 * 
 * @author sammc
 */
public class Snappy {

    public static void main(String[] args) {
        // Make sure the projects directory exists
        File projectDir = new File(Util.PROJECTS_DIRECTORY);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }

        // Make the actual application
        SceneEditFrame frame = new SceneEditFrame();
    }


}