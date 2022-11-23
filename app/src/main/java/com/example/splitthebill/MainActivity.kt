package com.example.splitthebill

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.splitthebill.model.Person
import com.example.splitthebill.R
import com.example.splitthebill.adapter.PersonAdapter
import com.example.splitthebill.databinding.ActivityMainBinding
import com.example.splitthebill.model.Constant.EXTRA_PERSON
import com.example.splitthebill.model.Constant.VIEW_PERSON

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val personList: MutableList<Person> = mutableListOf()

    private lateinit var personAdapter: PersonAdapter

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        personAdapter = PersonAdapter(this, personList)
        amb.personLv.adapter = personAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val person = result.data?.getParcelableExtra<Person>(EXTRA_PERSON)

                person?.let { _person->
                    val i = personList.indexOfFirst { it.id == _person.id }
                    if (_person.id != null) {
                        if (i != -1) {
                            personList[i] = _person
                        }
                        else {
                            personList.add(_person)
                        }
                    }
                }
            }
        }

        registerForContextMenu(amb.personLv)

        amb.personLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val person = personList[position]
                val personIntent = Intent(this@MainActivity, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_PERSON, person)
                personIntent.putExtra(VIEW_PERSON, true)
                startActivity(personIntent)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addPersonMi -> {
                carl.launch(Intent(this, PersonActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val index = (item.menuInfo as AdapterContextMenuInfo).position
        return when(item.itemId) {
            R.id.removePersonMi -> {
                personList.removeAt(index)
                personAdapter.notifyDataSetChanged()
                true
            }
            R.id.editPersonMi -> {
                val personIntent = Intent(this, PersonActivity::class.java)
                val person = personList[index]
                personIntent.putExtra(EXTRA_PERSON, person)
                personIntent.putExtra(VIEW_PERSON,false)
                carl.launch(personIntent)
                true
            }
            else -> { false }
        }
    }
}