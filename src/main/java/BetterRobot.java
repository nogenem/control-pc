import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.json.JSONArray;

public class BetterRobot {
    // BASED ON: https://stackoverflow.com/a/39141846

    private Robot robot;

    public static final int BASE_AUTO_DELAY = 10;

    public BetterRobot() throws AWTException {
        this(new Robot());
    }

    public BetterRobot(Robot robot) {
        this.robot = robot;
        this.robot.setAutoDelay(BASE_AUTO_DELAY);
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
    public void exec(String... commands) {
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];
            int modifiedKey = this.getModifierKeyValue(command);

            if (modifiedKey > 0)
                robot.keyPress(modifiedKey);
            else {
                try {
                    exec(command);
                } catch (IllegalArgumentException e) {
                    type(command);
                }
            }
        }

        for (int i = commands.length - 1; i >= 0; i--) {
            String command = commands[i];
            int modifiedKey = this.getModifierKeyValue(command);

            if (modifiedKey > 0)
                robot.keyRelease(modifiedKey);
        }
    }

    private int getModifierKeyValue(String key) {
        key = key.toLowerCase();
        switch (key) {
            case "_shift_":
                return KeyEvent.VK_SHIFT;
            case "_ctrl_":
            case "_control_":
                return KeyEvent.VK_CONTROL;
            case "_win_":
            case "_windows_":
            case "_cmd_":
            case "_command_":
                return KeyEvent.VK_WINDOWS;
            case "_alt_":
            case "_left_alt_":
                return KeyEvent.VK_ALT;
            case "_alt_gr_":
            case "_right_alt_":
                return KeyEvent.VK_ALT_GRAPH;
            default:
                return -1;
        }
    }

    public void exec(String cmd) {
        cmd = cmd.toLowerCase();
        switch (cmd) {
            case "_play_pause_":
            case "_play_":
            case "_pause_":
                doExec(KeyEvent.VK_CONTROL, KeyEvent.VK_F7);
                break;
            case "_volume_up_":
                doExec(KeyEvent.VK_CONTROL, KeyEvent.VK_F11);
                break;
            case "_volume_down_":
                doExec(KeyEvent.VK_CONTROL, KeyEvent.VK_F12);
                break;
            case "_volume_off_":
            case "_mute_":
                doExec(KeyEvent.VK_CONTROL, KeyEvent.VK_F10);
                break;
            case "_skip_previous_":
                doExec(KeyEvent.VK_CONTROL, KeyEvent.VK_F8);
                break;
            case "_skip_next_":
                doExec(KeyEvent.VK_CONTROL, KeyEvent.VK_F9);
                break;
            case "_tab_":
                doExec(KeyEvent.VK_TAB);
                break;
            case "_capslock_":
            case "_caps_lock_":
                doExec(KeyEvent.VK_CAPS_LOCK);
                break;
            case "_enter_":
                doExec(KeyEvent.VK_ENTER);
                break;
            case "_context_menu_":
                doExec(KeyEvent.VK_CONTEXT_MENU);
                break;
            case "_num_lock_":
                doExec(KeyEvent.VK_NUM_LOCK);
                break;
            case "_backspace_":
                doExec(KeyEvent.VK_BACK_SPACE);
                break;
            case "_escape_":
            case "_esc_":
                doExec(KeyEvent.VK_ESCAPE);
                break;
            case "_page_up_":
                doExec(KeyEvent.VK_PAGE_UP);
                break;
            case "_page_down_":
                doExec(KeyEvent.VK_PAGE_DOWN);
                break;
            case "_delete_":
                doExec(KeyEvent.VK_DELETE);
                break;
            case "_print_":
                doExec(KeyEvent.VK_PRINTSCREEN);
                break;
            case "_home_":
                doExec(KeyEvent.VK_HOME);
                break;
            case "_end_":
                doExec(KeyEvent.VK_END);
                break;
            case "_insert_":
                doExec(KeyEvent.VK_INSERT);
                break;
            case "_up_":
                doExec(KeyEvent.VK_UP);
                break;
            case "_down_":
                doExec(KeyEvent.VK_DOWN);
                break;
            case "_left_":
                doExec(KeyEvent.VK_LEFT);
                break;
            case "_right_":
                doExec(KeyEvent.VK_RIGHT);
                break;
            case "_f1_":
                doExec(KeyEvent.VK_F1);
                break;
            case "_f2_":
                doExec(KeyEvent.VK_F2);
                break;
            case "_f3_":
                doExec(KeyEvent.VK_F3);
                break;
            case "_f4_":
                doExec(KeyEvent.VK_F4);
                break;
            case "_f5_":
                doExec(KeyEvent.VK_F5);
                break;
            case "_f6_":
                doExec(KeyEvent.VK_F6);
                break;
            case "_f7_":
                doExec(KeyEvent.VK_F7);
                break;
            case "_f8_":
                doExec(KeyEvent.VK_F8);
                break;
            case "_f9_":
                doExec(KeyEvent.VK_F9);
                break;
            case "_f10_":
                doExec(KeyEvent.VK_F10);
                break;
            case "_f11_":
                doExec(KeyEvent.VK_F11);
                break;
            case "_f12_":
                doExec(KeyEvent.VK_F12);
                break;
            default:
                throw new IllegalArgumentException("Cannot exec command " + cmd);
        }
    }

    private void doExec(int... keyCodes) {
        doExec(keyCodes, 0, keyCodes.length);
    }

    private void doExec(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }

        robot.keyPress(keyCodes[offset]);
        doExec(keyCodes, offset + 1, length - 1);
        robot.keyRelease(keyCodes[offset]);
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

    public void scroll_up(int sensibility) {
        this.robot.mouseWheel(-5 * sensibility);
    }

    public void scroll_down(int sensibility) {
        this.robot.mouseWheel(5 * sensibility);
    }
}