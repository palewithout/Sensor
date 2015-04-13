package com.example.sensor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements SensorEventListener{
	SensorManager sensorManager;
	Sensor Acc,Gyr,Pres;
	TextView Acc_x, Acc_y, Acc_z,
			Gyr_x, Gyr_y, Gyr_z,
			Pres_v;
	 TextView labelView;
	 String timedata,Sensordata_Acc="",Sensordata_Gyr="",Sensordata_Pre="";
	    int timeSpan = 1000;
	    long startTime;         //记录开始的时间
	    long now;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 labelView=(TextView)findViewById(R.id.textView);
		 
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE); 	
		checksdcard();
	    Button btn1 = (Button) findViewById(R.id.button1);  
	    Button btn2 = (Button) findViewById(R.id.button2);
	    btn1.setOnClickListener(new Button.OnClickListener()
	    {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onResume();
			}
	  	    });
	    btn2.setOnClickListener(new Button.OnClickListener()
	    {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPause();
			}
	    	
	    });
	}
	
	protected void onResume(){	
		super.onResume();
		RegisterForAcc();
		RegisterForGyr();
		RegisterForPres(); 
 
	}
	
	public String gettime() {
	        Calendar ca = Calendar.getInstance();
	/*      int year = ca.get(Calendar.YEAR);//获取年份
	        int month = ca.get(Calendar.MONTH)+1;//获取月份
	        int day = ca.get(Calendar.DAY_OF_MONTH);//获取日
	        int minute = ca.get(Calendar.MINUTE);//分
	        int hour = ca.get(Calendar.HOUR_OF_DAY);//小时
	        int second = ca.get(Calendar.SECOND);//秒	*/
	        timedata=DateFormat.format("yyyyMMddkkmmss",ca.getTime()).toString()+" ";
	        labelView.setText(timedata);
	        return timedata;
	    }

	public void writedata(String string){
		 String str="";  
	        EditText editText =(EditText)findViewById(R.id.editText);  
	        str=editText.getText().toString();
	        String fileName =str+".txt";
	        File dir = new File("/sdcard/");
	        if (dir.exists() && dir.canWrite()) {
	            File newFile = new File(dir.getAbsolutePath() + "/" + fileName);
	            FileOutputStream fos = null;
	            try {
	                newFile.createNewFile();
	                if (newFile.exists() && newFile.canWrite()) {
	                    fos = new FileOutputStream(newFile,true);

	                    fos.write(string.getBytes());
	                    //  fos.write("写入的内容".getBytes());
	                  //  labelView.setText(fileName + "文件写入SD 卡");
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	                if (fos != null) {
	                    try{
	                        fos.flush();
	                        fos.close();
	                    }
	                    catch (IOException e) { }
	                }
	            }
	        }
	    }

	public void checksdcard(){
	        //  boolean mExternalStorageAvailable = false;
	        //boolean mExternalStorageWriteable = false;
	        String state = Environment.getExternalStorageState();
	        if (state.equals(Environment.MEDIA_MOUNTED)) {
	// 对SD 卡上的存储可以进行读/写操作
	            labelView.setText("对SD 卡上的存储可以进行读/写操作");
	            //   mExternalStorageAvailable = mExternalStorageWriteable = true;
	        } else
	        if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
	        {
	//对SD 卡上的存储可以进行读操作
	            labelView.setText("对SD 卡上的存储可以进行读操作");
	            //    mExternalStorageAvailable = true;
	            //     mExternalStorageWriteable = false;
	        } else {
	//对SD 卡上的存储不可用
	            labelView.setText("对SD 卡上的存储不可用");
	            //    mExternalStorageAvailable = mExternalStorageWriteable = false;
	        }
	    }
	
	protected void onPause() {
		 super.onPause();
		 sensorManager.unregisterListener(this); // 解除监听器注册
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			switch(event.sensor.getType())
			{
				case Sensor.TYPE_ACCELEROMETER:
					setAccelerometer(event);
					if(Sensordata_Acc!=""&&Sensordata_Gyr!=""&&Sensordata_Pre!=""){
					writedata(gettime());
			        writedata(Sensordata_Acc);				
			        writedata(Sensordata_Gyr);
			        writedata(Sensordata_Pre);
					}
					break;
				case Sensor.TYPE_GYROSCOPE:
					setGyroscope(event);
					break;
				case Sensor.TYPE_PRESSURE:
					setPressure(event);
					break;
			}
		 }
	}

	private void setPressure(SensorEvent event) {
		// TODO Auto-generated method stub
		float pre = event.values[0];
		Pres_v.setText("Pressure = " + pre);
		  Sensordata_Pre=pre+" \r\n";
	        //writedata("Pre", gettime());
	        //writedata("Pre",Sensordata_Pre);
	}

	private void setGyroscope(SensorEvent event) {
		// TODO Auto-generated method stub
		float gy_x = event.values[sensorManager.DATA_X];
		float gy_y = event.values[sensorManager.DATA_Y];
		float gy_z = event.values[sensorManager.DATA_Z];
		Gyr_x.setText("X = " + gy_x);
		Gyr_y.setText("Y = " + gy_y);
		Gyr_z.setText("Z = " + gy_z);
		 Sensordata_Gyr=gy_x+" "+gy_y+" "+gy_z+" ";
	        //writedata("Gyr",gettime());
	        //writedata("Gyr",Sensordata_Gyr);
	}
	private void setAccelerometer(SensorEvent event) {
		// TODO Auto-generated method stub
		float ac_x = event.values[sensorManager.DATA_X];
		float ac_y = event.values[sensorManager.DATA_Y];
		float ac_z = event.values[sensorManager.DATA_Z];
		Acc_x.setText("X = " + ac_x);
		Acc_y.setText("Y = " + ac_y);
		Acc_z.setText("Z = " + ac_z);
		  Sensordata_Acc=ac_x+" "+ac_y+" "+ac_z+" ";
	        //writedata("Acc",gettime());
	        //writedata( "Acc",Sensordata_Acc);
	}
	
	private void RegisterForGyr() {
		// TODO Auto-generated method stub
		sensorManager.registerListener(this, Gyr, sensorManager.SENSOR_DELAY_GAME);
		Gyr = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		Gyr_x = (TextView)findViewById(R.id.Gyr_x);
		Gyr_y = (TextView)findViewById(R.id.Gyr_y);
		Gyr_z = (TextView)findViewById(R.id.Gyr_z);
	}
	
	private void RegisterForAcc() {
		// TODO Auto-generated method stub
		sensorManager.registerListener(this, Acc, sensorManager.SENSOR_DELAY_GAME);
		Acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
		Acc_x = (TextView)findViewById(R.id.Acc_x);
		Acc_y = (TextView)findViewById(R.id.Acc_y);
		Acc_z = (TextView)findViewById(R.id.Acc_z);
	}
	
	private void RegisterForPres() {
		// TODO Auto-generated method stub
		sensorManager.registerListener(this, Pres, sensorManager.SENSOR_DELAY_GAME);
		Pres = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		Pres_v = (TextView)findViewById(R.id.Pres_v);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
