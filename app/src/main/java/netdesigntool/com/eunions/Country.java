package netdesigntool.com.eunions;

class Country {
    private final int euni;   // 0= Country is a member of EU
    private final int schen;  // 0= Contry is a member of Shengen
    private final String ISO;

    public Country(String sISO, int euni, int schen) {
        this.euni = euni;
        this.schen = schen;
        this.ISO = sISO;
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
}
