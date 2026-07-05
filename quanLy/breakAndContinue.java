

public class breakAndContinue {
    public static void test()
    {
        for (int i = 1; i < 100; i ++)
        {
            if (i == 74) break;
            if (i % 9 != 0) continue;
            System.out.println(i);
        }
    }
}
