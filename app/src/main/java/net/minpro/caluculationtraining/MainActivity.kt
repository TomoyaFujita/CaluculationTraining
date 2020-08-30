package net.minpro.caluculationtraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //選択肢を入れるためのArrayAdapterをセット
 //       val arrayAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item)
 //       arrayAdapter.add(10)
 //       arrayAdapter.add(20)
 //       arrayAdapter.add(30)

          val arrayAdapter = ArrayAdapter.createFromResource(this, R.array.number_of_question, android.R.layout.simple_spinner_item)

        spinner.adapter = arrayAdapter

        button.setOnClickListener {
            //スタートボタンを押したら

            val numberOfQuestion: Int  = spinner.selectedItem.toString().toInt()

            //Todo.1.テスト画面を開く(Spinnerから渡した問題数を渡す)
            val intent = Intent(this@MainActivity, TestActivity2::class.java) //インテントの渡し先
            intent.putExtra("numberOfQuestion", numberOfQuestion) //渡す情報をインテントにセット
            startActivity(intent) //新しい画面を開いてインテントを渡す
        }
    }
}