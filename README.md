_Restaurant voting application_
===============================
Developed as an example of Rest API , for students of topjava.ru, the application should demonstrate the main ways to solve problems in the design of Rest API in Java.
We connect Postgress SQL and create Docker file in additional lessons.

##### Current Version 1.1
Application for user voting for the best restaurant for lunch. It is a simple REST API and uses basic authorization. Made according to the technical requirement.
[Project on GitHub](https://github.com/AntlimagLight/restaurants_voting)
Designed by [AntimagLight](https://www.linkedin.com/in/anton-dvorko-53a545263/)

------------------
- Stack: [JDK 17](http://jdk.java.net/17/), [Maven](https://maven.apache.org/), Spring Boot 3.0 (web, datajpa, validation, security, cache, tests), JUnit Jupiter API, Lombok, H2, mapstruct, swagger, [SoapUI](https://www.soapui.org/).
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
Use the inMemory database to make it easier to run and test your application.

P.S.: Make sure everything works with the latest version that is on github :)  
P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

------------------
## _REST API documentation_
[Link to Swagger](http://localhost:8080/swagger-ui/index.html) /// [Link to H2 Console](http://localhost:8080/h2)