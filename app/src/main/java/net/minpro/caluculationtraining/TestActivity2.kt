package net.minpro.caluculationtraining

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_test2.*
import java.util.*
import kotlin.concurrent.schedule


class TestActivity2 : AppCompatActivity(), View.OnClickListener {
    //問題数
    var numberOfQuestion: Int = 0
    //残り問題数
    var numberOfRemaining: Int = 0
    //正解数
    var numberOfCorrect: Int = 0
    //効果音
    lateinit var soundPool: SoundPool
    //soundID
    var intSoundIdCorrect: Int = 0
    var intSoundIdInCorrect: Int = 0
    //タイマー
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        //テスト画面を開いたら
        //１．前の問題から渡された問題数を画面に表示させる
       val bundle = intent.extras
       numberOfQuestion = bundle!!.getInt("numberOfQuestion")
        textViewRemainning.text = numberOfQuestion.toString()
        numberOfRemaining = numberOfQuestion
        numberOfCorrect = 0

        //「こたえ合わせ」ボタンが押されたら
        buttonAnserCheck.setOnClickListener {
            if (textViewAnswer.text.toString() != "" && textViewAnswer.text.toString() != "-") {
                anserCheck()
            }

        }

        //「もどる」ボタンが押されたら
         buttonBack.setOnClickListener {
             finish()
         }


        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        buttonMinus.setOnClickListener(this)
        buttonClear.setOnClickListener(this)

        //1問目を出す
        question()
    }

    override fun onResume() {
        super.onResume()
        //soundPoolの準備
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ //Androidのver5.0以上の場合
            SoundPool.Builder().setAudioAttributes(
                AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build())
                .setMaxStreams(1)
                .build()
        } else {
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
        //効果音ファイルをメモリにロード
         intSoundIdCorrect = soundPool.load(this, R.raw.sound_correct, 1)
        intSoundIdInCorrect = soundPool.load(this, R.raw.sound_incorrect, 1)

        //タイマーの準備
           timer = Timer()
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()

        //タイマーキャンセル
        timer.cancel()
    }


   //問題を出す処理をするメソッド
   //問題が出されたら（questionメソッド）
    private fun question() {
       // １．「もどる」ボタンを使えなくする
       buttonBack.isEnabled = false
       //２．「こたえ合わせ」ボタンと電卓ボタンを使えるようにする
       buttonAnserCheck.isEnabled = true
       button0.isEnabled = true
       button1.isEnabled = true
       button2.isEnabled = true
       button3.isEnabled = true
       button4.isEnabled = true
       button5.isEnabled = true
       button6.isEnabled = true
       button7.isEnabled = true
       button8.isEnabled = true
       button9.isEnabled = true
       buttonMinus.isEnabled = true
       buttonClear.isEnabled = true

       //３．問題の２つの数字を１~100からランダムに設定して表示
       val random = Random()
       val intQuestionLeft = random.nextInt(100) + 1
       val intQuestionRight = random.nextInt(100) + 1
       textViewLeft.text =  intQuestionLeft.toString()
       textViewRight.text = intQuestionRight.toString()

       //４．計算方法を「+」「-」からランダムに設定して表示
       when(random.nextInt(2) + 1) {
           1 -> textViewOperator.text = "+"
           2 -> textViewOperator.text = "-"
       }

       //５．前の問題で入力した自分のこたえを消す
       textViewAnswer.text = ""

       //６．〇・×画像を見えないようにする
       imageView.visibility = View.INVISIBLE
    }

    //こたえあわせ処理をするメソッド
    //こたえあわせ処理（answerCheckメソッド）
    private fun anserCheck() {
        //   １．「もどる」「こたえ合わせ」「電卓」ボタンを使えなくする
        buttonBack.isEnabled = false
        buttonAnserCheck.isEnabled = false
        button0.isEnabled = false
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
        buttonMinus.isEnabled = false
        buttonClear.isEnabled = false

        //   ２．のこり問題数を１つ減らして表示させる
        numberOfRemaining -= 1
        textViewRemainning.text = textViewRemainning.toString()

        //   ３．〇・×画像を見えるようにする
        imageView.visibility = View.VISIBLE

        //   ４．自分の入力したこたえと本当にこたえを比較する
        val intMyAnswer: Int = textViewAnswer.text.toString().toInt()

        val intRealAnswer: Int =
         if (textViewOperator.text == "+") {
             textViewLeft.text.toString().toInt() + textViewRight.text.toString().toInt()
         } else {
             textViewLeft.text.toString().toInt() - textViewRight.text.toString().toInt()
         }

        //比較
        if (intMyAnswer == intRealAnswer) {
            //   ５．合っている場合 ⇒ 正解数を１つ増やして表示・〇画像・ピンポン音
            numberOfCorrect += 1
            textViewCorrect.text = numberOfCorrect.toString()
            imageView.setImageResource(R.drawable.pic_correct)
            soundPool.play(intSoundIdCorrect, 1.0f, 1.0f, 0, 0, 1.0f)
        } else {
            //   ６．間違っている場合 ⇒ ×画像・ブー音
            imageView.setImageResource(R.drawable.pic_incorrect)
            soundPool.play(intSoundIdInCorrect, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        //   7．正答率を計算して表示（正解数÷出題済み問題数）
        val intPoint: Int = ((numberOfCorrect.toDouble() / (numberOfQuestion - numberOfRemaining).toDouble()) * 100).toInt()
        textViewPoint.text = intPoint.toString()

        if (numberOfRemaining == 0) {
            //   8．残り問題数がなくなった場合（テストが終わった場合）
            //　　　　　⇒ もどるボタン〇、こたえあわせボタン×、「テスト終了」表示
            buttonBack.isEnabled = true
            buttonAnserCheck.isEnabled = false
            textViewMessage.text = "テスト終了"
        } else {
            //   9．残り問題数がある場合 ⇒ １秒後に次の問題を出す(questionメソッド)
            timer.schedule(1000, {runOnUiThread { question() }})
        }
    }

    //ボタンが押されたときにやることを書く場所
    //電卓ボタンが押されたら
    override fun onClick(v: View?) {
        //   １．電卓ボタンを押すたびに１文字ずつ表示
        //　　　　（「-」は先頭だけ/０は先頭にならない/１文字目が０の場合０は押せない）
        val button: Button = v as Button

        when(v?.id) {
            //クリアボタン->消す
            R.id.buttonClear -> textViewAnswer.text = ""

            //マイナスボタン -> 先頭だけ
            R.id.buttonMinus -> if(textViewAnswer.text.toString() == "")
                textViewAnswer.text = "-"

            //1文字目が0かマイナス以外
            R.id.button0 -> if(textViewAnswer.text.toString() != "0" && textViewAnswer.text.toString() != "-")
                textViewAnswer.append(button.text)

            //1~9の数字処理
            else -> if(textViewAnswer.text.toString() == "0")
                textViewAnswer.text = button.text
            else textViewAnswer.append(button.text)
        }

    }
}

