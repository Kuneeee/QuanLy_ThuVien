abstract class Actor {
    abstract void act();
}
class HappyActor extends Actor {
    public void act() { 
        System.out.println("Happy Actor is acting!");
    }
}
class SadActor extends Actor {
    public void act() { 
        System.out.println("Sad Actor is acting!");
    }
}
class Stage {
    Actor a = new HappyActor();
    void change() { a = new SadActor(); }
    void go() { a.act(); }
}
public class Transmogrify {
    /*
    public static void main(String[] args){
        Stage s = new Stage();
        s.go(); // happy actor
        s.change();
        s.go(); // sad actor
    }
    */
}
