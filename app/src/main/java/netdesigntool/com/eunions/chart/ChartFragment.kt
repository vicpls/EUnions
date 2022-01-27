package netdesigntool.com.eunions.chart

import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import netdesigntool.com.eunions.R

@AndroidEntryPoint
class ChartFragment : Fragment() {

    var lChart : LineChart? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_chart, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lChart = view.findViewById(R.id.lChart)

        val vModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        vModel.ldWHI.observe(viewLifecycleOwner, { drawLine(it as Map<String, Float>) })
    }


    class MyXAxisFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return value.toUInt().toString()
        }

    }

    private fun drawLine(rMap: Map<String, Float>) {

        // На основании массива точек создадим первую линию с названием
        val lEntrs = mapToEntryConverter(rMap)
        val dataset = LineDataSet(lEntrs, "WHI")

        // Создадим переменную данных для графика
        val data = LineData( styleDataSet(dataset) as LineDataSet)
        data.setDrawValues(false)


        //data.setDrawValues(true)
        lChart?.let { styleChart(it) }

        // Передадим данные для графика в сам график
        lChart?.data = data

        // Не забудем отправить команду на перерисовку кадра, иначе график не отобразится
        lChart?.invalidate()
    }

    private fun styleChart (c: LineChart){

        val xAx: XAxis = c.xAxis
        xAx.position = XAxis.XAxisPosition.BOTTOM
        xAx.textSize = 10f
        xAx.textColor = getColorAnyWay(R.color.colorPrimaryDark)
        //xAxis.setDrawLabels(true)
        xAx.setDrawAxisLine(true)
        xAx.axisMinimum=2010f
        xAx.axisMaximum=2020f
        xAx.spaceMax = 2f
        xAx.spaceMin = 1f
        xAx.valueFormatter = MyXAxisFormatter()

        xAx.enableGridDashedLine(0.2f, .2f, 0f)

        //c.setDrawBorders(true)
        // disable description text
        //c.description.isEnabled = false
        c.description.text = resources.getString(R.string.whi)
        c.description.setPosition(0f, 30f)
        c.description.textSize = 14f
        c.description.textAlign = Paint.Align.LEFT
        c.description.textColor = getColorAnyWay( R.color.text)

        c.axisLeft.setDrawLabels(false)
        c.axisRight.isEnabled=false
        c.axisRight.setDrawLabels(false)
        //c.axisRight.
    }


    private fun getColorAnyWay(colorId: Int): Int {

        return if (Build.VERSION.SDK_INT < 23)
                @Suppress("DEPRECATION")
                resources.getColor(colorId)
            else
                resources.getColor(colorId, null)
    }

    private fun styleDataSet(dataset: DataSet<Entry>): DataSet<Entry>{

        dataset.color = getColorAnyWay(R.color.colorPrimaryDark)
        //dataset.

        if (dataset is LineDataSet) {
            dataset.setDrawFilled(true)
            dataset.fillColor = getColorAnyWay(R.color.colorPrimary)
            dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        return dataset
    }

    private fun mapToEntryConverter(m: Map<String, Float>): List<Entry> {

        val result = mutableListOf<Entry>()

        for ((x, y) in m)
            result.add(Entry(x.toFloat(), y.toFloat()))

        return result.sortedBy { it.x }
    }

}