package com.karrar.movieapp.ui.match

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchScreenBinding
import com.karrar.movieapp.ui.base.BaseFragment

class MatchScreenFragment(): BaseFragment<FragmentMatchScreenBinding> (){
    override val layoutIdFragment: Int = R.layout.fragment_match_screen
    override val viewModel: MatchScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
        binding.startButton.setOnClickListener {
            findNavController().navigate(R.id.action_matchFragment_to_matchQuestionFragment)
        }
    }
}