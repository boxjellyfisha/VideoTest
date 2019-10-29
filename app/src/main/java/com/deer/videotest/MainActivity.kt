package com.deer.videotest

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.deer.lib.OkMyLib
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*


/**
 * reference
 * Media + TextureView
 * https://www.jianshu.com/p/420f7b14d6f6
 *
 * ExpPalyer
 * https://exoplayer.dev/hello-world.html
 *
 * github
 * 2.8 start Java
 * https://github.com/danylovolokh/VideoPlayerManager
 *
 * 9.2 start Java
 * https://github.com/lipangit/JiaoZiVideoPlayer
 *
 * IjkPlayer
 * https://github.com/Bilibili/ijkplayer
 */
class MainActivity : AppCompatActivity() {

    private val url = "https://2bite.com/assets/images/video-short_1.mp4"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = OkMyLib().helloJavaLib()
        initVideo()
        initTexture()
        initExo()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        exoPlayer?.release()
    }

    private fun initVideo() {
        //在非match_parent的情況下
        //會自己調整是創大小去符合影片比例
//        val mController = MediaController(videoView.context)
//        videoView.setMediaController(mController)
        videoView.setVideoURI(Uri.parse(url))
        videoView.resolveAdjustedSize(300, View.MeasureSpec.UNSPECIFIED)
        videoView.requestFocus()
        videoView.start()
    }

    private var mediaPlayer: MediaPlayer? = null
    private fun initTexture() {
        //可以手動調整 View 的大小
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)

        texture.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}
            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return false
            }
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                mediaPlayer?.setSurface(Surface(surface))
                mediaPlayer?.prepareAsync()
            }
        }

        mediaPlayer?.setOnPreparedListener {
            it.start()
        }
    }

    private var exoPlayer: SimpleExoPlayer? = null
    private fun initExo() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        player.player = exoPlayer

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name))
        )
        // This is the MediaSource representing the media to be played.
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(url))
        // Prepare the player with the source.
        exoPlayer?.prepare(videoSource)
    }
}
