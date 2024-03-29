package netdesigntool.com.eunions.ui.main

import android.app.Activity
import android.graphics.Color
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.Util.getColorAnyWay
import netdesigntool.com.eunions.ui.description.DescFrag.Companion.newInstance


interface DescriptionFragmentManager{
    fun showDesc(desc: Desc)
}



/**
 *  Support class for managing fragments with description of organization EU and Schengen
 *  into the MainActivity.
 *
 *  @param activity must be extended [FragmentActivity] and implements [EuFragDesc]
 *
 *  The principle - only one fragment in a moment can be present to user.
 *
 *  If the same fragment is called again, it is removed.
 *  When calling another fragment, the first visible one will be removed.
 */
internal class DescFragmentMang(private val activity: Activity): DescriptionFragmentManager
{

    init{
        if (activity !is EuFragDesc || activity !is FragmentActivity)
            throw Exception("""${javaClass.simpleName} must be implemented  
                               ${EuFragDesc::class.simpleName} and extend 
                               ${FragmentActivity::class.simpleName}""")
    }


    private var isStacked : Boolean = false

    override fun showDesc(desc: Desc)
    {
        val fr = finDescFrag(desc)

        if (desc.show) {
            showFragmentTrans((activity as EuFragDesc).getPlaceId(desc), fr ?: createFragment(desc))
        } else {
            fr?.let{ removeFragmentTrans(fr)}
        }
    }


    private fun createFragment(desc: Desc): Fragment =
        newInstance(
            desc.mark,
            desc.descr,
            getColorAnyWay(
                getBackgColor(desc),
                activity,
                null
            ),
            getTxtColor(desc)
        )

    private fun getTxtColor(desc: Desc): Int =
        if (desc is Desc.EU) Color.WHITE else Color.BLACK

    private fun getBackgColor(desc: Desc): Int =
        if (desc is Desc.EU) R.color.euroUnionNoA else R.color.schengenNoA


    private fun showFragmentTrans(@IdRes placeId: Int, fr: Fragment?)
    {
        (activity as FragmentActivity).supportFragmentManager
            .beginTransaction()
            .replace(placeId, fr!!)
            .apply{
                if (!isStacked) {
                addToBackStack(null)
                isStacked=true
            }}
            //.setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out
            // , R.anim.to_right_in, R.anim.to_right_out)
            .commit()
    }

    private fun removeFragmentTrans(fr: Fragment)
    {
        (activity as FragmentActivity).supportFragmentManager
            .apply{ popBackStack()
                    isStacked = false
                  }
            .beginTransaction()
            .remove(fr)
            //.setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out
            // , R.anim.to_right_in, R.anim.to_right_out)
            .commit()
    }

    // Find existing fragments for showing description text.
    private fun finDescFrag(desc: Desc): Fragment? =
        (activity as MainActivity)
            .supportFragmentManager.findFragmentById(activity.getPlaceId(desc))

}