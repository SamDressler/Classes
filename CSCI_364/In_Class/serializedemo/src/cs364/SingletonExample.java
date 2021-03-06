package cs364;
import java.io.Serializable;
public class SingletonExample implements Serializable{
	private static SingletonExample instance = new SingletonExample();
	String name = "UND";
	
	private SingletonExample() {
	}

	public static SingletonExample getInstance() {
		return instance;
	}
	
	public String toString() {
		return name;
	}

    public Object readResolve(){
        return instance;        
    }	
}
