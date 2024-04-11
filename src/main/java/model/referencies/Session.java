package model.referencies;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

    //Хранилище данных о сессиях и активных пользователях приложения
    private static Map<String, Integer> sessions = new ConcurrentHashMap<>();

    /**
     * Возвращает идентификатор текущей сессии.
     *
     * @return s - значение ранее сохраненного идентификатора сессии пользователя
     */
    public static String getSessionID() {
        return System.getProperty("sessionID");
    }

    /**
     * Устанавливает для текущей сессии идентификатор текущего пользователя
     *
     * @param personId - идентификатор пользователя
     */
    public static void setCurrentPersonId(int personId) {
        sessions.put(getSessionID(), personId);
    }

    /**
     * Возвращает идентификатор пользователя текущей сессии.
     *
     * @return Optional - контейнер с идентификатором пользователя
     */
    public static Optional<Integer> getCurrentPersonId() {
        return Optional.ofNullable(sessions.get(getSessionID()));
    }

    /**
     * Выполняет очистку идентификатора пользователя текущей сессии при его выходе из приложения.
     */
    public static void logoutCurrentPerson() {
        sessions.remove(getSessionID());
    }
}
