package model.referencies;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Training {
    //keys
    private Person person;
    private LocalDate date;
    private TrainingType type;

    //resources
    private int calorage;
    private int duration;
    private String info;

    //----------------------------------------Temporal data storage---------------------------------------------------------------------

    private static Map<Person, Map<LocalDate,
            List<Training>>> trainings = new ConcurrentSkipListMap<>();

    //----------------------------------------Standard methods---------------------------------------------------------------------

    // Getter and Setter methods
    public Person getPerson() {
        return this.person;
    }
    public LocalDate getDate() {
        return this.date;
    }
    public TrainingType getType() {
        return this.type;
    }
    public int getCalorage() {
        return this.calorage;
    }
    public int getDuration() {
        return this.duration;
    }
    public String getInfo() {
        return this.info;
    }
    public void setCalorage(int calorage) {
        this.calorage = calorage;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public static Map<Person, Map<LocalDate,
            List<Training>>> getShedule() {
        return trainings;
    }

    // Parameterized Constructors
    public Training(Person person, LocalDate date, TrainingType type) {
        this.person = person;
        this.date = date;
        this.type = type;
    }

    public Training(Person person, LocalDate date, TrainingType type,
                    int calorage, int duration, String info) {
        this.person = person;
        this.date = date;
        this.type = type;
        this.calorage = calorage;
        this.duration = duration;
        this.info = info;
    }

    //HashCode & equals
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Training training = (Training) o;
        return Objects.equals(person, training.getPerson())
                && Objects.equals(date, training.getDate())
                && Objects.equals(type, training.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, date, type);
    }


    //----------------------------------------Functional methods---------------------------------------------------------------------

    /**
     * Выполняет запись во временную коллекцию новой тренировки
     * @param training - тренировка для добавления
     * @param overwrite - boolean - допустима ли перезапись существующего элемента
     * @return boolean - результат успешности выполнения операции
     */
    public static boolean recordItem(Training training, boolean overwrite) {
        Person person = training.getPerson();
        LocalDate date = training.getDate();

        Map<LocalDate, List<Training>> sheduleByDate =
                trainings.computeIfAbsent(person, k -> new ConcurrentSkipListMap<>());


        List<Training> trainingsList = sheduleByDate.computeIfAbsent(date, k -> new CopyOnWriteArrayList<>());

        //check entries by training type
        for (Training item : trainingsList) {
            if (item.equals(training)) {
                if (overwrite) {
                    item.setCalorage(training.getCalorage());
                    item.setDuration(training.getDuration());
                    item.setInfo(training.getInfo());
                    return true;
                }

                return false;
            }
        }

        if (overwrite) {
            return false;
        }

        trainingsList.add(training);
        return true;
    }

    /**
     * Выполняет удаление из временной коллекции переданной тренировки
     * @param training - тренировка для удаления
     */
    public static void deleteItem(Training training) {
        Map<LocalDate, List<Training>> sheduleByDate =
                trainings.get(training.getPerson());
        if (sheduleByDate == null) {
            return;
        }

        List<Training> trainingsList = sheduleByDate.get(training.getDate());
        if (trainingsList == null) {
            return;
        }

        trainingsList.remove(training);
    }
}