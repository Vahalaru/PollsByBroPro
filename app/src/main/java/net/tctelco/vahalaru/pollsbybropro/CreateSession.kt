package net.tctelco.vahalaru.pollsbybropro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.NonNull
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.system.Os.bind
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.pawegio.kandroid.textWatcher
import info.hoang8f.widget.FButton
import java.util.*
import kotlin.collections.HashMap
import kotlinx.android.synthetic.main.activity_create_session.*
import kotlinx.android.synthetic.main.activity_create_session.view.*
import org.jetbrains.anko.design.textInputEditText

/**
 * Created by jerem on 12/12/2017.
 */
class CreateSession : AppCompatActivity() {

    val TAG = "CreateSession"

    //listing firestore keys for key value pairs
    var SESSION_TITTLE_KEY: String = "sName"
    var QUESTION_NUMBER: String = "qNumber"
    var QUESTION_TITLE_KEY: String = "qName"
    var RESPONSE_A_TEXT_KEY: String = "AnswerA"
    var RESPONSE_B_TEXT_KEY: String = "AnswerB"
    var RESPONSE_C_TEXT_KEY: String = "AnswerC"
    var RESPONSE_D_TEXT_KEY: String = "AnswerD"

    private var qName: String = ""
    private var qNum: String = ""
    private var qAA: String = ""
    private var qAB: String = ""
    private var qAC: String = ""
    private var qAD: String = ""

    private lateinit var tvTitleText: Any
    private lateinit var gCountTracker: Any
    private lateinit var qCurrentCount: Any

    //creating firestore object
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_session)

        db = FirebaseFirestore.getInstance()
        btnAddAnotherQuestion.setOnClickListener(View.OnClickListener { getEditTextData() })
        btnFinishAddingPoll.setOnClickListener(View.OnClickListener { goHome() })
        textInputEditTextWatcher()
    }

        fun getEditTextData(){
        qNum = questionNumAdd.text.toString()
        qName = etQuestionsForPolls.text.toString()
        qAA = addAText.text.toString()
        qAB = addBText.text.toString()
        qAC = addCText.text.toString()
        qAD = addDText.text.toString()

        var qText = HashMap<String, Any>()
        qText.put(QUESTION_TITLE_KEY, qName)
        if (qAA.isNotEmpty()){
                qText.put(RESPONSE_A_TEXT_KEY, qAA)
        }
            if (qAB.isNotEmpty()){
                qText.put(RESPONSE_B_TEXT_KEY, qAB)
            }
            if (qAC.isNotEmpty()){
        qText.put(RESPONSE_C_TEXT_KEY, qAC)
            }
            if (qAD.isNotEmpty()){
        qText.put(RESPONSE_D_TEXT_KEY, qAD)
            }
         qText.put(QUESTION_NUMBER, qNum)

            try {
                db.document("/sessions/${titTitleText.text}/questions/${questionNumAdd.text}").set(qText).addOnSuccessListener {
                    void: Void? -> Toast.makeText(this, "Successfully uploaded to the database :)", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                    exception: java.lang.Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                }
            } catch (e:Exception){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
            clearUI()
    }

    fun clearUI(){
        gCountTracker = questionNumAdd.text.toString().toInt()
        qCurrentCount = (gCountTracker as Int).plus(1)
        questionNumAdd.text = qCurrentCount.toString()
        etQuestionsForPolls.text = null
        addAText.text = null
        addBText.text = null
        addCText.text = null
        addDText.text = null
        AnswerBView.visibility = View.INVISIBLE
        AnswerCView.visibility = View.INVISIBLE
        AnswerDView.visibility = View.INVISIBLE
        fabAddResponse.visibility = View.INVISIBLE
        titleViewManager()
    }

    fun titleViewManager(){
        tvTitleText = titTitleText.text
        if (tvTitle.visibility == View.GONE){
            textInputTitle.visibility = View.GONE
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = tvTitleText as Editable
            var sNameDoc = HashMap<String, Any>()
            sNameDoc.put(SESSION_TITTLE_KEY, titTitleText.text.toString())
           try{
                db.document("/sessions/${titTitleText.text}").set(sNameDoc).addOnSuccessListener {
                void: Void? -> Toast.makeText(this, "Successfully uploaded to the database :)", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                        exception: java.lang.Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                    }
        } catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        } else if (tvTitle.visibility == View.VISIBLE){
            tvTitle.text = tvTitleText as Editable
        }
    }

    // this activities text Input watcher
    fun textInputEditTextWatcher(){
        AnswerAView.addAText.textWatcher {
            beforeTextChanged {text, start, count, after ->  before()}
            onTextChanged { text, start, before, count -> onChanged(AnswerBView)}
            afterTextChanged {text -> after()}
        }
        AnswerBView.addBText.textWatcher {
            beforeTextChanged {text, start, count, after ->  before()}
            onTextChanged { text, start, before, count -> onChanged(AnswerCView)}
            afterTextChanged {text -> after()}
        }
        AnswerCView.addCText.textWatcher {
            beforeTextChanged {text, start, count, after ->  before()}
            onTextChanged { text, start, before, count -> onChanged(AnswerDView)}
            afterTextChanged {text -> after()}
        }
    }

    fun after(){
    }
    fun before(){
    }
    fun onChanged(thePassedView: View){
        //btnAddAnotherQuestion.setOnClickListener(View.OnClickListener { getEditTextData() })
        fabAddResponse.visibility = View.VISIBLE
        fabAddResponse.setOnClickListener(View.OnClickListener { fabOnClick(thePassedView) })
    }

    fun fabOnClick(thePassedView: View){
        thePassedView.visibility = View.VISIBLE
        fabAddResponse.visibility = View.INVISIBLE
    }

    fun goHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}