# USER
**uid**
- * pseudo [StrLim]
- * nom [StrLim]
- * prenom [StrLim]
- * email [StrLim]
- * tel [StrLim]
- * role [User/Admin/Manager/Artist]
- * verified [Yes/No]
- * date_creation [Date]
- * notification [None/Email/Tel]
- * popularity [Int (Ici le + de followers sur l'un de ses réseaux par exemple)]
- description [Str]
? a2f

# ARTIST
**aid**
***user_id*** (uid)
***manager_id*** (uid)
- * genre [Str]
- * validation_automatique [Yes/No]
- * min_fee_per_minute [0 = None]
- * is_fee_private [Yes/No]
- * zone_km_max [0 = None]
- * min_participants [0 = None]
- * number_max_reservations [0 = No Limit]
- * number_of_reservations_this_week
- * is_artist_mineur [Yes/No]
- * max_hours_playings [Int]
- * rest_hours_needed_after_booking [Int]
? private
? need_technic

# PLANNING
**pid**
***artist_id*** (aid)
- * start_date [Date]
- * end_date [Date]
- * is_valid [Yes/No]
- * artist_occuped [Yes/No]
- fee_special_per_minute [Double]
- note [Str]
- is_note_public [Yes/No]

# BOOKING
**bid**
***booker_id*** (uid)
***artist_id*** (aid)
***date_booking_id*** (pid)
- * status [En attente/Confirmé/Confirmé sans total paiement/Rejeté/Renvoyé/Cancel] 
- * date_created [Date]
- * date_updated [Date]
- * number_of_participants [Int]
- note_with_the_booking [Str]
- list_of_DJs [Str]
- * annulation_delay [Date]
- remboursement_annulation [Double]
- date_revelation_booking [Date, Vide = Public]
- * with_technicals_DJ [Yes/No]
- * deposit_amount_fee
- * amount_fee_required
? public_note
? private_note
? total_fee
? deposit_amount
? payment_status
? policy_status

# REQUIREMENT
**rid**
- * name_requirement [StrLim]
- description [Str]

# NECCESITY
**nid**
***artist_id*** (aid)
***object_id*** (rid)
? quantity
? notes

# LOCALISATION
**lid**
- * name [StrLim]
- address [Str]
- latitude [Double]
- longitude [Double]
- capacity_people [Int]
? contact

# BLACKLIST
**banid**
***artist_id*** (aid)
- * type_blacklisted [Event/Artists/Lieu/Organisations...]
- reason_note [Str]
? name_of_the_blacklisted
? id_of_the_blacklisted

# AVIS
***user_avis_id*** (uid)
***user_cibled_id*** (uid)
- * note [Int between 0 and 10]
- reason_avis [Str]
- * public_avis [Yes/No]

# MESSAGE
***user_recever_id*** (uid)
***user_sender_id*** (uid)
- * Message [Str]
- * Date_created [Date]
? Pièces jointes

# LOGS
**logid**
***utilisateur_id*** (uid)
- * name_action [StrLim]
- * date_created [Date]
- description [Str]