package com.ecosnap.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ecosnap.R
import kotlinx.android.synthetic.main.fragment_day.view.*
import android.webkit.WebSettings;
import android.webkit.WebView;

class DayFragment : Fragment() {
    private var test = ""
    private var content = ""

    fun newInstance(str: String): DayFragment {
        val dayFragment= DayFragment()
        val args = Bundle()
        args.putString("day", str)
        dayFragment.arguments = args
        return dayFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test = arguments?.getSerializable("day") as String
        content = ("<html>"
                + "  <head>"
                + "    <style>"
                + "    svg { text-align: center!important; }"
                + "    </style>"
                + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.charts.load(\"current\", {packages:[\"corechart\"]});"
                + "      google.charts.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Status', 'Count'],"
                + "          ['Recyclable', 2],"
                + "          ['Not Recyclable', 1]"
                + "        ]);"
                + "        var options = {"
                + "          pieHole: 0.4,"
                + "          chartArea: {left: 16, top: 32},"
                + "          forceIFrame: true,"
                + "          colors: ['#00796B', '#a94850'],"
                + "          legend: {position: 'bottom', alignment: 'center'},"
                + "          tooltip: {text: 'value'}"
                + "        };"
                + "        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body style=\"width: 100%;\">"
                + "    <div id=\"chart_div\" style=\"width: 100%; height: 80%;\" ></div>"
                + "  </body>" + "</html>")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        val webview = view.chart_day
        val webSettings = webview.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webview.requestFocusFromTouch()
        webview.loadDataWithBaseURL("file:///android_asset/", content, "text/html", "utf-8", null)
        return view
    }
}
