package ${package}.services;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Model {
    public Model() {
    }

    private int id;

    @Size(max = 10, min = 5)
    private String name;
    @Min(10)
    private int Age;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return Age;
    }
    public void setAge(int age) {
        Age = age;
    }


}
