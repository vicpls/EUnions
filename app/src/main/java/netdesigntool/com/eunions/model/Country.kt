package netdesigntool.com.eunions.model

data class Country(
    override val iso: String,
    private val euni: Int,          // 0<= Country is a member of EU
    private val schen: Int,         // 0<= Country is a member of Shengen
    override val name: String
) : CommonCountry {

    val isEU: Boolean
        get() = euni <= 0

    val isSchen: Boolean
        get() = schen <= 0
}