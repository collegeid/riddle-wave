package riddlewave;

import java.io.*;
import java.util.*;

public class ProvinceManager {
    private static final String FILE_PATH = "resources/save/provinces.properties";
    private static final Map<String, Boolean> provinceStatus = new LinkedHashMap<>(); // Gunakan LinkedHashMap agar urutan terjaga

    static {
        load(); // Panggil saat pertama
    }

    public static void unlock(String province) {
        provinceStatus.put(province, false); // false = unlocked
        save();
    }

   public static void unlockNext(String currentProvince) {
    try {
        Properties prop = new Properties();
        File file = new File(FILE_PATH);
        FileInputStream fis = new FileInputStream(file);
        prop.load(fis);
        fis.close();

        List<String> keys = new ArrayList<>(prop.stringPropertyNames());
        Collections.sort(keys); // Pastikan urut jika perlu
        int index = keys.indexOf(currentProvince);

        if (index >= 0 && index + 1 < keys.size()) {
            String nextProvince = keys.get(index + 1);
            if ("true".equals(prop.getProperty(nextProvince))) {
                prop.setProperty(nextProvince, "false");
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    prop.store(fos, "Unlocked next Pulau");
                }

                // ✅ Reload ke memory setelah perubahan file
                load();

                System.out.println("Unlocked province: " + nextProvince);
            }
        }
    } catch (IOException e) {
        System.out.println("Gagal unlock Pulau berikutnya: " + e.getMessage());
    }
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
                // Default initial setup
                provinceStatus.put("Java", false); // Sudah terbuka
                provinceStatus.put("Sumatera", true);
                provinceStatus.put("Kalimantan", true);
                provinceStatus.put("Sulawesi", true);
                provinceStatus.put("Bali", true);
                provinceStatus.put("Nusa", true);
                provinceStatus.put("Maluku", true);
                provinceStatus.put("Papua", true);

                save();
            } else {
                try (FileInputStream in = new FileInputStream(FILE_PATH)) {
                    prop.load(in);
                    provinceStatus.clear();
                    for (String key : prop.stringPropertyNames()) {
                        provinceStatus.put(key, Boolean.parseBoolean(prop.getProperty(key)));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat Pulau: " + e.getMessage());
        }
    }

    private static void save() {
        try {
            Properties prop = new Properties();
            for (Map.Entry<String, Boolean> entry : provinceStatus.entrySet()) {
                prop.setProperty(entry.getKey(), entry.getValue().toString());
            }
            try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
                prop.store(out, "Pulau Riddle Wave");
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan Pulau: " + e.getMessage());
        }
    }
}
