//sam Dressler
//inclass deadlock example
//two instances of friends, friend is a static class.
//the entire class becomes the lock object
//1st. two threads start and call bow
//2nd. alphonse runs bow and retreives the lock on the friend class,prints its message, and tries to call bowBack
//3rd. 2nd thread starts and calls bow and prints its message but then deadlocks.
//Both friends can call bow but are locked waiting for bowBack.

public class Deadlock {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s"+ "  has bowed to me!%n",this.name, bower.getName());
            bower.bowBack(this);
        }
        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s"+ " has bowed back to me!%n",this.name, bower.getName());
        }
    }

    public static void main(String[] args) {
    //create two friend classes
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
    //create a new runnable object
        new Thread(new Runnable() {
    //overwrite the run method
            public void run() { alphonse.bow(gaston); }
        }).start();
    //create a new runnable object
        new Thread(new Runnable() {
            public void run() { gaston.bow(alphonse); }
        }).start();
    }
}
