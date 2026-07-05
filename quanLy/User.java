

public class User {
   private static int age;
   private static String name;
   private static String address;
   private static String heal;
   private static int num;

   public static  String getname(){
    return name;
   }
   public static int getage(){
    return age;
   }
   public static String getaddress(){
    return address;
   }
   public static String getheal(){
    return heal;
   }
   public static int getnum(){
    return num;
   }
   public static void setUser(String hoTen,int tuoi,String diaChi,String sucKhoe,int sdt) {
    name = hoTen;
    age = tuoi;
    address = diaChi;
    heal = sucKhoe;
    num = sdt;
   }
   }
   

