# Site - BookTonDJ

Informations disponible prochainement.

```
mvn clean package
mvn cargo:run
psql -U postgres -d booktondj
java -cp ~/.m2/repository/com/h2database/h2/* jar org.h2.tools.Server -web
```

[Lien](http://localhost:8080/booktondj/)

Table SQL COUNT :
CREATE TABLE day_clicks (
    day date PRIMARY KEY,
    counter int NOT NULL DEFAULT 0
);