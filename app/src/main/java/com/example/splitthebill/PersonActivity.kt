package com.example.splitthebill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.splitthebill.model.Person
import com.example.splitthebill.databinding.ActivityPersonBinding
import com.example.splitthebill.model.Constant.EXTRA_PERSON
import com.example.splitthebill.model.Constant.VIEW_PERSON
import kotlin.random.Random

class PersonActivity : AppCompatActivity() {
    private val acb: ActivityPersonBinding by lazy {
        ActivityPersonBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        val receivedPerson = intent.getParcelableExtra<Person>(EXTRA_PERSON)
        receivedPerson?.let{ _receivedPerson ->
            with(acb) {
                with(_receivedPerson) {
                    nameEt.setText(name)
                    descEt.setText(desc)
                    moneyEt.setText(money.toString())
                }
            }
        }
        val viewPerson = intent.getBooleanExtra(VIEW_PERSON, false)
        if (viewPerson) {
            acb.nameEt.isEnabled = false
            acb.descEt.isEnabled = false
            acb.moneyEt.isEnabled = false
            acb.saveBt.visibility = View.GONE
        }

        acb.saveBt.setOnClickListener {
            val person = Person(
                id = receivedPerson?.id?: Random(System.nanoTime()).nextInt(),
                name = acb.nameEt.text.toString(),
                money = acb.moneyEt.text.toString().toDouble(),
                desc = acb.descEt.text.toString(),
            )
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_PERSON, person)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}