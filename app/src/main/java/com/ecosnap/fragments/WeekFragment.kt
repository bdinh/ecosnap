package com.ecosnap.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.Model.WeekChartData
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_week.view.*

class WeekFragment : Fragment() {
    private lateinit var data: WeekChartData
    private var content = ""

    fun newInstance(str: String, data: WeekChartData): WeekFragment {
        val weekFragment= WeekFragment()
        val args = Bundle()
        args.putString("week", str)
        args.putSerializable("data", data)
        weekFragment.arguments = args
        return weekFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.getSerializable("data") as WeekChartData
        content = ("<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.charts.load(\"current\", {packages:[\"corechart\"]});"
                + "      google.charts.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Dates', 'Recyclable', 'Not Recyclable'],"
                + "          ['" + data.weekData[6].date + "'," + data.weekData[6].dayR + ", " + data.weekData[6].dayNR + "],"
                + "          ['" + data.weekData[5].date + "', " + data.weekData[5].dayR + ", " + data.weekData[5].dayNR + "],"
                + "          ['" + data.weekData[4].date + "', " + data.weekData[4].dayR + ", " + data.weekData[4].dayNR + "],"
                + "          ['" + data.weekData[3].date + "', " + data.weekData[3].dayR + ", " + data.weekData[3].dayNR + "],"
                + "          ['" + data.weekData[2].date + "', " + data.weekData[2].dayR + ", " + data.weekData[2].dayNR + "],"
                + "          ['" + data.weekData[1].date + "', " + data.weekData[1].dayR + ", " + data.weekData[1].dayNR + "],"
                + "          ['" + data.weekData[0].date + "', " + data.weekData[0].dayR + ", " + data.weekData[0].dayNR + "]"
                + "        ]);"
                + "        var options = {"
                + "          chartArea: {left: 72, top: 32},"
                + "          forceIFrame: true,"
                + "          colors: ['#00796B', '#a94850'],"
                + "          legend: {position: 'bottom', alignment: 'center'}"
                + "        };"
                + "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"chart_div\" style=\"width: 100%; height: 80%;\" ></div>"
                + "  </body>" + "</html>")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        val webview = view.chart_week
        val webSettings = webview.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webview.requestFocusFromTouch()
        webview.loadDataWithBaseURL("file:///android_asset/", content, "text/html", "utf-8", null)
        return view
    }
}