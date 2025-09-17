package com.karrar.movieapp.ui.match

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchResultBinding

class MatchResultsFragment : Fragment(R.layout.fragment_match_result) {

    private lateinit var binding: FragmentMatchResultBinding
    private val posterAdapter = MatchPostersAdapter()

    private var items: List<MatchItemUI> = emptyList()

    private val pageCb = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.itemMatch = items[position]
            binding.executePendingBindings()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMatchResultBinding.bind(view)

        binding.callbacks = object : MatchResultCallbacks {

            override fun onViewDetails(item: MatchItemUI) {
                Toast.makeText(requireContext(), "Details: ${item.title}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPlay(item: MatchItemUI) {
                Toast.makeText(requireContext(), "Play trailer: ${item.title}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onSave(item: MatchItemUI) {
                Toast.makeText(requireContext(), "Saved: ${item.title}", Toast.LENGTH_SHORT).show()
            }

            override fun onDetails(item: MatchItemUI) {
                Toast.makeText(requireContext(), "Open details: ${item.title}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.poster.adapter = posterAdapter
        binding.poster.registerOnPageChangeCallback(pageCb)

        val demo = listOf(
            MatchItemUI(
                id = 1,
                title = "Until Dawn",
                posterUrl = "https://image.tmdb.org/t/p/w500/8a1p4pQK8C9ZK1rK8Zk.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/8a1p4pQK8C9ZK1rK8Zk.jpg",
                genres = "Horror, Mystery, Science Fiction",
                voteAverage = 8.5,
                runtimeFormatted = "2h 32m",
                releaseDateShort = "2008, Jul 18",
                isMovie = true
            ),
            MatchItemUI(
                id = 2,
                title = "Fountain of Youth",
                posterUrl = "https://image.tmdb.org/t/p/w500/aaaBBBcccDDD.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/aaaBBBcccDDD.jpg",
                genres = "Adventure, Fantasy, Mystery",
                voteAverage = 7.9,
                runtimeFormatted = "1h 58m",
                releaseDateShort = "2016, May 05",
                isMovie = true
            ),
            MatchItemUI(
                id = 3,
                title = "The Dark Knight",
                posterUrl = "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                backdropUrl = "https://image.tmdb.org/t/p/w780/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                genres = "Drama, Action, Crime, Thriller",
                voteAverage = 9.0,
                runtimeFormatted = "2h 32m",
                releaseDateShort = "2008, Jul 18",
                isMovie = false
            )
        )
        update(demo)
    }

    private fun update(newItems: List<MatchItemUI>) {
        items = newItems
        if (items.isEmpty()) return

        binding.itemMatch = items.first()
        binding.poster.adapter = posterAdapter

        posterAdapter.submitList(items) {
            binding.poster.post {
                val pager = binding.poster
                val rv = pager.getChildAt(0) as RecyclerView

                val PAGE_WIDTH_FRACTION = 0.74f
                val pageWidth = (pager.width * PAGE_WIDTH_FRACTION).toInt()
                val side = (pager.width - pageWidth) / 2

                rv.setPadding(side, 0, side, 0)
                rv.clipToPadding = false
                rv.overScrollMode = View.OVER_SCROLL_NEVER
                pager.offscreenPageLimit = 3

                pager.setPageTransformer { page, position ->
                    val p = kotlin.math.abs(position)
                    val scale = 0.90f + (1f - p) * 0.10f
                    page.scaleX = scale
                    page.scaleY = scale
                    page.alpha = 0.80f + (1f - p) * 0.20f
                }

                val middle = items.size / 2
                pager.setCurrentItem(middle, false)

                binding.itemMatch = items[middle]
                binding.executePendingBindings()
            }
        }
    }
}