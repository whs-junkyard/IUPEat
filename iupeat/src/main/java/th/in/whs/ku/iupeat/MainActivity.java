package th.in.whs.ku.iupeat;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorListener {

    float last_x, last_y, last_z;
    long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomText();

        setupAccelerometer();
    }

    private void setupAccelerometer() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if(sensor == SensorManager.SENSOR_ACCELEROMETER){
            long curTime = System.currentTimeMillis();
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float x = values[SensorManager.DATA_X];
            float y = values[SensorManager.DATA_Y];
            float z = values[SensorManager.DATA_Z];

            float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

            if(speed > 3000){
                randomText();
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    private void randomText() {
        TextView menuText = (TextView) findViewById(R.id.menuName);
        String[] menu = getResources().getStringArray(R.array.menu);
        int index = (int) Math.floor(Math.random() * menu.length);
        menuText.setText(menu[index]);
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }
}
