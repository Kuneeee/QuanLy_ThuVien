

public class Flower {
    int petalCount = 0;
    String s = new String("null");
    Flower(int petal) { petalCount = petal; }
    Flower(String ss) { s = ss; }
    Flower(String s, int petal) {
        this(petal);
        this.s = s;
    }
    Flower() { this("hi", 47); }
}