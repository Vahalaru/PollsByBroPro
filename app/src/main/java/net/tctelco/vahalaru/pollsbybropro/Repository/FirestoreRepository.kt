package net.tctelco.vahalaru.pollsbybropro.Repository

import android.arch.lifecycle.LiveData
import com.bumptech.glide.load.engine.Resource
import com.google.firebase.firestore.FirebaseFirestore
import net.tctelco.vahalaru.pollsbybropro.Model.Questions

/**
 * Created by jerem on 2/5/2018.
 */
class FirestoreRepository : Repository {
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun loadQuestions(): LiveData<Resource<List<Questions>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




    companion object {
        private val QUESTIONS_REF = "questions"
        private val SESSSION_COL_REF = ""
    }
}