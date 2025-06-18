package net.afanasev.otonfm.screens.contacts

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.components.Dialog
import net.afanasev.otonfm.ui.components.DialogItem

private const val TAG = "ContactsScreen"

@Composable
fun ContactsScreen() {
    val context = LocalContext.current

    Dialog {
        DialogItem(
            R.string.contacts_website,
            onClick = {
                val uri = "https://oton.fm?ref=android".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )

        HorizontalDivider()

        DialogItem(
            R.string.contacts_email,
            onClick = {
                val uri = "mailto:show@oton.fm".toUri()
                val intent = Intent(Intent.ACTION_SENDTO, uri)

                if (intent.resolveActivity(context.packageManager) == null) {
                    Log.e(TAG, "No email client found")
                    val noEmailApp = R.string.contacts_email_no_client
                    Toast.makeText(context, noEmailApp, Toast.LENGTH_LONG).show()
                } else {
                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            context.getString(R.string.contacts_email)
                        )
                    )
                }
            },
        )

        HorizontalDivider()

        DialogItem(
            R.string.contacts_telegram,
            onClick = {
                val uri = "https://t.me/otonfm".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )

        HorizontalDivider()

        DialogItem(
            R.string.contacts_instagram,
            onClick = {
                val uri = "https://www.instagram.com/oton.fm/".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )

        HorizontalDivider()

        DialogItem(
            R.string.contacts_github,
            onClick = {
                val uri = "https://github.com/aafanasev/otonfm".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )
    }
}