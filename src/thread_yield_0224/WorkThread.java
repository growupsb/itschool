package thread_yield_0224;

public class WorkThread extends Thread {
	public boolean work=true;
	public WorkThread(String name) {
		setName(name);
	}
	@Override
	public void run() {
		while(true) {
			if(work) {
				System.out.println(getName()+" : 작업처리" );
			}else {
				Thread.yield();
			}
		}
	}
	
}
