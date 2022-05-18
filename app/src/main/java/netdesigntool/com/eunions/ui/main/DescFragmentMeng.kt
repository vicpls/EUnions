package netdesigntool.com.eunions.ui.main

import android.graphics.Color
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.Util.getColorAnyWay
import netdesigntool.com.eunions.ui.description.DescFrag.Companion.newInstance



internal interface DescriptionFragmentManager{
    fun showDesc(desc: Desc)
}



/**
 *  Support class for managing fragments with description of organization EU and Schengen
 *  into the MainActivity only.
 *
 *  The principle - only one fragment in a moment can be present to user.
 *
 *  If the same fragment is called again, it is removed.
 *  When calling another fragment, the first visible one will be removed.
 */
internal class DescFragmentMeng(private val mainActivity: MainActivity): DescriptionFragmentManager
{

    private var isStacked : Boolean = false

    override fun showDesc(desc: Desc)
    {
        val fr = finDescFrag(desc)

        if (desc.show) {
            showFragmentTrans(mainActivity.getPlaceId(desc), fr ?: createFragment(desc))
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
                mainActivity,
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
        mainActivity.supportFragmentManager
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
        mainActivity.supportFragmentManager
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
        mainActivity.supportFragmentManager.findFragmentById(mainActivity.getPlaceId(desc))

}