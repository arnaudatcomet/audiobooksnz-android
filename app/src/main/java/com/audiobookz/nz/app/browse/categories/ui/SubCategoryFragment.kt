package com.audiobookz.nz.app.browse.categories.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.databinding.FragmentCategoriesBinding
import com.audiobookz.nz.app.databinding.FragmentSubCategoriesBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel

class SubCategoryFragment : Fragment(), Injectable {
    private val args: SubCategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSubCategoriesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = SubCategoryAdapter()

        binding.subCategoryRecyclerView.adapter = adapter
        adapter.submitList(args.SubCategories?.toMutableList())
        return binding.root
    }
}