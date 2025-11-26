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

- Pb de génération de créneaux (trop de génération de créneaux --> Créer une table créneaux)
- Bloquer les jours avant aujourd'hui
- Afficher dans Heures, les créneaux Overnight

- Pas de JPA
- Pas de Filtres
- Spring MVC
- ORM : EclipseLink
- Authen Avec Filtres

+ :
- Mail
- Upload