什么是Synchronized
>synchronized是Java中的关键字，是一种同步锁，它修饰的对象有以下几种：

* 修饰一个代码块，被修饰的代码块被称为同步语句块，其作用的范围是大括号{}起来的代码，作用的对象是调用这个代码块的对象
* 修饰一个方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象
* 修饰一个静态的方法，其作用的范围是整个静态方法，作用的对象是这个类的所有对象
* 修饰一个类，其作用的范围是synchronized后面括号起来的部分，作用的对象是这个类的所有对象

目录
=================

 * [1. 修饰一个代码块](#1-修饰一个代码块)
 	*  [1.1 dome1-两个线程访问同一个加锁对象](#11-dome1-两个线程访问同一个加锁对象)
 	*  [1.2 dome2-两个线程访问不同的加锁对象](#12-dome2-两个线程访问不同的加锁对象)
 	*  [1.3 dome3-两个线程访问同一个既有synchronized修饰的代码块，也有非synchronized修饰的代码块的对象](#13-dome3-两个线程访问同)
 * [2. 修饰一个方法](#2-修饰一个方法)
 * [3. 修饰一个静态的方法](#3-修饰一个静态的方法)
 * [4. 修饰一个类](#4-修饰一个类)
 * [5. 结束](#5-结束)
 	
	

# 1. 修饰一个代码块
修饰一个代码块，被修饰的代码块被称为同步语句块，其作用的范围是大括号{}起来的代码，作用的对象是调用这个代码块的对象

>dome class:
<pre>
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
</pre>
## 1.1 dome1-两个线程访问同一个加锁对象
一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞,必须等待当前线程执行完这个代码块以后才能执行该代码块。

>Test：
<pre>
SynThread synThread = new SynThread();
Thread thread1 = new Thread(<a href="#learn"><strong>synThread</strong></a>,"SynThread1");
Thread thread2 = new Thread(<a href="#learn"><strong>synThread</strong></a>,"SynThread2");
thread1.start();
thread2.start();
</pre>
>结果：
<pre>
SynThread2;0
SynThread2;1
SynThread2;2
SynThread2;3
SynThread2;4
SynThread1;5
SynThread1;6
SynThread1;7
SynThread1;8
SynThread1;9
</pre>
## 1.2 dome2-两个线程访问不同的加锁对象
当两个并发线程(thread1和thread2)访问不同对象的synchronized代码块时，这时会有两把锁分别锁定第一个syncThread对象和第二个syncThread对象，而这两把锁是互不干扰的，不形成互斥，所以两个线程可以同时执行。

>Test:
<pre>
Thread thread1 = new Thread(<a href="#learn"><strong>new SynThread()</strong></a>,"SynThread1");
Thread thread2 = new Thread(<a href="#learn"><strong>new SynThread()</strong></a>,"SynThread2");
thread1.start();
thread2.start();
</pre>
>结果：
<pre>
SynThread1;0
SynThread2;1
SynThread1;2
SynThread2;3
SynThread2;4
SynThread1;5
SynThread1;6
SynThread2;7
SynThread1;8
SynThread2;9
</pre>
## 1.3 dome3-两个线程访问同一个既有synchronized修饰的代码块，也有非synchronized修饰的代码块的对象

当一个线程访问对象的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该对象中的非synchronized(this)同步代码块。

>dome class:
<pre>
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
</pre>

>Test:
<pre>
Counter counter = new Counter();
Thread thread1 = new Thread(counter,"A");
Thread thread2 = new Thread(counter,"B");
thread1.start();
thread2.start();
</pre>

>结果：
<pre>
A:0
Bcount:1
A:1
Bcount:2
A:2
Bcount:3
A:3
Bcount:4
A:4
Bcount:5
</pre>
## 2. 修饰一个方法
>用法：
<pre>
public <strong>synchronized</strong> void <strong>method()</strong> 
{
 //todo...
} 
</pre>

<a>等价于修饰代码块中的-></a>
<pre>
public void method(){
	synchronized(this){
		//todo
	}
}
</pre>
>注意

* synchronized关键字不能继承，子类继承父类必须重写该方法，并且加上synchronized关键字。
<pre>
class parent{
	public synchronized void method(){}
}

class child{
	public synchronized void method(){}
}

</pre>
* 在定义接口方法是不能使用synchronized关键字。
* 在构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步。

# 3. 修饰一个静态的方法
>用法:

<pre>
public synchronized static void method(){
	//todo
}
</pre>
由于静态方法属于类而不属于对象，同样，synchronized修饰的静态方法锁定的是这个类的<a><strong>所有对象</strong></a>。
>dome class:

<pre>
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
</pre>
>test:
<pre>
SyncThread <a><strong>syncThread1</strong></a> = new SyncThread();
SyncThread <a><strong>syncThread2</strong></a> = new SyncThread();
Thread thread1 = new Thread(<a><strong>syncThread1</strong></a>,"SyncThread1");
Thread thread2 = new Thread(<a><strong>syncThread2</strong></a>,"SyncThread2");
thread1.start();
thread2.start();
</pre>
>result:
<pre>
SyncThread1:0
SyncThread1:1
SyncThread1:2
SyncThread1:3
SyncThread1:4
SyncThread2:5
SyncThread2:6
SyncThread2:7
SyncThread2:8
SyncThread2:9
</pre>
# 4. 修饰一个类
>用法：
<pre>
class ClassName{
	public void method(){
		<a><strong>synchronized</strong></a>(ClassName.class){
			//TODO
		}
	}
}
</pre>
>dome class:
<pre>
class SyncThread implements Runnable{

	private static int count;
	
	public SyncThread(){
		count = 0;
	}
	
	public void method(){
		synchronized(SyncThread.class){
			for(int i = 0; i < 5; i++){
				try{
					System.out.println(Thread.currentThread().getName() + ":" +(count ++));
					Thread.sleep(100);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		method();
	}
	
}
</pre>
>test:
<pre>
SyncThread <a><strong>syncThread1</strong></a> = new SyncThread();
SyncThread <a><strong>syncThread2</strong></a> = new SyncThread();
Thread thread1 = new Thread(<a><strong>syncThread1</strong></a>,"SyncThread1");
Thread thread2 = new Thread(<a><strong>syncThread2</strong></a>,"SyncThread2");
thread1.start();
thread2.start();
</pre>
>result:
<pre>
SyncThread1:0
SyncThread1:1
SyncThread1:2
SyncThread1:3
SyncThread1:4
SyncThread2:5
SyncThread2:6
SyncThread2:7
SyncThread2:8
SyncThread2:9
</pre>
其效果和synchronized作用域静态类一致，作用对象为该类的所有对象。
# 5. 结束
Thank you !
Good bye !


