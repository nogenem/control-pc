import java.awt.AWTException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class BetterRobotTest {

    public BetterRobot robot;

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

    private void openNotepad() throws Exception {
        Runtime.getRuntime().exec("notepad.exe");
        Thread.sleep(1000L);
    }
}