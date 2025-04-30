package riddlewave;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ProvinceManager {
    private static final String FILE_PATH = "resources/save/provinces.properties";
    private static final Map<String, Boolean> provinceStatus = new HashMap<>();

    static {
        load(); // Panggil saat pertama
    }

    public static void unlock(String province) {
        provinceStatus.put(province, false); // false = unlocked
        save();
    }

    public static boolean isLocked(String province) {
        return provinceStatus.getOrDefault(province, true); // default locked
    }

    public static Map<String, Boolean> getAllStatus() {
        return provinceStatus;
    }

    private static void load() {
        try {
            Properties prop = new Properties();
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Default init
                provinceStatus.put("Java", false); // awal hanya Java terbuka
                provinceStatus.put("Kalimantan", true);
                provinceStatus.put("Sulawesi", true);
       
                save();
            } else {
                try (FileInputStream in = new FileInputStream(FILE_PATH)) {
                    prop.load(in);
                    for (String key : prop.stringPropertyNames()) {
                        provinceStatus.put(key, Boolean.valueOf(prop.getProperty(key)));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat provinsi: " + e.getMessage());
        }
    }

    private static void save() {
        try {
            Properties prop = new Properties();
            for (Map.Entry<String, Boolean> entry : provinceStatus.entrySet()) {
                prop.setProperty(entry.getKey(), entry.getValue().toString());
            }
            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                prop.store(out, "Provinsi Riddle Wave");
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan provinsi: " + e.getMessage());
        }
    }
}
