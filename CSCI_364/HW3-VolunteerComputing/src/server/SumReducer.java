//Sam Dressler
//SumReducer.java
package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import api.Worker;

/**
 * @author Sam Dressler
 */
public class SumReducer<T extends Number> extends Worker
{
    private static final long serialVersionUID = 4688671069127105445L;
    private List<T> list = new ArrayList<T>();
    private double total = 0.0;

    @SafeVarargs
    public SumReducer(int task_id, T... arr) {
        super(task_id, "SumReducer");
        this.list.addAll(Arrays.asList(arr));
    }

    @Override
    public void doWork() {
        this.total = 0.0;
        for(T num : this.list){
            this.total = this.total + num.doubleValue();
        }
    }
    public double getTotal(){
        return this.total;
    }
    /**
     * Testing Method
     */
    public void main(String[] args){
        Integer[] fib = {1,1,2,3,5,8,13};
        Worker worker = new SumReducer<Integer>(0,fib);
        worker.doWork();
        System.out.println(total);

        Float[] nums = {2.0f, 3.0f, 4.0f};
        Worker worker2 = new SumReducer<Float>(0,nums);
        worker2.doWork();
        System.out.println();
    }
}