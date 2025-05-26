/*package visualization

import org.apache.spark.sql.{Row, SparkSession}
import java.io.{File, PrintWriter}

object DataVisualization {
  def visualize(results: Map[String, Any], spark: SparkSession): Unit = {
    val sessionStats = results("sessionStats").asInstanceOf[Row]
    val salesByCategory = results("salesByCategory").asInstanceOf[Array[Row]]
    val reviewAnalysis = results("reviewAnalysis").asInstanceOf[Array[Row]]
    val deviceAnalysis = results("deviceAnalysis").asInstanceOf[Array[Row]]
    val countryAnalysis = results("countryAnalysis").asInstanceOf[Array[Row]]
    val correlation = results("correlation").asInstanceOf[Double]

    println("\nDurée moyenne de session: " + sessionStats.getAs[Double]("avg_duration") + " minutes")
    println("Corrélation durée/montant d'achat: " + f"$correlation%.2f")

    println("\nTop 5 catégories de ventes:")
    salesByCategory.take(5).foreach { row =>
      println(f"- ${row.getAs[String]("product_category")} : ${row.getAs[Double]("total_sales")}%2.2f €")
    }

    val outputDir = new File("visualizations")
    if (!outputDir.exists()) outputDir.mkdir()
    val writer = new PrintWriter(new File(outputDir, "rapport_synthese.md"))
    writer.println("# Rapport Synthèse E-commerce\n")
    writer.println(s"**Durée moyenne de session:** ${sessionStats.getAs[Double]("avg_duration")} minutes")
    writer.println(s"**Corrélation durée/achat:** ${correlation.formatted("%.3f")}\n")
    writer.println("## Ventes par catégorie\n")
    salesByCategory.foreach { row =>
      writer.println(s"- ${row.getAs[String]("product_category")}: ${row.getAs[Double]("total_sales")} €")
    }
    writer.close()
    println("\nRapport de synthèse généré dans le dossier 'visualizations'.")
  }
}
package visualization

import org.apache.spark.sql.{Row, SparkSession}
import java.io.{File, PrintWriter}

import org.jfree.chart.{ChartFactory, ChartUtils}
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.general.DefaultPieDataset
import java.awt.Color

object DataVisualization {

  def visualize(results: Map[String, Any], spark: SparkSession): Unit = {
    val sessionStats = results("sessionStats").asInstanceOf[Row]
    val salesByCategory = results("salesByCategory").asInstanceOf[Array[Row]]
    val reviewAnalysis = results("reviewAnalysis").asInstanceOf[Array[Row]]
    val deviceAnalysis = results("deviceAnalysis").asInstanceOf[Array[Row]]
    val countryAnalysis = results("countryAnalysis").asInstanceOf[Array[Row]]
    val correlation = results("correlation").asInstanceOf[Double]

    println("\nDurée moyenne de session: " + sessionStats.getAs[Double]("avg_duration") + " minutes")
    println("Corrélation durée/montant d'achat: " + f"$correlation%.2f")

    println("\nTop 5 catégories de ventes:")
    salesByCategory.take(5).foreach { row =>
      println(f"- ${row.getAs[String]("product_category")} : ${row.getAs[Double]("total_sales")}%2.2f €")
    }

    val outputDir = new File("visualizations")
    if (!outputDir.exists()) outputDir.mkdir()

    val writer = new PrintWriter(new File(outputDir, "rapport_synthese.md"))
    writer.println("# Rapport Synthèse E-commerce\n")
    writer.println(s"**Durée moyenne de session:** ${sessionStats.getAs[Double]("avg_duration")} minutes")
    writer.println(s"**Corrélation durée/achat:** ${correlation.formatted("%.3f")}\n")
    writer.println("## Ventes par catégorie\n")
    salesByCategory.foreach { row =>
      writer.println(s"- ${row.getAs[String]("product_category")}: ${row.getAs[Double]("total_sales")} €")
    }
    writer.close()

    // ✅ Appel des méthodes définies plus bas dans ce même objet
    createBarChart(salesByCategory)

    println("\nRapport de synthèse généré dans le dossier 'visualizations'.")
    println("Graphiques générés : bar_chart_ventes.png, pie_chart_devices.png")
  }

  // ✅ Fonction bar chart
  def createBarChart(salesByCategory: Array[Row]): Unit = {
    val dataset = new DefaultCategoryDataset()
    salesByCategory.foreach { row =>
      val category = row.getAs[String]("product_category")
      val totalSales = row.getAs[Double]("total_sales")
      dataset.addValue(totalSales, "Ventes", category)
    }

    val chart = ChartFactory.createBarChart(
      "Ventes par Catégorie",
      "Catégorie",
      "Ventes (€)",
      dataset
    )
    chart.setBackgroundPaint(Color.white)

    val chartFile = new File("visualizations/bar_chart_ventes.png")
    ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600)
  }




}
*/
package visualization

import org.apache.spark.sql.{Row, SparkSession}

object DataVisualization {
  def visualize(results: Map[String, Any], spark: SparkSession): Unit = {
    val sessionStats = results("sessionStats").asInstanceOf[Row]
    val salesByCategory = results("salesByCategory").asInstanceOf[Array[Row]]
    val reviewAnalysis = results("reviewAnalysis").asInstanceOf[Array[Row]]
    val deviceAnalysis = results("deviceAnalysis").asInstanceOf[Array[Row]]
    val countryAnalysis = results("countryAnalysis").asInstanceOf[Array[Row]]
    val correlation = results("correlation").asInstanceOf[Double]

    HtmlReportGenerator.generateHtmlReport(
      sessionStats, correlation,
      salesByCategory, reviewAnalysis,
      deviceAnalysis, countryAnalysis
    )
  }
}
