import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;

public class KeyboardLayoutManager {
    // BASED ON:
    // https://github.com/holgerbrandl/windowlicker/blob/master/src/core/main/com/objogate/wl/keyboard/KeyboardLayout.java

    public final static String DEFAULT_LAYOUT = "ABNT2";
    // It gets from the classpath: target/classes/...
    public final static String LAYOUTS_BASE_PATH = "public/keyboardLayouts";

    private Map<String, Map<String, int[][]>> keyboardLayoutsMap;

    public KeyboardLayoutManager() {
        this.keyboardLayoutsMap = new HashMap<>();
        this.loadKeyboardLayouts();
    }

    public int[][] getKeyCodes(String key) {
        return this.getKeyCodes(KeyboardLayoutManager.DEFAULT_LAYOUT, key);
    }

    public int[][] getKeyCodes(String keyboardLayout, String key) {
        if (!this.keyboardLayoutsMap.containsKey(keyboardLayout)) {
            return null;
        }

        Map<String, int[][]> map = this.keyboardLayoutsMap.get(keyboardLayout);

        if (!map.containsKey(key)) {
            return null;
        }

        return map.get(key);
    }

    private void loadKeyboardLayouts() {
        String filesPath = KeyboardLayoutManager.LAYOUTS_BASE_PATH;
        try (ScanResult scanResult = new ClassGraph()
                .disableNestedJarScanning()
                .disableModuleScanning()
                .acceptPaths(filesPath)
                .scan()) {

            ResourceList allResources = scanResult.getAllResources();

            allResources.forEach((Resource resource) -> {
                try {
                    String name = resource.getPath()
                            .replace(filesPath + "/", "")
                            .replace(".json", "");
                    JSONArray layoutData = new JSONArray(resource.getContentAsString());

                    this.addKeyboardLayout(name, layoutData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void addKeyboardLayout(String name, JSONArray layoutData) {
        if (!this.keyboardLayoutsMap.containsKey(name)) {
            this.keyboardLayoutsMap.put(name, new HashMap<>());
        }

        Map<String, int[][]> map = this.keyboardLayoutsMap.get(name);
        for (int i = 0; i < layoutData.length(); i++) {
            JSONArray row = layoutData.getJSONArray(i);

            for (int j = 0; j < row.length(); j++) {
                JSONObject keyData = row.getJSONObject(j);

                if (!keyData.has("key")) {
                    continue;
                }

                String key = keyData.getString("key");
                JSONArray keyCodesArrWrapper = keyData.getJSONArray("keyCodes");

                for (int k = 0; k < keyCodesArrWrapper.length(); k++) {
                    JSONArray keyCodesArr = keyCodesArrWrapper.getJSONArray(k);

                    int[] keyCodesToBeAdded = new int[keyCodesArr.length()];

                    boolean keyCodeNotFound = false;
                    for (int l = 0; l < keyCodesArr.length(); l++) {
                        String keyCodeStr = keyCodesArr.getString(l);
                        int keyCodeInt = this.getKeyEventKeyCodeFor(keyCodeStr);

                        if (keyCodeInt > -1) {
                            keyCodesToBeAdded[l] = keyCodeInt;
                        } else {
                            keyCodeNotFound = true;
                        }
                    }

                    if (keyCodeNotFound) {
                        continue;
                    }

                    int[][] keyCodes = null;
                    if (map.containsKey(key)) {
                        keyCodes = map.get(key);
                    } else {
                        keyCodes = new int[keyCodesArrWrapper.length()][];
                    }
                    keyCodes[k] = keyCodesToBeAdded;

                    map.put(key, keyCodes);
                }
            }
        }
    }

    public int getKeyEventKeyCodeFor(String keyCodeStr) {
        try {
            Field field = KeyEvent.class.getField("VK_" + keyCodeStr.toUpperCase());
            field.setAccessible(true);
            return field.getInt(null);
        } catch (Exception e) {
            System.err.println("--> " + e.toString());
            return -1;
        }
    }

    public Map<String, Map<String, int[][]>> getKeyboardLayoutsMap() {
        return this.keyboardLayoutsMap;
    }

    public boolean hasLayout(String layout) {
        return this.keyboardLayoutsMap.containsKey(layout);
    }
}