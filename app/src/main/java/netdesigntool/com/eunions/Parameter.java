package netdesigntool.com.eunions;


import netdesigntool.com.eunions.wiki.HumanReadableNumber;

/** This is container for data got from DataProvider
 *  e.g. Wiki.
 *
 */

public class Parameter {

    /* Provider for getting human readable number*/
    private static HumanReadableNumber hrProvider;

    /** Wiki identifier of parameter */
    public String pId;

    /** Name of parameter. e.g. 'Population of country' */
    public String pName;

    /** Qualifier */
    String pQualifier;

    /** Date of value availability, e.g. '2015 year' */
    public String pDate;

    /** Value of parameter, e.g. '1234.5' */
    public String pValue;

    /** Can pValue transform to human readable form */
    private final boolean isSuitableHRTransform;

    /** Unit of Value, e.g. 'million' */
    String pUnit;


    public Parameter(String pId, String pName, String pQualifier, String pDate, String pValue, boolean isSuitableHRTransform, String pUnit) {
        this.pId = pId;
        this.pName = pName;
        this.pQualifier = pQualifier;
        this.pDate = pDate;
        this.pValue = pValue;
        this.pUnit = pUnit;
        this.isSuitableHRTransform = isSuitableHRTransform;
    }

    public String getQualifiedSting() {

        StringBuilder result = new StringBuilder (pQualifier);

        if (pUnit !=null && !pUnit.isEmpty()) result.append(" ").append(pUnit).append(" ");

        if (result.length() >0 && pDate !=null && !pDate.isEmpty()) result.append(", ");

        if (pDate !=null && !pDate.isEmpty()) result.append("(").append(pDate).append(")");

        return result.toString();
    }

    /**
     * Generates a more convenient for a human format number .
     * @return number in string
     */
    public String getHumanReadableValue(){

        if ( hrProvider !=null && isSuitableHRTransform) {
            return hrProvider.roundNumber(pValue, 1) +" "+ hrProvider.getPrefixForNumber(pValue);
        }
        else return pValue;
    }


    /**
     * Set provider for getting more human readable number value.
     * @param hrProvider provider
     */
    public static void setHrProvider(HumanReadableNumber hrProvider) {
        Parameter.hrProvider = hrProvider;
    }

    /**
     * Get human readable provider.
     * @return provider
     */
    public static HumanReadableNumber getHrProvider() {
        return hrProvider;
    }
}
