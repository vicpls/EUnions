package netdesigntool.com.eunions.wiki;



public interface HumanReadableNumber {

    /** Round number in string.
     *
     * @param value number by string, e.g."736539.456"
     * @param accuracy sign quantity after delimiter point, e.g. 2
     * @return string with rounded to nearest billion\thousand number, e.g. "736539.46"
     */
    String roundNumber(String value, int accuracy);


    /**
     * Abbreviate for given number in shorted human readable style.
     * e.g. 1234 = ths, 1234567 = m
     *
     * @param value number in string
     * @return Abbreviate
     */
    String getPrefixForNumber(String value);
}
