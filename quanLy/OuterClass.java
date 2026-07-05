public class OuterClass {
    private String outerField = "Đây là biến của OuterClass";

    // InnerClass
    public class InnerClass {
        // Phương thức hiển thị bình thường
        public void display() {
            System.out.println("Gọi từ InnerClass:");
            System.out.println(outerField);
        }

        // Phương thức có callback
        public void doSomethingWithCallback(Callback callback) {
            System.out.println("Gọi từ InnerClass:");
            System.out.println(outerField);

            if (callback != null) {
                callback.onDone("InnerClass đã hoàn tất công việc!");
            }
        }
    }
}
