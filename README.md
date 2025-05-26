# EcommerceSparkAnalysis

Analyse de données e-commerce avec Apache Spark et Scala

## 🧩 Objectif du projet
Analyser le comportement des utilisateurs et les performances de vente d’une plateforme e-commerce à partir d’un fichier CSV enrichi. Le projet est structuré pour tirer parti de la puissance de Spark via une application Scala modulaire.

## 🛠️ Technologies utilisées
- **Scala 2.12.15**
- **Apache Spark 3.3.0**
- **SBT** (build tool)
- **IntelliJ IDEA**
- **JavaScript & HTML** (pour visualisation web)

## 📁 Architecture du projet
```
EcommerceSparkAnalysis/
├── build.sbt
├── src/
│   ├── main/
│   │   ├── scala/
│   │   │   ├── Main.scala
│   │   │   ├── preprocessing/
│   │   │   │   └── DataPreprocessing.scala
│   │   │   ├── analysis/
│   │   │   │   └── DataAnalysis.scala
│   │   │   ├── visualization/
│   │   │   │   ├── DataVisualization.scala
│   │   │   │   └── HtmlReportGenerator.scala
│   │   └── resources/
│   │       └── ecommerce_data_enriched.csv
├── visualizations/
│   ├── rapport_synthese.md (généré)
│   ├── rapport.html (rapport interactif HTML)
│   └── bar_chart_ventes.png (image générée)
```

## ⚙️ Installation et exécution

### 1. Prérequis
- Java JDK 8 ou +
- IntelliJ IDEA (avec plugin Scala)
- Navigateur Web (pour visualiser `rapport.html`)

### 2. Cloner le projet
```bash
git clone https://github.com/votre-utilisateur/EcommerceSparkAnalysis.git
cd EcommerceSparkAnalysis
```

### 3. Ouvrir dans IntelliJ
- Fichier > Ouvrir > Sélectionner le dossier du projet
- Accepter l’import SBT

### 4. Placer le fichier de données
Copier `ecommerce_data_enriched.csv` dans le dossier :
```
src/main/resources/
```

### 5. Exécuter le programme principal
- Ouvrir `Main.scala`
- Clic droit > `Run 'Main'`

### 6. Consulter les visualisations
- Rapport texte : `visualizations/rapport_synthese.md`
- Rapport interactif (HTML) : `visualizations/rapport.html`
- Graphique exporté : `visualizations/bar_chart_ventes.png`

## 🧪 Fonctionnement du projet

### 🔹 1. Prétraitement (`DataPreprocessing`)
- Chargement CSV
- Nettoyage des données
- Conversion types (int, double, string)
- Création de RDD et DataFrame

### 🔹 2. Analyse (`DataAnalysis`)
- Statistiques globales
- Analyse par catégorie, appareil, pays, score
- Corrélation entre session et achat

### 🔹 3. Visualisation (`DataVisualization` & `HtmlReportGenerator`)
- Affichage console
- Rapport `Markdown` généré dans `visualizations/rapport_synthese.md`
- Rapport web interactif généré dans `visualizations/rapport.html`
- Graphique en image généré dans `visualizations/bar_chart_ventes.png`

## 📈 Exemple de rapport généré
```markdown
# Rapport Synthèse E-commerce

Durée moyenne de session: 12.4 minutes
Corrélation durée/achat: 0.61
...etc.
```

## 📦 build.sbt
```scala
name := "EcommerceSparkAnalysis"
version := "0.1"
scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.3.0",
  "org.apache.spark" %% "spark-sql" % "3.3.0"
)
```

## 💡 Suggestions d’évolution
- Ajouter des visualisations graphiques avec **Vegas** ou **Chart.js**
- Intégration d’un dashboard web avec **Plotly** ou **Grafana**
- Export CSV/Excel des résultats
- Étendre l’analyse aux commentaires utilisateurs (`review_text`)

## 👨‍💻 Auteur
Massi – Projet réalisé dans le cadre de l'atelier Spark & Scala

---

> Pour toute question ou bug, merci d’ouvrir une issue sur le dépôt GitHub.
