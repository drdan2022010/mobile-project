package com.example.mymemorygame01

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.media.MediaPlayer
import androidx.appcompat.widget.Toolbar

class Game04_2 : AppCompatActivity() {
    private lateinit var imageList: MutableList<Int>
    private val selectedImages = mutableListOf<Int>()
    private lateinit var messageTextView: TextView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var correctSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game04_2)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize the list of image resources
        imageList = mutableListOf(
            R.drawable.png0, R.drawable.png0,
            R.drawable.png1, R.drawable.png1,
            R.drawable.png2, R.drawable.png2,
            R.drawable.png3, R.drawable.png3,
            R.drawable.png4, R.drawable.png4,
        )

        fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return true
        }

        // Shuffle the image list
        imageList.shuffle()

        // Set up the RecyclerView with the adapter
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        imageAdapter = ImageAdapter(imageList) { position ->
            onImageClicked(position)
        }
        recyclerView.adapter = imageAdapter

        // Get reference to the message TextView
        messageTextView = findViewById(R.id.messageTextView)

        // Initialize the correct sound
        correctSound = MediaPlayer.create(this, R.raw.sound)
    }

    private fun onImageClicked(position: Int) {
        val selectedImage = imageList[position]

        // Check if the image is already selected
        if (selectedImages.contains(selectedImage)) {
            // Match found
            // Update UI and handle the match
            val selectedImagePositions = mutableListOf<Int>()
            for (i in imageList.indices) {
                if (imageList[i] == selectedImage) {
                    selectedImagePositions.add(i)
                }
            }
            if (selectedImagePositions.size == 2) {
                // Correct pair selected
                for (pos in selectedImagePositions) {
                    imageList[pos] = -1 // Mark the image as "disappeared"
                }
                imageAdapter.notifyDataSetChanged() // Refresh the RecyclerView
                showCorrectMessage()
            }
        } else {
            // No match found
            // Show a message to the user and reset their selections
            selectedImages.add(selectedImage)
            imageAdapter.notifyDataSetChanged() // Refresh the RecyclerView
        }
    }

    private fun showCorrectMessage() {
        messageTextView.text = "Correct!"
        messageTextView.visibility = View.VISIBLE
        playCorrectSound()
    }

    private fun showIncorrectMessage() {
        messageTextView.text = "Incorrect!"
        messageTextView.visibility = View.VISIBLE
    }

    private fun playCorrectSound() {
        var mp = MediaPlayer.create(this, R.raw.sound)
        mp.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        correctSound.release()
    }
}class ImageAdapter(
    private val imageList: List<Int>,
    private val onImageClickListener: (Int) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageResource = imageList[position]
        if (imageResource == -1) {
            holder.imageView.visibility = View.INVISIBLE
        } else {
            holder.imageView.visibility = View.VISIBLE
            holder.imageView.setImageResource(imageResource)
        }
        holder.itemView.setOnClickListener {
            onImageClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}