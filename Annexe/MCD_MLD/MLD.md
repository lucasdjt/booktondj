# MLD dans notre format

## USERS
**uid**
- * pseudo [StrLim]
- * nom [StrLim]
- * surname [StrLim]
- * email [StrLim]
- * role [Enum : User/Admin/Ressource]
- * verified [Boolean]
- * creation_date [Date]
- tel [StrLim] (> Null = Pas de tel)
- notif [Enum : Email/Tel] (> Null = Pas de Notif)
- description [Str] (> Null = Pas de Description)

## LOCALISATIONS
**lid**
- * name [StrLim]
- * type [StrLim]
- * address [Str]

## SERVICES
**sid**
***uid*** (uid)
*lid* 
- * name [StrLim]
- * type [StrLim]
- * capacity [Int]
- description [Str]
- is_active [Boolean]
- price [Double]

## PLANNINGS
**pid**
***sid***
- * start_date [Date]
- * end_date [Date]
- * is_available [Boolean]
- * is_note_public [Boolean] (dépend de `note`)
- planning_note [Str] (> Null = Pas de note sur cette date)
- special_price [Double] (> Null = Pas de fee special pour cette date (Jour férié = fee special par exemple))

## BOOKINGS
**bid**
***uid***
***sid***
***pid***
*lid*
- * status [Enum : Completed/Pending/Rejected/Cancelled] 
- * created_date [Date]
- * updated_date [Date]
- * nb_participants [Int]
- * amount [Double]
- * amount_required [Double]
- note [Str] (> Null = Pas de note sur ce booking)

## BLACKLISTS
**ban_id**
***sid***
- * type [Enum : Users/Localisations/Others]
- * reason [Str]
- * identifier [Str]
- * ban_date [Date]
- end_date [Date] (> Null = Blacklist permanent )

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

## MESSAGES
**mid**
***sender_id*** (uid)
***receiver_id*** (uid)
- * message [Str]
- * sent_date [Date]

# MLD

Users(**uid**,pseudo,name,surname,email,role,verified,creation_date,tel,notif,description)
Localisations(**lid**,name,type,address)
Services(**sid**,name,type,capacity,description,is_active,price,*uid*,*lid*)
Plannings(**pid**,start_date,end_date,is_available,is_note_public,planning_note,special_price,*sid*)
Bookings(**bid**,status,created_date,updated_date,nb_participants,amount,amount_required,note,*uid*,*sid*,*pid*,*lid*)
Blacklists(**ban_id**,type,reason,identifier,ban_date,end_date,*sid*)
Logs(**log_id**,action_name,log_date,log_description,*uid*)
Messages(**mid**,message,sent_date,*sender_id*,*receiver_id*)
Avis(***reviewer_id,evaluated_id***,score,is_public,posted_date,reason)