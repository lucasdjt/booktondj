# Site - BookTonDJ

Informations disponible prochainement.

```
mvn clean spring-boot:run

# WINDOWS : 
setx MAIL_USERNAME "prenom.nom.etu"
setx MAIL_PASSWORD "xxxx"
setx MAIL_FROM "prenom.nom.etu@univ-lille.fr"
```

[Lien](http://localhost:8080/booktondj/)

Table SQL COUNT :
CREATE TABLE day_clicks (
    day date PRIMARY KEY,
    counter int NOT NULL DEFAULT 0
);