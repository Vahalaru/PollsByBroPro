package net.tctelco.vahalaru.pollsbybropro

import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_manage_session.*
import net.tctelco.vahalaru.pollsbybropro.Model.Session
import net.tctelco.vahalaru.pollsbybropro.viewholder.SessionViewholder


/**
 * Created by jerem on 12/30/2017.
 */
class ManageSessions: AppCompatActivity()  {

    val TAG = "ManageSessions"

    var adapter: FirestoreRecyclerAdapter<Session, SessionViewholder>? = null


    var db: FirebaseFirestore? = null
    var firestoreListener: ListenerRegistration? = null
    var sessionsList = mutableListOf<Session>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_session)

        db = FirebaseFirestore.getInstance()

        val mLayoutManager = LinearLayoutManager(applicationContext)
        rvOfSessions.layoutManager = mLayoutManager //as RecyclerView.LayoutManager?
        rvOfSessions.itemAnimator = DefaultItemAnimator()

        loadSessionsList()

        firestoreListener = db!!.collection("sessions")
                .addSnapshotListener(EventListener { documentSnapshots, e ->
                    if(e != null) {
                        Log.e(TAG, "Listen Failed!", e)
                        return@EventListener
                    }

                    sessionsList = mutableListOf()

                    for (doc in documentSnapshots) {
                        val session = doc.toObject(Session::class.java)
                        session.id = doc.id
                        sessionsList.add(session)
                    }


                    adapter!!.notifyDataSetChanged()
                    rvOfSessions.adapter = adapter
                })
    }

    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }

    public override fun onStart() {
        super.onStart()

        adapter!!.startListening()
    }

    public override fun onStop() {
        super.onStop()

        adapter!!.stopListening()
    }


    private fun loadSessionsList(){

        val query = db!!.collection("sessions")

        val response = FirestoreRecyclerOptions.Builder<Session>()
                .setQuery(query, Session::class.java)
                .build()

        adapter = object : FirestoreRecyclerAdapter<Session, SessionViewholder>(response){
            override fun onBindViewHolder(holder: SessionViewholder, position: Int, model: Session) {
                val session = sessionsList[position]

                holder.sName.text = session.sName
                holder.cardView.setOnClickListener { updateSession(session) }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewholder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_sessions_cardview, parent, false)

                return SessionViewholder(view)
            }


            override fun onError(e: FirebaseFirestoreException) {
                super.onError(e)
                Log.e("error", e!!.message)
            }

        }

        adapter!!.notifyDataSetChanged()
        rvOfSessions.adapter = adapter
    }



    fun updateSession(session: Session){

    }

    fun sessionListEmpty(){
        if(sessionsList.isEmpty()){
            noSessionsMessage.visibility = View.VISIBLE
        }else {
            noSessionsMessage.visibility = View.GONE
        }
    }

}
