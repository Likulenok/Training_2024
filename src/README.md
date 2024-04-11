Проект представляет собой консольное приложение для ведения тренировок пользователями.

Архитектура проекта:
Main 	- исполняемый файл проекта. 
		Запускает в цикле чтение команд пользователя с консоли до ввода EXIT
CommandAnalyzer 
		- парсер введенных с консоли команд и передача управления в соответствующие методы.

src.main.java.model:
    referencies:
          Person - хранение списка пользователей приложения (простой ключ)
		  TrainingType - ведение списка типов тренировок (простой ключ)
		  Training - хранение списка тренировок (составной ключ)
          Session - хранение данных сессий пользователей JVM
	reports:
          ReportReferencies - вывод на печать отчетов по объектам с простым ключом
          ReportTrainings - вывод на печать отчетов по объекту Trainings
	service:
		  Logger - примитивная запись лога в файл
          Roles - хранение доступных ролей пользователей
src.main.java.controllers:
	PersonController - функции по работе с объектами пользователей


Сигнатура команд приложения:
Authentification:  LOGIN <name> <password> - log in the application
	                 REG <name> <password> - new registration
	                 LOGOUT - log out /n" +
Trainings:         ATR <date in format dd-MM-yyyy> <type> <duration> <calorage> <info> - add new training for current person if not exists
	                 CTR <date in format dd-MM-yyyy> <type> <duration> <calorage> <info> - change training for current person if exists
	                 DTR <date in format dd-MM-yyyy> <type> - delete training for current person
Training types:    ATT <type> - add new training type if not exists
Reporting:         SHEDULE - print shedule for current person
					TYPES - print list of training types
	                ASHEDULE - print shedule for all people (admin only)
					PEOPLE - print list of registered people (admin only)
Exit:              EXIT - close the application



Примеры сценариея работы с приложением:

login user 1
//залогинились как обычный пользователь

atr 11-03-2024 yoga 200 90
//зарегистрировали тренировку на март, тип yoga, 200 калорий, 90 минут
atr 10-04-2024 cardio 400 90 enough for today
//зарегистрировали тренировку на апрель, тип cardio, 400 калорий, 90 минут
atr 11-04-2024 yoga 200 90 just relax
//зарегистрировали тренировку на апрель, тип yoga, 200 калорий, 90 минут
atr 10-03-2024 cardio 400 90
//зарегистрировали тренировку на март, тип cardio, 400 калорий, 90 минут
shedule
//посмотрели все свое расписание
аshedule
//попробовали посмотреть все расписания, получили запрет доступа

logout
//разлогинились

login admin 1
//залогинились как админ

atr 11-04-2024 yoga 200 90 just relax
//зарегистрировали тренировку для админа на апрель, тип yoga, 200 калорий, 90 минут
atr 11-04-2024 cardio 400 90 enough for today
//зарегистрировали тренировку для админа на апрель, тип cardio, 400 калорий, 90 минут
atr 10-04-2024 yoga 200 90 
//зарегистрировали тренировку для админа на апрель, тип yoga, 200 калорий, 90 минут
atr 10-04-2024 cardio 400 90
//зарегистрировали тренировку для админа на апрель, тип cardio, 400 калорий, 90 минут
dtr 11-04-2024 cardio
//удалили тренировку для админа от 11 апреля с типом cardio
ctr 10-04-2024 cardio 300 90 some enough for today
//изменили тренировку для админа от 10 апреля с типом cardio

shedule
//посмотрели все свое расписание
аshedule
//посмотрели расписания всех пользователей