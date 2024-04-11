import controllers.PersonController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PersonTest {

    @BeforeAll
    public static void createTestEnvironment() {
        System.setProperty("sessionID", String.valueOf(System.currentTimeMillis()));
    }

    @Test
    @DisplayName("Проверка входа в систему под правами администратора")
    public void testLoginAdministrator() {
        boolean result = PersonController.loginPerson("admin", "1");
        assertTrue(result);
    }

    @Test
    @DisplayName("Проверка несанкционированного доступа")
    public void testWrongLogin() {
        boolean result = PersonController.loginPerson("admin"+System.currentTimeMillis(), "1");
        assertFalse(result);
    }

    @Test
    @DisplayName("Проверка входа в систему под правами обычного пользователя")
    public void testLoginUser() {
        boolean result = PersonController.loginPerson("user", "1");
        assertTrue(result);
    }

    @Test
    @DisplayName("Проверка несанкционированного доступа пользователя к тренировкам всех пользователей")
    public void testAccessViolationAllSchedules() {
        assertFalse(CommandAnalyzer.printAllShedules());
    }
}
