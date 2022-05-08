package netdesigntool.com.eunions.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

private const val ARG_DESC = "Desc"

/**
 * A simple [Fragment] subclass.
 * Use the [DescFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class DescFrag : Fragment() {
    private var descId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            descId = it.getInt(ARG_DESC)
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
                ShowContent(descId!!, closeMe)
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
            fun newInstance(@StringRes desc: Int) =
                DescFrag().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_DESC, desc)
                    }
                }
        }

    }