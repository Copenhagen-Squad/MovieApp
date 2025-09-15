package com.karrar.movieapp.ui.match

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.data.MatchResultsAdapter

class MatchResultsFragment : Fragment(R.layout.fragment_match_results), MatchResultCallbacks {

    private lateinit var adapter: MatchResultsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pager = view.findViewById<ViewPager2>(R.id.vpResults)
        adapter = MatchResultsAdapter(callbacks = this)
        pager.adapter = adapter

        // Peek neighbors (card carousel look)
        pager.offscreenPageLimit = 3
        (pager.getChildAt(0) as RecyclerView).apply {
            val side = resources.getDimensionPixelSize(R.dimen.spacing_large) + 48
            setPadding(side, 0, side, 0)
            clipToPadding = false
        }
        pager.setPageTransformer { page, pos ->
            val scale = 0.92f + (1 - kotlin.math.abs(pos)) * 0.08f
            page.scaleY = scale
            page.alpha = 0.85f + (1 - kotlin.math.abs(pos)) * 0.15f
        }

        // Load data (replace with your real list)
        val demo = listOf(
            MatchItemUI(
                1, "Until Dawn", ".../w500/xxx.jpg", ".../w780/xxx.jpg",
                "Horror, Mystery, Science Fiction", 8.5, "2h 32m", "2008, Jul 18"
            ),
            MatchItemUI(
                2, "Fountain of Youth", "...", "...",
                "Adventure, Fantasy, Mystery", 8.5, "2h 32m", "2008, Jul 18"
            ),
            MatchItemUI(
                3, "The Dark Knight", "...", "...",
                "Drama, Action, Crime, Thriller", 8.5, "2h 32m", "2008, Jul 18"
            )
        )
        adapter.submitList(demo)

//        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    // Callbacks from the page:
    override fun onPlay(item: MatchItemUI) { /* open trailer */
    }

    override fun onSave(item: MatchItemUI) { /* toggle bookmark */
    }

    override fun onDetails(item: MatchItemUI) { /* navigate to details */
    }
}
