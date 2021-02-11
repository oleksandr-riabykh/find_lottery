package com.limestudio.findlottery.presentation.ui.profile.seller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.svprogresshud.SVProgressHUD
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.showWarning
import com.limestudio.findlottery.extensions.toDateFormat
import com.limestudio.findlottery.presentation.base.BaseDialogFragment
import com.limestudio.findlottery.presentation.ui.auth.AuthActivity
import com.limestudio.findlottery.presentation.ui.fullscreen_photo.FullscreenPhotoActivity
import com.limestudio.findlottery.presentation.ui.profile.ARG_VIEW_MODE
import com.limestudio.findlottery.presentation.ui.profile.ProfileViewModel
import com.limestudio.findlottery.presentation.ui.profile.SELECTED_USER
import com.limestudio.findlottery.presentation.ui.tickets.draws.DrawsAdapter
import com.limestudio.findlottery.presentation.ui.tickets.draws.SELECTED_DRAW
import com.limestudio.findlottery.presentation.ui.tickets.list.TicketsFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class SellerProfileDialogFragment : BaseDialogFragment() {

    companion object {
        fun newInstance(userId: String) = SellerProfileDialogFragment().apply {
            arguments = Bundle().apply { putSerializable(SELECTED_USER, userId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    private val profileViewModel: ProfileViewModel by viewModels { viewModelFactory }
    private lateinit var viewAdapter: DrawsAdapter
    private var mUser: User = User()

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
        profileViewModel.loadData(arguments?.getString(SELECTED_USER))
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getUser(
            arguments?.get(SELECTED_USER)?.toString() ?: Firebase.auth.currentUser?.uid ?: ""
        )
    }

    private fun initAdapter() {
        viewAdapter = DrawsAdapter({
                val bundle = Bundle().apply {
                    putParcelable(SELECTED_DRAW, it)
                    putBoolean(ARG_VIEW_MODE, arguments?.get(SELECTED_USER) != null)
                    putString(
                        "title",
                        Date(it.timestamp).toDateFormat(resources.configuration.locales[0])
                    )
                }
            TicketsFragment.newInstance(bundle).show(childFragmentManager, "tickets_fragment")
        }, {
            profileViewModel.deleteDraw(it)
        })
    }

    private fun initViews() {
        draws_list.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        if (arguments?.get(SELECTED_USER) != null) logout_button.visibility = View.GONE
        logout_button.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(R.string.logout_popup)
            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                Firebase.auth.signOut()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initStateListener() {
        profileViewModel.user.observe(viewLifecycleOwner, { user ->
            mUser = user
            if (hudSync.isShowing) hudSync.dismiss()
            username_text_view?.text = "${mUser.name} ${mUser.lastName}"
            if (user.city?.isNotEmpty() == true)
                city_text_view?.text =
                    mUser.city?.substring(0, 1)?.toUpperCase(Locale.ROOT) + mUser.city?.substring(1)
                        ?.toLowerCase(Locale.ROOT)
            if (user.nationalId?.isNotEmpty() == true) {
                nationalid_text_view?.text =
                    getString(R.string.national_id_s1, mUser.nationalId?.toUpperCase(Locale.ROOT))
                nationalid_text_view.visibility = View.VISIBLE
            }

            if (user.line?.isNotEmpty() == true) {
                line_id?.text = getString(R.string.line_s1, mUser.line?.toUpperCase(Locale.ROOT))
                line_id.visibility = View.VISIBLE
            }
            if (user.wechat?.isNotEmpty() == true) {
                wechat_id?.text =
                    getString(R.string.wechat_s1, mUser.wechat?.toUpperCase(Locale.ROOT))
                wechat_id.visibility = View.VISIBLE
            }
            if (user.whatsapp?.isNotEmpty() == true) {
                whatsapp_id?.text =
                    getString(R.string.whatsapp_s1, mUser.whatsapp?.toUpperCase(Locale.ROOT))
                whatsapp_id.visibility = View.VISIBLE
            }

        }
        )
        profileViewModel.avatarUrl.observe(viewLifecycleOwner, { item ->
            mUser.avatar = item
            if (hudSync.isShowing) hudSync.dismiss()
            if (item != null) Glide.with(requireActivity()).load(item).into(circleImage)
        }
        )
        profileViewModel.idCardUrl.observe(viewLifecycleOwner, { item ->
            if (hudSync.isShowing) hudSync.dismiss()
            mUser.photoId = item
            id_card_image.setOnClickListener {
                if (item != null) {
                    val photoIntent = Intent(requireContext(), FullscreenPhotoActivity::class.java)
                    photoIntent.putExtra(FullscreenPhotoActivity.ARG_PHOTO, item)
                    startActivity(photoIntent)
                }
            }
        }
        )
        profileViewModel.draws.observe(viewLifecycleOwner, { items ->
            if (hudSync.isShowing) hudSync.dismiss()
            if (arguments?.get(SELECTED_USER)?.toString()?.isNotEmpty() == true)
                viewAdapter.setViewMode()
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