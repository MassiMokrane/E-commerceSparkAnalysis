package visualization

import java.io.{File, PrintWriter}
import org.apache.spark.sql.Row

object HtmlReportGenerator {

  def generateHtmlReport(
                          sessionStats: Row,
                          correlation: Double,
                          salesByCategory: Array[Row],
                          reviewAnalysis: Array[Row],
                          deviceAnalysis: Array[Row],
                          countryAnalysis: Array[Row]
                        ): Unit = {

    val htmlContent =
      s"""
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard E-commerce Pro</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #4361ee;
      --primary-light: #4895ef;
      --secondary: #3f37c9;
      --success: #4cc9f0;
      --dark: #212529;
      --light: #f8f9fa;
      --gray: #6c757d;
      --light-gray: #e9ecef;
      --white: #ffffff;
      --shadow: rgba(0, 0, 0, 0.1);
    }

    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Poppins', sans-serif;
      background-color: #f5f7fa;
      color: var(--dark);
      overflow-x: hidden;
    }

    .container {
      display: flex;
    }

    /* Sidebar */
    .sidebar {
      width: 280px;
      background: linear-gradient(180deg, var(--primary) 0%, var(--secondary) 100%);
      color: var(--white);
      min-height: 100vh;
      position: fixed;
      z-index: 10;
      box-shadow: 5px 0 15px var(--shadow);
      transition: all 0.3s ease;
    }

    .sidebar-header {
      padding: 25px 20px;
      text-align: center;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }

    .sidebar-header h2 {
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 5px;
    }

    .sidebar-header p {
      font-size: 14px;
      opacity: 0.8;
    }

    .nav-menu {
      padding: 20px 0;
    }

    .nav-item {
      padding: 12px 25px;
      display: flex;
      align-items: center;
      font-size: 15px;
      font-weight: 500;
      color: rgba(255, 255, 255, 0.85);
      text-decoration: none;
      transition: all 0.3s;
      border-left: 4px solid transparent;
    }

    .nav-item:hover, .nav-item.active {
      background: rgba(255, 255, 255, 0.1);
      color: var(--white);
      border-left: 4px solid var(--success);
    }

    .nav-item i {
      margin-right: 15px;
      width: 20px;
      text-align: center;
    }

    /* Main Content */
    .main-content {
      flex: 1;
      margin-left: 280px;
      background-color: #f5f7fa;
      min-height: 100vh;
      position: relative;
    }

    header {
      background-color: var(--white);
      padding: 20px 30px;
      box-shadow: 0 2px 10px var(--shadow);
      position: sticky;
      top: 0;
      z-index: 5;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    header h1 {
      font-size: 24px;
      font-weight: 600;
      color: var(--primary);
    }

    .date-info {
      font-size: 14px;
      color: var(--gray);
    }

    .dashboard-content {
      padding: 30px;
    }

    .dashboard-header {
      margin-bottom: 30px;
    }

    .dashboard-header h2 {
      font-size: 28px;
      font-weight: 600;
      margin-bottom: 10px;
      color: var(--dark);
    }

    .dashboard-header p {
      color: var(--gray);
      font-size: 15px;
    }

    .stats-row {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 20px;
      margin-bottom: 30px;
    }

    .stat-card {
      background: var(--white);
      border-radius: 12px;
      padding: 20px;
      box-shadow: 0 5px 15px var(--shadow);
      transition: transform 0.3s;
    }

    .stat-card:hover {
      transform: translateY(-5px);
    }

    .stat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
    }

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 12px;
      background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary) 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-size: 20px;
    }

    .stat-value {
      font-size: 24px;
      font-weight: 600;
      color: var(--dark);
    }

    .stat-label {
      font-size: 14px;
      color: var(--gray);
      margin-top: 5px;
    }

    .chart-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 30px;
      margin-bottom: 30px;
    }

    @media (max-width: 1200px) {
      .chart-grid {
        grid-template-columns: 1fr;
      }
    }

    .chart-card {
      background: var(--white);
      border-radius: 15px;
      padding: 25px;
      box-shadow: 0 5px 15px var(--shadow);
      position: relative;
      overflow: hidden;
    }

    .chart-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .chart-title {
      font-size: 18px;
      font-weight: 600;
      color: var(--dark);
    }

    .chart-icon {
      width: 40px;
      height: 40px;
      border-radius: 10px;
      background: rgba(67, 97, 238, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--primary);
    }

    .chart-container {
      position: relative;
      height: 300px;
      width: 100%;
    }

    .full-width {
      grid-column: 1 / -1;
    }

    footer {
      background-color: var(--white);
      padding: 20px 30px;
      text-align: center;
      font-size: 14px;
      color: var(--gray);
      border-top: 1px solid var(--light-gray);
    }

    .correlation-badge {
      display: inline-block;
      padding: 5px 12px;
      border-radius: 20px;
      font-size: 14px;
      font-weight: 500;
      margin-left: 10px;
      background-color: ${if (correlation > 0.5) "#4cc9f0" else if (correlation > 0) "#90be6d" else "#f94144"};
      color: ${if (correlation > 0.5 || correlation < 0) "#ffffff" else "#2b2d42"};
    }
  </style>
</head>
<body>

<div class="container">
  <nav class="sidebar">
    <div class="sidebar-header">
      <h2>E-Commerce Pro</h2>
      <p>Analyse de performance</p>
    </div>
    <div class="nav-menu">
      <a href="#overview" class="nav-item active">
        <i class="fas fa-home"></i> Vue d'ensemble
      </a>
      <a href="#stats" class="nav-item">
        <i class="fas fa-chart-line"></i> Statistiques
      </a>
      <a href="#sales" class="nav-item">
        <i class="fas fa-shopping-cart"></i> Ventes
      </a>
      <a href="#devices" class="nav-item">
        <i class="fas fa-mobile-alt"></i> Appareils
      </a>
      <a href="#countries" class="nav-item">
        <i class="fas fa-globe-europe"></i> Pays
      </a>
      <a href="#reviews" class="nav-item">
        <i class="fas fa-star"></i> Évaluations
      </a>
    </div>
  </nav>

  <div class="main-content">
    <header>
      <h1>Dashboard Analytique</h1>
      <div class="date-info">
        Dernière mise à jour: ${java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy"))}
      </div>
    </header>

    <div class="dashboard-content">
      <div class="dashboard-header" id="overview">
        <h2>Vue d'ensemble</h2>
        <p>Performances globales et indicateurs clés</p>
      </div>

      <div class="stats-row" id="stats">
        <div class="stat-card">
          <div class="stat-header">
            <div>
              <div class="stat-value">${"%.1f".format(sessionStats.getAs[Double]("avg_duration"))}</div>
              <div class="stat-label">Durée moyenne des sessions (min)</div>
            </div>
            <div class="stat-icon">
              <i class="fas fa-clock"></i>
            </div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-header">
            <div>
              <div class="stat-value">${sessionStats.getAs[Double]("avg_pages").toString}</div>
              <div class="stat-label">Pages vues en moyenne</div>
            </div>
            <div class="stat-icon">
              <i class="fas fa-file"></i>
            </div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-header">
            <div>
              <div class="stat-value">${sessionStats.getAs[Double]("min_duration").toString}</div>
              <div class="stat-label">Durée minimum (min)</div>
            </div>
            <div class="stat-icon">
              <i class="fas fa-hourglass-start"></i>
            </div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-header">
            <div>
              <div class="stat-value">${sessionStats.getAs[Double]("max_duration").toString}</div>
              <div class="stat-label">Durée maximum (min)</div>
            </div>
            <div class="stat-icon">
              <i class="fas fa-hourglass-end"></i>
            </div>
          </div>
        </div>


      </div>

      <div class="chart-grid">
        ${generateChartCard("sales", "Ventes par Catégorie", "fa-shopping-cart", "bar", salesByCategory.map(_.getAs[String]("product_category")), salesByCategory.map(_.getAs[Double]("total_sales")), "Ventes (€)")}

        ${generateChartCard("reviews", "Notes Moyennes par Catégorie", "fa-star", "bar", reviewAnalysis.map(_.getAs[String]("product_category")), reviewAnalysis.map(_.getAs[Double]("avg_score")), "Score moyen")}
      </div>

      <div class="chart-grid">
        ${generateChartCard("devices", "Appareils Utilisés", "fa-mobile-alt", "doughnut", deviceAnalysis.map(_.getAs[String]("device_type")), deviceAnalysis.map(_.getAs[Long]("sessions").toDouble), "Sessions")}

        ${generateChartCard("countries", "Ventes par Pays", "fa-globe-europe", "bar", countryAnalysis.map(_.getAs[String]("country")), countryAnalysis.map(_.getAs[Double]("total_sales")), "Ventes (€)")}
      </div>
    </div>

    <footer>
      &copy; ${java.time.Year.now().getValue()} E-Commerce Analytics Pro | Powered by Spark & Chart.js
    </footer>
  </div>
</div>

<script>
  // Active menu item
  document.addEventListener('DOMContentLoaded', function() {
    const navItems = document.querySelectorAll('.nav-item');

    navItems.forEach(item => {
      item.addEventListener('click', function() {
        navItems.forEach(i => i.classList.remove('active'));
        this.classList.add('active');
      });
    });

    // Scroll check for menu highlight
    window.addEventListener('scroll', function() {
      const scrollPosition = window.scrollY;

      document.querySelectorAll('section[id]').forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.offsetHeight;

        if (scrollPosition >= sectionTop && scrollPosition < sectionTop + sectionHeight) {
          const id = section.getAttribute('id');
          document.querySelectorAll('.nav-item').forEach(item => {
            item.classList.remove('active');
            if (item.getAttribute('href') === '#' + id) {
              item.classList.add('active');
            }
          });
        }
      });
    });
  });
</script>

</body>
</html>
"""
    val pw = new PrintWriter(new File("visualizations/report.html"))
    pw.write(htmlContent)
    pw.close()
  }

  private def generateChartCard(id: String, title: String, iconClass: String, chartType: String, labels: Array[String], data: Array[Double], datasetLabel: String): String = {
    val labelsJs = labels.map(label => "\"" + label.replace("\"", "") + "\"").mkString("[", ",", "]")
    val dataJs = data.mkString("[", ",", "]")
    val canvasId = id + "Chart"

    val colorPalette = if (chartType == "doughnut" || chartType == "pie") {
      """[
        "#4361ee", "#3a0ca3", "#4895ef", "#4cc9f0", "#560bad",
        "#f72585", "#7209b7", "#3f37c9", "#4895ef", "#4cc9f0"
      ]"""
    } else {
      """generateGradient("rgba(67, 97, 238, 0.8)", "rgba(76, 201, 240, 0.8)")"""
    }

    s"""
    <div class="chart-card" id="$id">
      <div class="chart-header">
        <h3 class="chart-title">$title</h3>
        <div class="chart-icon">
          <i class="fas $iconClass"></i>
        </div>
      </div>
      <div class="chart-container">
        <canvas id="$canvasId"></canvas>
      </div>
      <script>
        // Chart for $id
        (function() {
          const ctx = document.getElementById('$canvasId').getContext('2d');

          function generateGradient(colorStart, colorEnd) {
            const gradient = ctx.createLinearGradient(0, 0, 0, 400);
            gradient.addColorStop(0, colorStart);
            gradient.addColorStop(1, colorEnd);
            return gradient;
          }

          const chartConfig = {
            type: '$chartType',
            data: {
              labels: $labelsJs,
              datasets: [{
                label: '$datasetLabel',
                data: $dataJs,
                backgroundColor: $colorPalette,
                borderColor: '$chartType' === 'line' ? "#4361ee" : 'white',
                borderWidth: 2,
                borderRadius: 5,
                tension: 0.4
              }]
            },
            options: {
              responsive: true,
              maintainAspectRatio: false,
              plugins: {
                legend: {
                  position: '${if (chartType == "doughnut" || chartType == "pie") "right" else "top"}',
                  labels: {
                    font: {
                      family: 'Poppins',
                      size: 12
                    },
                    padding: 15
                  }
                },
                tooltip: {
                  enabled: true,
                  backgroundColor: 'rgba(0, 0, 0, 0.7)',
                  titleFont: {
                    family: 'Poppins',
                    size: 14
                  },
                  bodyFont: {
                    family: 'Poppins',
                    size: 13
                  },
                  padding: 12,
                  cornerRadius: 6
                }
              },
              scales: ${
      if (chartType == "doughnut" || chartType == "pie") {
        "{}"
      } else {
        """
                  {
                    x: {
                      grid: {
                        display: false,
                        drawBorder: false
                      },
                      ticks: {
                        font: {
                          family: 'Poppins',
                          size: 12
                        }
                      }
                    },
                    y: {
                      grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                      },
                      ticks: {
                        font: {
                          family: 'Poppins',
                          size: 12
                        }
                      }
                    }
                  }
                  """
      }
    }
            }
          };

          new Chart(ctx, chartConfig);
        })();
      </script>
    </div>
    """
  }
}