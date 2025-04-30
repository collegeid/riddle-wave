package riddlewave;

import java.io.*;
import java.util.*;

public class ProvinceManager {
    private static final String FILE_PATH = "resources/save/provinces.properties";
    private static final Map<String, Boolean> provinceStatus = new LinkedHashMap<>();

    private static final String[] ORDERED_KEYS = {
        "Sumatera", "Java", "Bali", "Nusa",
        "Kalimantan", "Sulawesi", "Maluku", "Papua"
    };

    static {
        load();
    }

    public static void unlock(String province) {
        provinceStatus.put(province, false); // false = unlocked
        save();
    }

    public static void unlockNext(String currentProvince) {
        int index = -1;
        for (int i = 0; i < ORDERED_KEYS.length; i++) {
            if (ORDERED_KEYS[i].equals(currentProvince)) {
                index = i;
                break;
            }
        }

        if (index != -1 && index + 1 < ORDERED_KEYS.length) {
            String nextProvince = ORDERED_KEYS[index + 1];
            if (provinceStatus.getOrDefault(nextProvince, true)) {
                provinceStatus.put(nextProvince, false);
                save();
                System.out.println("Unlocked province: " + nextProvince);
            }
        }
    }

    public static boolean isLocked(String province) {
        return provinceStatus.getOrDefault(province, true);
    }

    public static Map<String, Boolean> getAllStatus() {
        return new LinkedHashMap<>(provinceStatus); // Return copy to prevent outside mutation
    }

    private static void load() {
        try {
            File file = new File(FILE_PATH);
            provinceStatus.clear();

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Init default
                provinceStatus.put("Sumatera", false); // awal buka
                provinceStatus.put("Java", true);
                provinceStatus.put("Bali", true);
                provinceStatus.put("Nusa", true);
                provinceStatus.put("Kalimantan", true);
                provinceStatus.put("Sulawesi", true);
                provinceStatus.put("Maluku", true);
                provinceStatus.put("Papua", true);
                save();
            } else {
                Properties prop = new Properties();
                try (FileInputStream in = new FileInputStream(file)) {
                    prop.load(in);
                }

                for (String key : ORDERED_KEYS) {
                    String val = prop.getProperty(key, "true");
                    provinceStatus.put(key, Boolean.parseBoolean(val));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat Pulau: " + e.getMessage());
        }
    }

    private static void save() {
        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {
            writer.println("#Unlocked next Pulau");
            writer.println("#" + new Date());

            for (String key : ORDERED_KEYS) {
                boolean status = provinceStatus.getOrDefault(key, true);
                writer.println(key + "=" + status);
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan Pulau: " + e.getMessage());
        }
    }
}
