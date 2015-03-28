package com.aesthetikx.spotify_tracker

import android.content.{BroadcastReceiver, Context, Intent, SharedPreferences}
import android.preference.PreferenceManager
import android.util.Log

import java.io._

class SongReceiver extends BroadcastReceiver {

  override def onReceive(context: Context, intent: Intent) {
    val trackId: String = intent.getStringExtra("id")
    val artistName: String = intent.getStringExtra("artist")
    val albumName: String = intent.getStringExtra("album")
    val trackName: String  = intent.getStringExtra("track")
    //val trackLengthInSec: Int = intent.getIntExtra("length", 0)

    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    // Spotify has a habbit of sending lots of repetitive intents
    if (prefs.getString("last_id", "") != trackId) {
      Log.d("SpotifyTracker", "New song: " + trackName)

      val line = System.currentTimeMillis() +
        " --- " + trackId +
        " --- " + artistName +
        " --- " + albumName +
        " --- " + trackName

      logToFile(line)
    }

    prefs.edit.putString("last_id", trackId).commit()
  }

  private def logToFile(line: String) {
     val logFile = new File("sdcard/spotify_tracker_log.txt")

     // Create file initially
     if (!logFile.exists()) {
       try {
         logFile.createNewFile()
       } catch {
         case e: IOException =>
           e.printStackTrace()
       }
     }

     try {
       val writer: BufferedWriter = new BufferedWriter(new FileWriter(logFile, true))
       writer.append(line)
       writer.newLine()
       writer.close()
     } catch {
       case e: IOException =>
         e.printStackTrace()
     }
  }

}
