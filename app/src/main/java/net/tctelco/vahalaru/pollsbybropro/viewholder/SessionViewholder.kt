package net.tctelco.vahalaru.pollsbybropro.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.recycler_sessions_cardview.view.*
import net.tctelco.vahalaru.pollsbybropro.R

/**
 * Created by jerem on 1/22/2018.
 */
class SessionViewholder(view: View) : RecyclerView.ViewHolder(view){
    var sName: TextView
    var cardView: CardView
    init {
        sName = view.findViewById(R.id.tvSName)
        cardView = view.findViewById(R.id.sessionCardView)
    }
}