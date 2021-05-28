package com.example.mudahtest

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mudahtest.adapter.MessageListAdapter
import com.example.mudahtest.databinding.ActivityMainBinding
import com.example.mudahtest.util.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mMessageAdapter: MessageListAdapter? = null
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mMessageAdapter = MessageListAdapter(arrayListOf())
        binding.recyclerGchat.apply {
            adapter = mMessageAdapter
        }

        binding.buttonGchatSend.setOnClickListener {
            timer.cancel()
            this.hideKeyboard()
            mainActivityViewModel.sendChat(binding.editGchatMessage.text.toString())
            binding.editGchatMessage.setText("")
        }

        mainActivityViewModel.localChatList.observe(this, {
            setTimer()
            mMessageAdapter?.apply {
                mMessageAdapter!!.submitList(it)
            }
            binding.recyclerGchat.smoothScrollToPosition(it.size)
        })

        mainActivityViewModel.showToastMessage.observe(this, {
            if (it.isNotEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        mainActivityViewModel.getLocalChatList()
    }

    fun setTimer() {
        timer = object: CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                mainActivityViewModel.insertAreYouThereMessage()
            }
        }
        timer.start()
    }
}