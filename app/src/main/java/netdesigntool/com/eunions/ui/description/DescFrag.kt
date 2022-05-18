package netdesigntool.com.eunions.ui.description

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import netdesigntool.com.eunions.Util.LTAG
import netdesigntool.com.eunions.ui.main.EuFragDesc

private const val ARG_DESC = "Desc"     // Tag: 'EU' or 'SC'
private const val ARG_BGCOLOR = "BgClr"
private const val ARG_TXTCOLOR = "TxtClr"
private const val ARG_TXTDESC = "TxtDesc"

/**
 * A simple [Fragment] subclass.
 * Use the [DescFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class DescFrag : Fragment() {
    private var descId: Int? = null
    private var bgColor: Int = 0x362FF0
    private var txtColor: Int = 0xFFFFFF
    private var desc: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            descId = getInt(ARG_TXTDESC)
            bgColor = getInt(ARG_BGCOLOR)
            txtColor = getInt(ARG_TXTCOLOR)
            desc = getString(ARG_DESC) ?: ""
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
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent { descId?.
                let {
                    val clickHandler: ()->Unit  =
                        if ( activity is EuFragDesc) {
                            {(activity as EuFragDesc).onFragClick(desc)}
                        } else {
                            { Log.e(LTAG,
                                """Host activity for
                                    ${this::class.simpleName}
                                    must be ${EuFragDesc::class.simpleName}""")}
                        }
                    ShowContent(descId!!, Color(bgColor), Color(txtColor), clickHandler)
                }
            }
        }
    }

            companion object {
            /**
             * Factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param desc Mark for fragment.
             * @param backColor Color of background, not Id resource
             * @param txtColor Color of text of description, not Id resource
             * @param descId Id of String resource of description
             * @return A new instance of fragment DescFrag.
             */
            @JvmStatic
            fun newInstance(desc: String, @StringRes descId: Int, backColor: Int, txtColor: Int) =
                DescFrag().apply {
                    arguments = Bundle().apply {
                        putString(ARG_DESC, desc)
                        putInt(ARG_BGCOLOR, backColor)
                        putInt(ARG_TXTCOLOR, txtColor)
                        putInt(ARG_TXTDESC, descId)
                    }
                }
        }

    }