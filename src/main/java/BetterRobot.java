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

    public BetterRobot() throws AWTException {
        this.robot = new Robot();
    }

    public BetterRobot(Robot robot) {
        this.robot = robot;
    }
    
//    public static void main(String[] args) throws Exception {
//        Runtime.getRuntime().exec("notepad.exe");
//        Thread.sleep(1000L);
//        Keyboard keyboard = new Keyboard();
//        keyboard.type("`1234567890-=[]\\;',./\n");
//        keyboard.type("~!@#$%^&*()_+{}|:\"<>?\n");
//        keyboard.type("abcdefghijklmnopqrstuvwxyz\n\tABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        keyboard.type("\n\n\twh\bat");
//        keyboard.exec("ctrl", "f7");
//    }
    
    /* Type */
    public void type(CharSequence characters) {
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            try {
                type(character);
            } catch (IllegalArgumentException e) {
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_NUMPAD0);
                robot.keyRelease(KeyEvent.VK_NUMPAD0);
                String altCode = Integer.toString(character);
                for (int j = 0; j < altCode.length(); j++) {
                    char c = (char) (altCode.charAt(j) + '0');
                    robot.keyPress(c);
                    robot.keyRelease(c);
                }
                robot.keyRelease(KeyEvent.VK_ALT);
            }
        }
    }

    public void type(char character) {
        switch (character) {
            case 'a':
                doType(KeyEvent.VK_A);
                break;
            case 'b':
                doType(KeyEvent.VK_B);
                break;
            case 'c':
                doType(KeyEvent.VK_C);
                break;
            case 'd':
                doType(KeyEvent.VK_D);
                break;
            case 'e':
                doType(KeyEvent.VK_E);
                break;
            case 'f':
                doType(KeyEvent.VK_F);
                break;
            case 'g':
                doType(KeyEvent.VK_G);
                break;
            case 'h':
                doType(KeyEvent.VK_H);
                break;
            case 'i':
                doType(KeyEvent.VK_I);
                break;
            case 'j':
                doType(KeyEvent.VK_J);
                break;
            case 'k':
                doType(KeyEvent.VK_K);
                break;
            case 'l':
                doType(KeyEvent.VK_L);
                break;
            case 'm':
                doType(KeyEvent.VK_M);
                break;
            case 'n':
                doType(KeyEvent.VK_N);
                break;
            case 'o':
                doType(KeyEvent.VK_O);
                break;
            case 'p':
                doType(KeyEvent.VK_P);
                break;
            case 'q':
                doType(KeyEvent.VK_Q);
                break;
            case 'r':
                doType(KeyEvent.VK_R);
                break;
            case 's':
                doType(KeyEvent.VK_S);
                break;
            case 't':
                doType(KeyEvent.VK_T);
                break;
            case 'u':
                doType(KeyEvent.VK_U);
                break;
            case 'v':
                doType(KeyEvent.VK_V);
                break;
            case 'w':
                doType(KeyEvent.VK_W);
                break;
            case 'x':
                doType(KeyEvent.VK_X);
                break;
            case 'y':
                doType(KeyEvent.VK_Y);
                break;
            case 'z':
                doType(KeyEvent.VK_Z);
                break;
            case 'A':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_A);
                break;
            case 'B':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_B);
                break;
            case 'C':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_C);
                break;
            case 'D':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_D);
                break;
            case 'E':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_E);
                break;
            case 'F':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_F);
                break;
            case 'G':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_G);
                break;
            case 'H':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_H);
                break;
            case 'I':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_I);
                break;
            case 'J':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_J);
                break;
            case 'K':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_K);
                break;
            case 'L':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_L);
                break;
            case 'M':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_M);
                break;
            case 'N':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_N);
                break;
            case 'O':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_O);
                break;
            case 'P':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_P);
                break;
            case 'Q':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Q);
                break;
            case 'R':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_R);
                break;
            case 'S':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_S);
                break;
            case 'T':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_T);
                break;
            case 'U':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_U);
                break;
            case 'V':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_V);
                break;
            case 'W':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_W);
                break;
            case 'X':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_X);
                break;
            case 'Y':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Y);
                break;
            case 'Z':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Z);
                break;
            case '`':
                doType(KeyEvent.VK_BACK_QUOTE);
                break;
            case '0':
                doType(KeyEvent.VK_0);
                break;
            case '1':
                doType(KeyEvent.VK_1);
                break;
            case '2':
                doType(KeyEvent.VK_2);
                break;
            case '3':
                doType(KeyEvent.VK_3);
                break;
            case '4':
                doType(KeyEvent.VK_4);
                break;
            case '5':
                doType(KeyEvent.VK_5);
                break;
            case '6':
                doType(KeyEvent.VK_6);
                break;
            case '7':
                doType(KeyEvent.VK_7);
                break;
            case '8':
                doType(KeyEvent.VK_8);
                break;
            case '9':
                doType(KeyEvent.VK_9);
                break;
            case '-':
                doType(KeyEvent.VK_MINUS);
                break;
            case '=':
                doType(KeyEvent.VK_EQUALS);
                break;
            case '~':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE);
                break;
            case '!':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_1);
                break;
            case '@':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_2);
                break;
            case '#':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_3);
                break;
            case '$':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_4);
                break;
            case '%':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_5);
                break;
            case '^':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_6);
                break;
            case '&':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_7);
                break;
            case '*':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_8);
                break;
            case '(':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_9);
                break;
            case ')':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_0);
                break;
            case '_':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS);
                break;
            case '+':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_EQUALS);
                break;
            case '\t':
                doType(KeyEvent.VK_TAB);
                break;
            case '\n':
                doType(KeyEvent.VK_ENTER);
                break;
            case '[':
                doType(KeyEvent.VK_OPEN_BRACKET);
                break;
            case ']':
                doType(KeyEvent.VK_CLOSE_BRACKET);
                break;
            case '\\':
                doType(KeyEvent.VK_BACK_SLASH);
                break;
            case '{':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET);
                break;
            case '}':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET);
                break;
            case '|':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH);
                break;
            case ';':
                doType(KeyEvent.VK_SEMICOLON);
                break;
            case ':':
                doType(KeyEvent.VK_COLON);
                break;
            case '\'':
                doType(KeyEvent.VK_QUOTE);
                break;
            case '"':
                doType(KeyEvent.VK_QUOTEDBL);
                break;
            case ',':
                doType(KeyEvent.VK_COMMA);
                break;
            case '<':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA);
                break;
            case '.':
                doType(KeyEvent.VK_PERIOD);
                break;
            case '>':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD);
                break;
            case '/':
                doType(KeyEvent.VK_SLASH);
                break;
            case '?':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH);
                break;
            case ' ':
                doType(KeyEvent.VK_SPACE);
                break;
            default:
                throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    private void doType(int... keyCodes) {
        doType(keyCodes, 0, keyCodes.length);
    }

    private void doType(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }

        robot.keyPress(keyCodes[offset]);
        doType(keyCodes, offset + 1, length - 1);
        robot.keyRelease(keyCodes[offset]);
    }
    
    /* Exec */
    public void exec(String ...commands) {
    	for(int i = 0; i < commands.length; i++) {
    		String command = commands[i];
    		int modifiedKey = this.getModifierKeyValue(command);
    		
    		if(modifiedKey > 0) robot.keyPress(modifiedKey);
    		else {
    			try {
        			exec(command);    		
        		} catch (IllegalArgumentException e) {
        			type(command);
        		}
    		}
    	}
    	
    	for(int i = commands.length-1; i >= 0; i--) {
    		String command = commands[i];
    		int modifiedKey = this.getModifierKeyValue(command);
    		
    		if(modifiedKey > 0) robot.keyRelease(modifiedKey);
    	}
    }
    
    private int getModifierKeyValue(String key) {
    	key = key.toLowerCase();
    	switch(key) {
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
    	switch(cmd) {
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
        doType(keyCodes, offset + 1, length - 1);
        robot.keyRelease(keyCodes[offset]);
    }
    
    /* Move Mouse */
    public void move_mouse(JSONArray movements) {
    	PointerInfo a = MouseInfo.getPointerInfo();
    	Point b = a.getLocation();
    	int x = (int) b.getX();
    	int y = (int) b.getY();
    	
    	for(int i = 0; i < movements.length(); i++) {
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
		this.robot.mouseWheel(-5*sensibility);
	}
	
	public void scroll_down(int sensibility) {
		this.robot.mouseWheel(5*sensibility);
	}
}