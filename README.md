# 📱 Smart Task Manager

Une application Android de gestion de tâches intelligente avec une interface Material Design, développée en Java.

## 📸 Captures d'écran

*[Ajoutez vos captures d'écran ici]*

## ✨ Fonctionnalités

### 🔐 Authentification
- Connexion locale avec validation des champs
- Sauvegarde de session avec SharedPreferences
- Déconnexion sécurisée

### 📝 Gestion des tâches (CRUD)
- ✅ **Créer** une tâche avec :
  - Titre
  - Description
  - Date (avec sélecteur)
  - Priorité (Haute/Moyenne/Basse)
  - Catégorie
- ✏️ **Modifier** une tâche existante
- 🗑️ **Supprimer** une tâche (appui long)
- 🔄 **Marquer** comme terminée (checkbox)

### 🔍 Recherche et filtrage
- 🔎 Barre de recherche en temps réel
- 🎯 Filtrage par priorité (Haute/Moyenne/Basse)
- ✅ Filtrage par statut (Terminé/Non terminé)
- 🧹 Réinitialisation des filtres

### 📊 Statistiques visuelles
- 📈 Nombre total de tâches
- ✅ Tâches terminées
- 📊 Taux de progression
- 🎨 Graphique de répartition par priorité

### 🎨 Interface utilisateur
- 🌙 **Mode sombre** paramétrable
- 🎭 Material Design 3
- 📱 Navigation par Bottom Navigation
- 🗂️ Drawer Layout avec menu latéral
- 🃏 Cartes (CardView) pour les tâches
- ➕ Floating Action Button (FAB)
- 📋 RecyclerView optimisé

### 🎯 Visualisation avancée des tâches
- ✅ **Terminées** : Texte barré, fond grisé, indicateur vert
- ⏳ **En cours** : Indicateur orange
- ⚠️ **En retard** : Date en rouge, indicateur rouge
- 🔴 **Priorité Haute** : Badge rouge avec 🔴
- 🟠 **Priorité Moyenne** : Badge orange avec 🟠
- 🟢 **Priorité Basse** : Badge vert avec 🟢

## 🛠️ Technologies utilisées

- **Langage** : Java
- **IDE** : Android Studio
- **Base de données** : SQLite
- **Architecture** : MVVM (Model-View-ViewModel)
- **UI Components** : 
  - Material Design Components
  - RecyclerView
  - CardView
  - CoordinatorLayout
  - DrawerLayout
  - BottomNavigationView
 ## 📦 Structure du projet
    app/src/main/java/com/projectdmn/
├── activities/ # Activités principales
│ ├── LoginActivity.java
│ ├── MainActivity.java
│ ├── AddEditTaskActivity.java
│ └── SettingsActivity.java
├── fragments/ # Fragments
│ ├── TasksFragment.java
│ └── StatisticsFragment.java
├── adapters/ # Adaptateurs RecyclerView
│ └── TaskAdapter.java
├── models/ # Classes modèles
│ └── Task.java
├── helpers/ # Helpers (base de données)
│ └── DatabaseHelper.java
└── utils/ # Utilitaires
├── SharedPrefManager.java
└── DarkModePrefs.java



## 🚀 Installation

1. **Cloner le projet**
```bash
git clone https://github.com/votre-username/smart-task-manager.git
Ouvrir avec Android Studio

File > Open > Sélectionner le dossier du projet

Lancer l'application

Connecter un appareil Android ou démarrer un émulateur

Cliquer sur "Run" (▶️)

📱 Utilisation
Connexion
Lancez l'application

Entrez un nom d'utilisateur (n'importe lequel)

Entrez un mot de passe (minimum 4 caractères)

Cliquez sur "Se connecter"

Gestion des tâches
Ajouter : Cliquez sur le bouton ➕ en bas à droite

Modifier : Cliquez sur une tâche

Supprimer : Appui long sur une tâche

Terminer : Cochez la checkbox

Filtres et recherche
Utilisez la barre de recherche en haut

Filtrez par priorité ou statut avec les boutons

Réinitialisez avec le bouton ✕

Statistiques
Accédez à l'onglet "Statistiques"

Visualisez votre progression

Consultez la répartition par priorité

Mode sombre
Ouvrez le menu latéral

Allez dans "Paramètres"

Activez "Mode sombre"

 Auteur:
Nasri Mohammed 



## 📦 Structure du projet
