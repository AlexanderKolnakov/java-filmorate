# <span style="color: white">**JAVA-filmorate**
____
![JAVA-filmorate](/filmorate.jpg)

### <span style="color: white">Данный сервис предназначен для работы с фильмами, поиску/оценке/жанрам и т.п.  ###
<span style="color: white">Представленный в данном репозитории функционал сервиса в дальнейшем был расширен в ходе совместной 
работы с друими участниками проекта. Ссылка на репозиторий соместного проекта - https://github.com/AlexanderKolnakov/java-filmorate-teamwork


<span style="color: white">Приложение использует слудющие технологии:

- Java
- Spring Boot (web, validation, data-jpa)
- используемая БД - PostgreSQL
- Lombok
- Mock тестирование + Postman коллекция 

Сейчас приложение хранит информацию в БД, но так же в нем осталось возможность 
хранить информацияю в списках и массивах (классы InMemoryStorage) которая принименялась на первых этапах
разработки приложения.
![Схема БД](/Схема БД filmorate.jpg)


<span style="color: white">___Ниже приведены эндпоинты и кратное описаних их функционала:___
### <span style="color: white">User:
- GET /users - получение списка всех пользователей
- GET /users/{id} - получение данных о пользователе по id
- GET /users/{id}/friends — возвращает список друзей
- GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем
- POST /users - создание пользователя
- PUT /users - редактирование пользователя
- PUT /users/{id}/friends/{friendId} — добавление в друзья
- DELETE /users/{id}/friends/{friendId} — удаление из друзей


### <span style="color: white">Films:
- GET /films - получение списка всех фильмов
- GET /films/{id} - получение информации о фильме по его id
- GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков
- POST /films - создание фильма
- PUT /films - редактирование фильма
- PUT /films/{id}/like/{userId} — поставить лайк фильму
- DELETE /films/{id}/like/{userId} — удалить лайк фильма

### <span style="color: white">Genres:
- GET /genres - получение списка всех жанров фильмов
- GET /genres/{id} - получение жанра по id

<span style="color: white">Для тестирования данного приложения к корневой папке проекта имееста Postman коллекция.txt


