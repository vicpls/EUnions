package netdesigntool.com.eunions.ui

import android.annotation.SuppressLint
import android.view.View
import android.view.WindowInsets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class InsetsListener : View.OnApplyWindowInsetsListener {

    @SuppressLint("NewApi")
    override fun onApplyWindowInsets(v: View, windowInsets: WindowInsets): WindowInsets {

        val bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()
        or WindowInsetsCompat.Type.displayCutout())

        // Apply the insets as a margin to the view. This solution sets only the
        // bottom, left, and right dimensions, but you can apply whichever insets are
        // appropriate to your layout. You can also update the view padding if that's
        // more appropriate.

        v.updatePadding(
           bars.left ,
           bars.top,
          bars.right,
        bars.bottom
        )

        // Return CONSUMED if you don't want the window insets to keep passing
        // down to descendant views.
        return WindowInsets.CONSUMED
    }
}