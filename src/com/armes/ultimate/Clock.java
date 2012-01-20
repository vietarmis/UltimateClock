package com.armes.ultimate;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Clock extends SurfaceView implements SurfaceHolder.Callback
{
	long lastUpdate = 0;
	long sleepTime = 0;
	CustomClock timer;
	
	SurfaceHolder surfaceHolder;
	Context context;
	
	private ClockThread thread;
	
	void initView()
	{
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		// 24 hour clock
		timer = new CustomClock(1000, 60*1000, 60*60*1000, 24*60*60*1000, true, 0);
		
		thread = new ClockThread(holder, context, new Handler(), timer);
		setFocusable(true);
	}
	
	public Clock(Context contextS, AttributeSet attrs, int defStyle)
	{
		super(contextS, attrs, defStyle);
		context = contextS;
		initView();
	}
	
	public Clock(Context contextS, AttributeSet attrs)
	{
		super(contextS, attrs);
		context = contextS;
		initView();
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0)
	{
		if(thread.state == ClockThread.PAUSED) // is resuming
		{
			thread = new ClockThread(getHolder(), context, new Handler(), timer);
			thread.start();
		}
		else // already going
		{
			thread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		boolean retry = true;
		thread.state = ClockThread.PAUSED;
		while(retry)
		{
			try
			{
				// kill thread here
				thread.join();
				retry = false;
			} catch (InterruptedException e)
			{
				// do nothing!
			}
		}
	}
}