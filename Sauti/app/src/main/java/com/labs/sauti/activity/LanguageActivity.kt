package com.labs.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.labs.sauti.R
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity(), AdapterView.OnItemClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)


        ArrayAdapter.createFromResource(this,
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            select_language_spinner.adapter = adapter
        }

        button.setOnClickListener {
            // TODO save selected language
            val intent = Intent(this@LanguageActivity, DashBoardActivity::class.java)
            startActivity(intent)

            finish()
        }
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
