package model.referencies;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrainingType {
    private String name;

    //----------------------------------------Temporal data storage---------------------------------------------------------------------
    private static List<TrainingType> trainingTypes = new CopyOnWriteArrayList<>();
    private static volatile int idCounter = 1;

    static {
        trainingTypes.add(new TrainingType("CARDIO"));
        trainingTypes.add(new TrainingType("FORCE"));
        trainingTypes.add(new TrainingType("YOGA"));
    }

    //----------------------------------------Standard methods---------------------------------------------------------------------

    // Getter and Setter methods
    public String getName() {
        return this.name;
    }

    public static List<TrainingType> getTrainingTypes() {
        return trainingTypes;
    }

    // Default Constructor
    private TrainingType() {
        super();
    }

    // Parameterized Constructor
    public TrainingType(String name) {
        this.name = name.toUpperCase();
    }

    //HashCode & equals
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TrainingType trainingType = (TrainingType) o;
        return name.equalsIgnoreCase(trainingType.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    //----------------------------------------Functional methods---------------------------------------------------------------------

    /**
     * Выполняет запись во временную коллекцию нового типа тренировки
     *
     * @param trainingType - тип тренировки для записи
     */
    public static void recordItem(TrainingType trainingType) {
        trainingTypes.add(trainingType);
    }

    /**
     * Выполняет поиск во временной коллекции типа тренировки по переданному имени.
     *
     * @param name - имя типа тренировки для поиска
     * @return optional - контейнер с найденным типом тренировки
     */
    public static Optional<TrainingType> findByName(String name) {
        for (TrainingType trainingType : trainingTypes) {
            if (trainingType.getName().equalsIgnoreCase(name)) {
                return Optional.of(trainingType);
            }
            ;
        }
        return Optional.ofNullable(null);
    }
}
