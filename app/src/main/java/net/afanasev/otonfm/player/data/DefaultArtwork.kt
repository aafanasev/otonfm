package net.afanasev.otonfm.player.data

import android.content.Context
import net.afanasev.otonfm.R

/**
 * Shown while the station reports no artwork. It uses the resource id form, because Coil
 * doesn't load the `android.resource://<package>/drawable/<name>` form.
 */
fun Context.defaultArtworkUri(): String = "android.resource://$packageName/${R.drawable.default_artwork}"
