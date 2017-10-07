package synch;

public class Testsynch3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SyncThread syncThread1 = new SyncThread();
		SyncThread syncThread2 = new SyncThread();
		Thread thread1 = new Thread(syncThread1,"SyncThread1");
		Thread thread2 = new Thread(syncThread2,"SyncThread2");
		thread1.start();
		thread2.start();
	}

}

class SyncThread implements Runnable{

	private static int count;
	
	public SyncThread(){
		count = 0;
	}
	
	public synchronized static void method(){
		for(int i = 0; i < 5; i++){
			try{
				System.out.println(Thread.currentThread().getName() + ":" + (count ++));
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		method();
	}
	
}