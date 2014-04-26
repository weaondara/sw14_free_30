package sw.superwhateverjnr.scheduling;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import sw.superwhateverjnr.scheduling.Task.State;

import lombok.SneakyThrows;
import lombok.Synchronized;

public class Scheduler
{
	public static Runnable EMPTY = new Runnable()
	{
		@Override
		public void run(){}
	};
	
	
	
	private int id;
	private Timer timer;
	private Map<Integer, Task> tasks;
	
	@SneakyThrows
	public Scheduler()
	{
		timer=new Timer();
		id=0;
		tasks=new ConcurrentHashMap<>();
		
		Field f=Task.class.getDeclaredField("scheduler");
		f.setAccessible(true);
		f.set(null, this);
	}
	
	
	public Task registerTask(Runnable r, long delay)
	{
		Task t=newTask(r,delay,-1);
		timer.schedule(t, t.getDelay());
		
		return t;
	}
	public Task registerRepeatingTask(Runnable r, long delay, long period)
	{
		Task t=newTask(r,delay,period);
		timer.scheduleAtFixedRate(t, delay, period);
		
		return t;
	}
	
	@SneakyThrows
	public void cancelTask(int id)
	{
		Task t=tasks.get(id);
		if(t==null)
		{
			return;
		}
		
		if(t.getState()==State.None || t.getState() == State.Executed)
		{
		}
		else if(t.getState()==State.Pending)
		{
			Field f=Task.class.getDeclaredField("runnable");
			f.setAccessible(true);
			f.set(t, EMPTY);
		}
		else if(t.getState()==State.Running)
		{
			//we can't do anything ... D:
		}
		tasks.remove(t.getId());
		t.cancelTask();
	}
	
	public Map<Integer, Task> getPendingTasks()
	{
		return tasks;
	}
	
	@Synchronized
	private int getNewTaskId()
	{
		return id++;
	}
	
	private Task newTask(Runnable r, long delay, long period)
	{
		Task t=new Task(getNewTaskId(),r,delay,period);
		tasks.put(t.getId(), t);
		t.registered();
		
		return t;
	}
	
}
