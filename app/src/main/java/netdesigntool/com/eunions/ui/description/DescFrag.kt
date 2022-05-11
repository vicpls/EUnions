package netdesigntool.com.eunions.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

private const val ARG_DESC = "Desc"
private const val ARG_BGCOLOR = "BgClr"
private const val ARG_TXTCOLOR = "TxtClr"

/**
 * A simple [Fragment] subclass.
 * Use the [DescFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class DescFrag : Fragment() {
    private var descId: Int? = null
    private var bgColor: Int = 0x362FF0
    private var txtColor: Int = 0xFFFFFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            descId = it.getInt(ARG_DESC)
            bgColor = it.getInt(ARG_BGCOLOR)
            txtColor = it.getInt(ARG_TXTCOLOR)
        }
    }

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_desc, container, false)
            .apply {
                descId?.let { findViewById<TextView>(R.id.DescText)?.setText(it) }
            }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent { descId?.let {
                val closeMe: ()->Unit  = { activity?.supportFragmentManager?.beginTransaction()?.remove(this@DescFrag)?.commit() }
                ShowContent(descId!!, Color(bgColor), Color(txtColor), closeMe)
            } }
        }
    }

            companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param desc Id of String of description.
             * @return A new instance of fragment DescFrag.
             */
            @JvmStatic
            fun newInstance(@StringRes desc: Int, backColor: Int, txtColor: Int) =
                DescFrag().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_DESC, desc)
                        putInt(ARG_BGCOLOR, backColor)
                        putInt(ARG_TXTCOLOR, txtColor)
                    }
                }
        }

    }