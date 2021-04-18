# Covid Contact Tracer
This console application demonstrates a simple **client-server** kiosk type console system allowing people to sign in and out of public venues using a username and password.

During the [server](src/main/java/com/covid_contact_tracer/CTServer.java) lifetime, it records the log-in and log-out actions for each person and categorizes the person's [Risk Factor](src/main/java/com/covid_contact_tracer/RiskFactor.java) based on the number of times they have checked in to a public place(<5 = low risk, >5 & <10 = medium risk, and >10 = high risk).

E.g. A person who has logged in 3 times will be defined as "low risk", while a person having loggen in on 7 different occasions would be defined as a "medium risk".

On startup the server gets its list of [Person](src/main/java/com/covid_contact_tracer/Person.java) objects from the [Seed Data](src/main/java/com/covid_contact_tracer/data/Seed_Data.csv) file, which is used to populate a LinkedList. 
___
***Please note:*** If you wish to run this program, please run the [Server](src/main/java/com/covid_contact_tracer/CTServer.java) first, then you can run as many [Client](src/main/java/com/covid_contact_tracer/CTClient.java) instances as you wish.