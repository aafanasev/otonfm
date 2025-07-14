package net.afanasev.otonfm.screens.contacts

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import net.afanasev.otonfm.R
import net.afanasev.otonfm.log.Logger
import net.afanasev.otonfm.ui.components.Dialog
import net.afanasev.otonfm.ui.components.DialogItem

@Composable
fun ContactsScreen() {
    val context = LocalContext.current

    Dialog {
        DialogItem(
            R.string.contacts_website,
            onClick = {
                Logger.onContactSelect("website")

                val uri = "https://oton.fm?ref=android".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )

        DialogItem(
            R.string.contacts_email,
            onClick = {
                Logger.onContactSelect("email")

                val uri = "mailto:show@oton.fm".toUri()
                val intent = Intent(Intent.ACTION_SENDTO, uri)

                if (intent.resolveActivity(context.packageManager) == null) {
                    Logger.logMissingEmailClient()
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

        DialogItem(
            R.string.contacts_telegram,
            onClick = {
                Logger.onContactSelect("telegram")

                val uri = "https://t.me/otonfm".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )

        DialogItem(
            R.string.contacts_instagram,
            onClick = {
                Logger.onContactSelect("instagram")

                val uri = "https://www.instagram.com/oton.fm/".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )

        DialogItem(
            R.string.contacts_github,
            onClick = {
                Logger.onContactSelect("github")

                val uri = "https://github.com/aafanasev/otonfm".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
        )
    }
}