package minicore;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Set;

public class Application {
    public static void main(String[] args) throws IOException {


//        JavaPropsMapper mapper = new JavaPropsMapper();
//        URL resource = SystemConfig.class.getClassLoader().getResource("app.devlopment.properties.json");
//        // convert JSON file to map
//        ObjectMapper objectMapper = new ObjectMapper();
//        byte[] mapData = Files.readAllBytes(Paths.get(resource.getPath()));
//        Map<String, String> myMap = objectMapper.readValue(mapData, HashMap.class);
//        System.out.println("Map is: "+myMap);
//        // print map entries
//
//
//        Properties p= mapper.writeValueAsProperties(myMap);
//        for(Map.Entry<Object,Object> s:p.entrySet()){
//            System.out.println(s.getKey()+"="+s.getValue());
//
//        }
validate();


    }

    public  static  void validate()
    {
        Validator validator = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast( true )
                .buildValidatorFactory()
                .getValidator();

        Car car = new Car( null, false );

        Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

//        assertEquals( 1, constraintViolations.size() );
    }
}

class Car {

    @NotNull
    private String manufacturer;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    @AssertTrue
    private boolean isRegistered;

    public Car(String manufacturer, boolean isRegistered) {
        this.manufacturer = manufacturer;
        this.isRegistered = isRegistered;
    }

    //getters and setters...
}

