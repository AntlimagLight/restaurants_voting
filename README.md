_Restaurant voting application_
===============================

##### Current Version 1.0
Application for user voting for the best restaurant for lunch. It is a simple REST API and uses basic authorization. Made according to the technical requirement.
[Project on GitHub](https://github.com/AntlimagLight/restaurants_voting)
Designed by [AntimagLight](https://github.com/AntlimagLight)

------------------
- Stack: [JDK 17](http://jdk.java.net/17/), [Maven](https://maven.apache.org/), Spring Boot 3.0 (web, datajpa, validation, security, cache, tests), JUnit Jupiter API, Lombok, H2, [SoapUI](https://www.soapui.org/).
- Run: `mvn spring-boot:run` in root directory.

------------------
##  _Technical requirement_ 
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

The task is:
Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant, and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to GitHub repository. It should contain the code, README.md with API documentation and couple curl commands to test it, better - link to Swagger.

P.S.: Make sure everything works with the latest version that is on github :)  
P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

------------------
## _REST API documentation_
```
http://localhost:8080/  
```
Possible API requests are divided into user part(/user) and admin part(/admin). The only request available to anonymous users is the registration(/registration) request. Authorization occurs with a unique email and password. Application uses content-type: application/json. 
H2 console available: http://localhost:8080/h2
**Credentials:**
```
User: user_zero@yandex.ru / pass12
User: user@yandex.ru / password
Admin: admin@gmail.com / admin
```
##### Registration (POST)
```http://localhost:8080/registration```
Registers a user and adds it to the database. The user will automatically be assigned the USER role.
**Fields to fill:**
-name (string)
-email (string, unique)
-password (string)
```
{
	 "name": "RegistratedUser",
	 "email": "reguser12@new.ru",
 	 "password": "regpass"
}
```
**Test:**
```curl -XPOST -H "Content-type: application/json" -d "{\"name\": \"RegistratedUser\", \"email\": \"reguser12@new.ru\", \"password\": \"regpass\"}" "http://localhost:8080/registration"```

------------------
#### _USER API_

##### Get One Restaurant (GET)
```http://localhost:8080/user/restaurants/{restaurant_id}```
In response to the request, a restaurant with {restaurant_id} will be sent.
**Test:**
```curl -XGET -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/restaurants/100004"```
##### Vote (POST)
```http://localhost:8080/user/restaurants/{restaurant_id}/vote```
The user votes today for the restaurant with {restaurant_id}. If he has already voted today, two cases are possible:
- The time is less than 11:00 -> the previous vote for today is deleted, it is replaced by the vote for the restaurant with {restaurant_id}.
- Time 11:00 and more -> the vote will simply not be counted.

**Test:**
```curl -XPOST -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/restaurants/100004/vote"```
##### Get All Restaurants (GET)
```http://localhost:8080/user/restaurants```
In response to the request, a list of all restaurants from the database was sent.
**Test:**
```curl -XGET -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/restaurant"```
##### Get Statistic (GET)
```http://localhost:8080/user/restaurants/statistic?date=YYYY-MM-DD```
In response to the request, a map will be sent, the key is the unique name of the restaurant, the value is the number of votes. The calculation is relevant for the date specified in the parameters.
**Test:**
```curl -XGET -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/restaurants/statistic?date=2022-06-10"```
##### Get One Meal (GET)
```http://localhost:8080/user/restaurants/{restaurant_id}/menu/{meal_id}```
In response to the request, a meal with {meal_id} belonging to the restaurant with {restaurant_id} will be sent.
**Test:**
```curl -XGET -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/restaurants/100004/menu/100009"```
##### Get Restaurant Menu (GET)
```http://localhost:8080/user/restaurants/{restaurant_id}/menu```
In response to the request, you will receive a complete list of food belonging to the restaurant with {restaurant_id}.
**Test:**
```curl -XGET -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/restaurants/100004/menu"```
##### Get User Profile (GET)
```http://localhost:8080/user/profile```
In response, the data of the authorized user is received.
**Test:**
```curl -XGET -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/profile"```
##### Update User Profile (PUT)
```http://localhost:8080/user/profile```
Changes the data of an authorized user in the database.
**Fields to fill:**
-name (string)
-email (string, unique)
-password (string)
```
{
	 "name": "youUpdatedName",
	 "email": "upduser2@new.ru",
 	 "password": "newpass"
}
```
**Test:**
```curl -XPUT -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" -d "{\"name\": \"youUpdatedName\", \"email\": \"upduser2@new.ru\", \"password\": \"newpass\"}" "http://localhost:8080/user/profile"```
##### Delete User Profile (DELETE)
```http://localhost:8080/user/profile```
Removes an authorized user from the database.
**Tests:**
```curl -XDELETE -H "Content-type: application/json" -u "user_zero@yandex.ru:pass12" "http://localhost:8080/user/profile"```

------------------
#### _ADMIN API_

##### Create Restaurant (POST)
```http://localhost:8080/admin/restaurants```
Registers a new restaurant in the database.
**Fields to fill:**
-name (string, unique)
```
{
	 "name": "Created_restaurant"
}
```
**Test:**
```curl -XPOST -H "Content-type: application/json" -u "admin@gmail.com:admin" -d "{\"name\": \"Created_restaurant\"}" "http://localhost:8080/admin/restaurants"```
##### Update Restaurant (PUT)
```http://localhost:8080/admin/restaurants/{restaurant_id}```
Updates the restaurant with {restaurant_id} in the database
**Fields to fill:**
-name (string, unique)
```
{
	 "name": "Updated_restaurant"
}
```
**Test:**
```curl -XPUT -H "Content-type: application/json" -u "admin@gmail.com:admin" -d "{\"name\": \"Updated Burger Master\"}" "http://localhost:8080/admin/restaurants/100004"```
##### Delete Restaurant (DELETE)
```http://localhost:8080/admin/restaurants/{restaurant_id}```
Removes the restaurant with {restaurant_id} from the database.
**Test:**
```curl -XDELETE -H "Content-type: application/json" -u "admin@gmail.com:admin" "http://localhost:8080/admin/restaurants/100004"```
##### Create Meal (POST)
```http://localhost:8080/admin/restaurants/{restaurant_id}/menu```
Registers a new meal owned by the restaurant {restaurant_id} in the database.
**Fields to fill:**
-name (string, unique for restaurant)
-cost (integer, >0)
```
{
	 "name": "Created_tasty_food",
	 "cost": "99"
}
```
**Test:**
```curl -XPOST -H "Content-type: application/json" -u "admin@gmail.com:admin" -d "{\"name\": \"Created_tasty_food\",\"cost\": \"99\"}" "http://localhost:8080/admin/restaurants/100004/menu"```
##### Update Meal (PUT)
```http://localhost:8080/admin/restaurants/{restaurant_id}/menu/{meal_id}```
Updates the meal owned by the restaurant ID in the database.
**Fields to fill:**
-name (string, unique for restaurant)
-cost (integer, >0)
```
{
	 "name": "Updated_tasty_food",
	 "cost": "111"
}
```
**Test:**
```curl -XPUT -H "Content-type: application/json" -u "admin@gmail.com:admin" -d "{\"name\": \"Updated_Humburger\",\"cost\": \"99\"}" "http://localhost:8080/admin/restaurants/100004/menu/100009"```
##### Delete Meal (DELETE)
```http://localhost:8080/admin/restaurants/{restaurant_id}/menu/{meal_id}```
Removes the meal owned by the restaurant ID in the database.
**Test:**
```curl -XDELETE -H "Content-type: application/json" -u "admin@gmail.com:admin" "http://localhost:8080/admin/restaurants/100004/menu/100009"```
##### Find User By ID (GET)
```http://localhost:8080/admin/users/{user_id}```
In response, the data of user with {user_id} is received.
**Tests:**
```curl -XGET -H "Content-type: application/json" -u "admin@gmail.com:admin" "http://localhost:8080/admin/users/100001"```
##### Find User By Email (GET)
```http://localhost:8080/admin/users?email=XXXX@XXXX.XX```
In response, the user's data comes with the email specified in the parameter.
**Test:**
```curl -XGET -H "Content-type: application/json" -u "admin@gmail.com:admin" "http://localhost:8080/admin/users?email=user@yandex.ru"```
##### Get All Users (GET)
```http://localhost:8080/admin/users/list```
The response is a list of all users.
**Tests:**
```curl -XGET -H "Content-type: application/json" -u "admin@gmail.com:admin" "http://localhost:8080/admin/users/list"```
##### Create User (POST)
```http://localhost:8080/admin/users```
Creates a user and adds it to the database. You can specify user roles. If no roles are specified, the USER role will be set by default.
**Fields to fill:**
-name (string)
-email (string, unique)
-password (string)
-roles (enum, set)
```
{
	 "name": "CreatedAdmin",
	 "email": "newadmin@gmail.com",
 	 "password": "admin999",
 	 "roles":       [
         "USER",
         "ADMIN"
      ]
}
```
**Test:**
```curl -XPOST -H "Content-type: application/json" -u "admin@gmail.com:admin" -d "{\"name\": \"CreatedAdmin\", \"email\": \"newadmin@gmail.com\", \"password\": \"admin999\", \"roles\": [\"USER\", \"ADMIN\"]}" "http://localhost:8080/admin/users"```
##### Update User (PUT)
```http://localhost:8080/admin/users/{user_id}```
Updates a user with ID it to the database. You can specify user roles. If no roles are specified, the USER role will be set by default.
**Fields to fill:**
-name (string)
-email (string, unique)
-password (string)
-roles (enum, set)
```
{
	 "name": "UpdatedUser",
	 "email": "updated_user@yandex.ru",
 	 "password": "updpass",
 	 "roles":       [
         "USER",
         "ADMIN"
      ]
}
```
**Test:**
```curl -XPUT -H "Content-type: application/json" -u "admin@gmail.com:admin" -d "{\"name\": \"UpdatedUser\", \"email\": \"updated_user@yandex.ru\", \"password\": \"updpass\", \"roles\": [\"USER\", \"ADMIN\"]}" "http://localhost:8080/admin/users/100001"```
##### Block/Unblock User (PUT)
```http://localhost:8080/admin/users/{user_id}/block```
Blocks access to the application for the user with {user_id}. If the user is already blocked, unblocks him.
**Test:**
```curl -XPUT -H "Content-type: application/json" -u "admin@gmail.com:admin" "http://localhost:8080/admin/users/100001/block"```
##### Delete User (DELETE)
```http://localhost:8080/admin/users/{user_id}```
Removes the user with {user_id} from the database
**Test:**
```curl -XDELETE -H "Content-type: application/json" -u "admin@gmail.com:admin" "http://localhost:8080/admin/users/100001"```
