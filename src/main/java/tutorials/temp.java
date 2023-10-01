package tutorials;

import java.io.Serializable;

public class temp<V extends Serializable> {
    public temp(){
        Class<?> main = Integer.class;
        System.out.println("Type: " + main.getName());
    }

}
