import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.json.JSONArray;

public class BetterRobot {
    // BASED ON: https://stackoverflow.com/a/39141846

    private Robot robot;
    private KeyboardLayoutManager keyboardLayoutManager;

    public static final int BASE_AUTO_DELAY = 10;

    public BetterRobot() throws AWTException {
        this(new Robot());
    }

    public BetterRobot(Robot robot) {
        this.robot = robot;
        this.robot.setAutoDelay(BASE_AUTO_DELAY);

        this.keyboardLayoutManager = new KeyboardLayoutManager();
    }

    /* Type */
    public void type(CharSequence characters) {
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            this.type(character);
        }
    }

    public void type(char character) {
        this.robot.setAutoDelay(0);

        this.robot.keyPress(KeyEvent.VK_ALT);
        this.robot.keyPress(KeyEvent.VK_NUMPAD0);
        this.robot.keyRelease(KeyEvent.VK_NUMPAD0);

        String altCode = Integer.toString(character);
        for (int j = 0; j < altCode.length(); j++) {
            char c = (char) (altCode.charAt(j) + '0');
            this.robot.keyPress(c);
            this.robot.keyRelease(c);
        }
        this.robot.keyRelease(KeyEvent.VK_ALT);

        this.robot.setAutoDelay(BASE_AUTO_DELAY);
    }

    /* Exec */
    public void exec(String layout, String... commands) {
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];

            if (this.isModifierKey(command))
                this.press_key(layout, command);
            else {
                try {
                    this.exec(layout, command);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        for (int i = commands.length - 1; i >= 0; i--) {
            String command = commands[i];

            if (this.isModifierKey(command))
                this.release_key(layout, command);
        }
    }

    private void exec(String layout, String cmd) {
        int[][] keyCodes = this.keyboardLayoutManager.getKeyCodes(layout, cmd);
        if (keyCodes != null) {
            boolean didExecute = false;
            int index = 0;

            while (!didExecute && index < keyCodes.length) {
                didExecute = this.doExec(keyCodes[index]);
                index++;
            }

            if (didExecute)
                return;
        }

        throw new IllegalArgumentException("Couldn't exec command: " + cmd);
    }

    public boolean doExec(int... keyCodes) {
        boolean didExecute = true;

        try {
            didExecute = this.doExec(keyCodes, 0, keyCodes.length);
        } catch (IllegalArgumentException e) {
            System.err.println("Couldn't exec keycode: " + Integer.toHexString(keyCodes[0]));
            didExecute = false;
        }

        return didExecute;
    }

    private boolean doExec(int[] keyCodes, int offset, int length) {
        boolean didExecute = true;
        if (length == 0) {
            return didExecute;
        }

        this.robot.keyPress(keyCodes[offset]);
        try {
            didExecute = this.doExec(keyCodes, offset + 1, length - 1);
        } catch (IllegalArgumentException e) {
            System.err.println("Couldn't exec keycode: " + Integer.toHexString(keyCodes[offset]));
            didExecute = false;
        }
        this.robot.keyRelease(keyCodes[offset]);

        return didExecute;
    }

    /* Press Key */
    public void press_key(String layout, String key) {
        int[][] keyCodes = this.keyboardLayoutManager.getKeyCodes(layout, key);
        if (keyCodes != null) {
            boolean didExecute = false;
            int index = 0;

            while (!didExecute && index < keyCodes.length) {
                try {
                    this.doPressKey(keyCodes[index]);
                    didExecute = true;
                } catch (IllegalArgumentException e) {
                    System.err.println("Couldn't press keycode: " + Arrays.toString(keyCodes[index]));
                    didExecute = false;
                }
                index++;
            }

            if (didExecute)
                return;
        }

        if (!this.isSpecialCommand(key)) {
            this.type(key);
        }
    }

    private void doPressKey(int... keyCodes) {
        this.doPressKey(keyCodes, 0, keyCodes.length);
    }

    private void doPressKey(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }

        this.robot.keyPress(keyCodes[offset]);
        try {
            this.doPressKey(keyCodes, offset + 1, length - 1);
        } catch (IllegalArgumentException e) {
            this.robot.keyRelease(keyCodes[offset]);
            throw e;
        }
    }

    /* Release Key */
    public void release_key(String layout, String key) {
        int[][] keyCodes = this.keyboardLayoutManager.getKeyCodes(layout, key);
        if (keyCodes != null) {

            boolean didExecute = false;
            int index = 0;

            while (!didExecute && index < keyCodes.length) {
                try {
                    this.doReleaseKey(keyCodes[index]);
                    didExecute = true;
                } catch (IllegalArgumentException e) {
                    System.err.println("Couldn't release keycode: " +
                            Arrays.toString(keyCodes[index]));
                    didExecute = false;
                }
                index++;
            }

            if (didExecute)
                return;
        }

        System.err.println("Cant release key " + key);
    }

    private void doReleaseKey(int... keyCodes) {
        // Has to go from the back to the front
        doReleaseKey(keyCodes, keyCodes.length - 1, keyCodes.length);
    }

    private void doReleaseKey(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }

        robot.keyRelease(keyCodes[offset]);
        try {
            doReleaseKey(keyCodes, offset - 1, length - 1);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /* Move Mouse */
    public void move_mouse(JSONArray movements) {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();
        int y = (int) b.getY();

        for (int i = 0; i < movements.length(); i++) {
            JSONArray pos = (JSONArray) movements.get(i);
            x += pos.getInt(0);
            y += pos.getInt(1);

            this.robot.mouseMove(x, y);
        }
    }

    /* Mouse Click */
    public void mouse_down(int button) {
        this.robot.mousePress(button);
    }

    public void mouse_up(int button) {
        this.robot.mouseRelease(button);
    }

    public void mouse_click(int button) {
        this.robot.mousePress(button);
        this.robot.mouseRelease(button);
    }

    public void scroll_up(float sensibility) {
        this.robot.mouseWheel(Math.round(-5 * sensibility));
    }

    public void scroll_down(float sensibility) {
        this.robot.mouseWheel(Math.round(5 * sensibility));
    }

    /* Extra functions */
    public Robot getRobot() {
        return this.robot;
    }

    public KeyboardLayoutManager getKeyboardLayoutManager() {
        return this.keyboardLayoutManager;
    }

    private boolean isModifierKey(String key) {
        Pattern modifiersRegex = Pattern.compile(
                "_(shift|ctrl|control|win|windows|cmd|command|alt|alt_gr|alt_graph)_");
        return modifiersRegex.matcher(key).matches();
    }

    private boolean isSpecialCommand(String key) {
        Pattern modifiersRegex = Pattern.compile("_.+_");
        return modifiersRegex.matcher(key).matches();
    }
}