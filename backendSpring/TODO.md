~~Quand je créé un compte, je peux lire son nom~~
~~Quand je créé un compte sans date, la date d'ouverture est la date du jour~~
~~Quand je créé un compte avec une date, c'est cette date qui est prise en compte~~
~~Un compte possède un nom donné par la banque~~
~~Un compte est rattaché à une banque~~
~~Je peux visualiser tous mes comptes~~
~~Je peux visualiser les comptes d'une banque précise~~
~~Je peux visualiser un comptes par son nom~~
~~Je ne peux pas créer un compte avec un nom deja existant~~

~~Un compte est associé à une famille~~
~~Un famille porte un nom~~
~~Un compte appartient à 1 membre de la famille~~
~~Un compte peut appartenir à plusieurs membre de la famille~~
~~Un compte appartient par defaut à tous les membres de la famille~~
~~Je ne peux supprimer un membre de la famille si un compte lui est associé~~

~~Mes comptes peuvent être stockées dans une ressource externe. A la réception d'une commande, il faut que je raffraichisse mes comptes depuis cette ressource~~
~~Au rafraichissement des ressources externes, je ne récupère que les comptes de ma famille~~

~~Par defaut le solde du compte est 0~~

~~Je peux ajouter un débit à mon compte~~
~~Je peux ajouter un crédit à mon compte~~
~~QUand je demande mon solde du compte, celui ci correspond a la somme des débits et crédits~~
~~Quand je paye avec un compte, son solde est diminué du montant du compte~~
~~Quand j'ajoute un débit ou un crédit sur mon compte, celui ci est par défaut non pointé~~
~~Je peux pointer ou dépointer un débit ou un crédit~~
~~Je peux retrouver une transaction depuis son ID depuis un compte~~

~~Une transaction est valide si la somme des ces ventilations correspond a son montant~~
~~Une categorie est identifiée par un nom~~
~~Une categorie peut possèder plusieurs sous categories~~

~~Par defaut une ventilation n'est rattachée à aucune categorie~~
~~Un ventilation est rattachée à une catégorie ou une sous categorie~~


~~Quand un owner est ajouté, le mécanisme de persistence est notifié
Quand un owner est retiré, le mécanisme de persistence est notifié
Quand un owner est modifié, le mécanisme de persistence est notifié
Quand un compte est créé, modifié, supprimé, le mécanisme de persistence est notifié~~
~~Quand une transaction est ajouté à un compte, le mécanisme de persistence est notifié
Quand une transaction est modifié, le mécanisme de persistence est notifié
Quand une transaction est supprimé, le mécanisme de persistence est notifié~~

Quand j'ajoute une ventilation avec le même descriptif, je recherche parmis les autres ventilations la catégories
