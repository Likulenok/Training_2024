package model.referencies;

import model.service.Roles;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Person implements Comparable {

    private final Integer id;
    private final String name;
    private final String password;
    private Roles role;

    //----------------------------------------Temporal data storage---------------------------------------------------------------------

    private static final Map<Integer, Person> persons = new HashMap<>();
    private static volatile Integer idCounter = 1;

    static {
        //not threadsafe
        Person admin = new Person("admin", "1");
        admin.setRole(Roles.ROLE_ADMIN);
        recordItem(admin);

        Person user = new Person("user", "1");
        recordItem(user);
    }

    //----------------------------------------Standard methods---------------------------------------------------------------------

    // Getter and Setter methods
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public Roles getRole() {
        return role;
    }
    public void setRole(Roles role) {
        this.role = role;
    }
    public static Map<Integer, Person> getPersons() {
        return persons;
    }

    // Parameterized Constructor
    //not threadsafe
    public Person(String name, String password) {
        this.id = idCounter++;
        this.name = name;
        this.password = password;
        this.role = Roles.ROLE_USER;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o)
            return 0;
        if (o == null || getClass() != o.getClass())
            return 0;
        Person person = (Person) o;

        return this.name.compareTo(person.getName());
    }

    //----------------------------------------Functional methods---------------------------------------------------------------------

    /**
     * Выполняет поиск пользователя по переданному идентификатору
     * @param id - идентификатор пользователя для поиска
     * @return optional - контейнер с найденным пользователем
     */
    public static Optional<Person> findById(Integer id) {
        return Optional.ofNullable(persons.get(id));
    }

    /**
     * Выполняет поиск пользователя по переданному имени
     * @param name - имя пользователя для поиска
     * @return optional - контейнер с найденным пользователем
     */
    public static Optional<Person> findByName(String name) {
        for (Person person : persons.values()) {
            if (person.getName().equalsIgnoreCase(name)) {
                return Optional.of(person);
            }
        }
        return Optional.empty();
    }

    /**
     * Выполняет запись во временную коллекцию нового пользователя
     * @param person - пользователь для записи в базу данных
     */
    public static void recordItem(Person person) {
        persons.put(person.getId(), person);
    }
}
