package model.reports;

import model.referencies.Person;
import model.referencies.TrainingType;
import java.util.List;
import java.util.Map;

public class ReportReferencies {
    /**
     * Вывод на печать всех пользователей приложения
     * Внимание:  доступно только для пользователей с ролью ROLE_ADMIN
     */
    public static void printPeopleList() {
        final String FORMAT = "%-6s %-16s %-12s %-16s";
        Map<Integer, Person> peopleMap = Person.getPersons();

        System.out.println
                ("-----------------------------------------------------------------------------");
        System.out.printf(FORMAT, "PERSON ID", "NAME", "PASSWORD", "ROLE");
        System.out.println();
        System.out.println
                ("-----------------------------------------------------------------------------");
        for (Map.Entry<Integer, Person> entry : peopleMap.entrySet()) {
            Person person = entry.getValue();
            System.out.format(FORMAT, person.getId(),
                    person.getName(), person.getPassword(),
                    person.getRole());
            System.out.println();
        }
        System.out.println
                ("-----------------------------------------------------------------------------");
    }

    /**
     * Вывод на печать всех типов тренировок, зарегистрированных в приложении
     */
    public static void printTrainingTypesList() {
        final String FORMAT = "%-16s";

        List<TrainingType> trainingTypes = TrainingType.getTrainingTypes();

        System.out.println
                ("-----------------------------------------------------------------------------");
        System.out.printf(FORMAT, "TRAINING TYPE");
        System.out.println();
        System.out.println
                ("-----------------------------------------------------------------------------");
        for (TrainingType trainingType : trainingTypes) {
            System.out.format(FORMAT, trainingType.getName());
            System.out.println();
        }
        System.out.println
                ("-----------------------------------------------------------------------------");
    }
}
