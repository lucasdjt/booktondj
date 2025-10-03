# USER
**uid**
- * pseudo [StrLim]
- * nom [StrLim]
- * prenom [StrLim]
- * email [StrLim]
- * role [Enum : User/Admin/Manager/Artist]
- * verified [Boolean]
- * date_creation [Date]
- tel [StrLim]
- notification [Enum : Email/Tel]
- popularity [Int (Ici le + de followers sur l'un de ses réseaux par exemple)]
- description [Str]

# ARTIST
**aid**
***user_id*** (uid)
- * genre [Str]
- * validation_automatique [Boolean]
- * is_fee_private [Boolean]
- * is_artist_mineur [Boolean]
- * rest_hours_needed_after_booking [Int]
- * max_hours_playings [Int]
***manager_id*** (uid) (OPTIONNEL)
- min_fee_per_minute [Int]
- zone_km_max [Int]
- min_participants [Int]
- number_max_reservations [Int]

# PLANNING
**pid**
***artist_id*** (aid)
***localisation_id*** (lid)
- * start_date [Date]
- * end_date [Date]
- * artist_occuped [Boolean]
- fee_special_per_minute [Double]
- note [Str]
- is_note_public [Boolean]

# BOOKING
**bid**
***booker_id*** (uid)
***artist_id*** (aid)
***date_booking_id*** (pid)
- * status [Enum : En attente/Confirmé/Confirmé sans total paiement/Rejeté/Renvoyé par l'artiste/Cancel] 
- * date_created [Date]
- * date_updated [Date]
- * number_of_participants [Int]
- * annulation_delay [Date]
- * with_technicals_DJ [Boolean]
- * deposit_amount_fee [Int]
- * amount_fee_required [Int]
- note_with_the_booking [Str]
- list_of_DJs [Str]
- remboursement_annulation [Double]
- date_revelation_booking [Date]

# LOCALISATION
**lid**
- * name [StrLim]
- * type_localisation [Enum : Salle/Extérieur/Festival/Others]
- * address [Str]
- latitude [Double]
- longitude [Double]

# BLACKLIST
**banid**
***artist_id*** (aid)
- * type_blacklisted [Enum : Event/Artists/Lieu/Organisations/Others]
- * reason_note [Str]
- * id_of_the_blacklisted [Str]
- * date_blacklist [Date]
- end_date_blacklist [Date]

# AVIS
***user_avis_id*** (uid)
***user_cibled_id*** (uid)
- * note [Int between 0 and 10]
- * public_avis [Boolean]
- * date_created [Date]
- reason_avis [Str]

# MESSAGE
***user_recever_id*** (uid)
***user_sender_id*** (uid)
- * Message [Str]
- * Date_created [Date]

# LOGS
**logid**
***utilisateur_id*** (uid)
- * name_action [StrLim]
- * date_created [Date]
- description [Str]