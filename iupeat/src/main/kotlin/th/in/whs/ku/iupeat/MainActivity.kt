package th.`in`.whs.ku.iupeat

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.hardware.SensorEventListener
import android.hardware.SensorEvent
import kotlin.properties.Delegates
import android.widget.Spinner
import android.widget.ArrayAdapter

public class MainActivity() : Activity(), SensorEventListener {

    var last_x = 0f
    var last_y = 0f
    var last_z = 0f
    var lastUpdate = 0L

    val menuReader = MenuReader(this)
    val sensorManager : SensorManager by Delegates.lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val text : TextView by Delegates.lazy {
        findViewById(R.id.menuName) as TextView
    }

    val menuType : Spinner by Delegates.lazy {
        findViewById(R.id.menuType) as Spinner
    }

    var currentMenu = ""
        get() = menuType.getSelectedItem() as String
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Activity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupMenuList()
        randomText()
    }

    override fun onResume() {
        super<Activity>.onResume()
        setupAccelerometer()
    }

    override fun onPause(){
        super<Activity>.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun setupAccelerometer() {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(accelerometer == null){
            Toast.makeText(this, R.string.no_accel, Toast.LENGTH_LONG)
            return
        }
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event : SensorEvent) {
        val curTime = System.currentTimeMillis()
        val diffTime = (curTime - lastUpdate)
        lastUpdate = curTime

        val x = event.values!![0]
        val y = event.values!![1]
        val z = event.values!![2]

        val speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime.toFloat() * 10000

        if (speed > 3000) {
            randomText()
        }

        last_x = x
        last_y = y
        last_z = z
    }

    private fun setupMenuList(){
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menuReader.menuList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        menuType.setAdapter(adapter)
    }

    private fun randomText() {
        val menu = menuReader.getMenu(currentMenu)
        val index = Math.floor(Math.random() * menu!!.size.toDouble()).toInt()
        text.setText(menu[index])
    }

    override fun onAccuracyChanged(sensor : Sensor?, accuracy : Int) {
    }
}
