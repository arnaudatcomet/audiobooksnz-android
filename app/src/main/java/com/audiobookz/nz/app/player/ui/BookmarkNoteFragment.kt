package com.audiobookz.nz.app.player.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.audiobookz.nz.app.data.Result
import com.audiobookz.nz.app.R
import com.audiobookz.nz.app.api.AlertDialogsService
import com.audiobookz.nz.app.databinding.FragmentBookmarkNoteBinding
import com.audiobookz.nz.app.di.Injectable
import com.audiobookz.nz.app.di.injectViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class BookmarkNoteFragment : Fragment() , Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel
    private val args: BookmarkNoteFragmentArgs by navArgs()
    lateinit var noteTileTxt : TextInputEditText

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save-> {
                 viewModel.updateBookmark(args.bookmarkId, noteTileTxt.text.toString())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        val binding = FragmentBookmarkNoteBinding.inflate(inflater, container, false)
        noteTileTxt = binding.titleNote
        setHasOptionsMenu(true)
        subscribeUI(binding)
        return binding.root
    }

    private fun subscribeUI(binding: FragmentBookmarkNoteBinding) {
        viewModel.updateBookmarksResult.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Audiobooks NZ")
                        .setPositiveButton(resources.getString(R.string.yourNoteUpdate)) { dialog, which ->
                        }
                        .show()
                }
                Result.Status.LOADING -> {

                }
                Result.Status.ERROR -> {
                    result.message?.let { AlertDialogsService(context!!).simple("Error", it) }
                }
            }
        })

    }


}
