package com.limestudio.findlottery.presentation.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.svprogresshud.SVProgressHUD
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.extensions.navigateTo
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.extensions.toDateFormat
import com.limestudio.findlottery.presentation.base.BaseFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import com.limestudio.findlottery.presentation.ui.fullscreen_photo.FullscreenPhotoActivity
import com.limestudio.findlottery.presentation.ui.tickets.draws.DrawsAdapter
import com.limestudio.findlottery.presentation.ui.tickets.draws.SELECTED_DRAW
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*


class ProfileFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    private val profileViewModel: ProfileViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: DrawsAdapter

    private lateinit var hudSync: SVProgressHUD

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hudSync = SVProgressHUD(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViews()
        initStateListener()
        profileViewModel.loadData()
        profileViewModel.getUser()
    }

    private fun initAdapter() {
        viewAdapter = DrawsAdapter({
            navigateTo(
                R.id.navigation_ticketsFragment,
                R.id.navigation_profile,
                false,
                Bundle().apply {
                    putParcelable(SELECTED_DRAW, it)
                    putString(
                        "title",
                        Date(it.timestamp).toDateFormat(resources.configuration.locales[0])
                    )
                }
            )
        }, {
            profileViewModel.deleteDraw(it)
        })
    }

    private fun initViews() {
        draws_list.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        logout_button.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initStateListener() {
        profileViewModel.user.observe(viewLifecycleOwner, { user ->
            if (hudSync.isShowing) hudSync.dismiss()
            username_text_view?.text = "${user.name} ${user.lastName}"
            city_text_view?.text =
                user.city?.substring(0, 1)?.toUpperCase(Locale.ROOT) + user.city?.substring(1)
                    ?.toLowerCase(Locale.ROOT)
            nationalid_text_view?.text =
                getString(R.string.national_id_s1, user.nationalId?.toUpperCase(Locale.ROOT))
        }
        )
        profileViewModel.avatarUrl.observe(viewLifecycleOwner, { item ->
            if (hudSync.isShowing) hudSync.dismiss()
            if (item.isNotEmpty()) Picasso.get().load(item).into(circleImage)
        }
        )
        profileViewModel.idCardUrl.observe(viewLifecycleOwner, { item ->
            if (hudSync.isShowing) hudSync.dismiss()
            id_card_image.setOnClickListener {
                if (item.isNotEmpty()) {
                    val photoIntent = Intent(requireContext(), FullscreenPhotoActivity::class.java)
                    photoIntent.putExtra(FullscreenPhotoActivity.ARG_PHOTO, item)
                    startActivity(photoIntent)
                }
            }
        }
        )
        profileViewModel.draws.observe(viewLifecycleOwner, { items ->
            if (hudSync.isShowing) hudSync.dismiss()
            viewAdapter.setData(items)
        }
        )
        profileViewModel.error.observe(viewLifecycleOwner, { item ->
            if (hudSync.isShowing) hudSync.dismiss()
            showWarning(item.messageId)
        }
        )
    }
}
