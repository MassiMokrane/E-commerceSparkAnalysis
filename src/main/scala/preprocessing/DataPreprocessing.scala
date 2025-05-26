
package preprocessing

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.rdd.RDD

object DataPreprocessing {
  def process(sc: SparkContext, spark: SparkSession): (RDD[(String, Int, Int, String, Double, Double, String, String, String, String)], DataFrame) = {
    val dataPath = "src/main/resources/ecommerce_data_enriched.csv"
    val rawData = sc.textFile(dataPath)
    val header = rawData.first()
    val data = rawData.filter(_ != header)

    val structuredRDD = data.map(line => {
      val fields = line.split(",(?=([^\"]*\\\"[^\"]*\\\")*[^\"]*$)").map(_.trim)
      try {
        val user_id = fields(0)
        val session_duration = fields(1).toInt
        val pages_viewed = fields(2).toInt
        val product_category = fields(3).replace("\"", "")
        val purchase_amount = fields(4).toDouble
        val review_score = if (fields(5).isEmpty) 0.0 else fields(5).toDouble
        val review_text = fields(6).replace("\"", "")
        val timestamp = fields(7)
        val device_type = fields(8)
        val country = fields(9)
        val city = fields(10)
        (user_id, session_duration, pages_viewed, product_category, purchase_amount, review_score, review_text, timestamp, device_type, country)
      } catch {
        case _: Throwable => ("ERROR", 0, 0, "ERROR", 0.0, 0.0, "", "", "", "")
      }
    }).filter(_._1 != "ERROR")

    import spark.implicits._
    val df = structuredRDD.toDF("user_id", "session_duration", "pages_viewed", "product_category", "purchase_amount", "review_score", "review_text", "timestamp", "device_type", "country")
    val filteredRDD = structuredRDD.filter(r => r._2 >= 1 && r._2 <= 120)
    val filteredDF = df.filter($"session_duration" >= 1 && $"session_duration" <= 120)
    println("\nAperçu des données prétraitées:")
    filteredDF.show(5, truncate = false)
    (filteredRDD, filteredDF)
  }
}