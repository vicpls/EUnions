package netdesigntool.com.eunions.ui.main

import androidx.annotation.IdRes

interface EuFragDesc {
    @IdRes
    fun getPlaceId(desc: Desc): Int        // Return View Id for place Description fragment
    fun onFragClick(frDesc : String)       // callback for click on Description fragment
}