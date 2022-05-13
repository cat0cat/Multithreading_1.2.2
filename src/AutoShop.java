import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AutoShop {
    private final static int PRODUCE_TIME = 2000;
    private final static int BUY_TIME = 1000;
    private final static int COUNT_CARS = 10;
    private final List<Cadillac> cadillacs = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void buyCar() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " зашел в автосалон");
            while (cadillacs.isEmpty()) {
                System.out.println("Машин нет");
                condition.await();
            }
            Thread.sleep(BUY_TIME);
            System.out.println(Thread.currentThread().getName() + " уехал на новеньком авто");
            cadillacs.remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void produceCadillac() {
        for (int i = 0; i < COUNT_CARS; i++) {
            try {
                Thread.sleep(PRODUCE_TIME);
                System.out.println(Thread.currentThread().getName() + " выпустил новый Escalade");
                lock.lock();
                cadillacs.add(new Cadillac());
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

}


