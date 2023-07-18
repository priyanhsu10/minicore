package io.testing.services;





import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Model {
    public Model() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

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
@Size(max = 10,min = 5)
    private String name;
    @Min(10)
    private int Age;

}
