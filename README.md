#### Welcome to the Bookstore example.

This is a very simple CRUD Jakarta EE application, used for demonstration of [aicodoo](http://aicodoo.com).

To start the application, deploy it on a Jakarta EE application server (tested with WildFly and Payara) or start is just with the WildFly plugin:
`mvn wildfly:run` (If maven is complaining about not knowing wildfly, use full qualified plugin name: `mvn org.wildfly.plugins:wildfly-maven-plugin:run`.)

The application is using an in-memory database which is dropped at each deployment. Your data will not be saved permanently, until you configure an appropriate DB server connection.
