package synch;

public class Testsynchronized1 {

	public static void main(String[] args) {
//		SynThread synThread = new SynThread();
//		Thread thread1 = new Thread(synThread,"SynThread1");
//		Thread thread2 = new Thread(synThread,"SynThread2");
//		thread1.start();
//		thread2.start();
		
		
		Thread thread1 = new Thread(new SynThread(),"SynThread1");
		Thread thread2 = new Thread(new SynThread(),"SynThread2");
		thread1.start();
		thread2.start();
	}
	
	

}
class SynThread implements Runnable{
	
	private static int count;
	
	public SynThread(){
		count = 0;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(this){
			for(int i = 0; i < 5; i++){
				try{
					System.out.println(Thread.currentThread().getName() + ";" + (count ++));
					Thread.sleep(100);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getCount(){
		return count;
	}
}
