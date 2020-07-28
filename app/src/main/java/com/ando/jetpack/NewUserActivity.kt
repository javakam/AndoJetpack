package com.ando.jetpack

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/28  16:56
 */
class NewUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPLY = "REPLY"
    }

    private lateinit var mEtUserFirstName: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        mEtUserFirstName = findViewById(R.id.edit_user)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(mEtUserFirstName.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val user = mEtUserFirstName.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, user)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }


}