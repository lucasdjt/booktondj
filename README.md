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


- Projet Maven, principes MVC WEB avec DAO pour l'accès aux données

On souhaite maintenant pouvoir faire des réservations à des créneaux particuliers du jour choisi. En cliquant sur une case, on ouvre maintenant le planning du jour avec les créneaux de réservation possibles selon les contraintes imposées (notamment durée du créneau et nb de personnes par créneau). La sélection d’un créneau valide doit permettre d’indiquer le créneau comme réservé de manière persistante. Il doit être possible de retourner sur n’importe quel planning de n’importe quel jour afin de voir les réservations effectuées. Le planning général affiche maintenant dans chaque case non plus un compteur mais une synthèse du planning du jour, par exemple le nombre de créneaux encore disponibles et le nombre total de personnes 2pour la journée. On pourra éventuellement utiliser des codes couleur (plus ou moins rouge selon l’occupation des créneaux ou de la journée) Rien n’est encore vérifié concernant les personnes. Il n’y a pas encore d’authentification. Du coup n’importe qui peut réserver n’importe quoi, on n’a pas encore de compte utilisateur à créer et les rendez-vous ne contiennent pas encore d’identifiant utilisateur.

Réfléchir aux contraintes qui seront gérées et au moyen de les coder. On considèrera au minimum : 
— titre et logo 
— jours ouverts dans la semaine (pour la piscine c’est tous les jours, pour le médecin c’est fermé le Week-End). 
— heures de début, heure de fin de journée 
— durée d’un créneau (pour le médecin c’est 15mn, pour la piscine c’est 1h ou 2h) 
— nombredepersonnes acceptées par créneau (pour le médecin c’est 1, pour la piscine c’est 30) 
Il y a différentes manières de faire .... via un fichier properties; via une table parameters de la BDD, via une procédure d’init à lancer au préalable et qui insère les données nécessaires dans les tables sur SGBD ou via un objet Java . A vous de choisir. 

// exemple de Bean Constraints 
import java.time.LocalTime; 
import java.util.ArrayList; 
import java.util.Arrays; 
public class Piscine implements Constraints { 
    public static ArrayList<Integer> enabledDays = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7)); 
    public static int maxPerSlot = 30; 
    public static long minutesBetweenSlots = 60; 
    public static LocalTime start = LocalTime.of(11, 0); 
    public static LocalTime end = LocalTime.of(18, 0);
}

- Vérifiez que votre site permet de gérer le scenario medecin et le scenario piscine.
- Utiliser un CSS Responsive --> Bootstrap ou Tailwind
- L’ensemble doit toujours être “responsive” et fonctionner sur smartphone.
- Utiliser les derniers cours de Web
