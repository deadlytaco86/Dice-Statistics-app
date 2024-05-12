package com.rngesus.dicestatistics

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import java.math.RoundingMode
import kotlin.math.pow

private const val TAG = "MainActivity"
private const val INITIAL_DICE_AMOUNT: Long = 20
private const val INITIAL_CALL_AMOUNT: Long = 6
private var n: Long = INITIAL_DICE_AMOUNT
private var k: Long = INITIAL_CALL_AMOUNT

class MainActivity : AppCompatActivity() {

    private lateinit var diceDisplayLabel: TextView
    private lateinit var callDisplayLabel: TextView
    private lateinit var displayWindow1: TextView
    private lateinit var displayWindow2: TextView
    private lateinit var displayWindow3: TextView
    private lateinit var displayLabel1: TextView
    private lateinit var displayLabel2: TextView
    private lateinit var displayLabel3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceDisplayLabel = findViewById(R.id.diceDisplayLabel)
        callDisplayLabel = findViewById(R.id.callDisplayLabel)
        displayWindow1 = findViewById(R.id.displayWindow1)
        displayWindow2 = findViewById(R.id.displayWindow2)
        displayWindow3 = findViewById(R.id.displayWindow3)
        displayLabel1 = findViewById(R.id.displayLabel1)
        displayLabel2 = findViewById(R.id.displayLabel2)
        displayLabel3 = findViewById(R.id.displayLabel3)

        val diceDown: ImageButton = findViewById(R.id.diceDown)
        diceDown.setOnClickListener {
            if (n > 1) {
                n -= 1
                computeProbabilities(n, k)
            }
        }

        val diceUp: ImageButton = findViewById(R.id.diceUp)
        diceUp.setOnClickListener {
            n += 1
            computeProbabilities(n, k)
        }

        val callDown: ImageButton = findViewById(R.id.callDown)
        callDown.setOnClickListener {
            if (k > 1) {
                k -= 1
                computeProbabilities(n, k)
            }
        }

        val callUp: ImageButton = findViewById(R.id.callUp)
        callUp.setOnClickListener {
            k += 1
            computeProbabilities(n, k)
        }

        diceDisplayLabel.text = "$n"
        callDisplayLabel.text = "$k"
        computeProbabilities(n, k)

    }

    @SuppressLint("SetTextI18n")
    private fun computeProbabilities(n: Long, k: Long) {
        // see if call, call+1 and call+2 are valid (k <= n)
        // for every valid one, get the probability
        // for every invalid one, type impossible

        if (k <= n) { // can also try "%.2f".format() in the future
            val p = binomial(n, k).toBigDecimal().setScale(5, RoundingMode.UP).toDouble()
            Log.i(TAG, "n is $n, k is $k, probability is $p")
            displayWindow1.text = p.toString()
        } else {
            displayWindow1.text = "IMPOSSIBLE"
        }
        if (k+1 <= n) {
            val p = binomial(n, (k+1)).toBigDecimal().setScale(5, RoundingMode.UP).toDouble()
            Log.i(TAG, "n is $n, k is $k, probability is $p")
            displayWindow2.text = p.toString()
        } else {
            displayWindow2.text = "IMPOSSIBLE"
        }
        if (k+2 <= n) {
            val p = binomial(n, (k+2)).toBigDecimal().setScale(5, RoundingMode.UP).toDouble()
            Log.i(TAG, "n is $n, k is $k, probability is $p")
            displayWindow3.text = p.toString()
        } else {
            displayWindow3.text = "IMPOSSIBLE"
        }
        diceDisplayLabel.text = "$n"
        callDisplayLabel.text = "$k"
        displayLabel1.text = "Chance of ${k}/$n:"
        displayLabel2.text = "Chance of ${k + 1}/$n:"
        displayLabel3.text = "Chance of ${k + 2}/$n:"
    }

    private fun binomial(n: Long, k: Long): Double { // increase n by 1 and
        var sum = 0.0
        var tracker: Long = 0
        while (tracker < k) {
            sum += factorial(n) / (factorial(tracker) * factorial((n - tracker))) * (0.333333333333).pow(tracker.toInt()) * (0.666666666666).pow((n - tracker).toInt())
            tracker += 1
        }
        return 100 * (1 - sum)
    }

    private fun factorial(number: Long): Double {
        var result = 1.0
        for (i in 2..number) {
            result *= i.toDouble()
        }
        return result
    }
}
