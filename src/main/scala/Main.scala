
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Ecommerce Data Analysis").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder().appName("Ecommerce Data Analysis").master("local[*]").getOrCreate()
    sc.setLogLevel("ERROR")

    try {
      println("=== ANALYSE DES DONNÉES E-COMMERCE ===")
      val processedData = preprocessing.DataPreprocessing.process(sc, spark)
      val results = analysis.DataAnalysis.analyze(processedData, spark)
      visualization.DataVisualization.visualize(results, spark)
      println("\n=== ANALYSE TERMINÉE AVEC SUCCÈS ===")
    } catch {
      case e: Exception =>
        println(s"\nERREUR: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      spark.stop()
      sc.stop()
    }
  }
}


