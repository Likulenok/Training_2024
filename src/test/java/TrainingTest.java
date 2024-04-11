import controllers.PersonController;
import model.referencies.Person;
import model.referencies.Training;
import model.referencies.TrainingType;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingTest {
    static Training training;

    @BeforeAll
    public static void createTestEnvironment() {
        System.setProperty("sessionID", String.valueOf(System.currentTimeMillis()));
        PersonController.loginPerson("admin", "1");
        Optional<Person> optionalCurrentPerson =
                PersonController.returnCurrentPerson();
        if (optionalCurrentPerson.isPresent()) {
            training = new
                    Training(optionalCurrentPerson.get(), LocalDate.now(),
                    TrainingType.findByName("cardio").get(), 1, 1, "test");
        }
    }

    @Test
    @DisplayName("Вызов добавления новой записи тренировки")
    public void testAddTraining() {
        assertTrue(Training.recordItem(training, false));
    }

    @Test
    @DisplayName("Вызов изменения существующей записи тренировки")
    public void testChangeTraining1() {
        training.setCalorage(100);
        assertTrue(Training.recordItem(training, true));
    }

    @Test
    @DisplayName("Проверка установленного количества калорий в существующей тренировке")
    public void testChangeTraining2() {
        assertEquals(100, training.getCalorage());
    }

    @AfterAll
    public static void deleteTestData() {
        Training.deleteItem(training);
    }
}
