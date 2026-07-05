
class CellPhone {
    CellPhone(){ }
    public void ring(Tune t) { t.play(); }
}
class Tune {
    public void play() {
    System.out.println("Tune.play()");
    }
}
class ObnoxiousTune extends Tune{
    ObnoxiousTune() { 
    System.out.println("ObnoxiousTune.play()") ; }
}
public class Tuner {
    /*
    public static void main(String[] args) {
    CellPhone noiseMaker = new CellPhone();
    ObnoxiousTune ot = new ObnoxiousTune();
    noiseMaker.ring(ot); // ot works though Tune called for
    
    Tune t;
    double d = Math.random();
    if (d > 0.5)
    t = new Tune();
    else
    t = new ObnoxiousTune();
    noiseMaker.ring(t);
    }
    */
}

