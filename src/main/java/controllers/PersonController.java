package controllers;

import model.referencies.Person;
import model.referencies.Session;
import model.service.Roles;

import java.util.Optional;

public class PersonController {
    /**
     * Возвращает контейнер с текущим пользователем сессии.
     *
     * @return Optional - контейнер с текущим пользователем
     */
    public static Optional<Person> returnCurrentPerson() {
        Optional<Integer> optionalId = Session.getCurrentPersonId();

        if (optionalId.isPresent()) {
            return Person.findById(optionalId.get());
        }

        return Optional.empty();
    }

    /**
     * Возвращает результат входа в приложение по имени и паролю.
     *
     * @param name     - имя пользователя
     * @param password - пароль
     * @return boolean - результат успешного входа в систему
     */
    public static boolean loginPerson(String name, String password) {
        Optional<Person> optionalPerson = Person.findByName(name);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            if (person.getPassword().equals(password)) {
                Session.setCurrentPersonId(person.getId());
                return true;
            }
        }

        return false;
    }

    /**
     * Возвращает результат проверки наличия у пользователя административных прав
     *
     * @param person - пользователь приложения
     * @return boolean - истина, если у пользователя есть роль ROLE_ADMIN
     */
    public static boolean isAdmin(Person person) {
        return person.getRole() == Roles.ROLE_ADMIN;
    }
}