# Site - BookTonDJ

Informations disponible prochainement.

```
mvn clean package
mvn cargo:run
psql -U postgres -d booktondj
```

[Lien](http://localhost:8080/booktondj/)

Table SQL COUNT :
CREATE TABLE day_clicks (
    day date PRIMARY KEY,
    counter int NOT NULL DEFAULT 0
);