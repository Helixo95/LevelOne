# Bienvenu sur le jeu PokeSmart, projet de LevelOne.

## Requierements

Pour lancer le jeu, vous devez avoir installé JavaFX sur votre machine. Pour cela, vous pouvez suivre les instructions sur le site officiel de JavaFX : https://openjfx.io/openjfx-docs/#install-javafx.
La version de JavaFX utilisée pour ce projet est la 21.0.3.

Une fois les packages et le jeu téléchargés, placez dans un même dossier les fichiers suivants :
- LevelOne.jar
- Un dossier 'resources' contenant :
    - Un dossier 'Tiles' contenant les tuiles du jeu pour générer la carte
    - Un dossier 'Items' contenant certains du jeu
    - Un dossier 'Object' contenant d'autres objets du jeu
    - Un dossier 'Monster' contenant les images des monstres du jeu
    - Un dossier 'NPC' contenant les images des pnj du jeu
    - Un dossier 'Player' contenant les images du joueur

## Lancer le jeu

Pour lancer le jeu, vous pouvez utiliser la commande suivante dans le terminal :

```bash
java --module-path "/Users/aurelienruppe/Documents/javafx-sdk-21.0.3/lib" --add-modules javafx.controls,javafx.fxml -jar LevelOne.jar
```


## Comment jouer

Pour vous déplacer, il suffit d'utiliser les touches 'Z', 'Q', 'S' et 'D' de votre clavier. Pour interagir avec les personnages, il suffit de vous placer à côté d'eux et d'appuyer sur la touche 'E'. Pour ouvrir l'inventaire du joueur, appuyez sur la touche 'I'.


## Le jeu

L'objectif du jeu est de trouver le boss final et de le battre. Pour cela, vous devrez explorer la carte, combattre des monstres, récupérer des objets et interagir avec des personnages non-joueurs. 

