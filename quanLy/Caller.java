interface Incrementable {
    void increment();
}
class Callee implements Incrementable 
{
    private int i = 0;
    public void increment() 
    {
        i ++;
        System.out.println("Callee increments i to " + i);
    }
}

public class Caller {
    private Incrementable callbackReference;
    Caller(Incrementable cbr) {
    callbackReference = cbr;
    }
    void go() {
    callbackReference.increment(); //gọi lại
    }
}

