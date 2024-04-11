import controllers.PersonController;
import model.referencies.Person;
import model.referencies.Session;
import model.referencies.Training;
import model.referencies.TrainingType;
import model.reports.ReportReferencies;
import model.reports.ReportTraining;
import model.service.Logger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CommandAnalyzer {
    private static final String BAD_COMMAND =
            "Bad command. Input HELP to get help or EXIT to exit the program.";
    private static final String UNKNOWN_PERSON =
            "Unknown person. Please log in.";
    private static final String ACCESS_DENIED = "Access denied.";
    private static final String WRONG_NAME_OR_PASSWORD =
            "Wrong name or password";
    private static final String COMMAND_FAIL =
            "ERROR: Failed to execute command";

    private volatile static String[] tokens;

	/**
	 * Возвращает LocalDate из строку для логирования.
	 * @param  dateString - строка для форматирования
	 * @param  datePattern - паттерн для форматирования
     * @return Optional - результат преобразования строки в LocalDate
	 */
    private static Optional<LocalDate>
    returnDateFromString(String dateString, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        try {
            return Optional.of(LocalDate.parse(dateString, formatter));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

	/**
	 * Формирует строку для логирования.
	 * @param  stringMessage - строка с информацией о событии
     * @return  s - строка для записи в файл с логом
	 */
    public static String createLogMessage(String stringMessage) {
        Optional<Person> optionalCurrentPerson =
                PersonController.returnCurrentPerson();
        return optionalCurrentPerson.map(person -> LocalDate.now() + " " + LocalTime.now() + ": " +
                Session.getSessionID() + " [" +
                person.getName() + "] " + stringMessage).orElseGet(() -> LocalDate.now() + " " + LocalTime.now() + ": " +
                Session.getSessionID() + " [UNKNOWN] " + stringMessage);

    }

	/**
	 * Определяет тип текущей команды, переданной с консоли.
     * @return  s - строка с именем команды в верхнем регистре
	 */
    private static String getCommand() {
        if (tokens.length == 0) {
            return "HELP";
        }

        return tokens[0].toUpperCase();
    }

	/**
	 * Печать текста с сигнатурой команд приложения.
     * TODO: Требует оптимизации кода - работа с большим количеством строк
	 */
    private static void showHelp() {
        System.out.println
                ("Authentification:  LOGIN <name> <password> - log in the application \n"
                        + "                   REG <name> <password> - new registration \n" +
                        "                   LOGOUT - log out /n" +
                        "Trainings:         ATR <date in format dd-MM-yyyy> <type> <duration> <calorage> <info> - add new training for current person \n"
                        +
                        "                   CTR <date in format dd-MM-yyyy> <type> <duration> <calorage> <info> - change training for current person \n"
                        +
                        "                   DTR <date in format dd-MM-yyyy> <type> - delete training for current person \n"
                        +
                        "Training types:    ATT <type> - add new training type if not exists \n"
                        +
                        "Reporting:         SHEDULE <date in format dd-MM-yyyy> - shedule for current person to the date \n"
                        + "                   SHEDULE - shedule for current person \n" +
						"                   TYPES - print all training types \n" +
                        "                   ASHEDULE - shedule for all people (admin only) \n" +
						"                   PEOPLE - print all people (admin only) \n" +
                        "Exit:              EXIT - close the application /n");
    }

	/**
	 * Инициирует регистрацию нового пользователя.
     * @return boolean - успешность выполнения команды
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
	 */
    private static boolean registerPerson() {
        if (tokens.length != 3) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " REG: " + BAD_COMMAND));
            System.out.println(BAD_COMMAND);
            return false;
        }

        String name = tokens[1];
        String password = tokens[2];

        if (!name.isEmpty() & !password.isEmpty()) {
            Logger.writeLog(createLogMessage("INFO: Register person " + name));
            Person.recordItem(new Person(name, password));
            return true;
        } else {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " REG: " + WRONG_NAME_OR_PASSWORD));
            System.out.println(WRONG_NAME_OR_PASSWORD);
            return false;
        }
    }

	/**
	 * Инициирует сессию для пользователя приложения.
     * @return boolean - успешность выполнения команды
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
	 */
    private static boolean loginPerson() {
        if (tokens.length != 3) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " LOGIN: " + BAD_COMMAND));
            System.out.println(BAD_COMMAND);
            return false;
        }

        String name = tokens[1];
        String password = tokens[2];

        if (!name.isEmpty() & !password.isEmpty()) {
            if (PersonController.loginPerson(name, password)) {
                Logger.writeLog(createLogMessage("INFO: Login succesful"));
                System.out.println("Hallo " + name +
                        ". Today is a good day to do smth great!");
                return true;
            }
        }

        Logger.writeLog(createLogMessage
                (COMMAND_FAIL + " LOGIN: <" + name +
                        "> " + WRONG_NAME_OR_PASSWORD));
        System.out.println(WRONG_NAME_OR_PASSWORD);
        return false;
    }

	/**
	 * Инициирует завершение сессии текущего пользователя.
	 */
    private static void logoutPerson() {
        Logger.writeLog(createLogMessage("INFO: Logout"));
        Session.logoutCurrentPerson();
    }

	/**
	 * Инициирует корректировку тренировки в расписании текущего пользователя,
	 * если указанный тип тренировки в указанную дату существует.
     * @return boolean - успешность выполнения команды
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
	 */
    private static boolean changeTraining() {
        if (tokens.length < 5) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " P!TR: " + BAD_COMMAND));
            System.out.println(BAD_COMMAND);
            return false;
        }

        Optional<Person> optionalPerson =
                PersonController.returnCurrentPerson();
        if (optionalPerson.isEmpty()) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " P!TR: " + UNKNOWN_PERSON));
            System.out.println(UNKNOWN_PERSON);
            return false;
        }

        String date = tokens[1];

        LocalDate localDate;
        Optional<LocalDate> optionalLocalDate =
                returnDateFromString(date, "dd-MM-yyyy");
        if (optionalLocalDate.isPresent()) {
            localDate = optionalLocalDate.get();
        } else {
            System.out.println(BAD_COMMAND);
            return false;
        }

        String type = tokens[2];
        TrainingType trainingType;
        Optional<TrainingType> optionalTrainingType =
                TrainingType.findByName(type);
        if (optionalTrainingType.isPresent()) {
            trainingType = optionalTrainingType.get();
        } else {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " P!TR: Wrong training type"));
            System.out.println("Wrong training type");
            return false;
        }

        int duration = Integer.parseInt(tokens[3]);
        int calorage = Integer.parseInt(tokens[4]);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 5; i < tokens.length; i++)
            stringBuilder.append(" " + tokens[i]);
        String info = stringBuilder.toString().trim();


        if (Training.recordItem(new
                Training(optionalPerson.get(), localDate,
                trainingType, calorage, duration,
                info), true)) {
            Logger.writeLog(createLogMessage
                    ("INFO: Change training [person = " +
                            optionalPerson.get().getName() + "; date = " +
                            localDate + "; type = " + trainingType.getName() +
                            "; duration = " + duration + "; calorage = " +
                            calorage + "; info = " + info + "]"));
            System.out.println("Training changed.");
            return true;
        } else {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL +
                            " CTR: Record of this type does not exists."));
            System.out.println("Record of this type does not exists.");
            return false;
        }
    }

	/**
	 * Инициирует добавление тренировки в расписание текущего пользователя,
	 * если указанный тип тренировки в указанную дату не существует.
     * @return boolean - успешность выполнения команды
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
	 */
    private static boolean addTraining() {
        if (tokens.length < 5) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " ATR: " + BAD_COMMAND));
            System.out.println(BAD_COMMAND);
            return false;
        }

        Optional<Person> optionalPerson =
                PersonController.returnCurrentPerson();
        if (optionalPerson.isEmpty()) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " ATR: " + UNKNOWN_PERSON));
            System.out.println(UNKNOWN_PERSON);
            return false;
        }

        String date = tokens[1];

        LocalDate localDate;
        Optional<LocalDate> optionalLocalDate =
                returnDateFromString(date, "dd-MM-yyyy");
        if (optionalLocalDate.isPresent()) {
            localDate = optionalLocalDate.get();
        } else {
            System.out.println(BAD_COMMAND);
            return false;
        }

        String type = tokens[2];
        TrainingType trainingType;
        Optional<TrainingType> optionalTrainingType =
                TrainingType.findByName(type);
        if (optionalTrainingType.isPresent()) {
            trainingType = optionalTrainingType.get();
        } else {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " ATR: Wrong training type"));
            System.out.println("Wrong training type");
            return false;
        }

        int duration = Integer.parseInt(tokens[3]);
        int calorage = Integer.parseInt(tokens[4]);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 5; i < tokens.length; i++)
            stringBuilder.append(" " + tokens[i]);
        String info = stringBuilder.toString().trim();

        if (Training.recordItem(new
                Training(optionalPerson.get(), localDate,
                trainingType, calorage, duration,
                info), false)) {
            Logger.writeLog(createLogMessage("INFO: Add training [person = " +
                    optionalPerson.get().getName() +
                    "; date = " + localDate +
                    "; type = " +
                    trainingType.getName() +
                    "; duration = " + duration +
                    "; calorage = " + calorage +
                    "; info = " + info + "]"));
            System.out.println("New training recorded.");
            return true;
        } else {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL +
                            " ATR: Record of this type already exists."));
            System.out.println("Record of this type already exists.");
            return false;
        }
    }

	/**
	 * Инициирует удаление тренировки из расписания текущего пользователя
     * @return boolean - успешность выполнения команды
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
	 */
    private static boolean deleteTraining() {
        if (tokens.length != 3) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " DTR: " + BAD_COMMAND));
            System.out.println(BAD_COMMAND);
            return false;
        }

        Optional<Person> optionalPerson =
                PersonController.returnCurrentPerson();
        if (optionalPerson.isEmpty()) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " DTR: " + UNKNOWN_PERSON));
            System.out.println(UNKNOWN_PERSON);
            return false;
        }

        String date = tokens[1];

        LocalDate localDate;
        Optional<LocalDate> optionalLocalDate =
                returnDateFromString(date, "dd-MM-yyyy");
        if (optionalLocalDate.isPresent()) {
            localDate = optionalLocalDate.get();
        } else {
			Logger.writeLog(createLogMessage
					(COMMAND_FAIL + " DTR: Wrong date"));
            System.out.println("Wrong date.");
            return false;
        }

        String type = tokens[2];
        TrainingType trainingType;
        Optional<TrainingType> optionalTrainingType =
                TrainingType.findByName(type);
        if (optionalTrainingType.isPresent()) {
            trainingType = optionalTrainingType.get();
        } else {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " DTR: Wrong training type"));
            System.out.println("Wrong training type");
            return false;
        }

        Training.deleteItem(new
                Training(optionalPerson.get(), localDate,
                trainingType));
        Logger.writeLog(createLogMessage("INFO: Delete training [person = " +
                optionalPerson.get().getName() +
                "; date = " + localDate +
                "; type = " +
                trainingType.getName()));
        System.out.println("Training deleted.");
        return true;
    }

	/**
	 * Инициирует добавление нового типа тренировки, если такой пока не существует
     * @return boolean - успешность выполнения команды
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
	 */
    private static boolean addTrainingType() {
        if (tokens.length != 2) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " ATT: " + BAD_COMMAND));
            System.out.println(BAD_COMMAND);
            return false;
        }

        String type = tokens[1];
        TrainingType trainingType;
        Optional<TrainingType> optionalTrainingType =
                TrainingType.findByName(type);
        if (optionalTrainingType.isPresent()) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " ATT: Training type already exists"));
            System.out.println("Training type already exists");
            return false;

        }

        TrainingType.recordItem(new TrainingType(type));
        Logger.writeLog(createLogMessage
                ("INFO: Add new training type " + type.toUpperCase()));
        System.out.println("New training type recorded");
        return true;
    }

	/**
	 * Вызывает печать списка всех пользователей.
	 * Внимание:
	 * Доступно только для пользователя с ролью ROLE_ADMIN
     * @return boolean - успешность выполнения команды
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
	 */
    private static boolean printPeopleList() {
        Optional<Person> optionalCurrentPerson =
                PersonController.returnCurrentPerson();
        if (optionalCurrentPerson.isEmpty()) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " PEOPLE: " + UNKNOWN_PERSON));
            System.out.println(UNKNOWN_PERSON);
            return false;
        }

		if (!PersonController.isAdmin(optionalCurrentPerson.get())) {
			Logger.writeLog(createLogMessage
					(COMMAND_FAIL + " PEOPLE: " + ACCESS_DENIED));
			System.out.println(ACCESS_DENIED);
			return false;
		}

        Logger.writeLog(createLogMessage("INFO: PEOPLE Execute print people list"));
        ReportReferencies.printPeopleList();
        return true;
    }

	/**
	 * Вызывает печать списка всех типов тренировок.
	 */
    private static void printTrainingTypes() {

        ReportReferencies.printTrainingTypesList();
    }

	/**
	 * Вызывает печать тренировок для всех пользователей.
	 * Внимание:
	 * Доступно только для пользователя с ролью ROLE_ADMIN
     * @return boolean - успешность формирования отчета
     * TODO: Требует оптимизации кода и разработки комплекта Exceptions
     */
    public static boolean printAllShedules() {
        Optional<Person> optionalCurrentPerson =
                PersonController.returnCurrentPerson();
        if (optionalCurrentPerson.isEmpty()) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " ASHEDULE: " + UNKNOWN_PERSON));
            System.out.println(UNKNOWN_PERSON);
            return false;
        }

        if (!PersonController.isAdmin(optionalCurrentPerson.get())) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " ASHEDULE: " + ACCESS_DENIED));
            System.out.println(ACCESS_DENIED);
            return false;
        }

        Logger.writeLog(createLogMessage("INFO: ASHEDULE "));
        ReportTraining.printAllTrainings();
        return true;
    }

	/**
	 * Вызывает печать расписания тренировок для текущего пользователя
	 */
    private static void printPersonShedule() {
        Optional<Person> optionalCurrentPerson =
                PersonController.returnCurrentPerson();
        if (optionalCurrentPerson.isEmpty()) {
            Logger.writeLog(createLogMessage
                    (COMMAND_FAIL + " SHEDULE: " + UNKNOWN_PERSON));
            System.out.println(UNKNOWN_PERSON);
            return;
        }

        Logger.writeLog(createLogMessage("INFO: SHEDULE "));
        ReportTraining.printPersonShedule(optionalCurrentPerson.get());
    }

	/**
	 * Выполняет команду, переданную с консоли..
	 * Внимание:
	 * Команды содержат строгую сигнатуру. Подробнее - по команде HELP или ReadMe.md.
	 *
	 * @param  commandLine - строка с командой для парсинга и выполнения
     * @return  boolean - выполнено ли принудительное завершение приложения
	 */
    public static boolean executeCommand(String commandLine) {
        tokens = commandLine.replaceAll("\\s+", " ").split(" ");

        String command = getCommand();

        switch (command) {
            case "LOGIN" -> loginPerson();
            case "REG" -> registerPerson();
            case "LOGOUT" -> logoutPerson();
            case "ATR" -> addTraining();
            case "ATT" -> addTrainingType();
            case "CTR" -> changeTraining();
            case "DTR" -> deleteTraining();
            case "PEOPLE" -> printPeopleList();
            case "SHEDULE" -> printPersonShedule();
            case "ASHEDULE" -> printAllShedules();
            case "TYPES" -> printTrainingTypes();
            case "HELP" -> showHelp();
            case "EXIT" -> {return true;}
            default -> System.out.println(BAD_COMMAND);
        }

        return false;
    }
}