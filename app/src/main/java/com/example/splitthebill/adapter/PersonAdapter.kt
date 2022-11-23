package com.example.splitthebill.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.splitthebill.R
import com.example.splitthebill.model.Person

class PersonAdapter(
    context: Context,
    private val personList: MutableList<Person>
) : ArrayAdapter<Person>(context, R.layout.tile_person, personList) {
    private data class TilePersonHolder(val nameTv: TextView, val printValueTv: TextView)

    override fun getView(index: Int, convertView: View?, parent: ViewGroup): View {
        val person = personList[index]
        var personTileView = convertView
        if (personTileView == null) {
            // Inflo uma nova célula
            personTileView =
                (context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.tile_person,
                    parent,
                    false
                )

            val tilepersonHolder = TilePersonHolder(
                personTileView.findViewById(R.id.nameTv),
                personTileView.findViewById(R.id.printValueTv),
            )
            personTileView.tag = tilepersonHolder
        }

        with(personTileView?.tag as TilePersonHolder) {
            val valueAvgPerson = getValueAvgPerson()
            val paid = (person.money - valueAvgPerson)
            if (paid > 0) {
                printValueTv.text = String.format("Receber: R$%.2f", paid)
            } else if (paid.equals(0.0)) {
                printValueTv.text = String.format("Você não deve nada!!")
            } else {
                printValueTv.text = String.format("Pagar: R$%.2f", (paid * (-1)))
            }
            nameTv.text = person.name
        }
        return personTileView
    }

    fun getValueAvgPerson(): Double {
        var totalValue = 0.0
        personList.forEach{ totalValue += it.money }
        return (totalValue / personList.size)
    }
}