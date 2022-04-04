package netdesigntool.com.eunions.model

data class Country(
    val iSO: String,            // 0<= Country is a member of EU
    private val euni: Int,      // 0<= Country is a member of Shengen
    private val schen: Int,
    val name: String
) {
    val isEU: Boolean
        get() = euni <= 0

    val isSchen: Boolean
        get() = schen <= 0
}