package com.droid.manasshrestha.calculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG_NUMBER = "number"
    val TAG_OPERATOR = "operator"
    val TAG_OPERATION = "operation"

    val OPERATION_DEL = "⌫"
    val OPERATION_CLEAR = "C"
    val OPERATION_EQUALS = "="

    val OPERATOR_ADD = "+"
    val OPERATOR_SUBTRACT = "-"
    val OPERATOR_DIVIDE = "/"
    val OPERATOR_MULTIPLY = "x"
    val OPERATOR_PERCENTAGE = "%"

    var valueOne: Double = Double.NaN
    var valueTwo: Double = 0.0

    var currentAction: String = String()
    private val decimalFormat: DecimalFormat = DecimalFormat("#.##########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        setButtonClicks()
    }

    private fun setButtonClicks() {
        (0..grid_layout.childCount)
                .filter { grid_layout.getChildAt(it) != null }
                .forEach { grid_layout.getChildAt(it).setOnClickListener(this) }


    }

    override fun onClick(view: View) {
        if (view is Button)
            when (view.tag) {
                TAG_NUMBER -> tv_expression.text = tv_expression.text.toString() + view.text
                TAG_OPERATION -> performUserOperation(view.text)
                TAG_OPERATOR -> computeCalculation(view.text.toString())
            }
    }

    private fun performUserOperation(text: CharSequence) {
        when (text) {
            OPERATION_DEL -> performBackspace(tv_expression.text.toString())
            OPERATION_CLEAR -> clearResults()
            OPERATION_EQUALS -> evaluate(text.toString())
        }
    }

    private fun evaluate(text: String) {
        computeCalculation(text)

        valueOne = 0.0
        currentAction = "0"
    }

    private fun clearResults() {
        valueOne = 0.0
        valueTwo = 0.0
        tv_result.text = ""
        tv_expression.text = ""
    }

    private fun performBackspace(text: String) {
        when (text.length) {
            !in 0..1 -> tv_expression.text = tv_expression.text.subSequence(0, text.length - 1).toString()
            1 -> tv_expression.text = ""
        }
    }

    private fun computeCalculation(text: String) {
        if (java.lang.Double.isNaN(valueOne))
            valueOne = 0.0

        if (!TextUtils.isEmpty(tv_expression.text)) {
            valueTwo = java.lang.Double.parseDouble(tv_expression.text.toString())
            tv_expression.text = ""

            when (currentAction) {
                OPERATOR_ADD -> valueOne += valueTwo
                OPERATOR_DIVIDE -> valueOne /= valueTwo
                OPERATOR_MULTIPLY -> valueOne *= valueTwo
                OPERATOR_SUBTRACT -> valueOne -= valueTwo
                OPERATOR_PERCENTAGE -> valueOne = (valueOne / 100.0f) * valueTwo
                else -> valueOne = valueTwo
            }

            currentAction = text
            if (!text.contentEquals(OPERATION_EQUALS))
                tv_result.text = (decimalFormat.format(valueOne) + text)
            else tv_result.text = (decimalFormat.format(valueOne))

            tv_expression.text = ""
        }
    }

}


