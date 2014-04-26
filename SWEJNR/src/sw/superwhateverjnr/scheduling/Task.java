package sw.superwhateverjnr.scheduling;

import java.util.TimerTask;

import lombok.Getter;

@Getter
public class Task extends TimerTask
{
	private static Scheduler scheduler;
	private static void finished(int id)
	{
		scheduler.cancelTask(id);
	}

	
	
	private int id;
	private Runnable runnable;
	private long delay;
	private long period;
	private State state;
	
	public Task(int id, Runnable runnable, long delay, long period)
	{
		super();
		this.id = id;
		this.runnable = runnable;
		this.delay = delay;
		this.period = period;
		state=State.None;
	}
	
	public void registered()
	{
		state=State.Pending;
	}
	
	public void cancelTask()
	{
		state=State.Cancelled;
		cancel();
	}
	
	@Override
	public void run()
	{
		if(state==State.Cancelled)
		{
			return;
		}
		
		state=State.Running;
		runnable.run();
		state=State.Executed;
		
		finished(id);
	}
	
	public enum State
	{
		None,
		Pending,
		Running,
		Executed,
		Cancelled;
	}
}
