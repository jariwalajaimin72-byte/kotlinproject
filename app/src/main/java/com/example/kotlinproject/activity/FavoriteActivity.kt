package com.example.kotlinproject.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.adapter.FavoriteAdapter
import com.example.kotlinproject.model.FavoriteItem

class FavoriteActivity : AppCompatActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var layoutEmpty: View
    private lateinit var adapter: FavoriteAdapter

    // MUST be MutableList (important)
    private val favList = mutableListOf<FavoriteItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        initViews()
        setupRecyclerView()
        loadFavorites()
        setupSwipeToDelete()
    }

    private fun initViews() {
        rvFavorites = findViewById(R.id.rvFavorites)
        layoutEmpty = findViewById(R.id.layoutEmpty)
    }

    private fun setupRecyclerView() {
        rvFavorites.layoutManager = GridLayoutManager(this, 2)
        adapter = FavoriteAdapter(favList)
        rvFavorites.adapter = adapter
    }

    private fun loadFavorites() {
        // TEMP DATA (replace later with Firebase)
        favList.add(
            FavoriteItem(
                "1",
                "Granny Square Crop Top",
                "https://i.pinimg.com/1200x/5f/41/4a/5f414a3dd89aee137b7f44742c508ed5.jpg"
            )
        )
        favList.add(
            FavoriteItem(
                "2",
                "Cozy Cottage Crochet Tote",
                "https://i.pinimg.com/736x/bb/83/78/bb8378e1886491c85ff10182ad2d17e4.jpg"
            )
        )
        favList.add(
            FavoriteItem(
                "3",
                "Daisy Flower Keychain",
                "https://i.pinimg.com/1200x/5a/92/fc/5a92fcb376f124d5836efe13c158f593.jpg"
            )
        )

        adapter.notifyDataSetChanged()
        checkEmpty()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        favList.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        checkEmpty()
                    }
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(rvFavorites)
    }

    private fun checkEmpty() {
        if (favList.isEmpty()) {
            layoutEmpty.visibility = View.VISIBLE
            rvFavorites.visibility = View.GONE
        } else {
            layoutEmpty.visibility = View.GONE
            rvFavorites.visibility = View.VISIBLE
        }
    }
}