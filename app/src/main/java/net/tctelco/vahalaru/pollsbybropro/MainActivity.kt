package net.tctelco.vahalaru.pollsbybropro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Contacts
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.TextView
import com.firebase.ui.FirebaseUI
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val TAG = "MainActivity"

    var mAuth: FirebaseAuth? = null
    val RC_SIGN_IN = 123


    lateinit var uemail: NavigationView
    lateinit var navHead: View
    lateinit var tvEmail: TextView
    lateinit var tvDisplayName: TextView
    lateinit var  mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        setUserProfile()

        MobileAds.initialize(this, resources.getString(R.string.adMob_App_id))

        mAdView = adView
        var adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_start_session -> {

                val intent = Intent(this, UserSession::class.java)
                startActivity(intent)
                // Handle the camera action
            }
            R.id.nav_create_a_session -> {

                val intent = Intent(this, CreateSession::class.java)
                startActivity(intent)
            }
            R.id.nav_manage -> {
                val intent = Intent(this, ManageSessions::class.java)
                startActivity(intent)
            }
            R.id.nav_signout -> {

                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
            }
            R.id.nav_send -> {
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
        super.onStart()

        if (mAuth!!.currentUser == null){
            iForced()
        }
        setUserProfile()

    }


    fun setUserProfile() {


        mAuth = FirebaseAuth.getInstance()

        val mUser = mAuth?.currentUser

        uemail = findViewById<View>(R.id.nav_view) as NavigationView
        navHead = uemail.getHeaderView(0)
        tvEmail = navHead.findViewById(R.id.userEmail) as TextView
        tvDisplayName = navHead.findViewById(R.id.userName) as TextView
        tvEmail.text = mUser?.email
        tvDisplayName.text = mUser?.displayName

            Log.i(TAG, "User Display Name : ${mUser?.displayName}")
            Log.i(TAG, "Users Email :  ${mUser?.email}")

    }

    fun iForced(){
        startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setPrivacyPolicyUrl("https://www.bro-programming.pro/projects-apps/privacy-policy/")
                    .build(),
            RC_SIGN_IN)
    }


}








