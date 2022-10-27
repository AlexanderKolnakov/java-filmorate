# Filmorate
*Приложения по поиску и подбору фильмов с ограниченным функционалом.*

### Возможности: 
- обработка **GET, POST, PUT, DELETE** запросов (локально) по эндпоинтам:

   /users  (/{id}, /{id}/friends, /{id}/friends/common/{otherId}, /{id}/friends/{friendId})
   
   /films  (/popular, /{id}, /{id}/like/{userId})

   /mpa    (/{id})

   /genres (/{id})

 Данные запросы позволяют добавлять людей/фильмы, добавлять/удалять друзей, 
 ставить/снимать like фильмам, выводить списки друзей, 
 показывать списки наиболее популярных фильмов, жанры и рейтинги фильмов

Данные переданные через вышеуказанные эндпоинты хранятся и БД. Схема БД приведена ниже.

### Схема БД filmorate ###
![Схема БД filmorate](\src\main\resources\Схема%20БД%20filmorate.jpg)

*Дата создания первой версии: 14.09.2022* <br>
*Дата последнего обновления: 27.10.2022*

