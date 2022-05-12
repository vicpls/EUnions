package netdesigntool.com.eunions.ui.main

import android.graphics.Color
import androidx.annotation.IdRes
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.Util.getColorAnyWay
import netdesigntool.com.eunions.ui.description.DescFrag.Companion.newInstance
import netdesigntool.com.eunions.ui.main.MainActVM.Desc
import netdesigntool.com.eunions.ui.main.MainActVM.Desc.EU
import netdesigntool.com.eunions.ui.main.MainActVM.Desc.Schengen

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

    override fun showDesc(desc: Desc) {
        val fr = getFragment(desc)
        if (fr.isVisible) {
            removeFragmentTrans(fr)
            return
        } else addFragmentTrans(mainActivity.getPlaceId(desc), fr)

        val anotherFr = getAnotherFr(desc)
        if (anotherFr != null && anotherFr.isVisible) removeFragmentTrans(anotherFr)
    }

    private fun getAnotherFr(desc: Desc): Fragment? {
        val frags = findDescFragments()

        return when (desc) {
            is EU -> frags.second
            is Schengen -> frags.first
        }
    }

    // Return an appropriate fragment for showing description text. Create it if it not exist.
    private fun getFragment(desc: Desc): Fragment {

        val frags = findDescFragments()

        return when(desc){

            // For EU - "left" fragment
            is EU -> (if (frags.first != null) frags.first else newInstance(
                desc.descr, getColorAnyWay(R.color.euroUnionNoA, mainActivity, null), Color.WHITE
            ))!!

            // For Schengen "right" fragment
            is Schengen -> (if (frags.second != null) frags.second else newInstance(
                desc.descr, getColorAnyWay(R.color.schengenNoA, mainActivity, null), Color.BLACK
            ))!!
        }
    }

    private fun addFragmentTrans(@IdRes placeId: Int, fr: Fragment?) {
        mainActivity.supportFragmentManager
            .beginTransaction()
            .add(placeId, fr!!)
            .addToBackStack(null)
            //.setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out
            // , R.anim.to_right_in, R.anim.to_right_out)
            .commit()
    }

    private fun removeFragmentTrans(fr: Fragment) {
        mainActivity.supportFragmentManager
            .beginTransaction()
            .remove(fr)
            //.setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out
            // , R.anim.to_right_in, R.anim.to_right_out)
            .commit()
    }

    // Find left and right existing fragments for showing description text.
    private fun findDescFragments(): Pair<Fragment?, Fragment?> {
        val left: Fragment?
        val right: Fragment?
        val fm = mainActivity.supportFragmentManager
        //var placeId = mainActivity.binding.lfDescPlace.id
        var placeId = mainActivity.getPlaceId(EU())
        left = fm.findFragmentById(placeId)
        //placeId = mainActivity.binding.rtDescPlace.id
        placeId =  mainActivity.getPlaceId(Schengen())
        right = fm.findFragmentById(placeId)
        return Pair(left, right)
    }
}