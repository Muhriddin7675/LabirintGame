package com.example.labirintgame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_GAME
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.labirintgame.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }
    private val sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var prevX = 0f
    private var prevY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prevX = binding.ball.x
        prevY = binding.ball.y
    }

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val a1 = binding.barrier1.x
            val b1 = binding.barrier1.y
            val a2 = binding.barrier1.x + binding.barrier1.width
            val b2 = binding.barrier1.y + binding.barrier1.height

            val newX = binding.ball.x - 5 * event.values[0]
            val newY = binding.ball.y + 5 * event.values[1]

            if (newX >= binding.frame.x && newX <= binding.frame.x + binding.frame.width - binding.ball.width) {
                binding.ball.x = newX
            }
            if (newY >= binding.frame.y && newY <= binding.frame.y + binding.frame.height - binding.ball.height) {
                binding.ball.y = newY
            }

            // Collision detection
            if (binding.ball.x < a2 && binding.ball.x + binding.ball.width > a1 &&
                binding.ball.y < b2 && binding.ball.y + binding.ball.height > b1
            ) {
                // Move ball back to previous position
                binding.ball.x = prevX
                binding.ball.y = prevY
            } else {
                // Update previous position
                prevX = binding.ball.x
                prevY = binding.ball.y
            }

            // Finish
            if (abs(binding.ball.x - binding.deep.x) < binding.deep.x / 2 && abs(binding.ball.y - binding.deep.y) < binding.deep.y / 2) {
                gameFinish()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    private fun gameFinish() {
//        binding.ball.animate()
//            .setDuration(50)
//            .scaleY(0f)
//            .scaleX(0f)
//            .start()
    }

    override fun onStart() {
        super.onStart()
        sensorManager.registerListener(listener, sensor, SENSOR_DELAY_GAME)
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(listener)
    }
}
