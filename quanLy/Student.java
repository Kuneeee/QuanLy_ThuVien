public class Student implements Comparable<Student>
{
    public Student(String name, float gpa) 
    {
        this.name = name;
        this.gpa = gpa;
    }
    public Student() {}
    //Object is the ancestor of every class
    public int compareTo(Student o) {
        //cast the correct type (Student)o
        if (o.gpa < gpa) return 1;
        else if (o.gpa > gpa) return -1;
        else return 0;
    }
    // Can this equals() be improved and be consistent??
    public boolean equals(Student o) {
        //cast the correct type (Student)o
        if (gpa == o.gpa) return true;
        else return false;
}
    public String getName() { return name;}
    public float getGpa() { return gpa;}
    private String name;
    private float gpa;
}