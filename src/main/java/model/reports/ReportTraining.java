package model.reports;

import model.referencies.Person;
import model.referencies.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportTraining {
    /**
     * Вывод на печать всех тренировок всех пользователей
     * Внимание:  доступно только для пользователей с ролью ROLE_ADMIN
     */
    public static void printAllTrainings() {
        final String FORMAT = "%-16s %-12s %-12s %-10s %-10s %-20s";

        int totalCalorage = 0;
        int totalDuration = 0;

        Map<Person, Map<LocalDate, List<Training>>> sheduleByPerson =
                Training.getShedule();

        System.out.println
                ("-----------------------------------------------------------------------------");
        System.out.printf(FORMAT, "PERSON", "DATE", "TYPE", "CALORAGE",
                "DURATION", "INFO");
        System.out.println();
        System.out.println
                ("-----------------------------------------------------------------------------");
        for (Map.Entry<Person, Map<LocalDate, List<Training>>> entryPerson : sheduleByPerson.entrySet()) {
            Person person = entryPerson.getKey();
            System.out.format(FORMAT, person.getName(), "", "", "", "", "");
            System.out.println();

            Map<LocalDate, List<Training>> sheduleByDate =
                    entryPerson.getValue();

            for (Map.Entry<LocalDate, List<Training>> entryDate : sheduleByDate.entrySet
                    ()) {
                LocalDate date = entryDate.getKey();

                System.out.format(FORMAT, "", date, "", "", "", "");
                System.out.println();

                List<Training> trainingsList = entryDate.getValue();

                for (Training training : trainingsList) {
                    System.out.format(FORMAT, "", "",
                            training.getType().getName(),
                            training.getCalorage(),
                            training.getDuration(),
                            training.getInfo());
                    System.out.println();

                    totalCalorage += training.getCalorage();
                    totalDuration += training.getDuration();
                }
            }
        }
        System.out.println
                ("-----------------------------------------------------------------------------");

        System.out.format(FORMAT, "", "", "",
                totalCalorage,
                totalDuration,
                "");
        System.out.println();
        System.out.println
                ("-----------------------------------------------------------------------------");
    }

    /**
     * Вывод на печать всех тренировок переданного пользователя
     *
     * @param person - пользователь, для которого будет выводится список тренировок
     */
    public static void printPersonShedule(Person person) {
        final String FORMAT = "%-16s %-12s %-12s %-10s %-10s %-20s";

        int totalCalorage = 0;
        int totalDuration = 0;

        Map<Person, Map<LocalDate, List<Training>>> sheduleByPerson =
                Training.getShedule();
        Map<LocalDate, List<Training>> sheduleByDate =
                sheduleByPerson.get(person);

        System.out.println
                ("-----------------------------------------------------------------------------");
        System.out.printf(FORMAT, "PERSON", "DATE", "TYPE", "CALORAGE",
                "DURATION", "INFO");
        System.out.println();
        System.out.println
                ("-----------------------------------------------------------------------------");

        System.out.format(FORMAT, person.getName(), "", "", "", "", "");
        System.out.println();

        for (Map.Entry<LocalDate, List<Training>> entryDate : sheduleByDate.entrySet()) {
            LocalDate date = entryDate.getKey();

            System.out.format(FORMAT, "", date, "", "", "", "");
            System.out.println();

            List<Training> trainingsList = entryDate.getValue();

            for (Training training : trainingsList) {
                System.out.format(FORMAT, "", "",
                        training.getType().getName(),
                        training.getCalorage(),
                        training.getDuration(),
                        training.getInfo());
                System.out.println();

                totalCalorage += training.getCalorage();
                totalDuration += training.getDuration();
            }
        }

        System.out.println
                ("-----------------------------------------------------------------------------");
        System.out.format(FORMAT, "", "", "",
                totalCalorage,
                totalDuration,
                "");
        System.out.println();
        System.out.println
                ("-----------------------------------------------------------------------------");
    }
}
