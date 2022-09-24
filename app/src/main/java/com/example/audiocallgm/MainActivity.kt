package com.example.audiocallgm

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.audiocallgm.databinding.ActivityMainBinding
import io.agora.rtc.RtcEngine
import io.agora.rtc.IRtcEngineEventHandler

private lateinit var binding: ActivityMainBinding

private var mMute = false
private val PERMISSION_REQ_ID_RECORD_AUDIO = 22
private val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1

private val APP_ID = "22b0f0116b3e4c6dbafdcf37703285a6"
// Fill the channel name.
private val CHANNEL = "Test"
// Fill the temp token generated on Agora Console.
private val TOKEN = "cab5b8d7e6904d418f0695b7c249a571"
private var mRtcEngine: RtcEngine ?= null
private val mRtcEventHandler = object : IRtcEngineEventHandler() {
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_main)
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
            initializeAndJoinChannel();
        }
    }

        private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    requestCode
                )
                return false
            }
            return true
        }

        private fun initializeAndJoinChannel() {
            try {
                mRtcEngine = RtcEngine.create(baseContext, APP_ID, mRtcEventHandler)
            } catch (e: Exception) {
                Log.e("error", "initializeAndJoinChannel: ", )
            }
            mRtcEngine!!.joinChannel(TOKEN, CHANNEL, "", 0)
        }
        fun onCallEnded(view: View?){
            binding.btnCallEnd.setOnClickListener {
                mRtcEngine?.leaveChannel()
                RtcEngine.destroy()
            }
        }
        fun onLocalMutedClicked(view: View?){
            binding.btnMute.setOnClickListener {
                mMute = !mMute
                mRtcEngine!!.muteLocalAudioStream(mMute)
            }
        }
    }
