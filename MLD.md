# MLD dans notre format

## USER
**uid**
- * pseudo [StrLim]
- * nom [StrLim]
- * prenom [StrLim]
- * email [StrLim]
- * role [Enum : User/Admin/Manager/Artist]
- * verified [Boolean]
- * creation_date [Date]
- tel [StrLim] (> Null = Pas de tel)
- notif [Enum : Email/Tel] (> Null = Pas de Notif)
- popularity [Int] (> Null = Pas de données envoyés sur la popularité (Nb max de followers sur un réseau))
- description [Str] (> Null = Pas de Description)

## ARTIST
**aid**
***user_id*** (uid)
***manager_id*** (uid) (> Null = Pas de Manager)
- * genre [Str]
- * valid_auto [Boolean]
- * is_fee_prv [Boolean]
- * is_minor [Boolean]
- * rest_hours_needed [Int]
- * max_hours_playings [Int]
- min_fee [Int] (> Null = Pas de coûts minimal)
- km_max [Int] (> Null = Pas de distances max)
- min_participants [Int] (> Null = Pas de participants requis sur une scène minimum)
- max_reservations [Int] (> Null = Pas de réservations max par SEMAINE)

## PLANNING
**pid**
***bid***
***aid***
- * start_date [Date]
- * end_date [Date]
- * is_occupied [Boolean]
- special_fee [Double] (> Null = Pas de fee special pour cette date (Jour férié = fee special par exemple))
- planning_note [Str] (> Null = Pas de note sur cette date)
- is_note_public [Boolean] (dépend de `note`)

## BOOKING
**bid**
***uid***
***aid***
***pid***
***lid***
- * status [Enum : En attente/Confirmé/Confirmé sans total paiement/Rejeté/Renvoyé par l'artiste/Cancel] 
- * created_date [Date]
- * updated_date [Date]
- * nb_participants [Int]
- * annulation_delay [Date]
- * include_technicals [Boolean]
- * actual_amount [Int]
- * amount_required [Int]
- booking_note [Str] (> Null = Pas de note sur ce booking)
- dj_list [Str] (> Null = Pas de liste DJ données)
- amount_cancel [Double] (> Null = Pas de montant requis à donner en cas d'annulation)
- public_reveal_date [Date] (> Null = Booking mis en public)

## LOCALISATION
**lid**
- * name [StrLim]
- * localisation_type [Enum : Salle/Extérieur/Festival/Others]
- * address [Str]
- latitude [Double] (> Null = Pas de latitude donnée )
- longitude [Double] (> Null = Pas de longitude donnée )

## BLACKLIST
**ban_id**
***aid***
- * blacklist_type [Enum : Event/Artists/Lieu/Organisations/Others]
- * blacklist_ reason [Str]
- * blacklist_name [Str]
- * blacklist_date [Date]
- end_date_blacklist [Date] (> Null = Blacklist permanent )

## LOGS
**log_id**
***uid***
- * action_name [StrLim]
- * log_date [Date]
- log_description [Str] (> Null = Pas de description donnée dans le log )

## AVIS
***reviewer_id*** (uid)
***evaluated_id*** (uid)
- * score [Int between 0 and 10]
- * is_public [Boolean]
- * posted_date [Date]
- reason [Str] (> Null = Pas de raison donnée)

## MESSAGE
**mid**
***sender_id*** (uid)
***receiver_id*** (uid)
- * message [Str]
- * sent_date [Date]

# MLD

User(**uid**,pseudo,nom,prenom,email,role,verified,creation_date,tel,notif,popularity,description)
Artist(**aid**,genre,valid_auto,if_fee_prv,is_minor,rest_hours_needed,max_hours_playings,min_fee,km_max,min_participants,max_reservations,*user_id*,*manager_id*)
Planning(**pid**,start_date,end_date,is_occupied,special_fee,planning_note,is_note_public,*aid*,*bid*)
Localisation(**lid**,name,localisation_type,address,latitude,longitude)
Booking(**bid**,status,created_date,updated_date,nb_participants,annulation_delay,include_technicals,actual_amount,amount_required,booking_note,dj_list,amount_cancel,public_reveal_date,*uid*,*aid*,*pid*,*lid*)
Blacklist(**ban_id**,blacklist_type,blacklist_reason,blacklist_name,blacklist_date,end_date_blacklist,*artist_id*)
Logs(**log_id**,action_name,log_date,log_description,*uid*)
Message(**mid**,message,sent_date,*sender_id*,*receiver_id*)
Avis(***reviewer_id,evaluated_id***,score,is_public,posted_date,reason)