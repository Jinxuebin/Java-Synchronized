package synch;

public class Testsynch1_3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Counter counter = new Counter();
		Thread thread1 = new Thread(counter,"A");
		Thread thread2 = new Thread(counter,"B");
		thread1.start();
		thread2.start();
	}

}
class Counter implements Runnable{
	private int count;
	
	public Counter(){
		count = 0;
	}
	
	//synchronized修饰
	public void countAdd(){
		synchronized(this){
			try{
				for(int i = 0; i < 5; i++){
					System.out.println(Thread.currentThread().getName() + ":" + (count++));
					Thread.sleep(100);	
				}
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	//非synchronized修饰
	public void printCount(){
		for(int i = 0; i < 5; i++){
			try{
				System.out.println(Thread.currentThread().getName() + "count:" + count);
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String threadName = Thread.currentThread().getName();
		if(threadName.equals("A")){
			countAdd();
		}else if(threadName.equals("B")){
			printCount();
		}
		
	}
	
}
