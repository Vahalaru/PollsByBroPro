package net.tctelco.vahalaru.pollsbybropro

import android.content.Context
import android.content.res.Resources
import android.opengl.Visibility
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.firestore.FirestoreArray
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.pawegio.kandroid.i
import kotlinx.android.synthetic.main.activity_create_session.*
import kotlinx.android.synthetic.main.activity_user_session.*
import net.tctelco.vahalaru.pollsbybropro.Model.Questions
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity

/**
 * Created by jerem on 12/2/2017.
 */
class UserSession : AppCompatActivity() {

    val TAG = "UserSession"
    var firestoreListener: ListenerRegistration? = null
    val db by lazy { FirebaseFirestore.getInstance() }
    var questionList = mutableListOf<Questions>()
    lateinit var questionPasser: List<Questions>
    var i: Int = 0
    lateinit var sessionNameText: String
    var optionColor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_session)
        btnStartSession.setOnClickListener(View.OnClickListener { btnStartClick() })
        btnUserSessNext.setOnClickListener(View.OnClickListener { btnNextQuestionClicked() })
        val clickListener = View.OnClickListener { view ->
            when(view.id){
                R.id.optionACardView -> highlightSelectedOption(optionACardView)
                R.id.optionBCardView -> highlightSelectedOption(optionBCardView)
                R.id.optionCCardView -> highlightSelectedOption(optionCCardView)
                R.id.optionDCardView -> highlightSelectedOption(optionDCardView)
            }
        }
        optionACardView.setOnClickListener(clickListener)
        optionBCardView.setOnClickListener(clickListener)
        optionCCardView.setOnClickListener(clickListener)
        optionDCardView.setOnClickListener(clickListener)
    }


    fun btnStartClick() {
        val userSessionText: String = getSessionFromUser()
        if (!userSessionText.isEmpty()) {
            hideViews()
            btnStartSession.hideKeyboard()
                getSessionData(userSessionText)
        }
    }

    fun getSessionFromUser(): String {
            var usersSessionText: String? = null
            usersSessionText = userEnterSession.text.toString()
            return usersSessionText
    }

    fun hideViews() {
        userEnterSession.gone()
        btnStartSession.gone()
        if (joinedSessionProgressBar.visibility == View.INVISIBLE){
                joinedSessionProgressBar.visible()
        }
    }

    fun getSessionData(sessionText: String) {
        val sessionName = sessionText
        sessionNameText = sessionText
        var counter: Int = 1
        var questionReference = db.collection("sessions").document("${sessionName}").collection("questions")
        questionReference.get()
                .addOnCompleteListener { querysnapshots ->
                    if (querysnapshots.isSuccessful) {
                        for (document in querysnapshots.result) {
                                questionList.add(document.toObject(Questions::class.java))
                            }
                            questionPasser = questionList
                            showContent(questionList)
                    }
                    else {
                        Log.d(TAG, "Error getting documents: ")
                        }
                    }
    }

    fun showContent(questionList: List<Questions>){
        if (questionList.isNotEmpty()){
            var listSize = questionList.size
            settext(i, questionList)
            questionPasser = questionList
        }
    }

    fun settext(i: Int, questionList: List<Questions>) {
        var list = questionList
        if(joinedSessionProgressBar.visibility == View.VISIBLE){
            joinedSessionProgressBar.invisible()
        }
        sessionQNumberTV.text = list[i].qNumber
        sessionQTV.text = list[i].qName
        if(list[i].AnswerA.isNotEmpty()){
            optionAText.text = list[i].AnswerA
            optionACardView.visible()
        } else if (list[i].AnswerA.isEmpty()){
            optionACardView.invisible()
        }
        if (list[i].AnswerB.isNotEmpty()){
            optionBText.text = list[i].AnswerB
            optionBCardView.visible()
        } else if (list[i].AnswerB.isEmpty()){
            optionBCardView.invisible()
        }
        if (list[i].AnswerC.isNotEmpty()){
            optionCText.text = list[i].AnswerC
            optionCCardView.visible()
        } else if (list[i].AnswerC.isEmpty()){
            optionCCardView.invisible()
        }
        if (list[i].AnswerD.isNotEmpty()){
            optionDText.text = list[i].AnswerD
            optionDCardView.visible()
        } else if (list[i].AnswerD.isEmpty()){
            optionDCardView.invisible()
        }
        btnUserSessNext.visible()
        questionCardView.visible()

    }

    fun btnNextQuestionClicked(){
        joinedSessionProgressBar.visible()
        var iCounter =  i.increaseByOne()
        var size = questionPasser.size
        i = iCounter
        Log.i(TAG, "i Counter is at => ${iCounter}")
        Log.i(TAG, "questionPasser.size is  => ${questionPasser.size}")
        if ((iCounter < size)) {
            setRaisedOptionToDefault()
            settext(iCounter, questionPasser)
            sendSelectedAnswer(optionColor)
        } else {
            hideAllTextViews()
            startActivity<MainActivity>()
        }
    }

    fun Int.increaseByOne(): Int {
        var tmpResponse = this + 1
        return tmpResponse
    }

    fun highlightSelectedOption(view: View){
        val v = view
        if (optionColor.isNullOrEmpty()) {
            v.setBackgroundColor(resources.getColor(R.color.colorAccent))
            v.elevation = 6.0F
            if (v.id == R.id.optionACardView) optionColor = "a"
            if (v.id == R.id.optionBCardView) optionColor = "b"
            if (v.id == R.id.optionCCardView) optionColor = "c"
            if (v.id == R.id.optionDCardView) optionColor = "d"
        } else {
            setRaisedOptionToDefault()
            v.setBackgroundColor(resources.getColor(R.color.colorAccent))
            v.elevation = 2.0F
            if (v.id == R.id.optionACardView) optionColor = "a"
            if (v.id == R.id.optionBCardView) optionColor = "b"
            if (v.id == R.id.optionCCardView) optionColor = "c"
            if (v.id == R.id.optionDCardView) optionColor = "d"
        }
    }

    fun sendSelectedAnswer(answerSelectedWas: String){
        Toast.makeText(this, "You selected option ${answerSelectedWas}", Toast.LENGTH_SHORT).show()

        var tempUser = FirebaseAuth.getInstance()
        var mUser = tempUser.currentUser
        var respString = HashMap<String, Any>()

        respString.put("user-${mUser?.uid}", "1")

        try{
        db.collection("sessions").document("${sessionNameText}").collection("answers").document("${answerSelectedWas}").set(respString, SetOptions.merge()).addOnSuccessListener {
            void: Void? -> Toast.makeText(this, "Successfully uploaded to the database :)", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
                    exception: java.lang.Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                }
        } catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }


    fun setRaisedOptionToDefault(){
        if (optionColor.compareTo("a") == 0){
            optionACardView.setBackgroundColor(resources.getColor(R.color.colorWhite))
            optionACardView.elevation = 2.0F
        }
        if (optionColor.compareTo("b") == 0){
            optionBCardView.setBackgroundColor(resources.getColor(R.color.colorWhite))
            optionBCardView.elevation = 2.0F
        }
        if (optionColor.compareTo("c") == 0) {
            optionCCardView.setBackgroundColor(resources.getColor(R.color.colorWhite))
            optionCCardView.elevation = 2.0F
        }
        if (optionColor.compareTo("d") == 0){
            optionDCardView.setBackgroundColor(resources.getColor(R.color.colorWhite))
            optionDCardView.elevation = 2.0F
        }
    }

    fun hideAllTextViews(){

        questionCardView.invisible()
        optionACardView.invisible()
        optionBCardView.invisible()
        optionCCardView.invisible()
    }

// end no more
}












