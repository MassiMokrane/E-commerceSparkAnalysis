package analysis

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object DataAnalysis {
  def analyze(data: (RDD[(String, Int, Int, String, Double, Double, String, String, String, String)], DataFrame), spark: SparkSession): Map[String, Any] = {
    val (_, df) = data
    import spark.implicits._

    val sessionStats = df.select(
      round(avg($"session_duration"), 2).alias("avg_duration"),
      round(min($"session_duration"), 2).alias("min_duration"),
      round(max($"session_duration"), 2).alias("max_duration"),
      round(avg($"pages_viewed"), 2).alias("avg_pages")
    ).collect()(0)

    val salesByCategory = df.filter($"purchase_amount" > 0).groupBy($"product_category").agg(
      count("*").alias("transactions"),
      round(sum("purchase_amount"), 2).alias("total_sales")
    ).orderBy(desc("total_sales"))

    val reviewAnalysis = df.filter($"review_score" > 0).groupBy($"product_category").agg(
      count("*").alias("num_reviews"),
      round(avg($"review_score"), 2).alias("avg_score")
    ).orderBy(desc("avg_score"))

    val deviceAnalysis = df.groupBy("device_type").agg(
      count("*").alias("sessions"),
      round(avg("session_duration"), 2).alias("avg_duration"),
      round(sum("purchase_amount"), 2).alias("total_sales")
    )

    val countryAnalysis = df.groupBy("country").agg(
      count("*").alias("sessions"),
      round(sum("purchase_amount"), 2).alias("total_sales")
    ).orderBy(desc("total_sales"))

    val correlation = df.filter($"purchase_amount" > 0).stat.corr("session_duration", "purchase_amount")

    Map(
      "sessionStats" -> sessionStats,
      "salesByCategory" -> salesByCategory.collect(),
      "reviewAnalysis" -> reviewAnalysis.collect(),
      "deviceAnalysis" -> deviceAnalysis.collect(),
      "countryAnalysis" -> countryAnalysis.collect(),
      "correlation" -> correlation
    )
  }
}