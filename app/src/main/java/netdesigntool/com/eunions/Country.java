package netdesigntool.com.eunions;

public class Country {
    private final int euni;   // 0<= Country is a member of EU
    private final int schen;  // 0<= Contry is a member of Shengen
    private final String ISO;
    private final String name;

    public Country(String sISO, int euni, int schen, String name) {
        this.euni = euni;
        this.schen = schen;
        this.ISO = sISO;
        this.name = name;
    }


    public boolean isEU(){
        return euni <=0;
    }

    public boolean isSchen(){
        return schen <=0;
    }

    public String getISO(){
        return ISO;
    }

    public String getName(){return name;}
}
