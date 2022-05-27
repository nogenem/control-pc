import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class BetterRobotTest {

    public BetterRobot robot;
    public String layout = "ABNT2";

    @BeforeAll
    public void startRobot() throws AWTException {
        this.robot = new BetterRobot();
    }

    @Test
    @DisplayName("Type most characters")
    public void typeMostCharacters() throws Exception {
        this.openNotepad();

        robot.type("'\" 1!¹ 2@² 3#³ 4$£ 5%¢ 6¨¬ 7& 8* 9( 0) -_ =+§ / * -\n");
        robot.type("\tqwertyuiop ´` [{ª 7 8 9 +\n");
        robot.type("asdfghjklç ~^ ]}º 4 5 6 .\n");
        robot.type("\\| zxcvbnm ,< .> ;: /?° 1 2 3\n");
        robot.type("0 ,\n");
        robot.type("áàãâ ÁÀÃÂ\n");
    }

    @Test
    @DisplayName("Exec all non-special keys from the layout")
    public void execAllNonSpecialKeysFromLayout() throws Exception {
        this.openNotepad();

        Pattern cmdRegex = Pattern.compile("_\\w+_");
        Set<String> keySet = this.robot
                .getKeyboardLayoutManager()
                .getKeyboardLayoutsMap()
                .get(this.layout)
                .keySet();
        int idx = 1;
        for (String key : keySet) {
            if (!cmdRegex.matcher(key).matches()) {
                System.out.println(idx + ": " + key);
                this.robot.exec(key);
                idx++;
            }
        }
    }

    @Test
    @DisplayName("Exec all non-active keys from the KeyEvent class")
    public void execAllNonActiveKeysFromKeyEvent() throws Exception {
        this.openNotepad();

        Field[] fields = KeyEvent.class.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.startsWith("VK")) {
                int value = field.getInt(null);

                if (!this.isActionKey(value)) {
                    try {
                        this.robot.getRobot().keyPress(value);
                        this.robot.getRobot().keyRelease(value);
                        System.out.println(name + ": " + value);
                    } catch (IllegalArgumentException e) {
                        System.out.println("---> Couldn't find: " + name + " - " + value);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Exec certain commands")
    public void execCertainCommands() throws Exception {
        this.openNotepad();

        List<int[]> keyCodes = new ArrayList<>() {
            {
                add(new int[] { KeyEvent.VK_NUMPAD4 });
                add(new int[] { KeyEvent.VK_SHIFT, KeyEvent.VK_NUMPAD4 });
                add(new int[] { KeyEvent.VK_ALT_GRAPH, KeyEvent.VK_NUMPAD4 });
                add(new int[] { KeyEvent.VK_NUM_LOCK });
                add(new int[] { KeyEvent.VK_NUMPAD7 });
                add(new int[] { KeyEvent.VK_SHIFT, KeyEvent.VK_NUMPAD7 });
                add(new int[] { KeyEvent.VK_ALT_GRAPH, KeyEvent.VK_NUMPAD7 });
                add(new int[] { KeyEvent.VK_NUM_LOCK });
            }
        };

        keyCodes.forEach(keys -> {
            System.out.println(Arrays.toString(keys));
            this.robot.doExec(keys);
        });
    }

    private void openNotepad() throws Exception {
        Runtime.getRuntime().exec("notepad.exe");
        Thread.sleep(1000L);
    }

    /* Copied from KeyEvent, this should be static ¬¬ */
    private boolean isActionKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_HOME:
            case KeyEvent.VK_END:
            case KeyEvent.VK_PAGE_UP:
            case KeyEvent.VK_PAGE_DOWN:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_BEGIN:

            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_KP_UP:
            case KeyEvent.VK_KP_RIGHT:
            case KeyEvent.VK_KP_DOWN:

            case KeyEvent.VK_F1:
            case KeyEvent.VK_F2:
            case KeyEvent.VK_F3:
            case KeyEvent.VK_F4:
            case KeyEvent.VK_F5:
            case KeyEvent.VK_F6:
            case KeyEvent.VK_F7:
            case KeyEvent.VK_F8:
            case KeyEvent.VK_F9:
            case KeyEvent.VK_F10:
            case KeyEvent.VK_F11:
            case KeyEvent.VK_F12:
            case KeyEvent.VK_F13:
            case KeyEvent.VK_F14:
            case KeyEvent.VK_F15:
            case KeyEvent.VK_F16:
            case KeyEvent.VK_F17:
            case KeyEvent.VK_F18:
            case KeyEvent.VK_F19:
            case KeyEvent.VK_F20:
            case KeyEvent.VK_F21:
            case KeyEvent.VK_F22:
            case KeyEvent.VK_F23:
            case KeyEvent.VK_F24:
            case KeyEvent.VK_PRINTSCREEN:
            case KeyEvent.VK_SCROLL_LOCK:
            case KeyEvent.VK_CAPS_LOCK:
            case KeyEvent.VK_NUM_LOCK:
            case KeyEvent.VK_PAUSE:
            case KeyEvent.VK_INSERT:

            case KeyEvent.VK_FINAL:
            case KeyEvent.VK_CONVERT:
            case KeyEvent.VK_NONCONVERT:
            case KeyEvent.VK_ACCEPT:
            case KeyEvent.VK_MODECHANGE:
            case KeyEvent.VK_KANA:
            case KeyEvent.VK_KANJI:
            case KeyEvent.VK_ALPHANUMERIC:
            case KeyEvent.VK_KATAKANA:
            case KeyEvent.VK_HIRAGANA:
            case KeyEvent.VK_FULL_WIDTH:
            case KeyEvent.VK_HALF_WIDTH:
            case KeyEvent.VK_ROMAN_CHARACTERS:
            case KeyEvent.VK_ALL_CANDIDATES:
            case KeyEvent.VK_PREVIOUS_CANDIDATE:
            case KeyEvent.VK_CODE_INPUT:
            case KeyEvent.VK_JAPANESE_KATAKANA:
            case KeyEvent.VK_JAPANESE_HIRAGANA:
            case KeyEvent.VK_JAPANESE_ROMAN:
            case KeyEvent.VK_KANA_LOCK:
            case KeyEvent.VK_INPUT_METHOD_ON_OFF:

            case KeyEvent.VK_AGAIN:
            case KeyEvent.VK_UNDO:
            case KeyEvent.VK_COPY:
            case KeyEvent.VK_PASTE:
            case KeyEvent.VK_CUT:
            case KeyEvent.VK_FIND:
            case KeyEvent.VK_PROPS:
            case KeyEvent.VK_STOP:

            case KeyEvent.VK_HELP:
            case KeyEvent.VK_WINDOWS:
            case KeyEvent.VK_CONTEXT_MENU:
                return true;
        }
        return false;
    }
}