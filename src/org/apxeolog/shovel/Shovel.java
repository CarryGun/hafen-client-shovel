package org.apxeolog.shovel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Created by APXEOLOG on 31/08/2015.
 * Main class to init startup data like configs etc
 */
public class Shovel {
    private static String version = "1.0.1";
    private static Settings settings;
    private static File workingDirectory;

    /**
     * Load settings from settings.json
     */
    private static void loadSettings() {
        File settingsFile = new File(workingDirectory, "settings.json");
        try {
            if (settingsFile.exists()) {
                Gson gson = new Gson();
                settings = gson.fromJson(new FileReader(settingsFile), Settings.class);
            } else {
                settings = new Settings();
                saveSettings();
            }
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot load settings:", ex.getMessage());
            settings = new Settings();
            saveSettings();
        }
    }

    /**
     * Get Shovel settings
     * @return
     */
    public static Settings getSettings() {
        return settings;
    }

    /**
     * Save settings to file
     */
    public static void saveSettings() {
        File settingsFile = new File(workingDirectory, "settings.json");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.write(settingsFile.toPath(), gson.toJson(settings).getBytes(Charset.forName("utf-8")), StandardOpenOption.CREATE);
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot save settings file:", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Get Shovel client version
     * @return
     */
    public static String getVersion() {
        return version;
    }

    /**
     * Init method
     */
    public static void init() {
        try {
            // Detect working directory, in case jar not in WORKING_DIR
            workingDirectory = new File(Shovel.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot detect jar path, using working directory instead");
            workingDirectory = new File("");
        }
        loadSettings();
    }

    /**
     * Directory for custom resources
     * @return
     */
    public static File getCustomResourceDir() {
        File customResDir = new File(workingDirectory, "res");
        if (!customResDir.exists()) customResDir.mkdir();
        return customResDir;
    }

    public static void main(String[] args) {
        // Just for testing purposes
    }
}
